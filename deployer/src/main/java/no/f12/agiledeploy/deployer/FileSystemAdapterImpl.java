package no.f12.agiledeploy.deployer;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
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

}
