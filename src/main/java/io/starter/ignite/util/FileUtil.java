package io.starter.ignite.util;

import java.io.*;

/**
 * utility methods for files
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class FileUtil {

	/**
	 * reads a text file into a string... well we hope it's a text file...
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		try {
			while((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	/**
	 * build file path if it does not exist
	 * 
	 * @param f
	 */
	public static void ensurePathExists(File f) {

		if (f.exists()) {
			if (f.isDirectory())
				f.delete();
			return;
		}

		f.mkdirs();
		f.delete();
	}

	/**
	 * copy the folders easily
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdirs();
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {

			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			if (!dest.exists()) {
				dest.mkdirs();
				if (dest.isDirectory())
					dest.delete();
			}
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			// logger.info("File copied from " + src + " to " + dest);
		}
	}

	/**
	 * easily copy folder from source to location
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFolder(String src, String dest) throws IOException {
		FileUtil.copyFolder(new File(src), new File(dest));
	}
}
