package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class TestDataProvider {

	public static PackageSpecification createDefaultSpec(boolean snapshot) {
		PackageSpecification spec = new PackageSpecification("org.springframework", "spring-core", "2.5.6", snapshot);
		return spec;
	}

	public static File getZipFile() throws IOException {
		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource("classpath:application.zip");
		File zipFile = resource.getFile();
		return zipFile;
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

	public static File getDefaultTempDir() {
		return new File("./temp");
	}

	@Test
	public void shouldNotFailMavenBuildJustBecauseThereIsNoTestsInThisClass() {

	}
}
