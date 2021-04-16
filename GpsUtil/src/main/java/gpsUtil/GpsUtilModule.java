package gpsutil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;

@Configuration
public class GpsUtilModule {

	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

}
