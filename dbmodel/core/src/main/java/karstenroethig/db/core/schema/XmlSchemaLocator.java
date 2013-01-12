package karstenroethig.db.core.schema;

import java.net.URL;

public class XmlSchemaLocator {

    public enum XmlSchemaTypeEnum {
        database(), entity();
    }

    public URL resolveByXmlSchemaType( XmlSchemaTypeEnum type ) {
        return this.getClass().getResource( type.name() + ".xsd" );
    }

}
