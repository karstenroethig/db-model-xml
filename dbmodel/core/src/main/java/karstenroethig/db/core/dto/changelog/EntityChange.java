package karstenroethig.db.core.dto.changelog;

import java.util.List;

import karstenroethig.db.core.utils.DiffMatchPatch.Diff;

public class EntityChange {

	private String entityAttributeName;
	
	private String xmlAttributeName;
	
	private String oldValue;
	
	private String newValue;
	
	private String text;
	
	private List<Diff> diffDetails = null;
	
	public String getEntityAttributeName() {
		return entityAttributeName;
	}
	
	public void setEntityAttributeName( String entityAttributeName ) {
		this.entityAttributeName = entityAttributeName;
	}
	
	public String getXmlAttributeName() {
		return xmlAttributeName;
	}
	
	public void setXmlAttributeName( String xmlAttributeName ) {
		this.xmlAttributeName = xmlAttributeName;
	}
	
	public String getOldValue() {
		return oldValue;
	}
	
	public void setOldValue( String oldValue ) {
		this.oldValue = oldValue;
	}
	
	public String getNewValue() {
		return newValue;
	}
	
	public void setNewValue( String newValue ) {
		this.newValue = newValue;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText( String text ) {
		this.text = text;
	}
	
	public List<Diff> getDiffDetails() {
		return diffDetails;
	}
	
	public void setDiffDetails( List<Diff> diffDetails ) {
		this.diffDetails = diffDetails;
	}
	
	public boolean hasDiffDetails() {
		return diffDetails != null && !diffDetails.isEmpty();
	}
}
