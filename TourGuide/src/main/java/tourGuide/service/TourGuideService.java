package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Output;
import tourGuide.model.Preferences;
import tourGuide.model.Provider;
import tourGuide.model.User;
import tourGuide.model.UserPreferences;
import tourGuide.model.UserReward;
import tourGuide.model.VisitedLocation;
import tourGuide.tracker.Tracker;

@Service
public class TourGuideService {

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final RewardsService rewardsService;
	public final Tracker tracker;
	boolean testMode = true;
	private RestTemplate restTemplate = new RestTemplate();
	private String priceUrl = "http://trip-pricer-server:8083/getPrice";
	private String userLocationUrl = "http://gps-util-server:8081/getUserLocation";

	public TourGuideService(RewardsService rewardsService) {
		this.rewardsService = rewardsService;

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}
	
	public void setPriceUrl(String priceUrl) {
		this.priceUrl = priceUrl;
	}
	
	public void setUserLocationUrl(String userLocationUrl) {
		this.userLocationUrl = userLocationUrl;
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation() : trackUserLocation(user);
		return visitedLocation;
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		String URL = priceUrl;
		Map<String, String> map = new HashMap<>();
		map.put("tripPricerApiKey", tripPricerApiKey);
		map.put("userId", user.getUserId().toString());
		map.put("numberOfAdults", String.valueOf(user.getUserPreferences().getNumberOfAdults()));
		map.put("numberOfChildren", String.valueOf(user.getUserPreferences().getNumberOfChildren()));
		map.put("tripDuration", String.valueOf(user.getUserPreferences().getTripDuration()));
		map.put("cumulatativeRewardPoints", String.valueOf(cumulatativeRewardPoints));
		HttpEntity<Map<String, String>> entity = new HttpEntity<Map<String, String>>(map, null);
		ResponseEntity<List<Provider>> response = restTemplate.exchange(URL, HttpMethod.POST, entity, new ParameterizedTypeReference<List<Provider>>(){});
		List<Provider> providers = response.getBody();
		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocation trackUserLocation(User user) {
		String URL = userLocationUrl;
		HttpEntity<UUID> entity = new HttpEntity<UUID>(user.getUserId(), null);
		ResponseEntity<VisitedLocation> response = restTemplate.exchange(URL, HttpMethod.POST, entity, VisitedLocation.class);
		VisitedLocation visitedLocation = response.getBody();
		user.addToVisitedLocations(visitedLocation);
		return visitedLocation;
	}

	public List<Output> getNearByAttractions(VisitedLocation visitedLocation, User user) {
		List<Output> attractionList = new ArrayList<>();
		List<Output> nearByAttractions = new ArrayList<>();
		
		for (Attraction attraction : rewardsService.getAttractions()) {
			Output output = new Output();
			output.setAttractionName(attraction.attractionName);
			Location location = new Location(attraction.latitude, attraction.longitude);
			output.setAttractionLocation(location);
			output.setUserLocation(user.getLastVisitedLocation().location);
			output.setDistance(rewardsService.getDistance(attraction, visitedLocation.location));
			output.setAttractionRewardPoints(rewardsService.getRewardPoints(attraction, user));
			attractionList.add(output);
		}

		Collections.sort(attractionList, Comparator.comparingDouble(Output::getDistance));

		for (int i = 0; i < 5; i++) {
			nearByAttractions.add(attractionList.get(i));
		}

		return nearByAttractions;
	}

	public Map<String, Location> getAllCurrentLocations() {
		Map<String, Location> map = new HashMap<>();
		List<User> users = getAllUsers();

		for (int i = 0; i < users.size(); i++) {
			map.put(users.get(i).getUserId().toString(), users.get(i).getLastVisitedLocation().location);
		}

		return map;
	}

	public void editPreferences(User user, Preferences preferences) {
		UserPreferences userPreferences = new UserPreferences();
		userPreferences.setAttractionProximity(preferences.getAttractionProximity());
		CurrencyUnit currency = Monetary.getCurrency(preferences.getCurrency());
		userPreferences.setCurrency(currency);
		userPreferences.setLowerPricePoint(Money.of(preferences.getLowerPricePoint(), currency));
		userPreferences.setHighPricePoint(Money.of(preferences.getHighPricePoint(), currency));
		userPreferences.setTripDuration(preferences.getTripDuration());
		userPreferences.setTicketQuantity(preferences.getTicketQuantity());
		userPreferences.setNumberOfAdults(preferences.getNumberOfAdults());
		userPreferences.setNumberOfChildren(preferences.getNumberOfChildren());
		user.setUserPreferences(userPreferences);
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
