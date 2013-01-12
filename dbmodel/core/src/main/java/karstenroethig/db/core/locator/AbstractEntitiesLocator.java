package karstenroethig.db.core.locator;

import java.net.URL;

public class AbstractEntitiesLocator {

	public URL resolveByEntityName( String entityName ) {
        return this.getClass().getResource( entityName + ".xml" );
    }
	
}
