package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.junit.Test;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.Location;
import tourGuide.user.Output;
import tourGuide.user.Provider;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.VisitedLocation;

public class TestTourGuideService {

	@Test
	public void getUserLocation() {
		Locale.setDefault(new Locale("en", "US", "WIN"));

		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}

	@Test
	public void addUser() {

		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();

		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}

	@Test
	public void getAllUsers() {

		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();

		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void trackUser() {
		Locale.setDefault(new Locale("en", "US", "WIN"));

		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getNearbyAttractions() {
		Locale.setDefault(new Locale("en", "US", "WIN"));

		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

		List<Output> attractions = tourGuideService.getNearByAttractions(visitedLocation, user);

		tourGuideService.tracker.stopTracking();

		assertEquals(5, attractions.size());
	}

	@Test
	public void getTripDeals() {

		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(5, providers.size());
	}

	@Test
	public void getAllCurrentLocations() {

		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		tourGuideService.trackUserLocation(user);
		tourGuideService.trackUserLocation(user2);

		Map<String, Location> map = new HashMap<>();

		map.put(user.getUserId().toString(), user.getLastVisitedLocation().location);
		map.put(user2.getUserId().toString(), user2.getLastVisitedLocation().location);

		Map<String, Location> results = tourGuideService.getAllCurrentLocations();

		tourGuideService.tracker.stopTracking();

		assertEquals(results, map);
	}

	@Test
	public void editPreferences() {

		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		UserPreferences oldPreferences = user.getUserPreferences();
		UserPreferences userPreferences = new UserPreferences();
		userPreferences.setTripDuration(10);
		userPreferences.setTicketQuantity(4);
		userPreferences.setNumberOfAdults(2);
		userPreferences.setNumberOfChildren(2);
		user.setUserPreferences(userPreferences);
		UserPreferences newPreferences = user.getUserPreferences();

		tourGuideService.tracker.stopTracking();

		assertNotEquals(oldPreferences, newPreferences);
	}

}
