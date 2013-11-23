package karstenroethig.db.test.validators;

import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.Entity;
import karstenroethig.db.core.dto.datatypes.DatatypeEnum;
import karstenroethig.db.test.validation.AbstractDatabaseModelValidator;
import karstenroethig.db.test.validation.ValidationResult;


/**
 * Prüfungen:
 * - Die IDENTITY-Eigenschaft kann nur für die Datentypen INT und BIGINT definiert werden.
 * - Eine Entität kann nur eine IDENTITY-Eigenschaft besitzen.
 */
public class IdentityValidator extends AbstractDatabaseModelValidator {

    public IdentityValidator( Database database ) {
        super( database );
    }

    public ValidationResult validate() {

        ValidationResult result = new ValidationResult();

        for( Entity entity : database.getEntities() ) {
        	
        	boolean entityHasIdentity = false;

            for( Attribute attribute : entity.getAttributes() ) {
            	
            	if( attribute.hasIdentity() ) {
            		
            		DatatypeEnum datatypeEnum = attribute.getDatatype().getType();
            		
            		if( datatypeEnum != DatatypeEnum.INT
            			&& datatypeEnum != DatatypeEnum.BIGINT ) {
            			
            			result.addError( "Die IDENTITY-Eigenschaft ist nur für die Datentypen INT und BIGINT zugelassen (Attribut: "
            			+ entity.getName() + "." + attribute.getName() + ")." );
            		}
            		
            		if( entityHasIdentity ) {
            			
            			result.addError( "Für die Entität '" + entity.getName() + "' wurden mehrere IDENTITY-Eigenschaften angegeben." );
            			
            			break;
            		}
            		
            		entityHasIdentity = true;
            	}

            }

        }

        return result;
    }

}
