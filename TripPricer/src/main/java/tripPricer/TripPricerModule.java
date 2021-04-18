package tripPricer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TripPricerModule {

	@Bean
	public TripPricer getTripPricer() {
		return new TripPricer();
	}

}
