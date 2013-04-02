package karstenroethig.db.test;

import karstenroethig.db.core.DatabaseModel;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.locator.AbstractDatabaseLocator;
import karstenroethig.db.test.validation.IValidator;
import karstenroethig.db.test.validation.ValidationMessage;
import karstenroethig.db.test.validation.ValidationResult;
import karstenroethig.db.test.validators.UniqueAttributePerEntityValidator;
import karstenroethig.db.test.validators.XmlSchemaValidator;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class AbstractCommonTest extends TestCase {

    protected Collection<IValidator> getErrorValidators( Database database ) {

        List<IValidator> validators = new ArrayList<IValidator>();

        validators.add( new XmlSchemaValidator( getDatabaseLocator() ) );
        validators.add( new UniqueAttributePerEntityValidator( database ) );

        return validators;
    }

    protected Collection<IValidator> getWarningValidators( Database database ) {
        return new ArrayList<IValidator>();
    }

    @Test
    public void testValidators() {

        /*
         * Datenbank-Modell auslesen
         */
        assertNotNull( getDatabaseLocator() );

        Database database = DatabaseModel.loadDatabaseModel( getDatabaseLocator() );

        assertNotNull( database );

        /*
         * ERROR-Prüfungen ausführen
         */
        boolean failed = false;
        Collection<IValidator> errorValidators = getErrorValidators( database );

        for( IValidator validator : errorValidators ) {

            ValidationResult result = validator.validate();

            if( ( result != null ) && result.hasErrors() ) {

                failed = true;
                System.out.println( validator.getClass().getName() + " -> ERROR" );

                for( ValidationMessage validationMessage : result.getErrors() ) {

                    System.out.println( "- " + validationMessage.getMessage() );
                }

            } else {

                System.out.println( validator.getClass().getName() + " -> OK" );
            }

        }

        /*
         * WARNING-Prüfungen ausführen
         */
        Collection<IValidator> warningValidators = getWarningValidators( database );

        for( IValidator validator : warningValidators ) {

            ValidationResult result = validator.validate();

            if( ( result != null ) && result.hasErrors() ) {

                System.out.println( validator.getClass().getName() + " -> WARNING" );

                for( ValidationMessage validationMessage : result.getErrors() ) {

                    System.out.println( "- " + validationMessage.getMessage() );
                }

            } else {

                System.out.println( validator.getClass().getName() + " -> OK" );
            }

        }

        assertFalse( failed );
    }

    protected abstract AbstractDatabaseLocator getDatabaseLocator();

}
