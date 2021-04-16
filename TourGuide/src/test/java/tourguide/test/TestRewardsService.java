package tourguide.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

import tourguide.helper.InternalTestHelper;
import tourguide.model.Attraction;
import tourguide.model.User;
import tourguide.model.UserReward;
import tourguide.model.VisitedLocation;
import tourguide.service.RewardsService;
import tourguide.service.TourGuideService;

public class TestRewardsService {

	@Test
	public void userGetRewards() {
		RewardsService rewardsService = new RewardsService();
		rewardsService.setAttractionRewardPointsUrl("http://localhost:8082/getAttractionRewardPoints");
		rewardsService.setAttractionsUrl("http://localhost:8081/getAttractions");
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		tourGuideService.setUserLocationUrl("http://localhost:8081/getUserLocation");

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = rewardsService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		
		tourGuideService.trackUserLocation(user);
		rewardsService.calculateRewards(user);
		List<UserReward> userRewards = user.getUserRewards();
		
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		RewardsService rewardsService = new RewardsService();
		rewardsService.setAttractionsUrl("http://localhost:8081/getAttractions");
		Attraction attraction = rewardsService.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		RewardsService rewardsService = new RewardsService();
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		rewardsService.setAttractionRewardPointsUrl("http://localhost:8082/getAttractionRewardPoints");
		rewardsService.setAttractionsUrl("http://localhost:8081/getAttractions");
		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		
		tourGuideService.tracker.stopTracking();
		assertEquals(rewardsService.getAttractions().size(), userRewards.size());
	}

}
