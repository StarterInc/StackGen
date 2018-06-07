package io.starter.ignite.util;

import java.io.*;

/**
 * utility methods for files
 * 
 * @author john
 *
 */
public class FileUtil {

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
				io.starter.ignite.util.Logger.log("Directory copied from " + src + "  to "
						+ dest);
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
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			io.starter.ignite.util.Logger.log("File copied from " + src + " to " + dest);
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
