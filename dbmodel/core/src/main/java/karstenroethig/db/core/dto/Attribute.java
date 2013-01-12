package karstenroethig.db.core.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import karstenroethig.db.core.dto.datatypes.AbstractDatatype;

import org.apache.commons.lang3.StringUtils;

public class Attribute {

	private String name;
	private boolean primaryKey;
	private boolean nullable;
	private String descriptionShort;
	private String descriptionLong;
	private Integer fieldId;
	private String notation;
	
	private Map<String, String> properties;
	
	private AbstractDatatype datatype;
	
	public Attribute() {
		this.name = StringUtils.EMPTY;
		this.primaryKey = false;
		this.nullable = true;
		this.descriptionShort = StringUtils.EMPTY;
		this.descriptionLong = StringUtils.EMPTY;
		this.fieldId = null;
		this.notation = StringUtils.EMPTY;
		
		this.properties = new HashMap<String, String>();
		
		this.datatype = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public String getDescriptionLong() {
		return descriptionLong;
	}

	public void setDescriptionLong(String descriptionLong) {
		this.descriptionLong = descriptionLong;
	}

	public Integer getFieldId() {
		return fieldId;
	}

	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public String getNotation() {
		return notation;
	}

	public void setNotation(String notation) {
		this.notation = notation;
	}
	
	public Set<String> getPropertyKeys() {
		return properties.keySet();
	}

	public String getProperty( String key ) {
		
		if( StringUtils.isBlank( key ) ) {
			return null;
		}
		
		return properties.get( key );
	}

	public void addProperty( String key, String value ) {
		
		if( StringUtils.isBlank( key ) ) {
			return;
		}
		
		properties.put( key, value );
	}

	public AbstractDatatype getDatatype() {
		return datatype;
	}

	public void setDatatype(AbstractDatatype datatype) {
		this.datatype = datatype;
	}
	
}
