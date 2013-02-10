package karstenroethig.db.core.dto.transform;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.Entity;
import karstenroethig.db.core.dto.associations.AbstractAssociation;
import karstenroethig.db.core.dto.associations.AssociationTypeEnum;
import karstenroethig.db.core.dto.associations.JoinColumn;
import karstenroethig.db.core.dto.associations.ManyToOne;
import karstenroethig.db.core.dto.associations.OneToMany;
import karstenroethig.db.core.dto.associations.OneToOne;
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
import karstenroethig.db.core.utils.DateUtils;

import org.apache.commons.lang3.StringUtils;

public class Transformer {

	public static Database transformJaxb2Dto( karstenroethig.db.core.jaxb.database.Database jaxbDatabase,
		Map<String, karstenroethig.db.core.jaxb.entity.Entity> jaxbEntitiesMap ) {

		if( ( jaxbDatabase == null ) || ( jaxbEntitiesMap == null ) ) {
			return null;
		}
		
		Database database = new Database();
		
		database.setName( jaxbDatabase.getName() );
		database.setVersion( jaxbDatabase.getVersion() );
		
		java.util.Date createDate = DateUtils.parseDate( jaxbDatabase.getCreateDate() );
		
		if( createDate != null ) {
			database.setCreateDate( createDate );
		}
		
		if( StringUtils.isNotBlank( jaxbDatabase.getSvnRevision() ) &&
			StringUtils.isNumeric( jaxbDatabase.getSvnRevision() ) ) {
			
			try {
				database.setSvnRevision( Integer.parseInt( jaxbDatabase.getSvnRevision() ) );
			} catch( NumberFormatException ex ) {
				// Nothing to do
			}
		}
		
		// Entities
		karstenroethig.db.core.jaxb.database.Entities jaxbDbEntities = jaxbDatabase.getEntities();
		
		if( jaxbDbEntities != null ) {
			
			for( karstenroethig.db.core.jaxb.database.Entity jaxbDbEntity : jaxbDbEntities.getEntity() ) {
				
				if( jaxbDbEntity == null ) {
					continue;
				}
				
				karstenroethig.db.core.jaxb.entity.Entity jaxbEntity = jaxbEntitiesMap.get( jaxbDbEntity.getName() );
				
				if( jaxbEntity == null ) {
					continue;
				}
				
				Entity entity = transformJaxb2Dto( jaxbEntity );
				
				if( entity != null ) {
					database.addEntity( entity, jaxbDbEntity.getCategory() );
				}
				
			}
			
		}
		
		return database;
	}
	
	public static Entity transformJaxb2Dto( karstenroethig.db.core.jaxb.entity.Entity jaxbEntity ) {
		
		if( jaxbEntity == null ) {
			return null;
		}
		
		Entity entity = new Entity();
		
		entity.setName( jaxbEntity.getName() );
		entity.setDescription( jaxbEntity.getDescription() );
		
		// Properties
		karstenroethig.db.core.jaxb.entity.Properties jaxbProperties = jaxbEntity.getProperties();
		
		if( jaxbProperties != null ) {
			
			for( karstenroethig.db.core.jaxb.entity.Property jaxbProperty : jaxbProperties.getProperty() ) {
				
				if( jaxbProperty == null ) {
					continue;
				}
				
				entity.addProperty( jaxbProperty.getKey(), jaxbProperty.getValue() );
			
			}
			
		}
		
		// Attributes
		karstenroethig.db.core.jaxb.entity.Attributes jaxbAttributes = jaxbEntity.getAttributes();
		
		if( jaxbAttributes != null ) {
			
			for( karstenroethig.db.core.jaxb.entity.Attribute jaxbAttribute : jaxbAttributes.getAttribute() ) {
				
				if( jaxbAttribute == null ) {
					continue;
				}
				
				Attribute attribute = transformJaxb2Dto( jaxbAttribute );
				
				if( attribute != null ) {
					entity.addAttribute( attribute );
				}
				
			}
			
		}
		
		// Relationships
		karstenroethig.db.core.jaxb.entity.Relationships jaxbRelationships = jaxbEntity.getRelationships();
		
		if( jaxbRelationships != null ) {
			
			for( karstenroethig.db.core.jaxb.entity.OneToOne jaxbOneToOne : jaxbRelationships.getOneToOne() ) {
				
				if( jaxbOneToOne == null ) {
					continue;
				}
				
				OneToOne oneToOne = transformJaxb2Dto( jaxbOneToOne );
				
				if( oneToOne != null ) {
					entity.addRelationship( oneToOne );
				}
				
			}
			
			for( karstenroethig.db.core.jaxb.entity.OneToMany jaxbOneToMany : jaxbRelationships.getOneToMany() ) {
				
				if( jaxbOneToMany == null ) {
					continue;
				}
				
				OneToMany oneToMany = transformJaxb2Dto( jaxbOneToMany );
				
				if( oneToMany != null ) {
					entity.addRelationship( oneToMany );
				}
				
			}
			
			for( karstenroethig.db.core.jaxb.entity.ManyToOne jaxbManyToOne : jaxbRelationships.getManyToOne() ) {
				
				if( jaxbManyToOne == null ) {
					continue;
				}
				
				ManyToOne manyToOne = transformJaxb2Dto( jaxbManyToOne );
				
				if( manyToOne != null ) {
					entity.addRelationship( manyToOne );
				}
				
			}
			
		}
		
		return entity;
	}
	
	public static Attribute transformJaxb2Dto( karstenroethig.db.core.jaxb.entity.Attribute jaxbAttribute ) {
		
		if( jaxbAttribute == null ) {
			return null;
		}
		
		Attribute attribute = new Attribute();
		
		attribute.setName( jaxbAttribute.getName() );
		attribute.setDescription( jaxbAttribute.getDescription() );
		
		if( jaxbAttribute.isPrimaryKey() != null ) {
			attribute.setPrimaryKey( jaxbAttribute.isPrimaryKey() );
		}
		
		if( jaxbAttribute.isNullable() != null ) {
			attribute.setNullable( jaxbAttribute.isNullable() );
		}
		
		// Datentyp
		karstenroethig.db.core.jaxb.entity.Datatype jaxbDatatype = jaxbAttribute.getDatatype();
		
		if( jaxbDatatype != null ) {
			
			AbstractDatatype datatype = transformJaxb2Dto( jaxbDatatype );
			
			if( datatype != null ) {
				attribute.setDatatype( datatype );
			}
		}
		
		// Properties
		karstenroethig.db.core.jaxb.entity.Properties jaxbProperties = jaxbAttribute.getProperties();
		
		if( jaxbProperties != null ) {
			
			for( karstenroethig.db.core.jaxb.entity.Property jaxbProperty : jaxbProperties.getProperty() ) {
				
				if( jaxbProperty == null ) {
					continue;
				}
				
				attribute.addProperty( jaxbProperty.getKey(), jaxbProperty.getValue() );
				
			}
			
		}
		
		return attribute;
	}
	
	public static AbstractDatatype transformJaxb2Dto( karstenroethig.db.core.jaxb.entity.Datatype jaxbDatatype ) {
		
		if( jaxbDatatype == null ) {
			return null;
		}
		
		// Decimal
		if( jaxbDatatype.getType() == karstenroethig.db.core.jaxb.entity.DatatypeEnum.DECIMAL ) {
			
			Decimal datatype = new Decimal();
			
			if( jaxbDatatype.getPrecision() != null ) {
				datatype.setPrecision( jaxbDatatype.getPrecision().intValue() );
			}
			
			if( jaxbDatatype.getScale() != null ) {
				datatype.setScale( jaxbDatatype.getScale().intValue() );
			}
			
			if( StringUtils.isNotBlank( jaxbDatatype.getDefaultValue() ) && StringUtils.isNumeric( jaxbDatatype.getDefaultValue() ) ) {
				
				try {
					datatype.setDefaultValue( Long.parseLong( jaxbDatatype.getDefaultValue() ) );
				} catch( NumberFormatException ex ) {
					// Nothing to do
				}
				
			}
			
			return datatype;
		}
		
		// Char
		if( jaxbDatatype.getType() == karstenroethig.db.core.jaxb.entity.DatatypeEnum.CHAR ) {
			
			Char datatype = new Char();
			
			if( jaxbDatatype.getLength() != null ) {
				datatype.setLength( jaxbDatatype.getLength().intValue() );
			}
			
			if( jaxbDatatype.getDefaultValue() != null ) {
				datatype.setDefaultValue( StringUtils.substring( jaxbDatatype.getDefaultValue(), 0, datatype.getLength() ) );
			}
			
			return datatype;
		}
		
		// Varchar
		if( jaxbDatatype.getType() == karstenroethig.db.core.jaxb.entity.DatatypeEnum.VARCHAR ) {
			
			Varchar datatype = new Varchar();
			
			if( jaxbDatatype.getLength() != null ) {
				datatype.setLength( jaxbDatatype.getLength().intValue() );
			}
			
			if( jaxbDatatype.getDefaultValue() != null ) {
				datatype.setDefaultValue( StringUtils.substring( jaxbDatatype.getDefaultValue(), 0, datatype.getLength() ) );
			}
			
			return datatype;
		}
		
		// Int
		if( jaxbDatatype.getType() == karstenroethig.db.core.jaxb.entity.DatatypeEnum.INT ) {
			
			Int datatype = new Int();
			
			if( StringUtils.isNotBlank( jaxbDatatype.getDefaultValue() ) && StringUtils.isNumeric( jaxbDatatype.getDefaultValue() ) ) {
				
				try {
					datatype.setDefaultValue( Integer.parseInt( jaxbDatatype.getDefaultValue() ) );
				} catch( NumberFormatException ex ) {
					// Nothing to do
				}
				
			}
			
			return datatype;
		}
		
		// Datetime
		if( jaxbDatatype.getType() == karstenroethig.db.core.jaxb.entity.DatatypeEnum.DATETIME ) {
			
			Datetime datatype = new Datetime();
			
			datatype.setDefaultValue( DateUtils.parseDate( jaxbDatatype.getDefaultValue() ) ); 
			
			return datatype;
		}
		
		// Date
		if( jaxbDatatype.getType() == karstenroethig.db.core.jaxb.entity.DatatypeEnum.DATE ) {
			
			Date datatype = new Date();
			
			datatype.setDefaultValue( DateUtils.parseDate( jaxbDatatype.getDefaultValue() ) );
			
			return datatype;
		}
		
		// Timestamp
		if( jaxbDatatype.getType() == karstenroethig.db.core.jaxb.entity.DatatypeEnum.TIMESTAMP ) {
			
			Timestamp datatype = new Timestamp();
			
			datatype.setDefaultValue( DateUtils.parseDate( jaxbDatatype.getDefaultValue() ) );
			
			return datatype;
		}
		
		// Blob
		if( jaxbDatatype.getType() == karstenroethig.db.core.jaxb.entity.DatatypeEnum.BLOB ) {
			return new Blob();
		}
		
		return null;
	}
	
	public static OneToOne transformJaxb2Dto( karstenroethig.db.core.jaxb.entity.OneToOne jaxbOneToOne ) {

		if( jaxbOneToOne == null ) {
			return null;
		}
		
		OneToOne oneToOne = new OneToOne();
		
		oneToOne.setTargetEntity( jaxbOneToOne.getTargetEntity() );
		
		// JoinColumn
		List<karstenroethig.db.core.jaxb.entity.JoinColumn> jaxbJoinColumns = jaxbOneToOne.getJoinColumn();
		
		if( jaxbJoinColumns != null ) {
			
			for( karstenroethig.db.core.jaxb.entity.JoinColumn jaxbJoinColumn : jaxbJoinColumns ) {
				
				JoinColumn joinColumn = transformJaxb2Dto( jaxbJoinColumn );
				
				if( joinColumn != null ) {
					oneToOne.addJoinColumn( joinColumn );
				}
			}
			
		}
		
		return oneToOne;
	}
	
	public static OneToMany transformJaxb2Dto( karstenroethig.db.core.jaxb.entity.OneToMany jaxbOneToMany ) {

		if( jaxbOneToMany == null ) {
			return null;
		}
		
		OneToMany oneToMany = new OneToMany();
		
		oneToMany.setTargetEntity( jaxbOneToMany.getTargetEntity() );
		
		// JoinColumn
		List<karstenroethig.db.core.jaxb.entity.JoinColumn> jaxbJoinColumns = jaxbOneToMany.getJoinColumn();
		
		if( jaxbJoinColumns != null ) {
			
			for( karstenroethig.db.core.jaxb.entity.JoinColumn jaxbJoinColumn : jaxbJoinColumns ) {
				
				JoinColumn joinColumn = transformJaxb2Dto( jaxbJoinColumn );
				
				if( joinColumn != null ) {
					oneToMany.addJoinColumn( joinColumn );
				}
			}
			
		}
		
		return oneToMany;
	}
	
	public static ManyToOne transformJaxb2Dto( karstenroethig.db.core.jaxb.entity.ManyToOne jaxbManyToOne ) {

		if( jaxbManyToOne == null ) {
			return null;
		}
		
		ManyToOne manyToOne = new ManyToOne();
		
		manyToOne.setTargetEntity( jaxbManyToOne.getTargetEntity() );
		
		// JoinColumn
		List<karstenroethig.db.core.jaxb.entity.JoinColumn> jaxbJoinColumns = jaxbManyToOne.getJoinColumn();
		
		if( jaxbJoinColumns != null ) {
			
			for( karstenroethig.db.core.jaxb.entity.JoinColumn jaxbJoinColumn : jaxbJoinColumns ) {
				
				JoinColumn joinColumn = transformJaxb2Dto( jaxbJoinColumn );
				
				if( joinColumn != null ) {
					manyToOne.addJoinColumn( joinColumn );
				}
			}
			
		}
		
		return manyToOne;
	}
	
	public static JoinColumn transformJaxb2Dto( karstenroethig.db.core.jaxb.entity.JoinColumn jaxbJoinColumn ) {
		
		if( jaxbJoinColumn == null ) {
			return null;
		}
		
		JoinColumn joinColumn = new JoinColumn();
		
		joinColumn.setName( jaxbJoinColumn.getName() );
		
		if( StringUtils.isNotBlank( jaxbJoinColumn.getReferencedColumnName() ) ) {
			joinColumn.setReferencedColumnName( jaxbJoinColumn.getReferencedColumnName() );
		} else {
			joinColumn.setReferencedColumnName( joinColumn.getName() );
		}
		
		return joinColumn;
	}
	
	public static karstenroethig.db.core.jaxb.database.Database transformDto2JaxbDatabase( Database database ) {
		
		if( database == null ) {
			return null;
		}
		
		karstenroethig.db.core.jaxb.database.ObjectFactory jaxbDbObjectFactory = new karstenroethig.db.core.jaxb.database.ObjectFactory();
		
		karstenroethig.db.core.jaxb.database.Database jaxbDatabse = jaxbDbObjectFactory.createDatabase();
		
		jaxbDatabse.setName( database.getName() );
		jaxbDatabse.setVersion( database.getVersion() );
		jaxbDatabse.setCreateDate( DateUtils.formatDate( database.getCreateDate() ) );
		
		if( database.getSvnRevision() != null ) {
			jaxbDatabse.setSvnRevision( database.getSvnRevision().toString() );
		}
		
		// Entities
		karstenroethig.db.core.jaxb.database.Entities jaxbDbEntities = jaxbDbObjectFactory.createEntities();
		
		List<String> categories = database.getCategories();
		
		if( categories != null && categories.isEmpty() == false ) {
			
			for( String category : categories ) {
				
				for( Entity entity : database.getEntitiesByCategory( category ) ) {
					
					karstenroethig.db.core.jaxb.database.Entity jaxbDbEntity = jaxbDbObjectFactory.createEntity();
					
					jaxbDbEntity.setName( entity.getName() );
					jaxbDbEntity.setCategory(category);
					
					jaxbDbEntities.getEntity().add( jaxbDbEntity );
				}
				
			}
			
		} else {
			
			for( Entity entity : database.getEntities() ) {
				
				karstenroethig.db.core.jaxb.database.Entity jaxbDbEntity = jaxbDbObjectFactory.createEntity();
				
				jaxbDbEntity.setName( entity.getName() );
				
				jaxbDbEntities.getEntity().add( jaxbDbEntity );
			}
			
		}
		
		jaxbDatabse.setEntities( jaxbDbEntities );
		
		return jaxbDatabse;
	}
	
	public static Map<String, karstenroethig.db.core.jaxb.entity.Entity> transformDto2JaxbEntities( Database database ) {
		
		if( database == null ) {
			return null;
		}
		
		karstenroethig.db.core.jaxb.entity.ObjectFactory jaxbObjectFactory = new karstenroethig.db.core.jaxb.entity.ObjectFactory();
		
		Map<String, karstenroethig.db.core.jaxb.entity.Entity> jaxbEntitiesMap = new HashMap<String, karstenroethig.db.core.jaxb.entity.Entity>();
		
		for( Entity entity : database.getEntities() ) {
			
			karstenroethig.db.core.jaxb.entity.Entity jaxbEntity = jaxbObjectFactory.createEntity();
			
			jaxbEntity.setName( entity.getName() );
			jaxbEntity.setDescription( entity.getDescription() );
			
			// Properties
			Set<String> propertyKeys = entity.getPropertyKeys();
			
			if( propertyKeys.isEmpty() == false ) {
				
				karstenroethig.db.core.jaxb.entity.Properties jaxbProperties = jaxbObjectFactory.createProperties();
				
				for( String propertyKey : propertyKeys ) {
					
					String propertyValue = entity.getProperty( propertyKey );
					
					karstenroethig.db.core.jaxb.entity.Property jaxbProperty = jaxbObjectFactory.createProperty();
					
					jaxbProperty.setKey( propertyKey );
					jaxbProperty.setValue( propertyValue );
					
					jaxbProperties.getProperty().add( jaxbProperty );
				}
				
				jaxbEntity.setProperties( jaxbProperties );
			}
			
			// Attributes
			List<Attribute> attributes = entity.getAttributes();
			
			if( attributes != null && attributes.isEmpty() == false ) {
				
				karstenroethig.db.core.jaxb.entity.Attributes jaxbAttributes = jaxbObjectFactory.createAttributes();
				
				for( Attribute attribute : attributes ) {
					
					karstenroethig.db.core.jaxb.entity.Attribute jaxbAttribute = jaxbObjectFactory.createAttribute();
					
					jaxbAttribute.setName( attribute.getName() );
					jaxbAttribute.setDescription( attribute.getDescription() );
					jaxbAttribute.setPrimaryKey( attribute.isPrimaryKey() );
					jaxbAttribute.setNullable( attribute.isNullable() );
					
					// Properties
					propertyKeys = attribute.getPropertyKeys();
					
					if( propertyKeys.isEmpty() == false ) {
						
						karstenroethig.db.core.jaxb.entity.Properties jaxbProperties = jaxbObjectFactory.createProperties();
						
						for( String propertyKey : propertyKeys ) {
							
							String propertyValue = attribute.getProperty( propertyKey );
							
							karstenroethig.db.core.jaxb.entity.Property jaxbProperty = jaxbObjectFactory.createProperty();
							
							jaxbProperty.setKey( propertyKey );
							jaxbProperty.setValue( propertyValue );
							
							jaxbProperties.getProperty().add( jaxbProperty );
						}
						
						jaxbAttribute.setProperties( jaxbProperties );
					}
					
					// Datatype
					karstenroethig.db.core.jaxb.entity.Datatype jaxbDatatype = jaxbObjectFactory.createDatatype();
					
					AbstractDatatype datatype = attribute.getDatatype();
					
					if( datatype.getType() == DatatypeEnum.DECIMAL ) {
						
						Decimal decimalType = (Decimal)datatype;
						
						jaxbDatatype.setType( karstenroethig.db.core.jaxb.entity.DatatypeEnum.DECIMAL );
						
						if( decimalType.getPrecision() != null ) {
							jaxbDatatype.setPrecision( new BigInteger( decimalType.getPrecision() + StringUtils.EMPTY ) );
						}
						
						if( decimalType.getScale() != null ) {
							jaxbDatatype.setScale( new BigInteger( decimalType.getScale() + StringUtils.EMPTY ) );
						}
						
						if( decimalType.getDefaultValue() != null ) {
							jaxbDatatype.setDefaultValue( decimalType.getDefaultValue().toString() );
						}
						
					} else if( datatype.getType() == DatatypeEnum.CHAR ) {

						Char charType = (Char)datatype;
						
						jaxbDatatype.setType( karstenroethig.db.core.jaxb.entity.DatatypeEnum.CHAR );
						
						if( charType.getLength() != null ) {
							jaxbDatatype.setLength( new BigInteger( charType.getLength() + StringUtils.EMPTY ) );
						}
						
						jaxbDatatype.setDefaultValue( charType.getDefaultValue() );
						
					} else if( datatype.getType() == DatatypeEnum.VARCHAR ) {

						Varchar varcharType = (Varchar)datatype;
						
						jaxbDatatype.setType( karstenroethig.db.core.jaxb.entity.DatatypeEnum.VARCHAR );
						
						if( varcharType.getLength() != null ) {
							jaxbDatatype.setLength( new BigInteger( varcharType.getLength() + StringUtils.EMPTY ) );
						}
						
						jaxbDatatype.setDefaultValue( varcharType.getDefaultValue() );
						
					} else if( datatype.getType() == DatatypeEnum.INT ) {

						Int intType = (Int)datatype;
						
						jaxbDatatype.setType( karstenroethig.db.core.jaxb.entity.DatatypeEnum.INT );

						if( intType.getDefaultValue() != null ) {
							jaxbDatatype.setDefaultValue( intType.getDefaultValue().toString() );
						}
						
					} else if( datatype.getType() == DatatypeEnum.DATETIME ) {

						Datetime datetimeType = (Datetime)datatype;
						
						jaxbDatatype.setType( karstenroethig.db.core.jaxb.entity.DatatypeEnum.DATETIME );
						jaxbDatatype.setDefaultValue( DateUtils.formatDate( datetimeType.getDefaultValue() ) );
						
					} else if( datatype.getType() == DatatypeEnum.DATE ) {

						Date dateType = (Date)datatype;
						
						jaxbDatatype.setType( karstenroethig.db.core.jaxb.entity.DatatypeEnum.DATE );
						jaxbDatatype.setDefaultValue( DateUtils.formatDate( dateType.getDefaultValue() ) );
						
					} else if( datatype.getType() == DatatypeEnum.TIMESTAMP ) {

						Timestamp timestampType = (Timestamp)datatype;
						
						jaxbDatatype.setType( karstenroethig.db.core.jaxb.entity.DatatypeEnum.TIMESTAMP );
						jaxbDatatype.setDefaultValue( DateUtils.formatDate( timestampType.getDefaultValue() ) );
						
					} else if( datatype.getType() == DatatypeEnum.BLOB ) {

						jaxbDatatype.setType( karstenroethig.db.core.jaxb.entity.DatatypeEnum.BLOB );
						
					}
					
					jaxbAttribute.setDatatype( jaxbDatatype );
					
					jaxbAttributes.getAttribute().add( jaxbAttribute );
					
				}
				
				jaxbEntity.setAttributes( jaxbAttributes );
			}
			
			// Relationships
			List<AbstractAssociation> relationships = entity.getRelationships();
			
			if( relationships != null && relationships.isEmpty() == false ) {
				
				karstenroethig.db.core.jaxb.entity.Relationships jaxbRelationships = jaxbObjectFactory.createRelationships();
				
				for( AbstractAssociation relationship : relationships ) {
					
					// JoinColumn
					List<karstenroethig.db.core.jaxb.entity.JoinColumn> jaxbJoinColumns = new ArrayList<karstenroethig.db.core.jaxb.entity.JoinColumn>();
					
					for( JoinColumn joinColumn : relationship.getJoinColumns() ) {
						
						karstenroethig.db.core.jaxb.entity.JoinColumn jaxbJoinColumn = jaxbObjectFactory.createJoinColumn();
						
						jaxbJoinColumn.setName( joinColumn.getName() );
						
						if( StringUtils.equals( joinColumn.getName(), joinColumn.getReferencedColumnName() ) == false ) {
							jaxbJoinColumn.setReferencedColumnName( joinColumn.getReferencedColumnName() );
						}
						
						jaxbJoinColumns.add( jaxbJoinColumn );
					}
					
					// OneToOne
					if( relationship.getAssociationType() == AssociationTypeEnum.ONE_TO_ONE ) {
						
						karstenroethig.db.core.jaxb.entity.OneToOne jaxbOneToOne = jaxbObjectFactory.createOneToOne();
						
						jaxbOneToOne.setTargetEntity( relationship.getTargetEntity() );
						jaxbOneToOne.getJoinColumn().addAll( jaxbJoinColumns );
						
						jaxbRelationships.getOneToOne().add( jaxbOneToOne );
					}
					
					// OneToMany
					if( relationship.getAssociationType() == AssociationTypeEnum.ONE_TO_MANY ) {
						
						karstenroethig.db.core.jaxb.entity.OneToMany jaxbOneToMany = jaxbObjectFactory.createOneToMany();
						
						jaxbOneToMany.setTargetEntity( relationship.getTargetEntity() );
						jaxbOneToMany.getJoinColumn().addAll( jaxbJoinColumns );
						
						jaxbRelationships.getOneToMany().add( jaxbOneToMany );
					}
					
					// ManyToOne
					if( relationship.getAssociationType() == AssociationTypeEnum.MANY_TO_ONE ) {
						
						karstenroethig.db.core.jaxb.entity.ManyToOne jaxbManyToOne = jaxbObjectFactory.createManyToOne();
						
						jaxbManyToOne.setTargetEntity( relationship.getTargetEntity() );
						jaxbManyToOne.getJoinColumn().addAll( jaxbJoinColumns );
						
						jaxbRelationships.getManyToOne().add( jaxbManyToOne );
					}
					
				}
				
				jaxbEntity.setRelationships( jaxbRelationships );
			}
			
			jaxbEntitiesMap.put( entity.getName(), jaxbEntity );
		}
		
		return jaxbEntitiesMap;
	}

}