package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

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
	public void deleteDir(File directory) throws IOException {
		FileUtils.deleteDirectory(directory);
	}

	@Override
	public void moveOneUp(File directory) {
		FileUtil.moveOneUp(directory);
	}

	@Override
	public void createSymbolicLink(File source, File symLink) {
		FileUtil.createSymbolicLink(source, symLink);
	}

	public void deleteDir(File dir, FileFilter filter) {
		try {
			FileUtil.deleteDir(dir, filter);
		} catch (IOException e) {
			throw new IllegalStateException("Could not delete directory " + dir, e);
		}
	}

	@Override
	public void changePermissionsOnFile(File file, String permission) {
		FileUtil.changePermissions(file, permission);
	}

}
