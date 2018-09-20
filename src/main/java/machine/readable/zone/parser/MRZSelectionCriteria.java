package machine.readable.zone.parser;

public class MRZSelectionCriteria {

	public final int fromIndex;
	public final int toIndex;
	public final int rowIndex;
	public final String fillerCharacter;

	public MRZSelectionCriteria(int from, int length, int rowNumber, String fillerCharacter) {
		if (length == 0) {
			throw new IllegalArgumentException("Parameter length cannot be 0");
		}
		this.fromIndex = from;
		this.toIndex = from + length;
		this.rowIndex = rowNumber;
		this.fillerCharacter = fillerCharacter;
	}

	/**
	 * Returns length of this range.
	 * 
	 * @return number of characters, which this range covers.
	 */
	public int length() {
		return toIndex - fromIndex;
	}
}
