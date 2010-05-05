package no.f12.agiledeploy.deployer;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;

public class AbstractFileSystemTest {
	
	@After
	public void deleteWorkingFiles() throws InterruptedException, IOException {
		try {
			FileUtils.deleteDirectory(TestDataProvider.getDefaultTempDir());
		} catch (IOException ioe) {
			if (!ioe.getMessage().contains("db-upgrade.sql")) {
				throw ioe;
			}
		}
	}

}
