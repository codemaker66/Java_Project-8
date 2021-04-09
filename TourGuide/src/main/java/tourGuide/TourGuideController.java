package tourGuide;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourGuide.model.Location;
import tourGuide.model.Output;
import tourGuide.model.Preferences;
import tourGuide.model.Provider;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.model.VisitedLocation;
import tourGuide.service.TourGuideService;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	// This method call the tourGuideService to retrieve the user location.
	@RequestMapping("/getLocation")
	public Location getLocation(@RequestParam String userName) {
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(tourGuideService.getUser(userName));
		return visitedLocation.location;
	}

	// This method call the tourGuideService to retrieve nearby attractions.
	@RequestMapping("/getNearbyAttractions")
	public List<Output> getNearbyAttractions(@RequestParam String userName) {
		User user = tourGuideService.getUser(userName);
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
		return tourGuideService.getNearByAttractions(visitedLocation, user);
	}

	// This method call the tourGuideService to retrieve the rewards.
	@RequestMapping("/getRewards")
	public List<UserReward> getRewards(@RequestParam String userName) {
		return tourGuideService.getUserRewards(tourGuideService.getUser(userName));
	}

	// This method call the tourGuideService to retrieve all current locations.
	@RequestMapping("/getAllCurrentLocations")
	public Map<String, Location> getAllCurrentLocations() {
		return tourGuideService.getAllCurrentLocations();
	}

	// This method call the tourGuideService to retrieve the trip deals.
	@RequestMapping("/getTripDeals")
	public List<Provider> getTripDeals(@RequestParam String userName) {
		return tourGuideService.getTripDeals(tourGuideService.getUser(userName));
	}

	// This method call the tourGuideService to save the user preferences.
	@PostMapping(value = "/editPreferences")
	public String editPreferences(@RequestParam String userName, @RequestBody Preferences preferences) {
		tourGuideService.editPreferences(tourGuideService.getUser(userName), preferences);
		return "Preferences saved with success";
	}

}