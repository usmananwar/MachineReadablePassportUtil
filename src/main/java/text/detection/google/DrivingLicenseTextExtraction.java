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

import text.detection.google.components.BaseCustomizedAnnotation;
import text.detection.google.components.CustomizedBlock;
import text.detection.google.components.CustomizedPage;
import text.detection.google.components.CustomizedParagraph;
import text.detection.google.components.CustomizedTextAnnotation;
import text.detection.utils.ImageMaskingUtil;
import text.detection.utils.PatternMatcher;

public class DrivingLicenseTextExtraction {

	public static void main(String... args) throws Exception {

		System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));

		// Instantiates a client

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			String fileName = "9";
			// The path to the image file to annotate
			String inputPath = "C:\\Users\\Usman\\Desktop\\Text_extraction-Specimens\\Korean_driving_licenses\\"
					+ fileName + ".jpg";
			String outputPath = "C:\\Users\\Usman\\Desktop\\Text_extraction-Specimens\\Korean_driving_licenses\\"
					+ fileName + "_masked.jpg";

			File outputfile = new File(outputPath);
			File inputFile = new File(inputPath);
			BufferedImage sourceBufferedImage = ImageIO.read(inputFile);

			// Reads the image file into memory
			Path path = Paths.get(inputPath);
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
		CustomizedTextAnnotation customizedTextAnnotation = new CustomizedTextAnnotation(textAnnotation);
		for (CustomizedPage page : customizedTextAnnotation.getPages()) {
			for (CustomizedBlock block : page.getBlocks()) {
				for (CustomizedParagraph para : block.getParagraphs()) {
					maskComponent(para, image);
					extractName(para);
					extractLicenseNumber(para);
				}
			}
		}
	}

	public static void extractLicenseNumber(BaseCustomizedAnnotation annotatedComponent) {
		String license = PatternMatcher.getMatchedPart(PatternMatcher.KOREA_DRIVING_LICENSE,
				annotatedComponent.getText());
		if (license != null) {
			System.out.println("License: " + license);
		}
	}

	public static void extractName(BaseCustomizedAnnotation annotatedComponent) {
		List<String> patterns = new ArrayList<String>();
		patterns.add("성 명 :");
		patterns.add("성명 :");
		patterns.add("성명:");
		patterns.add("성 명:");
		patterns.add("성명:");
		patterns.add("명:");
		patterns.add("명 :");
		String text = extractInfo(patterns, annotatedComponent);
		if (text != null) {
			String[] names = text.split(":");
			
			
			
			System.out.println("Name: " + names[1]);
		}
	}

	public static String extractInfo(List<String> patterns, BaseCustomizedAnnotation annotatedComponent) {
		String text = annotatedComponent.getText();
		String lines[] = text.split(System.lineSeparator());
		for (String line : lines) {
			for (String pattern : patterns) {
				if (PatternMatcher.isContained(pattern, line)) {
					return line;
				}
			}
		}
		return null;
	}

	public static void maskComponent(BaseCustomizedAnnotation annotatedComponent, BufferedImage image)
			throws Exception {
		if (PatternMatcher.isMatched(PatternMatcher.KOREA_DRIVING_LICENSE_CONTAINING_STR, annotatedComponent.getText())
				|| PatternMatcher.isMatched(PatternMatcher.KOREA_IDENTITY_NUMBER_CONTAINING_STR,
						annotatedComponent.getText())
				|| PatternMatcher.isMatched(PatternMatcher.KOREA_IDENTITY_NUMBER_LIKE_CONTAINING_STR,
						annotatedComponent.getText())) {

			ImageMaskingUtil.applyMask(image, annotatedComponent.getBoundingPoly().getVertices(0).getX(),
					annotatedComponent.getBoundingPoly().getVertices(0).getY(),
					annotatedComponent.getBoundingPoly().getVertices(2).getX(),
					annotatedComponent.getBoundingPoly().getVertices(2).getY());
		}
	}

}
