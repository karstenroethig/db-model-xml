package karstenroethig.db.core.formatter;

import karstenroethig.db.core.dto.datatypes.AbstractDatatype;
import karstenroethig.db.core.dto.datatypes.Blob;
import karstenroethig.db.core.dto.datatypes.Char;
import karstenroethig.db.core.dto.datatypes.DatatypeEnum;
import karstenroethig.db.core.dto.datatypes.Date;
import karstenroethig.db.core.dto.datatypes.Datetime;
import karstenroethig.db.core.dto.datatypes.Decimal;
import karstenroethig.db.core.dto.datatypes.Int;
import karstenroethig.db.core.dto.datatypes.Timestamp;
import karstenroethig.db.core.dto.datatypes.Varchar;

import org.apache.commons.lang3.StringUtils;

public class SimpleDatatypeFormatter implements IFormatter<AbstractDatatype> {

	@Override
	public String format( AbstractDatatype datatype ) {
		
		if( datatype == null ) {
			return StringUtils.EMPTY;
		}
		
		if( datatype.getType() == DatatypeEnum.DECIMAL ) {
			return formatDecimal( (Decimal)datatype );
		} else if( datatype.getType() == DatatypeEnum.CHAR ) {
			return formatChar( (Char)datatype );
		} else if( datatype.getType() == DatatypeEnum.VARCHAR ) {
			return formatVarchar( (Varchar)datatype );
		} else if( datatype.getType() == DatatypeEnum.INT ) {
			return formatInt( (Int)datatype );
		} else if( datatype.getType() == DatatypeEnum.DATETIME ) {
			return formatDatetime( (Datetime)datatype );
		} else if( datatype.getType() == DatatypeEnum.DATE ) {
			return formatDate( (Date)datatype );
		} else if( datatype.getType() == DatatypeEnum.TIMESTAMP ) {
			return formatTimestamp( (Timestamp)datatype );
		} else if( datatype.getType() == DatatypeEnum.BLOB ) {
			return formatBlob( (Blob)datatype );
		}
		
		throw new IllegalArgumentException( "unknown datatype" );
	}
	
	private String formatDecimal( Decimal datatype ) {

		StringBuffer desc = new StringBuffer();
		
		desc.append( "decimal(" );
		desc.append( datatype.getPrecision() );
		
		if( datatype.getScale() > 0 ) {
			desc.append( ", " );
			desc.append( datatype.getScale() );
		}
		
		desc.append( ")" );
		
		return desc.toString();
	}
	
	private String formatChar( Char datatype ) {
		return "char(" + datatype.getLength() + ")";
	}
	
	private String formatVarchar( Varchar datatype ) {
		return "varchar(" + datatype.getLength() + ")";
	}
	
	private String formatInt( Int datatype ) {
		return "int";
	}
	
	private String formatDatetime( Datetime datatype ) {
		return "datetime";
	}
	
	private String formatDate( Date datatype ) {
		return "date";
	}
	
	private String formatTimestamp( Timestamp datatype ) {
		return "timestamp";
	}
	
	private String formatBlob( Blob datatype ) {
		return "image";
	}
}
