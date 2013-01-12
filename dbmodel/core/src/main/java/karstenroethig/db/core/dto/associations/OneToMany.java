package karstenroethig.db.core.dto.associations;

public class OneToMany extends AbstractAssociation {

	@Override
	public AssociationTypeEnum getAssociationType() {
		return AssociationTypeEnum.ONE_TO_MANY;
	}

}
