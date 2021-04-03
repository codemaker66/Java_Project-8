package tourGuide;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.User;
import tourGuide.model.VisitedLocation;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

public class TestPerformance {

	private ExecutorService executorService = Executors.newFixedThreadPool(700);
	
	// Users should be incremented up to 100,000, and test finishes within 15 minutes
	@Test
	public void highVolumeTrackLocation() throws InterruptedException, ExecutionException {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(100000);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		allUsers.forEach(u -> completableFutureList.add(CompletableFuture.runAsync(() -> {
			tourGuideService.trackUserLocation(u);
		}, executorService)));
		
		CompletableFuture<Void> results = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
		results.get();
		
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	// Users should be incremented up to 100,000, and test finishes within 20 minutes
	@Test
	public void highVolumeGetRewards() throws InterruptedException, ExecutionException {
		RewardsService rewardsService = new RewardsService();
		InternalTestHelper.setInternalUserNumber(100000);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
		Attraction attraction = rewardsService.getAttractions().get(0);
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));
		
		allUsers.forEach(u -> completableFutureList.add(CompletableFuture.runAsync(() -> {
			rewardsService.calculateRewards(u);
		}, executorService)));
		
		CompletableFuture<Void> results = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
		results.get();
		
		for (User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
