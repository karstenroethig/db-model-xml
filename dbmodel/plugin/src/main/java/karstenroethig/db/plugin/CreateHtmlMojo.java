package karstenroethig.db.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.Entity;
import karstenroethig.db.core.formatter.SimpleDatatypeFormatter;
import karstenroethig.db.plugin.html.HtmlResourceLocator;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo( name = "create-html", defaultPhase = LifecyclePhase.PACKAGE )
public class CreateHtmlMojo extends AbstractCreateWithVelocityMojo {

    @Parameter(
        defaultValue = "${project.build.directory}/html",
        property = "outputDir",
        required = true
    )
    private File outputDirectory;

    private ResourceLocator resourceLocator = new HtmlResourceLocator();

    public void execute() throws MojoExecutionException {

    	super.execute();
    	
    	Database database = getDatebase();

        try {

            copyFile( "print.png", outputDirectory, "resources", null );
            copyFile( "database.png", outputDirectory, "resources", null );
            copyFile( "consulting.png", outputDirectory, "resources", null );
            copyFile( "style.css", outputDirectory, "resources", null );

        } catch( IOException ex ) {
            throw new MojoExecutionException( "Error copying files", ex );
        }

        try {

            List<String> entityPropertyKeys = new ArrayList<String>();

            for( Entity entity : database.getEntities() ) {

                for( String key : entity.getPropertyKeys() ) {

                    if( entityPropertyKeys.contains( key ) == false ) {
                        entityPropertyKeys.add( key );
                    }

                }

            }

            Collections.sort( entityPropertyKeys );

            Map<String, Object> databaseParams = new HashMap<String, Object>();

            databaseParams.put( "entityPropertyKeys", entityPropertyKeys );

            evaluateFileWithVelocity( "database.html", outputDirectory, null, "index.html", databaseParams );

            for( Entity entity : database.getEntities() ) {

                List<String> attributePropertyKeys = new ArrayList<String>();

                for( Attribute attribute : entity.getAttributes() ) {

                    for( String key : attribute.getPropertyKeys() ) {

                        if( attributePropertyKeys.contains( key ) == false ) {
                            attributePropertyKeys.add( key );
                        }

                    }

                }

                SimpleDatatypeFormatter datatypeFormatter = new SimpleDatatypeFormatter();

                Map<String, Object> entityParams = new HashMap<String, Object>();

                entityParams.put( "entity", entity );
                entityParams.put( "attributePropertyKeys", attributePropertyKeys );
                entityParams.put( "datatypeFormatter", datatypeFormatter );

                evaluateFileWithVelocity( "entity.html", outputDirectory, "entities", entity.getName() + ".html",
                    entityParams );
            }

        } catch( Exception ex ) {
            throw new MojoExecutionException( "Error creating files", ex );
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
