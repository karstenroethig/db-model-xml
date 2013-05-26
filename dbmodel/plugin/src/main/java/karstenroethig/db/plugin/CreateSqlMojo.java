package karstenroethig.db.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import karstenroethig.db.core.formatter.MssqlCreateTableFormatter;
import karstenroethig.db.core.formatter.MssqlDropTablesFormatter;
import karstenroethig.db.core.formatter.OracleCreateTableFormatter;
import karstenroethig.db.core.formatter.OracleDropTablesFormatter;
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

        	/*
        	 * MS SQL Server
        	 */
            Map<String, Object> params = new HashMap<String, Object>();

            params.put( "dropTablesFormatter", new MssqlDropTablesFormatter() );
            params.put( "createTableFormatter", new MssqlCreateTableFormatter() );
            params.put( "dbmsName", "MS SQL Server 2005" );
            params.put( "specificLines", null );

            evaluateFileWithVelocity( "create.sql", outputDirectory, null, "mssql_create.sql", params );

        	/*
        	 * Oracle
        	 */
            params.clear();

            params.put( "dropTablesFormatter", new OracleDropTablesFormatter() );
            params.put( "createTableFormatter", new OracleCreateTableFormatter() );
            params.put( "dbmsName", "Oracle" );
            params.put( "specificLines", "alter session set nls_date_format = \"DD.MM.YYYY\";" );

            evaluateFileWithVelocity( "create.sql", outputDirectory, null, "oracle_create.sql", params );

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
