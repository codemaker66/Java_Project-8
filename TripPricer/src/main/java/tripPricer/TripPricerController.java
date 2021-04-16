package trippricer;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tripPricer.Provider;
import trippricer.service.TripPricerService;

@RestController
public class TripPricerController {

	@Autowired
	TripPricerService tripPricerService;

	@PostMapping(value = "/getPrice")
	public List<Provider> editPreferences(@RequestBody Map<String, String> map) {
		return tripPricerService.getPrice(map);
	}

}