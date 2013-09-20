package karstenroethig.db.test.validators;

import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.Entity;
import karstenroethig.db.test.validation.AbstractDatabaseModelValidator;
import karstenroethig.db.test.validation.ValidationResult;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;


/**
 * Prüfung: Der Attribut-Name darf nur einmal pro Entität vorkommen.
 */
public class AttributeUniquePerEntityValidator extends AbstractDatabaseModelValidator {

    public AttributeUniquePerEntityValidator( Database database ) {
        super( database );
    }

    public ValidationResult validate() {

        ValidationResult result = new ValidationResult();

        for( Entity entity : database.getEntities() ) {

            Set<String> attributeNames = new HashSet<String>();

            for( Attribute attribute : entity.getAttributes() ) {

                String attrNameLowerCase = StringUtils.lowerCase( attribute.getName() );

                if( attributeNames.contains( attrNameLowerCase ) ) {

                    result.addError( "Das Attribut '" + attribute.getName() + "' wurde in der Entität '" +
                        entity.getName() + "' mehrfach angegeben." );

                } else {

                    attributeNames.add( attrNameLowerCase );

                }

            }

        }

        return result;
    }

}
