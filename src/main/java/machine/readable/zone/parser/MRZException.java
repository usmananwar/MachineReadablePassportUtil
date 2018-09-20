package machine.readable.zone.parser;

public class MRZException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	/**
	 * The MRZ string being parsed.
	 */
	public final String mrz;
	/**
	 * Range containing problematic characters.
	 */
	// public final MrzRange range;
	/**
	 * Expected MRZ format.
	 */
	public final MRZFormat format;

	public MRZException(Throwable th, String mrz, MRZFormat format) {
		super("Exception occured while parsing.", th);
		this.mrz = mrz;
		this.format = format;
	}

	public MRZException(String message, Throwable th, String mrz, MRZFormat format) {
		super(message, th);
		this.mrz = mrz;
		this.format = format;
	}
}
