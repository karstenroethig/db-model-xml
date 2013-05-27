package karstenroethig.db.core.formatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Entity;
import karstenroethig.db.core.dto.Identity;

import org.apache.commons.lang3.StringUtils;

public class MssqlCreateTableFormatter implements IFormatter<Entity> {
	
	private Map<Attribute, String> attributeLines = new HashMap<Attribute, String>();

	@Override
	public String format( Entity entity ) {
		
		attributeLines.clear();
		
		boolean hasPrimaryKey = false;
		String primaryKey = "    constraint PK_" + entity.getName() + " primary key( ";
		
		/*
		 * Name + Primary key
		 */
		for( Attribute attribute : entity.getAttributes() ) {
			
			String line = "    " + attribute.getName();
			
			attributeLines.put( attribute, line );
			
			// Primary key
			if( attribute.isPrimaryKey() ) {
				
				if( hasPrimaryKey ) {
					primaryKey += ", ";
				} else {
					hasPrimaryKey = true;
				}
				
				primaryKey += attribute.getName();
			}
		}
		
		primaryKey += " )";
		
		rightPadToMax();
		
		/*
		 * Datatype
		 */
		MssqlDatatypeFormatter datatypeFormatter = new MssqlDatatypeFormatter();
		
		for( Attribute attribute : entity.getAttributes() ) {
			
			String line = attributeLines.get( attribute );
			
			line += " ";
			line += datatypeFormatter.format( attribute.getDatatype() );
			
			attributeLines.put( attribute, line );
		}
		
		rightPadToMax();
		
		/*
		 * Identity
		 */
		for( Attribute attribute : entity.getAttributes() ) {
			
			if( !attribute.hasIdentity() ) {
				continue;
			}
			
			Identity identity = attribute.getIdentity();
			String line = attributeLines.get( attribute );
			
			line += " identity(";
			line += identity.getSeed();
			line += ",";
			line += identity.getIncrement();
			line += ")";
			
			attributeLines.put( attribute, line );
		}
		
		rightPadToMax();
		
		/*
		 * Default value
		 */
		DefaultValueFormatter defaultValueFormatter = new DefaultValueFormatter();
		
		for( Attribute attribute : entity.getAttributes() ) {
			
			String line = attributeLines.get( attribute );
			
			String defaultValue = defaultValueFormatter.format( attribute.getDatatype() );
			
			if( StringUtils.isNotBlank( defaultValue ) ) {
			
				line += " default ";
				line += defaultValue;
				
				attributeLines.put( attribute, line );
			}
		}
		
		rightPadToMax();
		
		/*
		 * Nullable
		 */
		for( Attribute attribute : entity.getAttributes() ) {
			
			String line = attributeLines.get( attribute );
			
			if( attribute.isNullable() ) {
				line += " null";
			} else {
				line += " not null";
			}
			
			attributeLines.put( attribute, line );
		}
		
		/*
		 * Create SQL
		 */
		StringBuffer sql = new StringBuffer();
		
		sql.append( "create table " + entity.getName() + " (\n" );
		
		boolean first = true;
		
		for( Attribute attribute : entity.getAttributes() ) {
			
			if( first ) {
				first = false;
			} else {
				sql.append( ",\n" );
			}
			
			sql.append( attributeLines.get( attribute ) );
		}
		
		if( hasPrimaryKey ) {
			sql.append( ",\n" );
			sql.append( primaryKey );
		}
		
		sql.append( "\n" );
		sql.append( ")\n" );
		sql.append( "go" );
		
		return sql.toString();
	}
	
	private void rightPadToMax() {
		
		int max = 0;
		
		for( String line : attributeLines.values() ) {
			
			if( line.length() > max ) {
				max = line.length();
			}
		}
		
		Set<Attribute> attributes = attributeLines.keySet();
		
		for( Attribute attribute : attributes ) {
			
			String line = attributeLines.get( attribute );
			
			attributeLines.put( attribute, StringUtils.rightPad( line, max ) );
		}
	}

}
