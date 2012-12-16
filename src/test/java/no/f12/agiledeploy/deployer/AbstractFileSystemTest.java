package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapter;
import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapterImpl;

import org.junit.After;
import org.junit.Test;

public class AbstractFileSystemTest {

	private File workingDirectory;

	protected File getWorkingDirectory() {
		if (this.workingDirectory == null) {
			this.workingDirectory = new File("./target/temp/" + getClass().getSimpleName());
		}
		return this.workingDirectory;
	}

	@Test
	public void emptyTestToAvoidFailure() {

	}

	@After
	public void deleteWorkingFiles() throws InterruptedException, IOException {
		deleteDirectoryWithFilter(getWorkingDirectory(), null);
	}

	/**
	 * DB Deploy has some issues with file handling. This is a short term fix.
	 * 
	 * @param directory
	 * @param filter
	 */
	public void deleteDirectoryWithFilter(File directory, FileFilter filter) {
		FileSystemAdapter fsAdapter = new FileSystemAdapterImpl();
		if (filter == null) {
			fsAdapter.deleteDir(directory);
		} else {
			fsAdapter.deleteDir(directory, filter);
		}
	}

}
