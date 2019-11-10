/**
 * 
 */
package io.starter.ignite.util;
import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
 
/**
 * utility to zip a file
 * 
 * @author john
 * @author www.codejava.net 
 */
public class ZipFileWriter {

    public static void zip(File zipf) {
        try {
            String zipFileName = zipf.getName().concat(".zip");
 
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
 
            zos.putNextEntry(new ZipEntry(zipf.getName()));
 
            byte[] bytes = Files.readAllBytes(Paths.get(zipf.getAbsolutePath()));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.close();
 
        } catch (FileNotFoundException ex) {
            System.err.format("The file %s does not exist", zipf.getAbsoluteFile());
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
    }

}