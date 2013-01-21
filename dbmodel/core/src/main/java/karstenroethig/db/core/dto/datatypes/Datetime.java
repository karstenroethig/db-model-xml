package karstenroethig.db.core.dto.datatypes;

import java.util.Date;

public class Datetime extends AbstractDatatype {

	private Date defaultValue;
	
	public Datetime() {
		this.defaultValue = null;
	}

	public Date getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Date defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public DatatypeEnum getType() {
		return DatatypeEnum.DATETIME;
	}

	@Override
	public String getSimpleDescription() {
		return "datetime";
	}
}
