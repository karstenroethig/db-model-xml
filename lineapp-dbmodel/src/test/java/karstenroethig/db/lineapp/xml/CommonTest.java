package karstenroethig.db.lineapp.xml;

import karstenroethig.db.core.locator.AbstractDatabaseLocator;
import karstenroethig.db.test.AbstractCommonTest;

public class CommonTest extends AbstractCommonTest {

    @Override
    protected AbstractDatabaseLocator getDatabaseLocator() {
        return new DatabaseLocator();
    }
}
