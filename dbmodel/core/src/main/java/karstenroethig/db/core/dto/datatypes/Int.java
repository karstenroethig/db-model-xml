package karstenroethig.db.core.dto.datatypes;

public class Int extends AbstractDatatype {

	private Integer defaultValue;
	
	public Int() {
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
		return DatatypeEnum.INT;
	}

	@Override
	public String getSimpleDescription() {
		return "int";
	}

}
