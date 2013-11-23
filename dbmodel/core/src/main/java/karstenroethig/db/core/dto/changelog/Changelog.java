package karstenroethig.db.core.dto.changelog;

import java.util.ArrayList;
import java.util.List;

public class Changelog {

	private String versionOld;
	
	private String versionNew;
	
	private List<EntityChangelog> entityChangelogs;
	
	public Changelog( String versionOld, String versionNew ) {
		this.versionOld = versionOld;
		this.versionNew = versionNew;
		
		this.entityChangelogs = new ArrayList<EntityChangelog>();
	}
	
	public String getVersionOld() {
		return versionOld;
	}
	
	public String getVersionNew() {
		return versionNew;
	}
	
	public void addEntityChangelog( EntityChangelog entityChangelog ) {
		entityChangelogs.add( entityChangelog );
	}
	
	public List<EntityChangelog> getEntityChangelogs() {
		return entityChangelogs;
	}
}
