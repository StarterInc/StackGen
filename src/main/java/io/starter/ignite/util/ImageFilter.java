package io.starter.ignite.util;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.PixelGrabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageFilter {

	protected static final Logger	logger			= LoggerFactory
			.getLogger(ImageFilter.class);

	private static final float		IMGD1			= 1.0f;
	private static final float		IMGD2			= 16.0f;
	private int						blurLevel		= 50;
	public static final int			IMAGE_UNKNOWN	= -1;
	public static final int			IMAGE_JPEG		= 0;
	public static final int			IMAGE_PNG		= 1;
	public static final int			IMAGE_GIF		= 2;

	/**
	 * @return the blurLevel
	 */
	public int getBlurLevel() {
		return blurLevel;
	}

	/**
	 * @param blurLevel
	 *            the blurLevel to set
	 */
	public void setBlurLevel(int blurLevel) {
		this.blurLevel = blurLevel;
	}

	static boolean imageLoaded = false;

	/**
	 * Blur the sourceImage
	 * 
	 * @param sourceImage
	 * @return blurred output
	 */
	public BufferedImage blurBufferedImage(Image sourceImage) {

		BufferedImage image = getImage(sourceImage);

		int sqblurLevel = blurLevel * blurLevel;

		float[] matrix = new float[sqblurLevel];
		for (int i = 0; i < sqblurLevel; i++)
			matrix[i] = 1.0f / sqblurLevel;

		BufferedImageOp op = new ConvolveOp(
				new Kernel(blurLevel, blurLevel, matrix),
				ConvolveOp.EDGE_ZERO_FILL, null);
		image = op.filter((BufferedImage) sourceImage, image);

		// and a quick soften blur over that
		sqblurLevel = 16;
		blurLevel = 4;

		float[] matrix1 = new float[sqblurLevel];
		for (int i = 0; i < sqblurLevel; i++)
			matrix1[i] = IMGD1 / IMGD2;

		BufferedImageOp op1 = new ConvolveOp(
				new Kernel(blurLevel, blurLevel, matrix1),
				ConvolveOp.EDGE_ZERO_FILL, null);
		image = op1.filter(image, null);

		return image;
	}

	/**
	 * Negative the sourceImage
	 * 
	 * @param sourceImage
	 * @return blurred output
	 */
	public BufferedImage negativeBufferedImage(Image sourceImage) {

		// Create a buffered image from the source image with a
		// format that's
		// compatible with the screen

		BufferedImage image = getImage(sourceImage);

		float[] matrix1 = new float[16];
		for (int i = 0; i < 16; i++) {
			matrix1[i] = .20f;
		}

		// again, this time with feeling
		BufferedImageOp op = new ConvolveOp(new Kernel(4, 4, matrix1),
				ConvolveOp.EDGE_ZERO_FILL, null);
		image = op.filter((BufferedImage) sourceImage, image);

		BufferedImageOp op2 = new ConvolveOp(new Kernel(4, 4, matrix1),
				ConvolveOp.EDGE_ZERO_FILL, null);
		image = op2.filter(image, null);
		return image;

	}

	/**
	 * washes out the sourceImage as if overexposed
	 * 
	 * @param sourceImage
	 * @return blurred output
	 */
	public BufferedImage overexposeBufferedImage(Image sourceImage) {

		// Create a buffered image from the source image with a
		// format that's
		// compatible with the screen

		BufferedImage image = getImage(sourceImage);

		float[] matrix1 = new float[16];
		for (int i = 0; i < 16; i++)
			matrix1[i] = 1.0f / 3.0f;

		// again, this time with feeling
		BufferedImageOp op = new ConvolveOp(new Kernel(4, 4, matrix1),
				ConvolveOp.EDGE_ZERO_FILL, null);
		image = op.filter((BufferedImage) sourceImage, image);

		return image;

	}

	private BufferedImage getImage(Image sourceImage) {

		// This triggers creation of the toolkit.
		// Because java.awt.headless property is set to true, this
		// will be an instance of headless toolkit.
		Toolkit tk = Toolkit.getDefaultToolkit();
		logger.debug("Created Toolkit: " + tk);

		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		if (GraphicsEnvironment.isHeadless()) {
			logger.debug("Headless ImageFilter.getImage() called...");

		} else {
			logger.debug("NOT Headless ImageFilter.getImage() called...");
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(sourceImage.getWidth(null),
				sourceImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(sourceImage, 0, 0, null);
		bGr.dispose();
		// If the source image has no alpha info use
		// Transparency.OPAQUE instead

		bimage = createHeadlessSmoothBufferedImage(bimage, IMAGE_JPEG, sourceImage
				.getWidth(null), sourceImage.getHeight(null));

		// Copy image to buffered image
		return bimage;
	}

	/**
	 * Creates a <code>BufferedImage</code> from an <code>Image</code>. This
	 * method can function on a completely headless system. This especially
	 * includes Linux and Unix systems that do not have the X11 libraries
	 * installed, which are required for the AWT subsystem to operate. The
	 * resulting image will be smoothly scaled using bilinear filtering.
	 * 
	 * @param source
	 *            The image to convert
	 * @param w
	 *            The desired image width
	 * @param h
	 *            The desired image height
	 * @return The converted image
	 * @param type
	 *            int
	 */
	public static BufferedImage createHeadlessSmoothBufferedImage(BufferedImage source, int type, int width, int height) {
		if (type == IMAGE_PNG && hasAlpha(source)) {
			type = BufferedImage.TYPE_INT_ARGB;
		} else {
			type = BufferedImage.TYPE_INT_RGB;
		}

		BufferedImage dest = new BufferedImage(width, height, type);

		int sourcex;
		int sourcey;

		double scalex = (double) width / source.getWidth();
		double scaley = (double) height / source.getHeight();

		int x1;
		int y1;

		double xdiff;
		double ydiff;

		int rgb;
		int rgb1;
		int rgb2;

		for (int y = 0; y < height; y++) {
			sourcey = y * source.getHeight() / dest.getHeight();
			ydiff = scale(y, scaley) - sourcey;

			for (int x = 0; x < width; x++) {
				sourcex = x * source.getWidth() / dest.getWidth();
				xdiff = scale(x, scalex) - sourcex;

				x1 = Math.min(source.getWidth() - 1, sourcex + 1);
				y1 = Math.min(source.getHeight() - 1, sourcey + 1);

				rgb1 = getRGBInterpolation(source
						.getRGB(sourcex, sourcey), source
								.getRGB(x1, sourcey), xdiff);
				rgb2 = getRGBInterpolation(source.getRGB(sourcex, y1), source
						.getRGB(x1, y1), xdiff);

				rgb = getRGBInterpolation(rgb1, rgb2, ydiff);

				dest.setRGB(x, y, rgb);
			}
		}

		return dest;
	}

	private static double scale(int point, double scale) {
		return point / scale;
	}

	private static int getRGBInterpolation(int value1, int value2, double distance) {
		int alpha1 = (value1 & 0xFF000000) >>> 24;
		int red1 = (value1 & 0x00FF0000) >> 16;
		int green1 = (value1 & 0x0000FF00) >> 8;
		int blue1 = (value1 & 0x000000FF);

		int alpha2 = (value2 & 0xFF000000) >>> 24;
		int red2 = (value2 & 0x00FF0000) >> 16;
		int green2 = (value2 & 0x0000FF00) >> 8;
		int blue2 = (value2 & 0x000000FF);

		int rgb = ((int) (alpha1 * (1.0 - distance) + alpha2 * distance) << 24)
				| ((int) (red1 * (1.0 - distance) + red2 * distance) << 16)
				| ((int) (green1 * (1.0 - distance) + green2 * distance) << 8)
				| (int) (blue1 * (1.0 - distance) + blue2 * distance);

		return rgb;
	}

	/**
	 * Determines if the image has transparent pixels.
	 * 
	 * @param image
	 *            The image to check for transparent pixel.s
	 * @return <code>true</code> of <code>false</code>, according to the result
	 */
	public static boolean hasAlpha(Image image) {
		try {
			PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
			pg.grabPixels();

			return pg.getColorModel().hasAlpha();
		} catch (InterruptedException e) {
			return false;
		}
	}
}