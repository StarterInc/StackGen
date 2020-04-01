/**
 * 
 */
package io.starter.ignite.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.multi.StorageServiceEventAdaptor;
import org.jets3t.service.multi.ThreadWatcher;
import org.jets3t.service.multi.event.CreateObjectsEvent;
import org.jets3t.service.multi.event.ServiceEvent;
import org.jets3t.service.multi.s3.ThreadedS3Service;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.utils.ByteFormatter;
import org.jets3t.service.utils.Mimetypes;
import org.jets3t.service.utils.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 * 
 */
public class S3FS extends StorageServiceEventAdaptor
		implements SystemConstants {

	protected static final Logger logger = LoggerFactory.getLogger(S3FS.class);

	public S3FS() {
		super();
		try {
			initialize();
		} catch (S3ServiceException e) {
			logger.error("S3FS.init failed: " + e.getMessage());
		} catch (ServiceException e) {
			logger.error("S3FS.init failed: " + e.getMessage());
		}
	}

	private static S3Service			s3service;
	private static ThreadedS3Service	storageService;
	private static S3Bucket				bucket;
	private List<S3Object>				s3Objs			= new ArrayList<S3Object>();
	private final Set<String>			s3ObjsCompleted	= new HashSet<String>();
	private boolean						isErrorOccured	= true;
	private final ByteFormatter			byteFormatter	= new ByteFormatter();
	private final TimeFormatter			timeFormatter	= new TimeFormatter();
	private S3FileUtils					fileUtils;

	private void initialize() throws ServiceException, S3ServiceException {
		String awsAccessKey = SystemConstants.awsAccessKey;
		String awsSecretKey = SystemConstants.awsSecretKey;

		AWSCredentials credentials = new AWSCredentials(awsAccessKey,
				awsSecretKey);
		s3service = new RestS3Service(credentials);
		// bucket = new S3Bucket(SystemConstants.);
		storageService = new ThreadedS3Service(s3service, this);
		fileUtils = new S3FileUtils(s3service);

	}

	public void createBucket(String bucketname) throws NoSuchAlgorithmException, IOException, S3ServiceException {
		String[] bkn = { bucketname };
		S3FS.storageService.createBuckets(bkn);
	}

	public void makeBucketPublic(String bkt) throws ServiceException {
		// Retrieve the bucket's ACL and modify it to grant public
		// access,
		// ie READ access to the ALL_USERS group.
		S3Bucket privateBket = s3service.getBucket(bkt);
		AccessControlList bucketAcl = s3service.getBucketAcl(privateBket);
		bucketAcl
				.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);

		// Update the bucket's ACL. Now anyone can view the list of
		// objects in
		// this bucket.
		privateBket.setAcl(bucketAcl);
		s3service.putBucketAcl(privateBket);
		logger.info("View bucket's object listing here: http://s3.amazonaws.com/"
				+ privateBket.getName());
	}

	public String uploadToBucket(String bucketname, DataInputStream dataInputStream, String fx) throws ServiceException {

		StorageObject[] sob = { new StorageObject() };
		sob[0].setDataInputStream(dataInputStream);
		sob[0].setName(fx);

		S3Bucket privateBket = s3service.getBucket(bucketname);
		AccessControlList bucketAcl = s3service.getBucketAcl(privateBket);

		sob[0].setAcl(bucketAcl);

		if (S3FS.storageService.putObjects(bucketname, sob)) {
			StorageObject objectDetailsOnly = s3service
					.getObjectDetails(bucketname, fx);
			logger.info("S3FS.uploadToBucket success: "
					+ objectDetailsOnly.getName());
			return objectDetailsOnly.getName();
		}
		logger.error("S3FS.uploadToBucket: " + bucketname + " failed.");
		return null;
	}

	public void uploadFolder(File folder) throws NoSuchAlgorithmException, IOException, S3ServiceException {
		readFolderContents(folder);
		uploadFilesInList(folder);
	}

	private void readFolderContents(File folder) throws NoSuchAlgorithmException, IOException, S3ServiceException {
		Iterator<?> filesinFolder = fileUtils.iterateFiles(folder, null, true);
		while (filesinFolder.hasNext()) {
			Object file = filesinFolder.next();
			logger.info("File: " + file);
			String key = "testfilnamekey";
			if (folder.isDirectory()) {
				S3Object s3Obj = new S3Object(bucket, (File) file);
				s3Obj.setKey(key);
				s3Obj.setContentType(Mimetypes.getInstance()
						.getMimetype(s3Obj.getKey()));
				s3Objs.add(s3Obj);
			} else {
				logger.info("S3FS.readFolderContents() File: "
						+ folder.getName() + " is not a folder.");
			}
		}
	}

	private void uploadFilesInList(File folder) {
		logger.info("Uploading files in folder " + folder.getAbsolutePath());
		isErrorOccured = false;
		s3ObjsCompleted.clear();

		storageService.putObjects(bucket.getName(), s3Objs
				.toArray(new S3Object[s3Objs.size()]));

		if (isErrorOccured || s3Objs.size() != s3ObjsCompleted.size()) {
			logger.info("Have to try uploading a few objects again for folder "
					+ folder.getAbsolutePath() + " - Completed = "
					+ s3ObjsCompleted.size() + " and Total =" + s3Objs.size());
			List<S3Object> s3ObjsRemaining = new ArrayList<S3Object>();
			for (S3Object s3Obj : s3Objs) {
				if (!s3ObjsCompleted.contains(s3Obj.getKey())) {
					s3ObjsRemaining.add(s3Obj);
				}
			}
			s3Objs = s3ObjsRemaining;
			uploadFilesInList(folder);
		}
	}

	@Override
	public void event(CreateObjectsEvent event) {
		super.event(event);
		if (ServiceEvent.EVENT_IGNORED_ERRORS == event.getEventCode()) {
			Throwable[] throwables = event.getIgnoredErrors();
			for (int i = 0; i < throwables.length; i++) {
				logger.error("Ignoring error: " + throwables[i].getMessage());
			}
		} else if (ServiceEvent.EVENT_STARTED == event.getEventCode()) {
			logger.info("**********************************Upload Event Started***********************************");
		} else if (event.getEventCode() == ServiceEvent.EVENT_ERROR) {
			isErrorOccured = true;
		} else if (event.getEventCode() == ServiceEvent.EVENT_IN_PROGRESS) {
			StorageObject[] storeObjs = event.getCreatedObjects();
			for (StorageObject storeObj : storeObjs) {
				s3ObjsCompleted.add(storeObj.getKey());
			}
			ThreadWatcher watcher = event.getThreadWatcher();
			if (watcher.getBytesTransferred() >= watcher.getBytesTotal()) {
				logger.info("Upload Completed.. Verifying");
			} else {
				int percentage = (int) (((double) watcher.getBytesTransferred()
						/ watcher.getBytesTotal()) * 100);

				long bytesPerSecond = watcher.getBytesPerSecond();
				StringBuilder transferDetailsText = new StringBuilder(
						"Uploading.... ");
				transferDetailsText.append("Speed: "
						+ byteFormatter.formatByteSize(bytesPerSecond) + "/s");

				if (watcher.isTimeRemainingAvailable()) {
					long secondsRemaining = watcher.getTimeRemaining();
					if (transferDetailsText.length() > 0) {
						transferDetailsText.append(" - ");
					}
					transferDetailsText.append("Time remaining: "
							+ timeFormatter.formatTime(secondsRemaining));
				}

				logger.info(transferDetailsText.toString() + " " + percentage);
			}
		} else if (ServiceEvent.EVENT_COMPLETED == event.getEventCode()) {
			logger.info("**********************************Upload Event Completed***********************************");
			if (isErrorOccured) {
				logger.info("**********************But with errors, have to retry failed uploads**************************");
			}
		}
	}

}