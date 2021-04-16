package tourguide;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourguide.model.Location;
import tourguide.model.Output;
import tourguide.model.Preferences;
import tourguide.model.Provider;
import tourguide.model.User;
import tourguide.model.UserReward;
import tourguide.model.VisitedLocation;
import tourguide.service.TourGuideService;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}
	
	/**
	 * This method call the tourGuideService to retrieve the user location.
	 * 
	 * @param userName is the user name of the user.
	 * @return an object of type Location.
	 */
	@RequestMapping("/getLocation")
	public Location getLocation(@RequestParam String userName) {
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(tourGuideService.getUser(userName));
		return visitedLocation.location;
	}
	
	/**
	 * This method call the tourGuideService to retrieve nearby attractions.
	 * 
	 * @param userName is the user name of the user.
	 * @return a list of objects of type Output.
	 */
	@RequestMapping("/getNearbyAttractions")
	public List<Output> getNearbyAttractions(@RequestParam String userName) {
		User user = tourGuideService.getUser(userName);
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
		return tourGuideService.getNearByAttractions(visitedLocation, user);
	}
	
	/**
	 * This method call the tourGuideService to retrieve the rewards.
	 * 
	 * @param userName is the user name of the user.
	 * @return a list of objects of type UserReward.
	 */
	@RequestMapping("/getRewards")
	public List<UserReward> getRewards(@RequestParam String userName) {
		return tourGuideService.getUserRewards(tourGuideService.getUser(userName));
	}
	
	/**
	 * This method call the tourGuideService to retrieve all current locations.
	 * 
	 * @return a map.
	 */
	@RequestMapping("/getAllCurrentLocations")
	public Map<String, Location> getAllCurrentLocations() {
		return tourGuideService.getAllCurrentLocations();
	}
	
	/**
	 * This method call the tourGuideService to retrieve the trip deals.
	 * 
	 * @param userName is the user name of the user.
	 * @return a list of objects of type Provider.
	 */
	@RequestMapping("/getTripDeals")
	public List<Provider> getTripDeals(@RequestParam String userName) {
		return tourGuideService.getTripDeals(tourGuideService.getUser(userName));
	}
	
	/**
	 * This method call the tourGuideService to save the user preferences.
	 * 
	 * @param userName is the user name of the user.
	 * @param preferences is an object of type Preferences.
	 * @return a success message.
	 */
	@PostMapping(value = "/editPreferences")
	public String editPreferences(@RequestParam String userName, @RequestBody Preferences preferences) {
		tourGuideService.editPreferences(tourGuideService.getUser(userName), preferences);
		return "Preferences saved with success";
	}

}