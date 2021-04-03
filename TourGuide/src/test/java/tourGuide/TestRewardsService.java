package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.junit.Test;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.Attraction;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tourGuide.user.VisitedLocation;

public class TestRewardsService {

	@Test
	public void userGetRewards() {
		Locale.setDefault(new Locale("en", "US", "WIN"));
		RewardsService rewardsService = new RewardsService();

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

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
		Attraction attraction = rewardsService.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {

		RewardsService rewardsService = new RewardsService();
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);

		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();
		assertEquals(rewardsService.getAttractions().size(), userRewards.size());
	}

}
