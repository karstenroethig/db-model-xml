package karstenroethig.db.core.dto.datatypes;

public class Varchar extends AbstractDatatype {

	private Integer length;
	private String defaultValue;
	
	public Varchar() {
		this.length = 0;
		this.defaultValue = null;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public DatatypeEnum getType() {
		return DatatypeEnum.VARCHAR;
	}
}
