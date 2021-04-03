package tourGuide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import tourGuide.service.TourGuideService;
import tourGuide.user.Provider;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.VisitedLocation;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	@RequestMapping("/getLocation")
	public String getLocation(@RequestParam String userName) {
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
	}

	@RequestMapping("/getNearbyAttractions")
	public String getNearbyAttractions(@RequestParam String userName) {
		User user = getUser(userName);
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
		return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation, user));
	}

	@RequestMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {
		return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
	}

	@RequestMapping("/getAllCurrentLocations")
	public String getAllCurrentLocations() {
		return JsonStream.serialize(tourGuideService.getAllCurrentLocations());
	}

	@RequestMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName) {
		List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
		return JsonStream.serialize(providers);
	}

	@PostMapping(value = "/editPreferences")
	public String editPreferences(@RequestParam String userName, @RequestBody UserPreferences preferences) {
		tourGuideService.editPreferences(getUser(userName), preferences);
		return JsonStream.serialize("Preferences saved with success");

	}

	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}

}