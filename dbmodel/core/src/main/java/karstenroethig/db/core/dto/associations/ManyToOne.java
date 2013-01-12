package karstenroethig.db.core.dto.associations;

public class ManyToOne extends AbstractAssociation {

	@Override
	public AssociationTypeEnum getAssociationType() {
		return AssociationTypeEnum.MANY_TO_ONE;
	}

}
