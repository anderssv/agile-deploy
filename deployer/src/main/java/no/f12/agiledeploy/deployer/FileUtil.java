package no.f12.agiledeploy.deployer;

import java.io.File;

import org.apache.log4j.Logger;

public class FileUtil {

	private static final Logger LOG = Logger.getLogger(FileUtil.class); 
	
	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns
	// false.
	public static boolean deleteDir(File dir) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}

			// This is a file, or directory is empty
			return dir.delete();
		}
		return true;
	}
	
	public static void deleteWithLogging(File dir) {
		if (!deleteDir(dir)) {
			LOG.warn("Could not delete: " + dir);
		}
	}

}
