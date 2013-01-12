package karstenroethig.db.test;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import karstenroethig.db.core.jaxb.entity.Attribute;
import karstenroethig.db.core.jaxb.entity.Attributes;
import karstenroethig.db.core.jaxb.entity.Datatype;
import karstenroethig.db.core.jaxb.entity.DatatypeEnum;
import karstenroethig.db.core.jaxb.entity.Entity;
import karstenroethig.db.core.jaxb.entity.JoinColumn;
import karstenroethig.db.core.jaxb.entity.ManyToOne;
import karstenroethig.db.core.jaxb.entity.OneToMany;
import karstenroethig.db.core.jaxb.entity.Properties;
import karstenroethig.db.core.jaxb.entity.Property;
import karstenroethig.db.core.jaxb.entity.Relationships;

import org.junit.Test;

public class MarshalEntityTest {

//	@Test
	public void testMarshal() throws Exception {
		
		Entity entity = new Entity();
		
		entity.setName("Headline");
		entity.setDescriptionShort("Kurzbeschreibung");
		entity.setDescriptionLong("Lange Beschreibung");
		
		// Entity -> Properteis
		Properties props = new Properties();
		List<Property> propsList = props.getProperty();
		
		Property prop = new Property();
		prop.setKey("key1");
		prop.setValue("value1");
		propsList.add(prop);
		
		prop = new Property();
		prop.setKey("key2");
		prop.setValue("value2");
		propsList.add(prop);
		
		entity.setProperties(props);
		
		// Attributes
		Attributes attrs = new Attributes();
		List<Attribute> attrsList = attrs.getAttribute();
		
		attrsList.add(createAttr("id", Boolean.TRUE, Boolean.FALSE, DatatypeEnum.DECIMAL));
		attrsList.add(createAttr("todoId", Boolean.FALSE, Boolean.FALSE, DatatypeEnum.DECIMAL));
		attrsList.add(createAttr("dateCreated", Boolean.FALSE, Boolean.FALSE, DatatypeEnum.DATETIME));
		attrsList.add(createAttr("lastUpdated", Boolean.FALSE, Boolean.TRUE, DatatypeEnum.DATETIME));
		
		entity.setAttributes(attrs);
		
		// Relationships
		Relationships rels = new Relationships();
		List<OneToMany> oneToManyList = rels.getOneToMany();
		
		OneToMany oneToMany = new OneToMany();
		oneToMany.setTargetEntity("TODO");
		
		List<JoinColumn> joinColList = oneToMany.getJoinColumn();
		
		JoinColumn joinColumn = new JoinColumn();
		joinColumn.setName("id");
		joinColumn.setReferencedColumnName("todoId");
		joinColList.add(joinColumn);
		oneToManyList.add(oneToMany);
		
		List<ManyToOne> manyToOneList = rels.getManyToOne();
		
		ManyToOne manyToOne = new ManyToOne();
		manyToOne.setTargetEntity("TODO");
		
		joinColList = manyToOne.getJoinColumn();
		
		joinColumn = new JoinColumn();
		joinColumn.setName("todoId");
		joinColumn.setReferencedColumnName("id");
		joinColList.add(joinColumn);
		manyToOneList.add(manyToOne);
		
		entity.setRelationships(rels);

        JAXBContext context = JAXBContext.newInstance( Entity.class );
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );

        marshaller.marshal( entity, new File( "target/" + entity.getName() + ".xml" ) );
	}
	
	private Attribute createAttr(String name, Boolean pk, Boolean nullable, DatatypeEnum type) {
		
		Attribute attr = new Attribute();
		attr.setName(name);
		attr.setPrimaryKey(pk);
		attr.setNullable(nullable);
		attr.setFieldId(new BigInteger("1"));
		attr.setNotation("V_" + name.toUpperCase());
		attr.setDescriptionShort("Kurzbeschreibung");
		attr.setDescriptionLong("Lange Beschreibung");
		
		Datatype datatype = new Datatype();
		datatype.setType(type);
		
		if(type == DatatypeEnum.DECIMAL){
			datatype.setPrecision(new BigInteger("18"));
			datatype.setScale(new BigInteger("0"));
		} else if( type == DatatypeEnum.CHAR ) {
			datatype.setLength(new BigInteger("9"));
		} else if( type == DatatypeEnum.VARCHAR ) {
			datatype.setLength(new BigInteger("1000"));
		}
		
		if(!nullable) {
			if(type == DatatypeEnum.DECIMAL || type == DatatypeEnum.INT){
				datatype.setDefaultValue("0");
			} else if( type == DatatypeEnum.CHAR || type == DatatypeEnum.VARCHAR ) {
				datatype.setDefaultValue("");
			} else if( type == DatatypeEnum.DATE || type == DatatypeEnum.DATETIME || type == DatatypeEnum.TIMESTAMP) {
				datatype.setDefaultValue("01.01.1800");
			}
		}else{
			datatype.setDefaultValue("null");
		}
		
		attr.setDatatype(datatype);

		Properties props = new Properties();
		List<Property> propsList = props.getProperty();
		
		Property prop = new Property();
		prop.setKey("key1");
		prop.setValue("value1");
		propsList.add(prop);
		
		prop = new Property();
		prop.setKey("key2");
		prop.setValue("value2");
		propsList.add(prop);
		
		attr.setProperties(props);
		
		return attr;
	}
}
