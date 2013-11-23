package karstenroethig.db.plugin;

import java.io.File;

import karstenroethig.db.core.DatabaseModel;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.locator.AbstractDatabaseLocator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractDatabaseMojo extends AbstractMojo {

    @Parameter(
        property = "databaseLocatorClass",
        required = true
    )
    private String databaseLocatorClass;
    
    private Database database = null;
    
    private AbstractDatabaseLocator databaseLocator = null;

    public void execute() throws MojoExecutionException {

        // Check class in classpath
        Class databaseLocatorClazz;

        try {
            databaseLocatorClazz = Class.forName( databaseLocatorClass );
        } catch( ClassNotFoundException ex ) {
            throw new MojoExecutionException( "Class not found: " + databaseLocatorClass, ex );
        }

        // Create instance
        Object databaseLocatorInstance;

        try {
            databaseLocatorInstance = databaseLocatorClazz.newInstance();
        } catch( Exception ex ) {
            throw new MojoExecutionException( "Could not create instance of class " + databaseLocatorClass, ex );
        }

        // Check instance
        if( databaseLocatorInstance instanceof AbstractDatabaseLocator ) {
            databaseLocator = ( AbstractDatabaseLocator )databaseLocatorInstance;
        } else {
            throw new MojoExecutionException( "databaseLocatorClass is no instance of " +
                AbstractDatabaseLocator.class.getName() );
        }

        // Load database model
        database = DatabaseModel.loadDatabaseModel( databaseLocator );

        if( database == null ) {
            throw new MojoExecutionException( "Could not load database model. Please run common test classes." );
        }

        // Create output directory
        File outputDirectory = getOutputDirectory();
        
        if( outputDirectory != null && !outputDirectory.exists() ) {
            outputDirectory.mkdirs();
        }

    }
    
    protected Database getDatabase() {
    	return database;
    }
    
    protected AbstractDatabaseLocator getDatabaseLocator() {
    	return databaseLocator;
    }
    
    protected abstract File getOutputDirectory();
    
}
