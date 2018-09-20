package machine.readable.zone.parser;

public enum MRZFormat {

	/**
	 * MRP Passport format: A two line long, 44 characters per line format.
	 */
	PASSPORT(2, 44, MRZPassport.class) {
		@Override
		public String getDateFormat() {
			return "yyMMdd";
		}
	};

	public final int rows;
	public final int columns;
	private final Class<? extends MRZBaseDocument> docType;

	private MRZFormat(int rows, int columns, Class<? extends MRZBaseDocument> docType) {
		this.rows = rows;
		this.columns = columns;
		this.docType = docType;
	}

	public abstract String getDateFormat();

	/**
	 * Checks if this format is correct.
	 * 
	 * @param mrzRows
	 *            MRZ record, separated into rows.
	 * @return true if given MRZ record matches its MRZ document type specifications
	 */
	private boolean isFormatOf(String[] mrzRows) {
		return rows == mrzRows.length && columns == mrzRows[0].length();
	}

	/**
	 * Detects given MRZ format.
	 * 
	 * @param mrz
	 *            the MRZ string.
	 * @return the format, never null.
	 */
	public static final MRZFormat detectFormat(String mrz) {
		final String[] rows = mrz.split(System.lineSeparator());
		final int cols = rows[0].length();
		for (int i = 1; i < rows.length; i++) {
			rows[i] = rows[i].replaceAll("\\s", "");
			if (rows[i].length() != cols) {
				throw new MRZException("Different row lengths: 0: " + cols + " and " + i + ": " + rows[i].length(),
						null, mrz, null);
			}
		}
		for (final MRZFormat f : values()) {
			if (f.isFormatOf(rows)) {
				return f;
			}
		}
		throw new MRZException("Unsupported format " + cols + "/" + rows.length, null, mrz, null);
	}

	public static final String[] getRows(String mrz) {
		final String[] rows = mrz.split(System.lineSeparator());
		final int cols = rows[0].length();
		for (int i = 1; i < rows.length; i++) {
			rows[i] = rows[i].replaceAll("\\s", "");
			if (rows[i].length() != cols) {
				throw new MRZException("Different row lengths: 0: " + cols + " and " + i + ": " + rows[i].length(),
						null, mrz, null);
			}
		}
		return rows;
	}

}
