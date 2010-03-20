package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public interface FileSystemAdapter {

	public void copyFile(File source, File target);

	public void deleteDir(File deployDirectory) throws IOException;

	public void deleteDir(File deployDirectory, FileFilter filter);

	public void moveOneUp(File file);

	public void createSymbolicLink(File source, File symLink);

	public void changePermissionsOnFile(File file, String permission);
	


}
