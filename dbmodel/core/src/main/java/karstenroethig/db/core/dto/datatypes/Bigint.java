package karstenroethig.db.core.dto.datatypes;

public class Bigint extends AbstractDatatype {

	private Integer defaultValue;
	
	public Bigint() {
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
		return DatatypeEnum.BIGINT;
	}

}
