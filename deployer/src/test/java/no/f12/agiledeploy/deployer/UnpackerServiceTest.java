package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class UnpackerServiceTest {

	File tempDir = TestDataProvider.getDefaultTempDir();

	@Test
	public void shouldUnpackCorrectly() throws IOException {
		tempDir.mkdir();

		File zipFile = TestDataProvider.getZipFile();

		UnpackerService unpacker = new UnpackerServiceImpl();
		unpacker.unpack(zipFile, tempDir);
		FileUtil.moveOneUp(tempDir, "myapp-server-0.1-SNAPSHOT");

		assertTrue(new File(tempDir, "bin").exists());
		assertTrue(new File(tempDir, "repo").exists());
		assertTrue(new File(tempDir, "bin/myapp").exists());
		assertTrue(new File(tempDir, "bin/myapp.bat").exists());
	}

	@After
	public void cleanupFiles() {
		FileUtil.deleteWithLogging(tempDir);
	}

}
