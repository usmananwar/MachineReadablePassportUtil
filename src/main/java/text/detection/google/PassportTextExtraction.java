package text.detection.google;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.protobuf.ByteString;

import machine.readable.zone.parser.MRZParser;
import machine.readable.zone.parser.MRZPassport;
import text.detection.google.components.BaseCustomizedAnnotation;
import text.detection.google.components.CustomizedBlock;
import text.detection.google.components.CustomizedPage;
import text.detection.google.components.CustomizedParagraph;
import text.detection.google.components.CustomizedTextAnnotation;
import text.detection.google.components.CustomizedWord;
import text.detection.utils.ImageMaskingUtil;
import text.detection.utils.PatternMatcher;

public class PassportTextExtraction {
	public static void main(String... args) throws Exception {

		System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));

		// Instantiates a client

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			// The path to the image file to annotate
			String fileName = "C:\\Users\\Usman\\Desktop\\Text_extraction-Specimens\\Passports\\Pakistan.jpg";
			String outputFile = "C:\\Users\\Usman\\Desktop\\Text_extraction-Specimens\\Passports\\Pakistan_masked.jpg";

			File outputfile = new File(outputFile);
			File inputFile = new File(fileName);
			BufferedImage sourceBufferedImage = ImageIO.read(inputFile);

			// Reads the image file into memory
			Path path = Paths.get(fileName);
			byte[] data = Files.readAllBytes(path);
			ByteString imgBytes = ByteString.copyFrom(data);

			// Builds the image annotation request
			List<AnnotateImageRequest> requests = new ArrayList<>();
			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
			requests.add(request);

			// Performs label detection on the image file
			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			BufferedImage resultedImage = ImageMaskingUtil.cloneImage(sourceBufferedImage);
			for (AnnotateImageResponse res : responses) {
				extractTextInfoAndMaskImage(res.getFullTextAnnotation(), resultedImage);
				ImageIO.write(resultedImage, "jpg", outputfile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void extractTextInfoAndMaskImage(TextAnnotation textAnnotation, BufferedImage image)
			throws Exception {

		System.out.println(textAnnotation.getText());

		List<String> mrzStrings = new ArrayList<String>();

		CustomizedTextAnnotation customizedTextAnnotation = new CustomizedTextAnnotation(textAnnotation);
		for (CustomizedPage page : customizedTextAnnotation.getPages()) {
			for (CustomizedBlock block : page.getBlocks()) {
				maskComponent(block, image);

				if (PatternMatcher.isMRZdetected(block.getText()) && !mrzStrings.contains(block.getText())) {
					mrzStrings.add(block.getText());
				}

				for (CustomizedParagraph paragraph : block.getParagraphs()) {

					if (PatternMatcher.isMRZdetected(paragraph.getText())
							&& !mrzStrings.contains(paragraph.getText())) {
						mrzStrings.add(paragraph.getText());
					}
					maskComponent(paragraph, image);
					for (CustomizedWord word : paragraph.getWords()) {
						maskComponent(word, image);
					}
				}
			}
		}

		for (String mrzString : mrzStrings) {
			try {
				MRZParser mrzParser = new MRZParser(mrzString);
				MRZPassport passport = mrzParser.getMRZPassport();
				System.out.println(passport.getPassportNumber());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void maskComponent(BaseCustomizedAnnotation annotatedComponent, BufferedImage image)
			throws Exception {

		if (PatternMatcher.isMRZdetected(annotatedComponent.getText())
				|| PatternMatcher.isPassportNo(annotatedComponent.getText())
				|| PatternMatcher.isPassportNoLike(annotatedComponent.getText())) {
			ImageMaskingUtil.applyMask(image, annotatedComponent.getBoundingPoly().getVertices(0).getX(),
					annotatedComponent.getBoundingPoly().getVertices(0).getY(),
					annotatedComponent.getBoundingPoly().getVertices(2).getX(),
					annotatedComponent.getBoundingPoly().getVertices(2).getY());
		}
	}

	
}