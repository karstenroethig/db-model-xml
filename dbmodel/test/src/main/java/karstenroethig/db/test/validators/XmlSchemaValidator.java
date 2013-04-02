package karstenroethig.db.test.validators;

import karstenroethig.db.core.DatabaseModel;
import karstenroethig.db.core.jaxb.database.Database;
import karstenroethig.db.core.jaxb.database.Entities;
import karstenroethig.db.core.jaxb.database.Entity;
import karstenroethig.db.core.locator.AbstractDatabaseLocator;
import karstenroethig.db.core.locator.AbstractEntitiesLocator;
import karstenroethig.db.core.schema.XmlSchemaLocator;
import karstenroethig.db.core.schema.XmlSchemaLocator.XmlSchemaTypeEnum;
import karstenroethig.db.test.validation.AbstractDatabaseLocatorValidator;
import karstenroethig.db.test.validation.ValidationResult;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;


public class XmlSchemaValidator extends AbstractDatabaseLocatorValidator {

    private static final String SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

    public XmlSchemaValidator( AbstractDatabaseLocator databaseLocator ) {
        super( databaseLocator );
    }

    public ValidationResult validate() {

        ValidationResult result = new ValidationResult();

        /*
         * Validatoren für die XML-Schemas erstellen
         */
        SchemaFactory factory = SchemaFactory.newInstance( SCHEMA_LANGUAGE );
        XmlSchemaLocator xmlSchemaLocator = new XmlSchemaLocator();

        Validator databaseValidator = null;
        Validator entityValidator = null;

        try {
            URL databaseSchemaUrl = xmlSchemaLocator.resolveByXmlSchemaType( XmlSchemaTypeEnum.database );
            Schema databaseSchema = factory.newSchema( databaseSchemaUrl );
            databaseValidator = databaseSchema.newValidator();

            URL entitySchemaUrl = xmlSchemaLocator.resolveByXmlSchemaType( XmlSchemaTypeEnum.entity );
            Schema entitySchema = factory.newSchema( entitySchemaUrl );
            entityValidator = entitySchema.newValidator();

        } catch( SAXException ex ) {
            result.addError( "Fehler bei der Erstellung der XML-Schema-Validatoren", ex );
        }

        if( ( databaseValidator == null ) || ( entityValidator == null ) ) {
            result.addError( "Die XML-Schema-Validatoren wurden nicht vollständig erstellt." );

            return result;
        }

        /*
         * Locator-Klassen für die Ermittlung der XML-Dateien prüfen
         */
        AbstractDatabaseLocator databaseLocator = getDatabaseLocator();

        if( databaseLocator == null ) {
            result.addError( "Es wurde kein DatabaseLocator angegeben." );

            return result;
        }

        AbstractEntitiesLocator entitiesLocator = databaseLocator.getEntitiesLocator();

        if( entitiesLocator == null ) {
            result.addError( "Es wurde kein EntitiesLocator angegeben." );

            return result;
        }

        /*
         * Allgemeine XML-Datei für Datenbankbeschreibung prüfen
         */
        URL databaseUrl = databaseLocator.resolve();

        if( databaseUrl == null ) {
            result.addError( "Es wurde keine XML-Datei für die Datenbank gefunden." );

            return result;
        }

        try {
            parseXml( databaseUrl, databaseValidator );

        } catch( SAXParseException ex ) {

            result.addError( databaseUrl + " -> " + ex.getMessage() + " (Line " + ex.getLineNumber() + ")", ex );

            return result;

        } catch( SAXException ex ) {

            result.addError( "Fehler beim Prüfen der XML-Datei " + databaseUrl, ex );

            return result;

        } catch( IOException ex ) {

            result.addError( "Fehler beim Prüfen der XML-Datei " + databaseUrl, ex );

            return result;
        }

        /*
         * XML-Dateien der einzelnen Entitäten prüfen
         */
        Database jaxbDatabase = DatabaseModel.loadJaxbDatabaseModel( databaseLocator );
        Entities jaxbEntities = jaxbDatabase.getEntities();

        if( ( jaxbEntities == null ) || ( jaxbEntities.getEntity() == null ) || jaxbEntities.getEntity().isEmpty() ) {
            result.addError( "Für die Datenbank wurden keine Entitäten angegeben." );

            return result;
        }

        for( Entity jaxbEntity : jaxbEntities.getEntity() ) {

            URL entityUrl = entitiesLocator.resolveByEntityName( jaxbEntity.getName() );

            if( entityUrl == null ) {
                result.addError( "Keine XML-Datei gefunden für Entität " + jaxbEntity.getName() );

                continue;
            }

            try {
                parseXml( entityUrl, entityValidator );

            } catch( SAXParseException ex ) {

                result.addError( entityUrl + " -> " + ex.getMessage() + " (Line " + ex.getLineNumber() + ")", ex );

            } catch( SAXException ex ) {

                result.addError( "Fehler beim Prüfen der XML-Datei " + entityUrl, ex );

            } catch( IOException ex ) {

                result.addError( "Fehler beim Prüfen der XML-Datei " + entityUrl, ex );
            }

        }

        return result;
    }

    private void parseXml( URL xmlUrl, Validator validator ) throws SAXParseException, SAXException, IOException {

        InputStream in = null;

        try {

            in = xmlUrl.openStream();
            validator.validate( new StreamSource( in ) );

//        } catch( SAXParseException ex ) {
//
//            System.out.println( xmlUrl );
//            System.out.println( ex.getMessage() + " (Line " + ex.getLineNumber() + ")" );

        } finally {

            if( in != null ) {

                try {
                    in.close();
                } catch( IOException ex ) {
                    // Nothing to do
                }
            }
        }

    }

}
