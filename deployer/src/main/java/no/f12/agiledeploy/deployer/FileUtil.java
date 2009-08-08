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

	public static boolean deleteDir(File file) {
		return deleteRecursive(file);
	}

	private static boolean deleteRecursive(File file) {
		boolean success = false;
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				for (File child : children) {
					deleteRecursive(child);
				}
				success = file.delete();
			} else {
				success = file.delete();
			}
			if (!success) {
				LOG.warn("Could not delete " + file);
			}
			return success;
		}
		return true;
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
