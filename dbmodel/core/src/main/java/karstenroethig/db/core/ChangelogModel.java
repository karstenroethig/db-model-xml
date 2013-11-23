package karstenroethig.db.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.Entity;
import karstenroethig.db.core.dto.changelog.Changelog;
import karstenroethig.db.core.dto.changelog.EntityChange;
import karstenroethig.db.core.dto.changelog.EntityChangelog;
import karstenroethig.db.core.formatter.DefaultValueFormatter;
import karstenroethig.db.core.formatter.SimpleDatatypeFormatter;
import karstenroethig.db.core.utils.DiffMatchPatch;
import karstenroethig.db.core.utils.DiffMatchPatch.Diff;

public class ChangelogModel {

	public static Changelog generateChangelog( Database databaseOld, Database databaseNew ) {
		
		Changelog changelog = new Changelog( databaseOld.getVersion(), databaseNew.getVersion() );
		
		List<EntityChangelog> entityChangelogs = generateEntityChangelogs( databaseOld, databaseNew );
		
		for( EntityChangelog entityChangelog : entityChangelogs ) {
			changelog.addEntityChangelog( entityChangelog );
		}
		
		return changelog;
	}
	
	private static List<EntityChangelog> generateEntityChangelogs( Database databaseOld, Database databaseNew ) {
		
		List<EntityChangelog> entityChangelogs = new ArrayList<EntityChangelog>();
		
		// first step: detect added and deleted entities
		Set<String> removedEntityNames = new HashSet<String>();
		
		for( Entity entityOld : databaseOld.getEntities() ) {
			
			String entityName = entityOld.getName();
			Entity entityNew = databaseNew.getEntityByName( entityName );
			
			if( entityNew == null ) {
				
				EntityChangelog entityChangelog = new EntityChangelog( entityName );
				
				entityChangelog.setText( "Die Entität wurde entfernt." );
				entityChangelogs.add( entityChangelog );
				
				removedEntityNames.add( entityName );
			}
		}
		
		for( Entity entityNew : databaseNew.getEntities() ) {
			
			String entityName = entityNew.getName();
			Entity entityOld = databaseOld.getEntityByName( entityName );
			
			if( entityOld == null ) {
				
				EntityChangelog entityChangelog = new EntityChangelog( entityName );
				
				entityChangelog.setText( "Die Entität wurde hinzugefügt." );
				entityChangelogs.add( entityChangelog );
			}
		}
		
		// differences of all entities
		for( Entity entityOld : databaseOld.getEntities() ) {
			
			String entityName = entityOld.getName();
			
			if( removedEntityNames.contains( entityName ) ) {
				continue;
			}
			
			Entity entityNew = databaseNew.getEntityByName( entityName );
			
			List<EntityChange> entityChanges = findEntityChanges( entityOld, entityNew );
			
			if( entityChanges != null && entityChanges.isEmpty() == false ) {
				
				EntityChangelog entityChangelog = new EntityChangelog( entityName );
				
				for( EntityChange entityChange : entityChanges ) {
					entityChangelog.addEntityChange( entityChange );
				}
				
				entityChangelogs.add( entityChangelog );
			}
		}
		
		return entityChangelogs;
	}
	
	private static List<EntityChange> findEntityChanges( Entity entityOld, Entity entityNew ) {
		
		List<EntityChange> entityChanges = new ArrayList<EntityChange>();
		
		// entity -> description
		String descriptionOld = entityOld.getDescription();
		String descriptionNew = entityNew.getDescription();
		
		if( StringUtils.equals( descriptionOld, descriptionNew ) == false ) {
			
			List<Diff> diffDetails = createDiffDetails( descriptionOld, descriptionNew );
			
			entityChanges.add( createEntityChange( null, "Beschreibung", descriptionOld, descriptionNew, diffDetails ) );
		}
		
		// entity -> properties
		entityChanges.addAll( findPropertyChanges( null, entityOld.getProperties(), entityNew.getProperties() ) );
		
		// compare all attributes
		Map<String, Attribute> newAttributes = new HashMap<String, Attribute>();
		
		for( Attribute attributeNew : entityNew.getAttributes() ) {
			newAttributes.put( attributeNew.getName(), attributeNew );
		}
		
		Set<String> oldAttributeNames = new HashSet<String>();
		
		for( Attribute attributeOld : entityOld.getAttributes() ) {
			
			String attributeName = attributeOld.getName();
			
			if( newAttributes.containsKey( attributeName ) ) {
				
				Attribute attributeNew = newAttributes.get( attributeName );
				
				// compare old and new
				entityChanges.addAll( findAttributeChanges( attributeOld, attributeNew ) );
			
			} else {
				
				// deleted attribute
				entityChanges.add( createEntityChange( attributeName, null, "Das Attribut wurde entfernt." ) );
			}
			
			oldAttributeNames.add( attributeName );
			
		}
		
		for( Attribute attributeNew : entityNew.getAttributes() ) {
			
			String attributeName = attributeNew.getName();
			
			if( oldAttributeNames.contains( attributeName ) == false ) {
				
				// added attribute
				entityChanges.add( createEntityChange( attributeName, null, "Das Attribut wurde hinzugefügt." ) );
			}
		}
		
		return entityChanges;
	}
	
	private static List<EntityChange> findAttributeChanges( Attribute attributeOld, Attribute attributeNew ) {
		
		List<EntityChange> attributeChanges = new ArrayList<EntityChange>();
		
		String attributeName = attributeOld.getName();
		
		// attribute -> primaryKey
		String pkOld = attributeOld.isPrimaryKey() ? "ja" : "nein";
		String pkNew = attributeNew.isPrimaryKey() ? "ja" : "nein";
		
		if( StringUtils.equals( pkOld, pkNew ) == false ) {
			attributeChanges.add( createEntityChange( attributeName, "Primary Key", pkOld, pkNew ) );
		}
		
		// attribute -> nullable
		String nullableOld = attributeOld.isNullable() ? "ja" : "nein";
		String nullableNew = attributeNew.isNullable() ? "ja" : "nein";
		
		if( StringUtils.equals( nullableOld, nullableNew ) == false ) {
			attributeChanges.add( createEntityChange( attributeName, "NULL erlaubt", nullableOld, nullableNew ) );
		}
		
		// attribute -> datatype
		SimpleDatatypeFormatter datatypeFormatter = new SimpleDatatypeFormatter();
		
		String datatypeStrOld = datatypeFormatter.format( attributeOld.getDatatype() );
		String datatypeStrNew = datatypeFormatter.format( attributeNew.getDatatype() );
		
		if( StringUtils.equals( datatypeStrOld, datatypeStrNew ) == false ) {
			attributeChanges.add( createEntityChange( attributeName, "Datentyp", datatypeStrOld, datatypeStrNew ) );
		}
		
		// attribute -> defaultValue
		DefaultValueFormatter defaultValueFormatter = new DefaultValueFormatter();
		
		String defaultValueStrOld = defaultValueFormatter.format( attributeOld.getDatatype() );
		String defaultValueStrNew = defaultValueFormatter.format( attributeNew.getDatatype() );
		
		if( StringUtils.equals( defaultValueStrOld, defaultValueStrNew ) == false ) {
			attributeChanges.add( createEntityChange( attributeName, "Default Value", defaultValueStrOld, defaultValueStrNew ) );
		}
		
		// attribute -> properties
		attributeChanges.addAll( findPropertyChanges( attributeName, attributeOld.getProperties(), attributeNew.getProperties() ) );
		
		// attribute -> description
		String descriptionOld = attributeOld.getDescription();
		String descriptionNew = attributeNew.getDescription();
		
		if( StringUtils.equals( descriptionOld, descriptionNew ) == false ) {
			
			List<Diff> diffDetails = createDiffDetails( descriptionOld, descriptionNew );
			
			attributeChanges.add( createEntityChange( attributeName, "Beschreibung", descriptionOld, descriptionNew, diffDetails ) );
		}
		
		return attributeChanges;
	}
	
	private static List<EntityChange> findPropertyChanges( String entityAttributeName, Map<String, String> propertiesOld, Map<String, String> propertiesNew ) {
		
		List<EntityChange> propertyChanges = new ArrayList<EntityChange>();
		
		List<String> allPropertyKeys = new ArrayList<String>();
		
		allPropertyKeys.addAll( propertiesOld.keySet() );
		
		for( String propertyKeyNew : propertiesNew.keySet() ) {
			
			if( allPropertyKeys.contains( propertyKeyNew ) == false ) {
				allPropertyKeys.add( propertyKeyNew );
			}
		}
		
		Collections.sort( allPropertyKeys );
		
		for( String propertyKey : allPropertyKeys ) {
			
			String propertyValueOld = null;
			String propertyValueNew = null;
			
			if( propertiesOld.containsKey( propertyKey ) ) {
				propertyValueOld = propertiesOld.get( propertyKey );
			} else {
				propertyChanges.add( createEntityChange( entityAttributeName, propertyKey, "Die Eigenschaft wurde hinzugefügt." ) );
				
				continue;
			}
			
			if( propertiesNew.containsKey( propertyKey ) ) {
				propertyValueNew = propertiesNew.get( propertyKey );
			} else {
				propertyChanges.add( createEntityChange( entityAttributeName, propertyKey, "Die Eigenschaft wurde entfernt." ) );
				
				continue;
			}
			
			if( StringUtils.equals( propertyValueOld, propertyValueNew ) == false ) {
				propertyChanges.add( createEntityChange( entityAttributeName, propertyKey, propertyValueOld, propertyValueNew ) );
			}
		}
		
		return propertyChanges;
	}
	
	private static List<Diff> createDiffDetails( String textOld, String textNew ) {
		
		if( StringUtils.isBlank( textOld ) || StringUtils.isBlank( textNew ) ) {
			return null;
		}
		
		/*
		 * http://code.google.com/p/java-diff-utils/
		 * http://code.google.com/p/google-diff-match-patch/
		 */
		DiffMatchPatch dmp = new DiffMatchPatch();
		LinkedList<Diff> diffs = dmp.diff_main( textOld, textNew );
		
		return diffs;
	}
	
	private static EntityChange createEntityChange( String entityAttributeName, String xmlAttributeName, String oldValue, String newValue ) {
		return createEntityChange( entityAttributeName, xmlAttributeName, oldValue, newValue, null );
	}
	
	private static EntityChange createEntityChange( String entityAttributeName, String xmlAttributeName, String oldValue, String newValue, List<Diff> diffDetails ) {
		
		EntityChange entityChange = new EntityChange();
		
		entityChange.setEntityAttributeName( entityAttributeName );
		entityChange.setXmlAttributeName( xmlAttributeName );
		entityChange.setOldValue( oldValue );
		entityChange.setNewValue( newValue );
		entityChange.setDiffDetails( diffDetails );
		
		return entityChange;
	}
	
	private static EntityChange createEntityChange( String entityAttributeName, String xmlAttributeName, String text ) {
		
		EntityChange entityChange = new EntityChange();
		
		entityChange.setEntityAttributeName( entityAttributeName );
		entityChange.setXmlAttributeName( xmlAttributeName );
		entityChange.setText( text );
		
		return entityChange;
	}
}
