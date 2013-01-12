package karstenroethig.db.plugin.html;

import java.net.URL;

public class HtmlResourceLocator {

	public URL resolve( String filename ) {
        return this.getClass().getResource( filename );
    }
	
}
