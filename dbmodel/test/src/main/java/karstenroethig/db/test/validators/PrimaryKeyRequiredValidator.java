package karstenroethig.db.test.validators;

import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.Entity;
import karstenroethig.db.test.validation.AbstractDatabaseModelValidator;
import karstenroethig.db.test.validation.ValidationResult;


/**
 * Prüfung: Für jede Entität muss ein Primärschlüssel angegeben werden.
 */
public class PrimaryKeyRequiredValidator extends AbstractDatabaseModelValidator {

    public PrimaryKeyRequiredValidator( Database database ) {
        super( database );
    }

    public ValidationResult validate() {

        ValidationResult result = new ValidationResult();

        for( Entity entity : database.getEntities() ) {

            boolean hasPrimaryKey = false;

            for( Attribute attribute : entity.getAttributes() ) {
            	
            	if( attribute.isPrimaryKey() ) {
            		hasPrimaryKey = true;
            		break;
            	}

            }
            
            if( hasPrimaryKey == false ) {
            	result.addError( "Für die Entität '" + entity.getName() + "' wurde kein Primärschlüssel angegeben." );
            }

        }

        return result;
    }

}
