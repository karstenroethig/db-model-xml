package karstenroethig.db.core.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karstenroethig.db.core.dto.associations.AbstractAssociation;

import org.apache.commons.lang3.StringUtils;

public class Entity {

	private String name;
	private String description;
	
	private Map<String, String> properties;
	
	private List<Attribute> attributes;
	private List<AbstractAssociation> relationships;
	
	public Entity() {
		this.name = StringUtils.EMPTY;
		this.description = StringUtils.EMPTY;
		
		this.properties = new HashMap<String, String>();
		
		this.attributes = new ArrayList<Attribute>();
		this.relationships = new ArrayList<AbstractAssociation>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<String> getPropertyKeys() {
		return properties.keySet();
	}
	
	public boolean hasProperty( String key ) {
		
		if( StringUtils.isBlank( key ) ) {
			return false;
		}
		
		return properties.containsKey( key );
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

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void addAttribute( Attribute attribute ) {
		
		if( attribute == null ) {
			return;
		}
		
		attributes.add( attribute );
	}

	public List<AbstractAssociation> getRelationships() {
		return relationships;
	}

	public void addRelationship( AbstractAssociation relationship ) {
		
		if( relationship == null ) {
			return;
		}
		
		relationships.add( relationship );
	}
	
}
