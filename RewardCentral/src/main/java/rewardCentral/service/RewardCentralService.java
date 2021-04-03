package rewardCentral.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

@Service
public class RewardCentralService {

	private final RewardCentral rewardsCentral;

	public RewardCentralService(RewardCentral rewardsCentral) {
		this.rewardsCentral = rewardsCentral;
	}

	public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
		return rewardsCentral.getAttractionRewardPoints(attractionId, userId);
	}

}
