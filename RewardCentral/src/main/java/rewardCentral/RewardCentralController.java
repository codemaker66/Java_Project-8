package rewardCentral;

import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import rewardCentral.service.RewardCentralService;

@RestController
public class RewardCentralController {

	@Autowired
	RewardCentralService rewardCentralService;

	@PostMapping(value = "/getAttractionRewardPoints")
	public int getAttractionRewardPoints(Map<String, UUID> map) {
		return rewardCentralService.getAttractionRewardPoints(map.get("attractionId"), map.get("userId"));
	}

}