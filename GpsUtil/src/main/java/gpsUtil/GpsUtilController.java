package gpsutil;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import gpsutil.service.GpsUtilService;

@RestController
public class GpsUtilController {

	@Autowired
	GpsUtilService gpsUtilService;

	@PostMapping("/getUserLocation")
	public VisitedLocation getUserLocation(@RequestBody UUID id) {
		return gpsUtilService.getUserLocation(id);
	}

	@RequestMapping("/getAttractions")
	public List<Attraction> getAttractions() {
		return gpsUtilService.getAttractions();
	}

}