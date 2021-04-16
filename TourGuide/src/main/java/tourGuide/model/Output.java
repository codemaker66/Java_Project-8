package tourguide.model;

public class Output {

	private String attractionName;
	private Location attractionLocation;
	private Location userLocation;
	private double distance;
	private int attractionRewardPoints;

	public String getAttractionName() {
		return attractionName;
	}

	public void setAttractionName(String attractionName) {
		this.attractionName = attractionName;
	}

	public Location getAttractionLocation() {
		return attractionLocation;
	}

	public void setAttractionLocation(Location attractionLocation) {
		this.attractionLocation = attractionLocation;
	}

	public Location getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getAttractionRewardPoints() {
		return attractionRewardPoints;
	}

	public void setAttractionRewardPoints(int attractionRewardPoints) {
		this.attractionRewardPoints = attractionRewardPoints;
	}

}
