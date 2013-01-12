package karstenroethig.db.lineapp.xml;

import karstenroethig.db.core.locator.AbstractDatabaseLocator;
import karstenroethig.db.core.locator.AbstractEntitiesLocator;
import karstenroethig.db.lineapp.xml.entities.EntitiesLocator;

public class DatabaseLocator extends AbstractDatabaseLocator {

	@Override
	public AbstractEntitiesLocator getEntitiesLocator() {
		return new EntitiesLocator();
	}
}
