package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class ConfigurationServiceTest {

	@Test
	public void shouldCopyFilesForCurrentEnvironmentIfTheyDontExist() throws IOException {
		File workingDirectory = createFiles();

		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		configService.configure(workingDirectory, "test");

		assertTrue(new File(workingDirectory, "datasource.properties").exists());
	}

	private File createFiles() throws IOException {
		UnpackerService unpacker = new UnpackerServiceImpl();
		File workingDirectory = TestDataProvider.getDefaultTempDir();
		unpacker.unpack(TestDataProvider.getZipFile(), workingDirectory);
		FileUtil.moveOneUp(workingDirectory, "myapp-server-0.1-SNAPSHOT");
		return workingDirectory;
	}

	@Test
	public void shouldNotCopyFilesIfTheyAlreadyExist() throws IOException {
		File workingDirectory = createFiles();

		File target = new File(workingDirectory, "datasource.properties");
		FileWriter writer = new FileWriter(target);
		writer.write("testing");
		writer.close();

		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		configService.configure(workingDirectory, "test");

		FileReader reader = new FileReader(target);
		BufferedReader bReader = new BufferedReader(reader);
		assertEquals("testing", bReader.readLine());
		bReader.close();
	}

	@After
	public void removeDir() {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}

}
