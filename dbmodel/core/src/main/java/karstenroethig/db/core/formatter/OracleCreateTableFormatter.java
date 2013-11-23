package karstenroethig.db.core.formatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Entity;

import org.apache.commons.lang3.StringUtils;

public class OracleCreateTableFormatter implements IFormatter<Entity> {
	
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
		OracleDatatypeFormatter datatypeFormatter = new OracleDatatypeFormatter();
		
		for( Attribute attribute : entity.getAttributes() ) {
			
			String line = attributeLines.get( attribute );
			
			line += " ";
			line += datatypeFormatter.format( attribute.getDatatype() );
			
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
		sql.append( "commit;" );
		
		String sequences = formatForSequences( entity );
		
		if( sequences != null ) {
			sql.append( "\n\n" );
			sql.append( sequences );
		}
		
		return sql.toString();
	}
	
	private String formatForSequences( Entity entity ) {
		
		Attribute identityAttribute = null;
		
		for( Attribute attribute : entity.getAttributes() ) {
			
			if( attribute.hasIdentity() ) {
				identityAttribute = attribute;
			}
		}
		
		if( identityAttribute == null ) {
			return null;
		}
		
		StringBuffer seq = new StringBuffer();
		
		/*
		 * Sequence
		 */
		String sequenceName = "seq_" + entity.getName() + "_" + identityAttribute.getName();
		
		seq.append( "create sequence " );
		seq.append( sequenceName );
		seq.append( " start with " );
		seq.append( identityAttribute.getIdentity().getSeed() );
		seq.append( " increment by " );
		seq.append( identityAttribute.getIdentity().getIncrement() );
		seq.append( ";\ncommit;\n\n" );
		
		/*
		 * Trigger
		 */
		seq.append( "create or replace trigger tpk_" );
		seq.append( entity.getName() );
		seq.append( "_" );
		seq.append( identityAttribute.getName() );
		seq.append( " before insert on " );
		seq.append( entity.getName() );
		seq.append( "\n" );
		seq.append( "for each row\n" );
		seq.append( "begin\n" );
		seq.append( "  if :new." + identityAttribute.getName() + " is null then\n" );
		seq.append( "    select " +sequenceName + ".nextval into :new." + identityAttribute.getName() + " from dual;\n" );
		seq.append( "  end if;\n" );
		seq.append( "end;\n" );
		seq.append( "/\n" );
		seq.append( "commit;" );
		
		return seq.toString();
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
