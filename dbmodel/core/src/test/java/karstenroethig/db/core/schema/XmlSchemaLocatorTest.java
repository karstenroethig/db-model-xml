package karstenroethig.db.core.schema;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import karstenroethig.db.core.schema.XmlSchemaLocator.XmlSchemaTypeEnum;

import org.junit.Test;

public class XmlSchemaLocatorTest {

    @Test
    public void testResolveByXmlSchemaType() throws Exception {

        for( XmlSchemaTypeEnum type : XmlSchemaTypeEnum.values() ) {

            URL url = new XmlSchemaLocator().resolveByXmlSchemaType( type );

            assertNotNull( url );

        }

    }
}
