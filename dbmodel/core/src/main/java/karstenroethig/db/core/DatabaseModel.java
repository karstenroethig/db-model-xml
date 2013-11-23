package karstenroethig.db.core;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.transform.Transformer;
import karstenroethig.db.core.locator.AbstractDatabaseLocator;

public class DatabaseModel {

	public static Database loadDatabaseModel( AbstractDatabaseLocator databaseLocator ) {
		
		if( databaseLocator == null ) {
			return null;
		}
		
		karstenroethig.db.core.jaxb.database.Database jaxbDatabase = null;
		Map<String, karstenroethig.db.core.jaxb.entity.Entity> jaxbEntitiesMap = null;
		
		try {
			
			// Database
			JAXBContext jaxbDbContext = JAXBContext.newInstance( karstenroethig.db.core.jaxb.database.Database.class );
			Unmarshaller dbUnmarshaller = jaxbDbContext.createUnmarshaller();
			
			jaxbDatabase = (karstenroethig.db.core.jaxb.database.Database)dbUnmarshaller.unmarshal( databaseLocator.resolve() );
			
			// Entities
			jaxbEntitiesMap = new HashMap<String, karstenroethig.db.core.jaxb.entity.Entity>();
			
			karstenroethig.db.core.jaxb.database.Entities jaxbDbEntities = jaxbDatabase.getEntities();
			
			if( jaxbDbEntities != null ) {
				
				JAXBContext jaxbEntityContext = JAXBContext.newInstance( karstenroethig.db.core.jaxb.entity.Entity.class );
				Unmarshaller entityUnmarshaller = jaxbEntityContext.createUnmarshaller();
				
				for( karstenroethig.db.core.jaxb.database.Entity jaxbDbEntity : jaxbDbEntities.getEntity() ) {
					
					if( jaxbDbEntity == null ) {
						continue;
					}
					
					URL url = databaseLocator.getEntitiesLocator().resolveByEntityName( jaxbDbEntity.getName() );
					
					karstenroethig.db.core.jaxb.entity.Entity jaxbEntity = (karstenroethig.db.core.jaxb.entity.Entity)entityUnmarshaller.unmarshal( url );
					
					jaxbEntitiesMap.put( jaxbDbEntity.getName(), jaxbEntity );
				}
			}
		
		}catch( JAXBException ex ) {
			return null;
		}
		
		// JAXB 2 DTO
		return Transformer.transformJaxb2Dto( jaxbDatabase, jaxbEntitiesMap );
	}

	public static Database loadDatabaseModel( File databaseXmlFile ) {
		
		if( databaseXmlFile == null || databaseXmlFile.exists() == false || databaseXmlFile.isDirectory() ) {
			return null;
		}
		
		File databaseXmlDirectory = databaseXmlFile.getParentFile();
		File entityXmlDirectory = new File( databaseXmlDirectory, "entities" );
		
		karstenroethig.db.core.jaxb.database.Database jaxbDatabase = null;
		Map<String, karstenroethig.db.core.jaxb.entity.Entity> jaxbEntitiesMap = null;
		
		try {
			
			// Database
			JAXBContext jaxbDbContext = JAXBContext.newInstance( karstenroethig.db.core.jaxb.database.Database.class );
			Unmarshaller dbUnmarshaller = jaxbDbContext.createUnmarshaller();
			
			jaxbDatabase = (karstenroethig.db.core.jaxb.database.Database)dbUnmarshaller.unmarshal( databaseXmlFile );
			
			// Entities
			jaxbEntitiesMap = new HashMap<String, karstenroethig.db.core.jaxb.entity.Entity>();
			
			karstenroethig.db.core.jaxb.database.Entities jaxbDbEntities = jaxbDatabase.getEntities();
			
			if( jaxbDbEntities != null ) {
				
				JAXBContext jaxbEntityContext = JAXBContext.newInstance( karstenroethig.db.core.jaxb.entity.Entity.class );
				Unmarshaller entityUnmarshaller = jaxbEntityContext.createUnmarshaller();
				
				for( karstenroethig.db.core.jaxb.database.Entity jaxbDbEntity : jaxbDbEntities.getEntity() ) {
					
					if( jaxbDbEntity == null ) {
						continue;
					}
					
					File entityXmlFile = new File( entityXmlDirectory, jaxbDbEntity.getName() + ".xml" );
					
					karstenroethig.db.core.jaxb.entity.Entity jaxbEntity = (karstenroethig.db.core.jaxb.entity.Entity)entityUnmarshaller.unmarshal( entityXmlFile );
					
					jaxbEntitiesMap.put( jaxbDbEntity.getName(), jaxbEntity );
				}
			}
		
		}catch( JAXBException ex ) {
			return null;
		}
		
		// JAXB 2 DTO
		return Transformer.transformJaxb2Dto( jaxbDatabase, jaxbEntitiesMap );
	}
	
    public static karstenroethig.db.core.jaxb.database.Database loadJaxbDatabaseModel( AbstractDatabaseLocator databaseLocator ) {

    	if( databaseLocator == null ) {
    		return null;
    	}

    	try {

    		// Database
    		JAXBContext jaxbDbContext = JAXBContext.newInstance( karstenroethig.db.core.jaxb.database.Database.class );
    		Unmarshaller dbUnmarshaller = jaxbDbContext.createUnmarshaller();

    		return ( karstenroethig.db.core.jaxb.database.Database )dbUnmarshaller.unmarshal( databaseLocator.resolve() );

    	} catch( JAXBException ex ) {
    	}

    	return null;
	}
	
}
