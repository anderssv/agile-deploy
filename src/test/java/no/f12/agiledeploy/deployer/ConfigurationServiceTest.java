package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import no.f12.agiledeploy.deployer.deploy.fs.ConfigurationServiceImpl;
import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapter;
import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapterImpl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfigurationServiceTest extends AbstractFileSystemTest {

	private File workingDirectory;
	private File environmentDirectory;
	private File installationDirectory;

	private static final String ARTIFACT_NAME = "spring-core";

	@Before
	public void setupDirectories() throws IOException {
		this.workingDirectory = getWorkingDirectory();
		this.environmentDirectory = new File(workingDirectory, ARTIFACT_NAME + "/test");
		this.installationDirectory = new File(workingDirectory, ARTIFACT_NAME + "/test/current");
		TestDataProvider.unpackDefaultTestZip(this.installationDirectory);
	}

	@Test
	public void shouldCopyFilesForCurrentEnvironmentIfTheyDontExist() throws IOException {
		ConfigurationServiceImpl configService = createService();
		configService.configure(environmentDirectory, "test",
				TestDataProvider.createDefaultDeploymentSpec(false, workingDirectory, null));

		assertTrue(new File(workingDirectory, ARTIFACT_NAME + "/test/datasource.properties").exists());
	}

	@Test
	public void shouldNotCopyFilesIfTheyAlreadyExist() throws IOException {
		File target = new File(workingDirectory, ARTIFACT_NAME + "/test/datasource.properties");
		TestDataProvider.writeContentToFile(target, "testing");

		ConfigurationServiceImpl configService = createService();
		configService.configure(environmentDirectory, "test",
				TestDataProvider.createDefaultDeploymentSpec(false, workingDirectory, null));

		TestDataProvider.assertFileContains(target, "testing");
	}

	@Test
	public void shouldCopyFilesForAllEnvironments() throws IOException {
		ConfigurationServiceImpl configService = createService();

		configService.configure(environmentDirectory, "test",
				TestDataProvider.createDefaultDeploymentSpec(false, workingDirectory, null));

		assertTrue(new File(workingDirectory, ARTIFACT_NAME + "/test/allenvs.properties").exists());
	}

	@Test
	public void shouldNotOverWriteCustomFileForEnvironmentWithFileForAllEnvironments() throws IOException {
		ConfigurationServiceImpl configService = createService();

		configService.configure(environmentDirectory, "test",
				TestDataProvider.createDefaultDeploymentSpec(false, workingDirectory, null));

		Resource propertyFile = new DefaultResourceLoader().getResource("file:" + environmentDirectory.getPath()
				+ "/system.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(propertyFile);
		assertTrue(props.get("env").equals("test"));
	}

	@Test
	public void shouldCreateAndSymToDirectories() throws IOException {
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = mock(FileSystemAdapter.class);
		configService.setFileSystemAdapter(fsAdapter);

		configService.configure(environmentDirectory, "test",
				TestDataProvider.createDefaultDeploymentSpec(false, workingDirectory, null));

		String[] dirNames = new String[] { "data", "logs" };
		for (String dirName : dirNames) {
			verify(fsAdapter).createSymbolicLink(new File(workingDirectory, ARTIFACT_NAME + "/test/" + dirName),
					new File(workingDirectory, ARTIFACT_NAME + "/test/current/" + dirName));
		}
	}

	@Test
	public void shouldSymLinkToPropertiesFiles() {
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = spy(new FileSystemAdapterImpl());
		configService.setFileSystemAdapter(fsAdapter);

		configService.configure(environmentDirectory, "test",
				TestDataProvider.createDefaultDeploymentSpec(false, workingDirectory, null));

		verify(fsAdapter).createSymbolicLink(new File(workingDirectory, ARTIFACT_NAME + "/test/allenvs.properties"),
				new File(workingDirectory, ARTIFACT_NAME + "/test/current/allenvs.properties"));
	}

	@Test
	public void shouldCopyIfSymLinkFails() throws IOException {
		File target = new File(workingDirectory, ARTIFACT_NAME + "/test/datasource.properties");
		TestDataProvider.writeContentToFile(target, "testing");

		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = mock(FileSystemAdapter.class);
		configService.setFileSystemAdapter(fsAdapter);

		doThrow(new IllegalStateException("No symlinks here")).when(fsAdapter).createSymbolicLink((File) anyObject(),
				(File) anyObject());

		configService.configure(environmentDirectory, "test",
				TestDataProvider.createDefaultDeploymentSpec(false, workingDirectory, null));

		verify(fsAdapter).copyFile(new File(workingDirectory, ARTIFACT_NAME + "/test/datasource.properties"),
				new File(workingDirectory, ARTIFACT_NAME + "/test/current/datasource.properties"));
	}

	@Test
	public void shouldChangePermissionsOnBinaryDirectory() {
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		FileSystemAdapter fsAdapter = spy(new FileSystemAdapterImpl());
		configService.setFileSystemAdapter(fsAdapter);

		configService.configure(environmentDirectory, "test",
				TestDataProvider.createDefaultDeploymentSpec(false, workingDirectory, null));

		verify(fsAdapter).changePermissionsOnFile(
				new File(workingDirectory, ARTIFACT_NAME + "/test/current/bin/application"), "u+x");
		verify(fsAdapter).changePermissionsOnFile(
				new File(workingDirectory, ARTIFACT_NAME + "/test/current/bin/application.bat"), "u+x");
	}

	private ConfigurationServiceImpl createService() {
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		configService.setFileSystemAdapter(new FileSystemAdapterImpl());
		return configService;
	}

}
