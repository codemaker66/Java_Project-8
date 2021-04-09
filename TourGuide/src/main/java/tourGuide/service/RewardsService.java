package tourGuide.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.model.VisitedLocation;

@Service
public class RewardsService {

	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	// proximity in miles
	private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private RestTemplate restTemplate = new RestTemplate();
	private String attractionRewardPointsUrl = "http://reward-central-server:8082/getAttractionRewardPoints";
	private String attractionsUrl = "http://gps-util-server:8081/getAttractions";
	
	public void setAttractionRewardPointsUrl(String attractionRewardPointsUrl) {
		this.attractionRewardPointsUrl = attractionRewardPointsUrl;
	}
	
	public void setAttractionsUrl(String attractionsUrl) {
		this.attractionsUrl = attractionsUrl;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	// This method calculate the user rewards.
	public void calculateRewards(User user) {

		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = getAttractions();

		for (VisitedLocation visitedLocation : userLocations) {
			for (Attraction attraction : attractions) {
				if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}

	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

	// This method retrieve the reward points.
	public int getRewardPoints(Attraction attraction, User user) {
		String URL = attractionRewardPointsUrl;
		Map<String, UUID> map = new HashMap<>();
		map.put("attractionId", attraction.attractionId);
		map.put("userId", user.getUserId());
		HttpEntity<Map<String, UUID>> entity = new HttpEntity<Map<String, UUID>>(map, null);
		ResponseEntity<Integer> response = restTemplate.exchange(URL, HttpMethod.POST, entity, Integer.class);
		return response.getBody();
	}

	// This method calculate the distance between two locations.
	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

	// This method retrieve the attractions.
	public List<Attraction> getAttractions() {
		String URL = attractionsUrl;
		ResponseEntity<List<Attraction>> response = restTemplate.exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>(){});
		return response.getBody();
	}

}
