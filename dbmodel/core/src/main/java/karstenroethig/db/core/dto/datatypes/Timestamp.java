package karstenroethig.db.core.dto.datatypes;

import java.util.Date;

public class Timestamp extends AbstractDatatype {

	private Date defaultValue;
	
	public Timestamp() {
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
		return DatatypeEnum.TIMESTAMP;
	}
}
