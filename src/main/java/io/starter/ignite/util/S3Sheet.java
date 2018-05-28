package io.starter.ignite.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import io.starter.ignite.util.S3FS;
import io.starter.ignite.util.SystemConstants;

import com.extentech.ExtenXLS.WorkBookHandle;

public class S3Sheet extends WorkBookHandle implements SystemConstants {

	private String fileName = "";

	public S3Sheet(String fn) throws MalformedURLException {
		super(new URL(S3_STARTER_SERVICE + S3_STARTER_SYSTEM_BUCKET + "/" + fn));
		fileName = fn;
	}

	/**
	 * write back to S3
	 */
	@Override
	public void close() {

		try {
			// write a temp file and upload. ya, weak sauce but the i/o is a
			// biotch
			File fos = File.createTempFile("tmp.", "xlsx");
			fos.deleteOnExit();
			OutputStream os = new BufferedOutputStream(
					new FileOutputStream(fos));
			this.write(os);
			os.flush();
			os.close();

			InputStream uploadedInputStream = new BufferedInputStream(
					new FileInputStream(fos));

			// S3FS is our friend
			S3FS s3fs = new S3FS();
			s3fs.uploadToBucket(S3_STARTER_MEDIA_BUCKET, new DataInputStream(
					uploadedInputStream), fileName);
		} catch (Exception ex) {
			RuntimeException er = new RuntimeException(ex.toString());
			er.setStackTrace(ex.getStackTrace());
			throw er;
		}

	}
}
