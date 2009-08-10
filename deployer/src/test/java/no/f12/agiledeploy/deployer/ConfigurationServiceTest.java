package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfigurationServiceTest {

	@Test
	public void shouldCopyFilesForCurrentEnvironmentIfTheyDontExist() throws IOException {
		File workingDirectory = createFiles();

		ConfigurationServiceImpl configService = createService();
		configService.configure(workingDirectory, "test");

		assertTrue(new File(workingDirectory, "datasource.properties").exists());
	}

	private ConfigurationServiceImpl createService() {
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		configService.setFileSystemAdapter(new FileSystemAdapterImpl());
		return configService;
	}

	private File createFiles() throws IOException {
		UnpackerService unpacker = new UnpackerServiceImpl();
		File workingDirectory = TestDataProvider.getDefaultTempDir();
		unpacker.unpack(TestDataProvider.getZipFile(workingDirectory), workingDirectory);
		FileUtil.moveOneUp(new File(workingDirectory, "myapp-server-0.1-SNAPSHOT"));
		return workingDirectory;
	}

	@Test
	public void shouldNotCopyFilesIfTheyAlreadyExist() throws IOException {
		File workingDirectory = createFiles();

		File target = new File(workingDirectory, "datasource.properties");
		FileWriter writer = new FileWriter(target);
		writer.write("testing");
		writer.close();

		ConfigurationServiceImpl configService = createService();
		configService.configure(workingDirectory, "test");

		FileReader reader = new FileReader(target);
		BufferedReader bReader = new BufferedReader(reader);
		assertEquals("testing", bReader.readLine());
		bReader.close();
	}

	@Test
	public void shouldCopyFilesForAllEnvironments() throws IOException {
		File workingDirectory = createFiles();
		ConfigurationServiceImpl configService = createService();

		configService.configure(workingDirectory, "test");

		assertTrue(new File(workingDirectory, "allenvs.properties").exists());
	}

	@Test
	public void shouldNotOverWriteCustomFileForEnvironmentWithFileForAllEnvironments() throws IOException {
		File workingDirectory = createFiles();
		ConfigurationServiceImpl configService = createService();

		configService.configure(workingDirectory, "test");

		Resource propertyFile = new DefaultResourceLoader().getResource("file:" + workingDirectory.getPath()
				+ "/system.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(propertyFile);
		assertTrue(props.get("env").equals("test"));
	}

	@Test
	public void shouldSymLinkToDataDirectoryIfItExists() throws IOException {
		File workingDirectory = createFiles();
		File dataDir = createDataDir(workingDirectory);

		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = mock(FileSystemAdapter.class);
		configService.setFileSystemAdapter(fsAdapter);
		
		configService.configure(workingDirectory, "test");
		
		verify(fsAdapter).createSymbolicLink(dataDir, new File(workingDirectory, "data"));
	}

	private File createDataDir(File workingDirectory) {
		File dataDir = new File(workingDirectory, "data");
		dataDir.mkdirs();
		return dataDir;
	}

	@After
	public void removeDir() {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}

}
