package karstenroethig.db.core.formatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.Entity;

import org.apache.commons.lang3.StringUtils;

public class MysqlDropTablesFormatter implements IFormatter<Database> {
	
	private Map<Entity, String> entityLines = new HashMap<Entity, String>();

	@Override
	public String format( Database database ) {
		
		entityLines.clear();
		
		for( Entity entity : database.getEntities() ) {
			
			String line = "drop table " + entity.getName();
			
			entityLines.put( entity, line );
		}
		
		rightPadToMax();
		
		StringBuffer sql = new StringBuffer();
		boolean first = true;
		
		for( Entity entity : database.getEntities() ) {
			
			String line = entityLines.get( entity );
			
			if( first ) {
				first = false;
			} else {
				sql.append( "\n" );
			}
			
			sql.append( line );
			sql.append( " if exists;" );
		}
		
		return sql.toString();
	}
	
	private void rightPadToMax() {
		
		int max = 0;
		
		for( String line : entityLines.values() ) {
			
			if( line.length() > max ) {
				max = line.length();
			}
		}
		
		Set<Entity> entities = entityLines.keySet();
		
		for( Entity entity : entities ) {
			
			String line = entityLines.get( entity );
			
			entityLines.put( entity, StringUtils.rightPad( line, max ) );
		}
	}

}
