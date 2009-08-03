package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class TestDataProvider {

	public static PackageSpecification createDefaultSpec(boolean snapshot) {
		String version = "2.5.6";
		if (snapshot) {
			version += "-SNAPSHOT";
		}
		PackageSpecification spec = new PackageSpecification("org.springframework", "spring-core", version);
		return spec;
	}

	public static File getZipFile() throws IOException {
		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource("classpath:application.zip");
		File zipFile = resource.getFile();
		return zipFile;
	}

	public static File getDefaultTempDir() {
		return new File("./temp");
	}

	@Test
	public void shouldNotFailMavenBuildJustBecauseThereIsNoTestsInThisClass() {

	}
}
