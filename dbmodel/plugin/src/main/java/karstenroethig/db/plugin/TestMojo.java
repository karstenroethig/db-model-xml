package karstenroethig.db.plugin;

import java.io.File;

import org.junit.Test;

public class TestMojo {

	@Test
	public void testMojo() throws Exception {
		
		CreateHtmlMojo mojo = new CreateHtmlMojo();
		
		mojo.setOutputDirectory( new File( "target" ) );
		
		mojo.execute();
		
	}
	
}
