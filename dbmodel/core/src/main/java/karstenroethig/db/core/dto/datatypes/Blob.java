package karstenroethig.db.core.dto.datatypes;

public class Blob extends AbstractDatatype {

	@Override
	public DatatypeEnum getType() {
		return DatatypeEnum.BLOB;
	}
}
