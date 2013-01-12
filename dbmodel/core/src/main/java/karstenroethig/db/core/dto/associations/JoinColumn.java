package karstenroethig.db.core.dto.associations;

import org.apache.commons.lang3.StringUtils;

public class JoinColumn {

	private String name;
	private String referencedColumnName;
	
	public JoinColumn() {
		this.name = StringUtils.EMPTY;
		this.referencedColumnName = StringUtils.EMPTY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReferencedColumnName() {
		return referencedColumnName;
	}

	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}
	
}
