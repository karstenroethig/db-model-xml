package karstenroethig.db.core.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class Database {

	private static final List<Entity> EMPTY_ENTITY_LIST = new ArrayList<Entity>();
	
	private String name;
	private String version;
	private Date createDate;
	private Integer svnRevision;
	
	private List<String> categories;
	private List<Entity> entities;
	
	private Map<String, List<Entity>> entitiesByCategory;
	private Map<String, Entity> entitiesByName;
	
	public Database() {
		this.name = StringUtils.EMPTY;
		this.version = StringUtils.EMPTY;
		this.createDate = new Date( 0l );
		this.svnRevision = 0;
		
		this.categories = new ArrayList<String>();
		this.entities = new ArrayList<Entity>();
		
		this.entitiesByCategory = new HashMap<String, List<Entity>>();
		this.entitiesByName = new HashMap<String, Entity>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getSvnRevision() {
		return svnRevision;
	}

	public void setSvnRevision(Integer svnRevision) {
		this.svnRevision = svnRevision;
	}

	public List<String> getCategories() {
		return categories;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public List<Entity> getEntitiesByCategory( String category ) {
		
		if( category == null ) {
			return EMPTY_ENTITY_LIST;
		}
		
		if( entitiesByCategory.containsKey( category ) ) {
			
			List<Entity> entities = entitiesByCategory.get( category );
			
			if( entities == null ) {
				return EMPTY_ENTITY_LIST;
			}
			
			return entities;
		}
		
		return EMPTY_ENTITY_LIST;
	}

	public Entity getEntityByName( String name ) {
		
		if( StringUtils.isBlank( name ) ) {
			return null;
		}
		
		if( entitiesByName.containsKey( name ) ) {
			return entitiesByName.get( name );
		}
		
		return null;
	}
	
	public void addEntity( Entity entity, String category ) {
		
		if( entity == null || StringUtils.isBlank( entity.getName() ) ) {
			return;
		}
		
		entities.add( entity );
		entitiesByName.put( entity.getName(), entity );
		
		if( StringUtils.isNotBlank( category ) ) {
			
			if( categories.contains( category ) == false ) {
				categories.add( category );
			}
			
			if( entitiesByCategory.containsKey( category ) == false ) {
				entitiesByCategory.put( category, new ArrayList<Entity>() );
			}
			
			List<Entity> entitesByCategoryList = entitiesByCategory.get( category );
			
			entitesByCategoryList.add( entity );
		}
	}
	
}
