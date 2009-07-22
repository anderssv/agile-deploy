package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class UnpackerServiceTest {

	File tmpDir = TestDataProvider.getDefaultTempDir();

	@Test
	public void shouldUnpackCorrectly() throws IOException {

		File tempDir = tmpDir;
		tempDir.mkdir();

		File zipFile = TestDataProvider.getZipFile();

		UnpackerService unpacker = new UnpackerServiceImpl();
		unpacker.unpack(zipFile, tempDir);

		assertTrue(new File(tempDir, "bin").exists());
		assertTrue(new File(tempDir, "bin/myapp").exists());
		assertTrue(new File(tempDir, "bin/myapp.bat").exists());
	}

	@After
	public void cleanupFiles() {
		TestDataProvider.deleteDir(tmpDir);
	}

}
