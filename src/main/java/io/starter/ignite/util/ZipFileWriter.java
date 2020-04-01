/**
 *
 */
package io.starter.ignite.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

/**
 * utility to zip a file
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 * @author www.codejava.net
 */
public class ZipFileWriter {

	/**
	 * A constants for buffer size used to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Compresses a list of files to a destination zip file
	 *
	 * @param listFiles   A collection of files and directories
	 * @param destZipFile The path of the destination zip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void zip(List<File> listFiles, String destZipFile) throws FileNotFoundException, IOException {
		final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));
		for (final File file : listFiles) {
			if (file.isDirectory()) {
				zipDirectory(file, file.getName(), zos);
			} else {
				zipFile(file, zos);
			}
		}
		zos.flush();
		zos.close();
	}

	/**
	 * Compresses files represented in an array of paths
	 *
	 * @param files       a String array containing file paths
	 * @param destZipFile The path of the destination zip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void zip(String[] files, String destZipFile) throws FileNotFoundException, IOException {
		final List<File> listFiles = new ArrayList<>();
		for (final String file : files) {
			listFiles.add(new File(file));
		}
		zip(listFiles, destZipFile);
	}

	/**
	 * Adds a directory to the current zip output stream
	 *
	 * @param folder       the directory to be added
	 * @param parentFolder the path of parent directory
	 * @param zos          the current zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos)
			throws FileNotFoundException, IOException {
		for (final File file : folder.listFiles()) {
			if (file.isDirectory()) {
				zipDirectory(file, parentFolder + "/" + file.getName(), zos);
				continue;
			}
			zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
			final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			final byte[] bytesIn = new byte[ZipFileWriter.BUFFER_SIZE];
			int read = 0;
			while ((read = bis.read(bytesIn)) != -1) {
				zos.write(bytesIn, 0, read);
			}
			zos.closeEntry();
			bis.close();
		}
	}

	/**
	 * Adds a file to the current zip output stream
	 *
	 * @param file the file to be added
	 * @param zos  the current zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void zipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException {
		zos.putNextEntry(new ZipEntry(file.getName()));
		final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		// long bytesRead = 0;
		final byte[] bytesIn = new byte[ZipFileWriter.BUFFER_SIZE];
		int read = 0;
		while ((read = bis.read(bytesIn)) != -1) {
			zos.write(bytesIn, 0, read);
			// bytesRead += read;
		}
		zos.closeEntry();
		bis.close();
	}

	public void zip(File file, String destZipFile) throws Exception {
		final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));

		if (file.isDirectory()) {
			zipDirectory(file, file.getName(), zos);
		} else {
			zipFile(file, zos);
		}

		zos.flush();
		zos.close();
	}

}