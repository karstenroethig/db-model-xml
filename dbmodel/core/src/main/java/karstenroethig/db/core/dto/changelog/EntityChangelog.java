package karstenroethig.db.core.dto.changelog;

import java.util.ArrayList;
import java.util.List;

public class EntityChangelog {

	private String entityName;
	
	private String text;
	
	private List<EntityChange> entityChanges;
	
	public EntityChangelog( String entityName ) {
		this.entityName = entityName;
		
		this.entityChanges = new ArrayList<EntityChange>();
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText( String text ) {
		this.text = text;
	}
	
	public List<EntityChange> getEntityChanges() {
		return entityChanges;
	}
	
	public void addEntityChange( EntityChange entityChange ) {
		entityChanges.add( entityChange );
	}
}
