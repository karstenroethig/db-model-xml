package karstenroethig.db.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "dd.MM.yyyy" );
	
	private static DateUtils singleton = null;
	
	private DateUtils() {
	}
	
	public static DateUtils getInstance() {
		
		if( singleton == null ) {
			
			synchronized ( DateUtils.class ) {
				
				if( singleton == null ) {
					singleton = new DateUtils();
				}
			}
		}
		
		return singleton;
	}
	
	public static Date parseDate( String dateString ) {
		return getInstance().syncParseDate( dateString );
	}
	
	public static String formatDate( Date date ) {
		return getInstance().syncFormatDate( date );
	}
	
	public synchronized Date syncParseDate( String dateString ) {
		
		if( StringUtils.isBlank( dateString ) ) {
			return null;
		}
		
		try{
			return DATE_FORMAT.parse( dateString );
		}catch( ParseException ex ) {
			// Nothing to do
		}
		
		return null;
	}
	
	public synchronized String syncFormatDate( Date date ) {
		
		if( date == null ) {
			return null;
		}
		
		return DATE_FORMAT.format( date );
	}
}
