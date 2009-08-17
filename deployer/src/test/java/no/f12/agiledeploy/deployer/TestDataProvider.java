package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

	public static File getZipFile(File workingdirectory) throws IOException {
		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource("classpath:application.zip");
		File zipFile = resource.getFile();

		workingdirectory.mkdirs();
		File result = new File(workingdirectory, zipFile.getName());
		new FileSystemAdapterImpl().copyFile(zipFile, result);

		return result;
	}

	public static File getDefaultTempDir() {
		return new File("./target/temp");
	}

	public static File getDefaultArtifactDir() {
		return new File(getDefaultTempDir(), "test-artifact");
	}

	public static void writeContentToFile(File target, String content) throws IOException {
		FileWriter writer = new FileWriter(target);
		writer.write(content);
		writer.close();
	}

	public static void assertFileContains(File target, String content) throws FileNotFoundException, IOException {
		FileReader reader = new FileReader(target);
		BufferedReader bReader = new BufferedReader(reader);
		assertEquals(content, bReader.readLine());
		bReader.close();
	}

	@Test
	public void shouldNotFailMavenBuildJustBecauseThereIsNoTestsInThisClass() {

	}
}
