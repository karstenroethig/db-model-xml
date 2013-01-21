package karstenroethig.db.core.dto.datatypes;

public class Date extends AbstractDatatype {

	private java.util.Date defaultValue;
	
	public Date() {
		this.defaultValue = null;
	}

	public java.util.Date getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(java.util.Date defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public DatatypeEnum getType() {
		return DatatypeEnum.DATE;
	}

	@Override
	public String getSimpleDescription() {
		return "date";
	}
}
