package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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
		TestDataProvider.unpackDefaultTestZip(this.installationDirectory);
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

	@Test
	public void shouldSymLinkToPropertiesFiles() {
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = spy(new FileSystemAdapterImpl());
		configService.setFileSystemAdapter(fsAdapter);

		configService.configure(environmentDirectory, "test");

		verify(fsAdapter).createSymbolicLink(new File(workingDirectory, "test-artifact/test/allenvs.properties"),
				new File(workingDirectory, "test-artifact/test/current/allenvs.properties"));
	}

	@Test
	public void shouldCopyIfSymLinkFails() throws IOException {
		File target = new File(workingDirectory, "test-artifact/test/datasource.properties");
		TestDataProvider.writeContentToFile(target, "testing");
		
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = mock(FileSystemAdapter.class);
		configService.setFileSystemAdapter(fsAdapter);

		doThrow(new IllegalStateException("No symlinks here")).when(fsAdapter).createSymbolicLink((File) anyObject(),
				(File) anyObject());

		configService.configure(environmentDirectory, "test");

		verify(fsAdapter).copyFile(new File(workingDirectory, "test-artifact/test/datasource.properties"),
				new File(workingDirectory, "test-artifact/test/current/datasource.properties"));
	}

	@Test
	public void shouldChangePermissionsOnBinaryDirectory() {
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = spy(new FileSystemAdapterImpl());
		configService.setFileSystemAdapter(fsAdapter);

		configService.configure(environmentDirectory, "test");

		verify(fsAdapter).changePermissionsOnFile(new File(workingDirectory, "test-artifact/test/current/bin/myapp"), "u+x");
		verify(fsAdapter).changePermissionsOnFile(new File(workingDirectory, "test-artifact/test/current/bin/myapp.bat"), "u+x");
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

	@After
	public void removeDir() throws InterruptedException {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}

}
