package text.detection.google.components;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.Page;
import com.google.cloud.vision.v1.TextAnnotation;

public class CustomizedTextAnnotation extends BaseCustomizedAnnotation {
	
	private List<CustomizedPage> pages = new ArrayList<CustomizedPage>();

	public CustomizedTextAnnotation(TextAnnotation fullTextAnnotation) {
		for (Page page : fullTextAnnotation.getPagesList()) {
			pages.add(new CustomizedPage(page));
		}
	}

	public String getText() {
		if (text == null) {
			StringBuilder builder = new StringBuilder();
			for (CustomizedPage page : pages) {
				builder.append(page.getText());
			}
			text = builder.toString();
		}
		return text;
	}

	public List<CustomizedPage> getPages() {
		return pages;
	}

	public void setPages(List<CustomizedPage> pages) {
		this.pages = pages;
	}

}
