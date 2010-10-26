package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class UnpackerServiceTest extends AbstractFileSystemTest {

	File unpackDir = getWorkingDirectory();

	@Test
	public void shouldUnpackCorrectly() throws IOException {
		TestDataProvider.unpackDefaultTestZip(unpackDir);

		assertTrue(new File(unpackDir, "bin").exists());
		assertTrue(new File(unpackDir, "repo").exists());
		assertTrue(new File(unpackDir, "bin/application").exists());
		assertTrue(new File(unpackDir, "bin/application.bat").exists());
		assertTrue(new File(unpackDir, "config/system.properties").exists());
	}

}
