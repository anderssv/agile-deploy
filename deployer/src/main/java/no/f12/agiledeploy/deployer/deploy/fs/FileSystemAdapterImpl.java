package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.FileFilter;

import org.springframework.stereotype.Service;

@Service
public class FileSystemAdapterImpl implements FileSystemAdapter {

	@Override
	public void copyFile(File source, File target) {
		FileUtil.copyFile(source, target);
	}

	@Override
	public void deleteDir(File directory) {
		FileUtil.deleteDir(directory);
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
		FileUtil.deleteDir(dir, filter);
	}

	@Override
	public void changePermissionsOnFile(File file, String permission) {
		FileUtil.changePermissions(file, permission);
	}

}
