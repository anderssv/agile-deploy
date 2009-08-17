package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfigurationServiceTest {

	private File workingDirectory;
	private File environmentDirectory;
	private File installationDirectory;

	@Before
	public void setupDirectories() throws IOException {
		this.workingDirectory = TestDataProvider.getDefaultTempDir();
		this.environmentDirectory = new File(workingDirectory, "test-artifact/test");
		this.installationDirectory = new File(workingDirectory, "test-artifact/test/current");
		createFiles(installationDirectory, workingDirectory);
	}

	@Test
	public void shouldCopyFilesForCurrentEnvironmentIfTheyDontExist() throws IOException {
		ConfigurationServiceImpl configService = createService();
		configService.configure(environmentDirectory, "test");

		assertTrue(new File(workingDirectory, "test-artifact/test/datasource.properties").exists());
	}

	@Test
	public void shouldNotCopyFilesIfTheyAlreadyExist() throws IOException {
		File target = new File(workingDirectory, "test-artifact/test/datasource.properties");
		TestDataProvider.writeContentToFile(target, "testing");

		ConfigurationServiceImpl configService = createService();
		configService.configure(environmentDirectory, "test");

		TestDataProvider.assertFileContains(target, "testing");
	}

	@Test
	public void shouldCopyFilesForAllEnvironments() throws IOException {
		ConfigurationServiceImpl configService = createService();

		configService.configure(environmentDirectory, "test");

		assertTrue(new File(workingDirectory, "test-artifact/test/allenvs.properties").exists());
	}

	@Test
	public void shouldNotOverWriteCustomFileForEnvironmentWithFileForAllEnvironments() throws IOException {
		ConfigurationServiceImpl configService = createService();

		configService.configure(environmentDirectory, "test");

		Resource propertyFile = new DefaultResourceLoader().getResource("file:" + environmentDirectory.getPath()
				+ "/system.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(propertyFile);
		assertTrue(props.get("env").equals("test"));
	}

	@Test
	public void shouldSymLinkToDataDirectoryIfItExists() throws IOException {
		createDataDir(environmentDirectory);

		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = mock(FileSystemAdapter.class);
		configService.setFileSystemAdapter(fsAdapter);

		configService.configure(environmentDirectory, "test");

		verify(fsAdapter).createSymbolicLink(new File(workingDirectory, "test-artifact/test/data"),
				new File(workingDirectory, "test-artifact/test/current/data"));
	}

	private File createDataDir(File baseDirectory) {
		File dataDir = new File(baseDirectory, "data");
		dataDir.mkdirs();
		return dataDir;
	}

	private ConfigurationServiceImpl createService() {
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		configService.setFileSystemAdapter(new FileSystemAdapterImpl());
		return configService;
	}

	private void createFiles(File unpackDir, File workingDir) throws IOException {
		UnpackerService unpacker = new UnpackerServiceImpl();
		unpacker.unpack(TestDataProvider.getZipFile(workingDir), unpackDir);

		FileUtil.moveOneUp(new File(unpackDir, "myapp-server-0.1-SNAPSHOT"));
	}

	@After
	public void removeDir() {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}

}
