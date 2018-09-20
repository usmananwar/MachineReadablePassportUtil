package text.detection.google.components;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Page;

public class CustomizedPage extends BaseCustomizedAnnotation {

	private List<CustomizedBlock> blocks = new ArrayList<CustomizedBlock>();

	public CustomizedPage(Page page) {
		for (Block block : page.getBlocksList()) {
			blocks.add(new CustomizedBlock(block));
		}
	}

	public String getText() {
		if (text == null) {
			StringBuilder builder = new StringBuilder();
			for (CustomizedBlock block : blocks) {
				builder.append(block.getText());
			}
			text = builder.toString();
		}
		return text;
	}

	public List<CustomizedBlock> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<CustomizedBlock> blocks) {
		this.blocks = blocks;
	}

}
