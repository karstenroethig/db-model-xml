package karstenroethig.db.test.validation;

import karstenroethig.db.core.dto.Database;


public abstract class AbstractDatabaseModelValidator implements IValidator {

    protected Database database;

    public AbstractDatabaseModelValidator( Database database ) {

        if( database == null ) {
            throw new IllegalArgumentException( "database cannot be null" );
        }

        this.database = database;
    }

    protected Database getDatabase() {
        return database;
    }
}
