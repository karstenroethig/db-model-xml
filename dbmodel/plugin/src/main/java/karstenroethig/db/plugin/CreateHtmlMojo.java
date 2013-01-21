package karstenroethig.db.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karstenroethig.db.core.DatabaseModel;
import karstenroethig.db.core.dto.Attribute;
import karstenroethig.db.core.dto.Database;
import karstenroethig.db.core.dto.Entity;
import karstenroethig.db.core.locator.AbstractDatabaseLocator;
import karstenroethig.db.core.utils.DateUtils;
import karstenroethig.db.plugin.html.HtmlResourceLocator;
import karstenroethig.db.plugin.utils.TextUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

@Mojo( name = "create-html", defaultPhase = LifecyclePhase.PACKAGE )
public class CreateHtmlMojo extends AbstractMojo {

    @Parameter(
        defaultValue = "${project.build.directory}/html",
        property = "outputDir",
        required = true
    )
    private File outputDirectory;

    /** DOCUMENT ME! */
    @Parameter(
        property = "databaseLocatorClass",
        required = true
    )
    private String databaseLocatorClass;

    /** DOCUMENT ME! */
    private HtmlResourceLocator resourceLocator = new HtmlResourceLocator();

    /** DOCUMENT ME! */
    private VelocityEngine velocityEngine = null;

    /**
     * DOCUMENT ME!
     *
     * @throws  MojoExecutionException  DOCUMENT ME!
     */
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
        AbstractDatabaseLocator databaseLocator;

        if( databaseLocatorInstance instanceof AbstractDatabaseLocator ) {
            databaseLocator = ( AbstractDatabaseLocator )databaseLocatorInstance;
        } else {
            throw new MojoExecutionException( "databaseLocatorClass is no instance of " +
                AbstractDatabaseLocator.class.getName() );
        }

        // Load database model
        Database database = DatabaseModel.loadDatabaseModel( databaseLocator );

        if( database == null ) {
            throw new MojoExecutionException( "Could not load database model. Please run common test classes." );
        }

        // Create output directory with common files
        if( !outputDirectory.exists() ) {
            outputDirectory.mkdirs();
        }

        try {

            copyFile( "print.png", outputDirectory, "resources", null );
            copyFile( "database.png", outputDirectory, "resources", null );
            copyFile( "consulting.png", outputDirectory, "resources", null );
            copyFile( "style.css", outputDirectory, "resources", null );

        } catch( IOException ex ) {
            throw new MojoExecutionException( "Error copying files", ex );
        }

        try {

            TextUtils textUtils = new TextUtils();

            List<String> entityPropertyKeys = new ArrayList<String>();

            for( Entity entity : database.getEntities() ) {

                for( String key : entity.getPropertyKeys() ) {

                    if( entityPropertyKeys.contains( key ) == false ) {
                        entityPropertyKeys.add( key );
                    }

                }

            }

            Collections.sort( entityPropertyKeys );

            String createDateStr = DateUtils.formatDate( database.getCreateDate() );

            Map<String, Object> databaseParams = new HashMap<String, Object>();

            databaseParams.put( "database", database );
            databaseParams.put( "createDateStr", createDateStr );
            databaseParams.put( "entityPropertyKeys", entityPropertyKeys );
            databaseParams.put( "textUtils", textUtils );

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

                Map<String, Object> entityParams = new HashMap<String, Object>();

                entityParams.put( "database", database );
                entityParams.put( "entity", entity );
                entityParams.put( "createDateStr", createDateStr );
                entityParams.put( "attributePropertyKeys", attributePropertyKeys );
                entityParams.put( "textUtils", textUtils );

                evaluateFileWithVelocity( "entity.html", outputDirectory, "entities", entity.getName() + ".html",
                    entityParams );
            }

        } catch( Exception ex ) {
            throw new MojoExecutionException( "Error creating files", ex );
        }

    }

    /**
     * DOCUMENT ME!
     *
     * @param  outputDirectory  DOCUMENT ME!
     */
    public void setOutputDirectory( File outputDirectory ) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   sourceFilename      DOCUMENT ME!
     * @param   outputDirectory     DOCUMENT ME!
     * @param   targetSubDirectory  DOCUMENT ME!
     * @param   targetFilename      DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    private void copyFile( String sourceFilename, File outputDirectory, String targetSubDirectory,
        String targetFilename ) throws IOException {

        if( StringUtils.isBlank( targetFilename ) ) {
            targetFilename = sourceFilename;
        }

        URL url = resourceLocator.resolve( sourceFilename );

        File targetFile = null;

        if( StringUtils.isNotBlank( targetSubDirectory ) ) {

            File targetDirectory = new File( outputDirectory, targetSubDirectory );
            targetFile = new File( targetDirectory, targetFilename );

        } else {

            targetFile = new File( outputDirectory, targetFilename );

        }

        FileUtils.copyURLToFile( url, targetFile );

        getLog().info( "Datei wurde kopiert: " + targetFile.getName() );

    }

    /**
     * DOCUMENT ME!
     *
     * @param   sourceFilename      DOCUMENT ME!
     * @param   outputDirectory     DOCUMENT ME!
     * @param   targetSubDirectory  DOCUMENT ME!
     * @param   targetFilename      DOCUMENT ME!
     * @param   params              DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void evaluateFileWithVelocity( String sourceFilename, File outputDirectory, String targetSubDirectory,
        String targetFilename, Map<String, Object> params ) throws Exception {

        if( velocityEngine == null ) {
            velocityEngine = new VelocityEngine();
            velocityEngine.init();
        }

        if( StringUtils.isBlank( targetFilename ) ) {
            targetFilename = sourceFilename;
        }

        File targetFile = null;

        if( StringUtils.isNotBlank( targetSubDirectory ) ) {

            File targetDirectory = new File( outputDirectory, targetSubDirectory );
            targetFile = new File( targetDirectory, targetFilename );

        } else {

            targetFile = new File( outputDirectory, targetFilename );

        }

        URL url = resourceLocator.resolve( sourceFilename );
        String template = StringUtils.EMPTY;
        InputStream in = null;

        try {
            in = url.openStream();

            List<String> lines = IOUtils.readLines( in );

            StringBuffer templateBuffer = new StringBuffer();

            for( String line : lines ) {
                templateBuffer.append( line );
                templateBuffer.append( "\n" );
            }

            template = templateBuffer.toString();

        } finally {
            IOUtils.closeQuietly( in );
        }

        VelocityContext context = new VelocityContext( params );
        StringWriter sw = new StringWriter();

        velocityEngine.evaluate( context, sw, "LOG", template );

        String fileContent = sw.toString();

        FileUtils.writeStringToFile( targetFile, fileContent );

        getLog().info( "Datei wurde erstellt: " + targetFile.getName() );

    }
}
