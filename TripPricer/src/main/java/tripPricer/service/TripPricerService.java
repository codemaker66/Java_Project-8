package tripPricer.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TripPricerService {

	private final TripPricer tripPricer;

	public TripPricerService(TripPricer tripPricer) {
		this.tripPricer = tripPricer;
	}

	public List<Provider> getPrice(Map<String, String> map) {
		return tripPricer.getPrice(map.get("tripPricerApiKey"), UUID.fromString(map.get("userId")),
				Integer.parseInt(map.get("numberOfAdults")), Integer.parseInt(map.get("getNumberOfChildren")),
				Integer.parseInt(map.get("tripDuration")), Integer.parseInt(map.get("cumulatativeRewardPoints")));
	}

}
