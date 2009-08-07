package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

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

	public static void copyFile(File source, File target) {
		try {
			FileChannel in = (new FileInputStream(source)).getChannel();
			FileChannel out = (new FileOutputStream(target)).getChannel();
			in.transferTo(0, source.length(), out);
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("File to copy from could not be found", e);
		} catch (IOException e) {
			throw new IllegalStateException("Error copying file " + source, e);
		}
	}

	public static void moveOneUp(File deployDirectory, String directoryName) {
		File dir = new File(deployDirectory, directoryName);
		File[] subFiles = dir.listFiles();
		if (subFiles != null) {
			for (File file : subFiles) {
				File target = new File(deployDirectory, file.getName());
				file.renameTo(target);
			}
		}
		dir.delete();
	}

}
