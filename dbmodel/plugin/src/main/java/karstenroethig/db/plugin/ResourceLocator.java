package karstenroethig.db.plugin;

import java.net.URL;

public class ResourceLocator {

	public URL resolve( String filename ) {
        return this.getClass().getResource( filename );
    }

}
