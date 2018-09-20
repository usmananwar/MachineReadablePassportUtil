package machine.readable.zone.parser;

import java.io.Serializable;

public abstract class MRZBaseDocument implements Serializable {

	public final MRZFormat format;

	protected MRZBaseDocument(MRZFormat format) {
		this.format = format;
	}

}
