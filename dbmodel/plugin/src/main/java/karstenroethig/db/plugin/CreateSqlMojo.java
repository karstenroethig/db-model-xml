package karstenroethig.db.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import karstenroethig.db.core.formatter.SimpleDatatypeFormatter;
import karstenroethig.db.plugin.sql.SqlResourceLocator;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo( name = "create-sql", defaultPhase = LifecyclePhase.PACKAGE )
public class CreateSqlMojo extends AbstractCreateWithVelocityMojo {

    @Parameter(
        defaultValue = "${project.build.directory}/sql",
        property = "outputDir",
        required = true
    )
    private File outputDirectory;

    private ResourceLocator resourceLocator = new SqlResourceLocator();

    public void execute() throws MojoExecutionException {

    	super.execute();

        try {
        	
        	SimpleDatatypeFormatter datatypeFormatter = new SimpleDatatypeFormatter();

            Map<String, Object> params = new HashMap<String, Object>();

            params.put( "datatypeFormatter", datatypeFormatter );

            evaluateFileWithVelocity( "mssql_create.sql", outputDirectory, null, null, params );

        } catch( Exception ex ) {
            throw new MojoExecutionException( "Error creating sql files", ex );
        }

    }
    
    @Override
    protected ResourceLocator getResourceLocator() {
    	return resourceLocator;
    }

    @Override
    protected File getOutputDirectory() {
        return outputDirectory;
    }
}
