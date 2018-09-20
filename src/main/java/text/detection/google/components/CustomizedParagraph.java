package text.detection.google.components;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Word;

public class CustomizedParagraph extends BaseCustomizedAnnotation {
	
	private List<CustomizedWord> words = new ArrayList<CustomizedWord>();
	

	public CustomizedParagraph(Paragraph paragraph) {
		for (Word word : paragraph.getWordsList()) {
			words.add(new CustomizedWord(word));
		}
		boundingPoly = paragraph.getBoundingBox();
	}

	public String getText() {
		if (text == null) {
			StringBuilder builder = new StringBuilder();
			for (CustomizedWord word : words) {
				builder.append(word.getText());
			}
			text = builder.toString();			
		}
		
		return text;
	}

	public List<CustomizedWord> getWords() {
		return words;
	}

	public void setWords(List<CustomizedWord> words) {
		this.words = words;
	}


}
