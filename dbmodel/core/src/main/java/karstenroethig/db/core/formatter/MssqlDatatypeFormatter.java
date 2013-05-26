package karstenroethig.db.core.formatter;

import karstenroethig.db.core.dto.datatypes.AbstractDatatype;
import karstenroethig.db.core.dto.datatypes.DatatypeEnum;

import org.apache.commons.lang3.StringUtils;

public class MssqlDatatypeFormatter extends SimpleDatatypeFormatter {

	@Override
    public String format( AbstractDatatype datatype ) {

        if( datatype == null ) {
            return StringUtils.EMPTY;
        }
        
        if( datatype.getType() == DatatypeEnum.DATE ) {

            return "datetime";

        } else if( datatype.getType() == DatatypeEnum.TIMESTAMP ) {

            return "datetime";

        }
        
        return super.format( datatype );
    }
}
