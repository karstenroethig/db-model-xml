package karstenroethig.db.test.validation;

import karstenroethig.db.core.locator.AbstractDatabaseLocator;


public abstract class AbstractDatabaseLocatorValidator implements IValidator {

    protected AbstractDatabaseLocator databaseLocator;

    public AbstractDatabaseLocatorValidator( AbstractDatabaseLocator databaseLocator ) {

        if( databaseLocator == null ) {
            throw new IllegalArgumentException( "databaseLocator cannot be null" );
        }

        this.databaseLocator = databaseLocator;
    }

    protected AbstractDatabaseLocator getDatabaseLocator() {
        return databaseLocator;
    }
}
