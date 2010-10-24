package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class FileSystemAdapterImpl implements FileSystemAdapter {

	@Override
	public void copyFile(File source, File target) {
		try {
			FileUtils.copyFile(source, target);
		} catch (IOException e) {
			throw new IllegalStateException("Could not copy file from " + source + " to " + target, e);
		}
	}

	@Override
	public void deleteDir(File directory) {
		try {
			FileUtils.deleteDirectory(directory);
		} catch (IOException e) {
			throw new IllegalStateException("Could not delete directory " + directory, e);
		}
	}

	@Override
	public void moveOneUp(File directory) {
		try {
			File targetDir = directory.getParentFile();
			File[] filesToMove = directory.listFiles();
			Assert.notEmpty(filesToMove);
			for (File file : filesToMove) {
				if (file.isDirectory()) {
					FileUtils.moveDirectoryToDirectory(file, targetDir, true);
				} else {
					FileUtils.moveFileToDirectory(file, targetDir, false);
				}
			}
			directory.delete();
		} catch (IOException e) {
			throw new IllegalStateException("Could not move directory one up", e);
		}
	}

	@Override
	public void createSymbolicLink(File source, File symLink) {
		FileUtil.createSymbolicLink(source, symLink);
	}

	public void deleteDir(File dir, FileFilter filter) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (filter.accept(file)) {
					if (file.isDirectory()) {
						FileUtils.deleteDirectory(file);
					} else {
						file.delete();
					}
				} else {
					file.delete();
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Could not delete directory " + dir, e);
		}
	}

	@Override
	public void changePermissionsOnFile(File file, String permission) {
		FileUtil.changePermissions(file, permission);
	}

}
