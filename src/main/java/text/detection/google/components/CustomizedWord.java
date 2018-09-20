package text.detection.google.components;

import java.util.List;

import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.TextAnnotation.DetectedBreak.BreakType;
import com.google.cloud.vision.v1.Word;

public class CustomizedWord extends BaseCustomizedAnnotation {
	public CustomizedWord(Word word) {

		List<Symbol> symbols = word.getSymbolsList();
		StringBuilder builder = new StringBuilder();
		for (Symbol symbol : symbols) {
			builder.append(symbol.getText());
			if (symbol.getProperty().getDetectedBreak().getType() == BreakType.LINE_BREAK
					|| symbol.getProperty().getDetectedBreak().getType() == BreakType.EOL_SURE_SPACE) {
				builder.append(System.lineSeparator());
			} else if (symbol.getProperty().getDetectedBreak().getType() == BreakType.SPACE) {
				builder.append(" ");
			} else if (symbol.getProperty().getDetectedBreak() != null) {
				// System.out.println(symbol.getProperty().getDetectedBreak().getType());
			}
		}
		text = builder.toString();		
		boundingPoly = word.getBoundingBox();
	}

}
