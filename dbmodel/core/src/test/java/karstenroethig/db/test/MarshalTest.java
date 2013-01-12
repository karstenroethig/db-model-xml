package karstenroethig.db.test;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import karstenroethig.db.core.jaxb.database.Database;
import karstenroethig.db.core.jaxb.database.Entities;
import karstenroethig.db.core.jaxb.database.Entity;

import org.junit.Test;

public class MarshalTest {

//	@Test
	public void testMarshal() throws Exception {

        Database database = new Database();
        database.setCreateDate( "${timestamp}" );
        database.setName( "LineApp-Datenbank" );
        database.setSvnRevision( "${buildNumber}" );
        database.setVersion( "${project.version}" );

        Entities entities = new Entities();
        List<Entity> entityList = entities.getEntity();

        Entity entity = new Entity();
        entity.setName( "Headline" );
        entity.setCategory( "Headlines" );
        entityList.add( entity );

        entity = new Entity();
        entity.setName( "Scene" );
        entity.setCategory( "Headlines" );
        entityList.add( entity );

        entity = new Entity();
        entity.setName( "Attachment" );
        entity.setCategory( "Headlines" );
        entityList.add( entity );

        entity = new Entity();
        entity.setName( "MailingList" );
        entity.setCategory( "Veröffentlichung" );
        entityList.add( entity );

        entity = new Entity();
        entity.setName( "Contact" );
        entity.setCategory( "Veröffentlichung" );
        entityList.add( entity );

        entity = new Entity();
        entity.setName( "User" );
        entity.setCategory( "Verwaltung" );
        entityList.add( entity );

        entity = new Entity();
        entity.setName( "MailProperty" );
        entity.setCategory( "Verwaltung" );
        entityList.add( entity );

        entity = new Entity();
        entity.setName( "PropertyNumber" );
        entity.setCategory( "Verwaltung" );
        entityList.add( entity );

        database.setEntities( entities );

        JAXBContext context = JAXBContext.newInstance( Database.class );
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );

        marshaller.marshal( database, new File( "target/database.xml" ) );
	}
}
