package karstenroethig.db.core.formatter;

import karstenroethig.db.core.dto.datatypes.AbstractDatatype;
import karstenroethig.db.core.dto.datatypes.Char;
import karstenroethig.db.core.dto.datatypes.DatatypeEnum;
import karstenroethig.db.core.dto.datatypes.Decimal;
import karstenroethig.db.core.dto.datatypes.Varchar;

import org.apache.commons.lang3.StringUtils;

public class SimpleDatatypeFormatter implements IFormatter<AbstractDatatype> {

	@Override
    public String format( AbstractDatatype datatype ) {

        if( datatype == null ) {
            return StringUtils.EMPTY;
        }

        if( datatype.getType() == DatatypeEnum.DECIMAL ) {

            Decimal datatypeDecimal = ( Decimal )datatype;

            StringBuffer desc = new StringBuffer();

            desc.append( "decimal(" );
            desc.append( datatypeDecimal.getPrecision() );

            if( datatypeDecimal.getScale() > 0 ) {
                desc.append( ", " );
                desc.append( datatypeDecimal.getScale() );
            }

            desc.append( ")" );

            return desc.toString();

        } else if( datatype.getType() == DatatypeEnum.CHAR ) {

            Char datatypeChar = ( Char )datatype;

            return "char(" + datatypeChar.getLength() + ")";

        } else if( datatype.getType() == DatatypeEnum.VARCHAR ) {

            Varchar datatypeVarchar = ( Varchar )datatype;

            return "varchar(" + datatypeVarchar.getLength() + ")";

        } else if( datatype.getType() == DatatypeEnum.INT ) {

            return "int";

        } else if( datatype.getType() == DatatypeEnum.BIGINT ) {

            return "bigint";

        } else if( datatype.getType() == DatatypeEnum.BIT ) {

            return "bit";

        } else if( datatype.getType() == DatatypeEnum.DATETIME ) {

            return "date";

        } else if( datatype.getType() == DatatypeEnum.DATE ) {

            return "datetime";

        } else if( datatype.getType() == DatatypeEnum.TIMESTAMP ) {

            return "timestamp";

        } else if( datatype.getType() == DatatypeEnum.BLOB ) {

            return "image";
        }

        throw new IllegalArgumentException( "unknown datatype" );
    }
}
