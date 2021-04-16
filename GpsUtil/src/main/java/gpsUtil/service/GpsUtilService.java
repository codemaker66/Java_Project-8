package gpsutil.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

@Service
public class GpsUtilService {

	private final GpsUtil gpsUtil;

	public GpsUtilService(GpsUtil gpsUtil) {
		this.gpsUtil = gpsUtil;
	}

	public VisitedLocation getUserLocation(UUID id) {
		Locale.setDefault(new Locale("en", "US", "WIN"));
		return gpsUtil.getUserLocation(id);
	}

	public List<Attraction> getAttractions() {
		return gpsUtil.getAttractions();
	}

}
