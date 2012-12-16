package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class UnpackerServiceTest extends AbstractFileSystemTest {

	File unpackDir = getWorkingDirectory();

	@Test
	public void shouldUnpackCorrectly() throws IOException {
		unpackDir.mkdir();
		File zipFile = TestDataProvider.getZipFile(unpackDir);
		UnpackerService unpacker = new UnpackerServiceImpl();
		unpacker.unpack(zipFile, unpackDir);

		File baseDir = new File(unpackDir, "myapp-server-0.1-SNAPSHOT");
		assertTrue(new File(baseDir, "bin").exists());
		assertTrue(new File(baseDir, "repo").exists());
		assertTrue(new File(baseDir, "bin/application").exists());
		assertTrue(new File(baseDir, "bin/application.bat").exists());
		assertTrue(new File(baseDir, "config/system.properties").exists());
	}

}
