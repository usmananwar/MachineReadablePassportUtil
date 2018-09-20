package text.detection.google.components;

import com.google.cloud.vision.v1.BoundingPoly;

public class BaseCustomizedAnnotation {
	protected String text;
	protected BoundingPoly boundingPoly;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public BoundingPoly getBoundingPoly() {
		return boundingPoly;
	}

	public void setBoundingPoly(BoundingPoly boundingPoly) {
		this.boundingPoly = boundingPoly;
	}
}
