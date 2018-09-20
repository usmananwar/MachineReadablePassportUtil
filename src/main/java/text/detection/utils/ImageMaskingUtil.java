package text.detection.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageMaskingUtil {

	public static void applyMask(BufferedImage image, double x1PixelValue, double y1PixelValue, double x2PixelValue,
			double y2PixelValue) throws Exception {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (x >= x1PixelValue && x <= x2PixelValue && y >= y1PixelValue && y <= y2PixelValue) {
					image.setRGB(x, y, Color.black.getRGB());
				}
			}
		}
	}

	public static BufferedImage cloneImage(BufferedImage image) throws Exception {
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(image.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
