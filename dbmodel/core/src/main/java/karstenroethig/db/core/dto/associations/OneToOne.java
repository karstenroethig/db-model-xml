package karstenroethig.db.core.dto.associations;

public class OneToOne extends AbstractAssociation {

	@Override
	public AssociationTypeEnum getAssociationType() {
		return AssociationTypeEnum.ONE_TO_ONE;
	}

}
