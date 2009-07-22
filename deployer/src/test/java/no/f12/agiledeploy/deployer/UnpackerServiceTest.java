package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class UnpackerServiceTest {

	@Test
	public void shouldUnpackCorrectly() throws IOException {
		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource("classpath:application.zip");

		File tempDir = new File("./temp");
		tempDir.mkdir();

		UnpackerService unpacker = new UnpackerServiceImpl();
		unpacker.unpack(resource.getFile(), tempDir);

		assertTrue(new File(tempDir, "bin").exists());
		assertTrue(new File(tempDir, "bin/myapp").exists());
		assertTrue(new File(tempDir, "bin/myapp.bat").exists());
	}
	
	@After
	public void cleanupFiles() {
		deleteDir(new File("./temp"));
	}

	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns
	// false.
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

}
