package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapter;
import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapterImpl;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

public class AbstractFileSystemTest {

	@Test
	public void emptyTestToAvoidFailure() {

	}

	@After
	public void deleteWorkingFiles() throws InterruptedException, IOException {
		deleteDirectoryWithDbDeployFix(TestDataProvider.getDefaultTempDir(), null);
	}

	/**
	 * DB Deploy has some issues with file handling. This is a short term fix.
	 * 
	 * @param directory
	 * @param filter
	 */
	public void deleteDirectoryWithDbDeployFix(File directory, FileFilter filter) {
		FileSystemAdapter fsAdapter = new FileSystemAdapterImpl();
		try {
			if (filter == null) {
				fsAdapter.deleteDir(directory);
			} else {
				fsAdapter.deleteDir(directory, filter);
			}
		} catch (IllegalStateException e) {
			if (!e.getCause().getMessage().contains("db-upgrade.sql")) {
				throw e;
			}
		}
	}

}
