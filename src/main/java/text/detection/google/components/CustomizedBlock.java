package text.detection.google.components;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Paragraph;

public class CustomizedBlock extends BaseCustomizedAnnotation {
	
	private List<CustomizedParagraph> paragraphs = new ArrayList<CustomizedParagraph>();

	public CustomizedBlock(Block block) {
		for (Paragraph paragraph : block.getParagraphsList()) {
			paragraphs.add(new CustomizedParagraph(paragraph));
		}
		boundingPoly = block.getBoundingBox();
	}

	public String getText() {
		if (text == null) {
			StringBuilder builder = new StringBuilder();
			for (CustomizedParagraph block : paragraphs) {
				builder.append(block.getText());
			}
			text = builder.toString();
		}
		return text;
	}

	public List<CustomizedParagraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<CustomizedParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

}
