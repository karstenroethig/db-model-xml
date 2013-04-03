package karstenroethig.db.core.dto.datatypes;

public class Bit extends AbstractDatatype {

	private Integer defaultValue;
	
	public Bit() {
		this.defaultValue = null;
	}

	public Integer getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Integer defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public DatatypeEnum getType() {
		return DatatypeEnum.BIT;
	}

}
