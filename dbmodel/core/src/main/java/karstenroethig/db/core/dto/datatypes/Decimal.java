package karstenroethig.db.core.dto.datatypes;

public class Decimal extends AbstractDatatype {

	private Integer precision;
	private Integer scale;
	private Long defaultValue;
	
	public Decimal() {
		this.precision = 0;
		this.scale = 0;
		this.defaultValue = null;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public Long getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Long defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public DatatypeEnum getType() {
		return DatatypeEnum.DECIMAL;
	}

}
