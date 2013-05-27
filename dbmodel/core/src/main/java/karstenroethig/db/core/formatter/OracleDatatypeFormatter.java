package karstenroethig.db.core.formatter;

import karstenroethig.db.core.dto.datatypes.AbstractDatatype;
import karstenroethig.db.core.dto.datatypes.DatatypeEnum;
import karstenroethig.db.core.dto.datatypes.Varchar;

import org.apache.commons.lang3.StringUtils;

public class OracleDatatypeFormatter extends SimpleDatatypeFormatter {

	@Override
    public String format( AbstractDatatype datatype ) {

        if( datatype == null ) {
            return StringUtils.EMPTY;
        }

        if( datatype.getType() == DatatypeEnum.VARCHAR ) {

            Varchar datatypeVarchar = ( Varchar )datatype;

            return "varchar2(" + datatypeVarchar.getLength() + ")";

        } else if( datatype.getType() == DatatypeEnum.BIT ) {

            return "int";

        } else if( datatype.getType() == DatatypeEnum.DATE ) {

            return "datetime";

        } else if( datatype.getType() == DatatypeEnum.BLOB ) {

            return "image";
        }
        
        return super.format( datatype );
    }
}
