package karstenroethig.db.core.locator;

import java.net.URL;

public abstract class AbstractDatabaseLocator {

	public URL resolve() {
        return this.getClass().getResource( getDatabaseXmlFileName() );
    }
	
	protected String getDatabaseXmlFileName() {
		return "database.xml";
	}
	
	public abstract AbstractEntitiesLocator getEntitiesLocator();

}
