package karstenroethig.db.core.formatter;

import karstenroethig.db.core.dto.datatypes.AbstractDatatype;
import karstenroethig.db.core.dto.datatypes.Bigint;
import karstenroethig.db.core.dto.datatypes.Bit;
import karstenroethig.db.core.dto.datatypes.Char;
import karstenroethig.db.core.dto.datatypes.DatatypeEnum;
import karstenroethig.db.core.dto.datatypes.Date;
import karstenroethig.db.core.dto.datatypes.Datetime;
import karstenroethig.db.core.dto.datatypes.Decimal;
import karstenroethig.db.core.dto.datatypes.Int;
import karstenroethig.db.core.dto.datatypes.Timestamp;
import karstenroethig.db.core.dto.datatypes.Varchar;
import karstenroethig.db.core.utils.DateUtils;

import org.apache.commons.lang3.StringUtils;

public class DefaultValueFormatter implements IFormatter<AbstractDatatype> {

	@Override
    public String format( AbstractDatatype datatype ) {

        if( datatype == null ) {
            return StringUtils.EMPTY;
        }

        if( datatype.getType() == DatatypeEnum.DECIMAL ) {

            Decimal datatypeDecimal = ( Decimal )datatype;

            return formatDefaultValue( datatypeDecimal.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.CHAR ) {

            Char datatypeChar = ( Char )datatype;

            return formatDefaultValue( datatypeChar.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.VARCHAR ) {

            Varchar datatypeVarchar = ( Varchar )datatype;

            return formatDefaultValue( datatypeVarchar.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.INT ) {

            Int datatypeInt = ( Int )datatype;

            return formatDefaultValue( datatypeInt.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.BIGINT ) {

            Bigint datatypeBigint = ( Bigint )datatype;

            return formatDefaultValue( datatypeBigint.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.BIT ) {

            Bit datatypeBit = ( Bit )datatype;

            return formatDefaultValue( datatypeBit.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.DATETIME ) {

            Datetime datatypeDatetime = ( Datetime )datatype;

            return formatDefaultValue( datatypeDatetime.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.DATE ) {

            Date datatypeDate = ( Date )datatype;

            return formatDefaultValue( datatypeDate.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.TIMESTAMP ) {

            Timestamp datatypeTimestamp = ( Timestamp )datatype;

            return formatDefaultValue( datatypeTimestamp.getDefaultValue() );

        } else if( datatype.getType() == DatatypeEnum.BLOB ) {

            return formatDefaultValue( ( String )null );
        }

        throw new IllegalArgumentException( "unknown datatype" );
    }

    private String formatDefaultValue( Number defaultValue ) {

        if( defaultValue != null ) {
            return defaultValue.toString();
        }

        return StringUtils.EMPTY;
    }

    private String formatDefaultValue( java.util.Date defaultValue ) {

        String defaultValueStr = null;

        if( defaultValue != null ) {
            defaultValueStr = DateUtils.formatDate( defaultValue );
        }

        return formatDefaultValue( defaultValueStr );
    }


    private String formatDefaultValue( String defaultValue ) {

        if( defaultValue == null ) {
            return StringUtils.EMPTY;
        }

        return "'" + defaultValue + "'";
    }
}
