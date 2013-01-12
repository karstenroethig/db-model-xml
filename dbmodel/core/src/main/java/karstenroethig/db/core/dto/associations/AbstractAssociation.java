package karstenroethig.db.core.dto.associations;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAssociation {

	private String targetEntity;
	
	private List<JoinColumn> joinColumns;
	
	public AbstractAssociation() {
		this.targetEntity = null;
		
		this.joinColumns = new ArrayList<JoinColumn>();
	}

	public String getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity( String targetEntity ) {
		this.targetEntity = targetEntity;
	}

	public List<JoinColumn> getJoinColumns() {
		return joinColumns;
	}

	public void addJoinColumn( JoinColumn joinColumn ) {
		
		if( joinColumn == null ) {
			return;
		}
		
		joinColumns.add( joinColumn );
	}
	
	public abstract AssociationTypeEnum getAssociationType();
}
