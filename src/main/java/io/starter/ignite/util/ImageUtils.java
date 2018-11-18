package io.starter.ignite.util;

import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.resize;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtils {

	protected static final Logger	logger					= LoggerFactory
			.getLogger(ImageUtils.class);

	// overwrite existing converted images
	private static boolean			overwrite				= true;

	public static int				IMAGE_BIG_WIDTH			= 800;
	public static int				IMAGE_STANDARD_WIDTH	= 400;
	public static int				IMAGE_BIG_ICON_WIDTH	= 256;
	public static int				IMAGE_MEDIUM_ICON_WIDTH	= 128;
	public static int				IMAGE_TINY_ICON_WIDTH	= 64;

	public static String			TEST_IMAGES_FOLDER		= "/testimages";

	/**
	 * returns the custom file path for the resource
	 * 
	 * TODO: currently only returns the "Standard" size... implement the other
	 * sizes..
	 * 
	 * @param inputname
	 *            image name
	 * @param uid
	 *            user id of upload owner
	 * @param imageSize
	 * @return
	 */
	public static String getImageURL(String inputname, int uid, int imageSize) {

		String path = SystemConstants.S3_STARTER_SERVICE
				+ SystemConstants.S3_STARTER_MEDIA_BUCKET + "/"
				+ SystemConstants.S3_STARTER_MEDIA_FOLDER;

		String filename = inputname.substring(0, inputname.lastIndexOf("."));
		String fending = inputname.substring(inputname.lastIndexOf("."));

		return path + "/" + uid + "/" + filename + "/IconBig" + fending;

	}

	/**
	 * entrypoint for tests
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (true) { // testing mode
			args = new String[1];
			args[0] = TEST_IMAGES_FOLDER;
		}
		File findir = new File(args[0]);
		if (findir.isDirectory()) {
			logger.debug("Processing folder: " + args[0]); // better hope
															// thats a
			// valid path ok

			File[] resultfiles = findir.listFiles();

			for (File f : resultfiles) {
				if (!f.isDirectory() && !f.getName().contains("DS")) {
					try {
						File[] processedo = ImageUtils.getStarterPics(f, -1);
						for (File xr : processedo) {
							logger.debug("Processed image: " + xr); // better
																	// hope
							// thats a
						}
					} catch (Exception e) {
						; // typical
					}
				}
			}
		} else {
			logger.debug("Please enter a foldername.  '" + args[0]
					+ "' is not a folder."); // better hope thats a
			// valid path ok

		}
	}

	/**
	 * returns 3 versions of an input image:
	 * 
	 * TRY THESE: 0: standardized 'medium' filesize 400w scaled 1: image blurred
	 * 512 w back 2: image sharp 157@2x thumb 3: image tiny 64@2x thumb
	 * 
	 * 
	 * @param input
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	public static File[] processExistingStarterPics(File input, int userId) throws IOException {

		String fnstart = input.getPath();
		String fnend = input.getName();
		fnend = fnend.substring(fnend.lastIndexOf(".")); // file type
		fnstart = fnstart.substring(0, fnstart.lastIndexOf(fnend));

		String outputpath = ImageUtils.TEST_IMAGES_FOLDER;
		if (!fnstart.startsWith("http:")) {
			outputpath = input.getPath()
					.substring(0, input.getPath().indexOf(input.getName()) - 1);

		}

		outputpath = outputpath + "/processed/" + userId + "/";

		String outputfn = input.getName();
		outputfn = outputfn.substring(0, outputfn.indexOf(fnend));

		// skip existing (or overwrite all)
		String checkExists = outputpath + outputfn + "/Original" + fnend;
		File fcheck = new File(checkExists);
		if (fcheck.exists() && !overwrite) {
			logger.debug("Skipping: " + checkExists);
			return null;
		}

		if (fnstart.startsWith("http:")) {

			File fin = File.createTempFile("ck-", fnend);
			fin.deleteOnExit();
			File fx = ImageUtils.readFromUrl(input.getPath(), fin);
			input = fx;
		}

		if (((fnend.toLowerCase().endsWith(".mov"))
				|| (fnend.toLowerCase().endsWith(".html"))
				|| (fnend.toLowerCase().endsWith(".m4a")))) {
			File[] ret = new File[1];
			ret[0] = input;
			return ret;
		}

		// morph and return
		File[] ret = new File[6];

		// IMAGE_STANDARD_WIDTH = 512;
		// IMAGE_BIG_WIDTH = 1024;
		// IMAGE_BIG_ICON_WIDTH = 157;
		// IMAGE_MEDIUM_ICON_WIDTH = 96;
		// IMAGE_TINY_ICON_WIDTH = 16;

		BufferedImage fuf = load(input);

		// File orig = new File(outputpath + outputfn + "/Original"
		// + fnend);
		// ret[0] = save(fuf, orig, fnend);

		// File thumbf = new File(outputpath + outputfn +
		// "/Thumbnail" + fnend);
		// ret[1] = save(createThumbnail(fuf), thumbf, fnend);

		File blurf = new File(outputpath + outputfn + "/Blurred" + fnend);
		ret[0] = save(createBlur(fuf), blurf, fnend);

		// File negt = new File(outputpath + outputfn + "/Negative"
		// + fnend);
		// ret[3] = save(createNegative(fuf), negt, fnend);

		File xiny = new File(outputpath + outputfn + "/Big" + fnend);
		ret[1] = save(resize(fuf, Method.QUALITY, IMAGE_BIG_WIDTH, OP_ANTIALIAS), xiny, fnend);

		File xinyx = new File(outputpath + outputfn + "/Standard" + fnend);
		ret[2] = save(resize(fuf, Method.QUALITY, IMAGE_STANDARD_WIDTH, OP_ANTIALIAS), xinyx, fnend);

		File zxiny = new File(outputpath + outputfn + "/IconBig" + fnend);
		ret[3] = save(resize(fuf, Method.ULTRA_QUALITY, IMAGE_BIG_ICON_WIDTH, OP_ANTIALIAS), zxiny, fnend);

		File xqiny = new File(outputpath + outputfn + "/IconMedium" + fnend);
		ret[4] = save(resize(fuf, Method.ULTRA_QUALITY, IMAGE_MEDIUM_ICON_WIDTH, OP_ANTIALIAS), xqiny, fnend);

		File xqinyz = new File(outputpath + outputfn + "/IconSmall" + fnend);
		ret[5] = save(resize(fuf, Method.ULTRA_QUALITY, IMAGE_TINY_ICON_WIDTH, OP_ANTIALIAS), xqinyz, fnend);

		return ret;
	}

	/**
	 * returns 3 versions of an input image:
	 * 
	 * TRY THESE: 0: standardized 'medium' filesize 400w scaled 1: image blurred
	 * 512 w back 2: image sharp 157@2x thumb 3: image tiny 64@2x thumb
	 * 
	 * 
	 * @param input
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	public static File[] getStarterPics(File input, int userId) throws IOException {

		String fnstart = input.getPath();
		String fnend = input.getName();
		fnend = fnend.substring(fnend.indexOf(".")); // file type
		fnstart = fnstart.substring(0, fnstart.indexOf(fnend));

		String outputfn = input.getName();
		outputfn = outputfn.substring(0, outputfn.indexOf(fnend));

		if (input.getPath().startsWith("http:")) {

			File fin = File.createTempFile("ck-", fnend);
			fin.deleteOnExit();
			File fx = ImageUtils.readFromUrl(input.getPath(), fin);
			input = fx;
		}

		// morph and return
		File[] ret = new File[6];

		// IMAGE_STANDARD_WIDTH = 512;
		// IMAGE_BIG_WIDTH = 1024;
		// IMAGE_BIG_ICON_WIDTH = 157;
		// IMAGE_MEDIUM_ICON_WIDTH = 96;
		// IMAGE_TINY_ICON_WIDTH = 16;

		BufferedImage fuf = load(input);
		if (fuf == null) {
			throw new IOException("Input File BufferedImage is null.");
		}
		// File orig = new File(outputpath + outputfn + "/Original"
		// + fnend);
		// ret[0] = save(fuf, orig, fnend);

		// File thumbf = new File(outputpath + outputfn +
		// "/Thumbnail" + fnend);
		// ret[1] = save(createThumbnail(fuf), thumbf, fnend);

		File blurf = getNamedTempFile("Blurred", fnend);
		ret[0] = save(createBlur(fuf), blurf, fnend);

		// File negt = new File(outputpath + outputfn + "/Negative"
		// + fnend);
		// ret[3] = save(createNegative(fuf), negt, fnend);

		File xiny = getNamedTempFile("Big", fnend); // File.createTempFile("Big",
													// fnend); // new
													// File(outputpath +
													// outputfn + "/Big" +
													// fnend);
		ret[1] = save(resize(fuf, Method.QUALITY, IMAGE_BIG_WIDTH, OP_ANTIALIAS), xiny, fnend);

		File xinyx = getNamedTempFile("Standard", fnend); // File.createTempFile("Standard",
															// fnend); // new
															// File(outputpath
															// + outputfn +
															// "/Standard" +
															// fnend);
		ret[2] = save(resize(fuf, Method.QUALITY, IMAGE_STANDARD_WIDTH, OP_ANTIALIAS), xinyx, fnend);

		File zxiny = getNamedTempFile("IconBig", fnend); // File.createTempFile("IconBig",
															// fnend); // new
															// File(outputpath +
															// outputfn +
															// "/IconBig" +
															// fnend);
		ret[3] = save(resize(fuf, Method.ULTRA_QUALITY, IMAGE_BIG_ICON_WIDTH, OP_ANTIALIAS), zxiny, fnend);

		File xqiny = getNamedTempFile("IconMedium", fnend); // File.createTempFile("IconMedium",
															// fnend); // new
															// File(outputpath
															// + outputfn +
															// "/IconMedium"
															// + fnend);
		ret[4] = save(resize(fuf, Method.ULTRA_QUALITY, IMAGE_MEDIUM_ICON_WIDTH, OP_ANTIALIAS), xqiny, fnend);

		File xqinyz = getNamedTempFile("IconSmall", fnend); // File.createTempFile("IconSmall",
															// fnend); // new
															// File(outputpath
															// + outputfn +
															// "/IconSmall"
															// + fnend);
		ret[5] = save(resize(fuf, Method.ULTRA_QUALITY, IMAGE_TINY_ICON_WIDTH, OP_ANTIALIAS), xqinyz, fnend);

		return ret;
	}

	// BEGIN EFFECTS

	private static File getNamedTempFile(String fname, String fnend) throws IOException {

		File ret = File.createTempFile(fname, fnend);
		ret.deleteOnExit();
		String stx = ret.getAbsolutePath()
				.substring(0, ret.getAbsolutePath().indexOf(ret.getName()))
				+ fname + fnend;
		ret.delete();
		File rex = new File(stx);
		rex.deleteOnExit();
		return rex;
	}

	/**
	 * thumbsized
	 * 
	 * @param img
	 * @return
	 */
	public static BufferedImage createThumbnail(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		// Let's add a little border before we return result.
		return resize(img, Method.ULTRA_QUALITY, IMAGE_BIG_ICON_WIDTH, OP_ANTIALIAS);

	}

	public static BufferedImage createBigImage(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		return resize(img, Method.QUALITY, IMAGE_BIG_WIDTH, OP_ANTIALIAS);
	}

	public static BufferedImage createTinyImage(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		return resize(img, Method.ULTRA_QUALITY, 125, OP_ANTIALIAS);
	}

	/**
	 * apply a negative effect
	 * 
	 * @param img
	 * @return
	 */
	public static BufferedImage createNegative(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		ImageFilter im = new ImageFilter();

		BufferedImage ret = resize(img, Method.ULTRA_QUALITY, ImageUtils.IMAGE_BIG_WIDTH);

		return im.negativeBufferedImage(ret);
	}

	/**
	 * apply a blur effect
	 * 
	 * @param img
	 * @return
	 */
	public static BufferedImage createBlur(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		ImageFilter im = new ImageFilter();

		BufferedImage ret = resize(img, Method.ULTRA_QUALITY, ImageUtils.IMAGE_BIG_WIDTH);

		return im.blurBufferedImage(ret);
	}

	// END EFFECTS

	protected static BufferedImage load(File name) {
		BufferedImage i = null;

		try {
			i = ImageIO.read(name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return i;
	}

	protected static File save(BufferedImage image, File fout, String fnend) {
		try {
			fout.mkdirs();
			fout.delete();
			fout = new File(fout.getPath());
			fnend = fnend.replace(".", "").toUpperCase();
			ImageIO.write(image, "PNG", new FileOutputStream(fout));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fout;
	}

	/**
	 * fetch the contents of remote files
	 * 
	 * @param urlstr
	 * @return
	 * @throws IOException
	 */
	public static File readFromUrl(String urlstr, File file) throws IOException {

		if (urlstr.indexOf("http://") < 0) {
			if (urlstr.indexOf("http:/") > -1) {
				urlstr = urlstr.replace("http:/", "http://");
			}
		}
		URL u = new URL(urlstr);
		URLConnection uc = u.openConnection();
		uc.connect();
		String contentType = uc.getContentType();
		int contentLength = uc.getContentLength();
		if (contentType.startsWith("text/") || contentLength == -1) {
			throw new IOException("This is not a binary file.");
		}
		InputStream raw = uc.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1)
				break;
			offset += bytesRead;
		}
		in.close();

		if (offset != contentLength) {
			throw new IOException("Only read " + offset + " bytes; Expected "
					+ contentLength + " bytes");
		}
		FileOutputStream out = new FileOutputStream(file);
		out.write(data);
		out.flush();
		out.close();

		return file;

	}
}