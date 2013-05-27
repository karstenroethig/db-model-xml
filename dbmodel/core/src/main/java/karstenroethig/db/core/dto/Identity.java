package karstenroethig.db.core.dto;

public class Identity {

	private Integer seed;
	private Integer increment;
	
	public Identity() {
		this.seed = 1;
		this.increment = 1;
	}
	
	public Identity( Integer seed, Integer increment ) {
		this.seed = seed;
		this.increment = increment;
	}

	public Integer getSeed() {
		return seed;
	}

	public void setSeed(Integer seed) {
		this.seed = seed;
	}

	public Integer getIncrement() {
		return increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}
}
