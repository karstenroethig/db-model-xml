package karstenroethig.db.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import karstenroethig.db.plugin.html.HtmlResourceLocator;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

@Mojo( name = "create-html", defaultPhase = LifecyclePhase.PACKAGE )
public class CreateHtmlMojo extends AbstractMojo {

	@Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
	private File outputDirectory;
	
	public void execute() throws MojoExecutionException {
		
		File f = outputDirectory;
		
		if( !f.exists() ) {
			f.mkdirs();
		}
		
		URL templateUrl = new HtmlResourceLocator().resolve( "mytemplate.vm" );
		File templateFile = new File( f, "mytemplate.vm" );
		
		try {
			FileUtils.copyURLToFile( templateUrl, templateFile );
		} catch( IOException ex ) {
			throw new MojoExecutionException( "Error creating file " + templateFile, ex );
		}
		
		try {
			Velocity.init();
		} catch( Exception ex ) {
			throw new MojoExecutionException( "Error initializing velocity", ex );
		}

		VelocityContext context = new VelocityContext();

		context.put( "name", new String( "Velocity" ) );

		Template template = null;

		try {
//		   template = Velocity.getTemplate( templateFile.getAbsolutePath() );
//		   template = Velocity.getTemplate( "karstenroethig/db/plugin/html/mytemplate.vm" );
		   template = Velocity.getTemplate( "mytemplate.vm" );
		} catch( ResourceNotFoundException rnfe ) {
		   // couldn't find the template
		} catch( ParseErrorException pee ) {
		  // syntax error: problem parsing the template
		} catch( MethodInvocationException mie ) {
		  // something invoked in the template
		  // threw an exception
		} catch( Exception e ) {
		}

		File createdFile = new File( f, "my.txt" );
		StringWriter sw = new StringWriter();

		try {
			template.merge( context, sw );
		
			String text = sw.toString();
		
			FileUtils.writeStringToFile( createdFile, text);
		} catch( IOException ex ) {
			throw new MojoExecutionException( "Error creating file " + createdFile, ex );
		}
		
		// Example
		File touch = new File( f, "touch.txt" );
		
		FileWriter w = null;
		
		try {
			w = new FileWriter( touch );
			
			w.write( "touch.txt" );

		} catch( IOException e ) {
			
			throw new MojoExecutionException( "Error creating file " + touch, e );
			
		} finally {
			
			if( w != null ) {
				
				try {
					w.close();
				} catch( IOException e ) {
					// ignore
				}
			}
		}

	}

	public void setOutputDirectory( File outputDirectory ) {
		this.outputDirectory = outputDirectory;
	}
}
