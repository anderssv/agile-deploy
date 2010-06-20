package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import no.f12.agiledeploy.deployer.deploy.fs.ConfigurationService;
import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapter;
import no.f12.agiledeploy.deployer.deploy.fs.ResourceConverterService;
import no.f12.agiledeploy.deployer.repo.PackageSpecification;
import no.f12.agiledeploy.deployer.repo.RepositoryService;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

public class DeployServiceTest extends AbstractFileSystemTest {

	private RepositoryService repoServ;
	private UnpackerService unpackServ;
	private ConfigurationService configServ;
	private FileSystemAdapter fsAdapter;
	private DataBaseService dbService;
	private ResourceConverterService resConvService;

	private DeployServiceImpl dServ;

	private File downloadedFile;
	private File tempDir = getWorkingDirectory();
	private File unpackDir = TestDataProvider.getDefaultTargetDirectory(tempDir);
	private File artifactDir = TestDataProvider.getDefaultArtifactDirectory(tempDir);

	public void createMocks() {
		repoServ = mock(RepositoryService.class);
		unpackServ = mock(UnpackerService.class);
		configServ = mock(ConfigurationService.class);
		fsAdapter = mock(FileSystemAdapter.class);
		dbService = mock(DataBaseService.class);
		resConvService = mock(ResourceConverterService.class);
	}

	private void mockDownload(File downloaded) {
		downloadedFile = downloaded;
		when(repoServ.fetchPackage((PackageSpecification) anyObject(), (File) anyObject())).thenReturn(downloaded);
	}

	public void createService() {
		dServ = new DeployServiceImpl();
		dServ.setRepositoryService(repoServ);
		dServ.setUnpackerService(unpackServ);
		dServ.setConfigurationService(configServ);
		dServ.setFileSystemAdapter(fsAdapter);
		dServ.setDatabaseService(dbService);
		dServ.setResourceConverterService(resConvService);
	}

	@Test
	public void shouldDownloadAndThenUnpackAndConfigure() {
		createMocks();
		mockDownload(new File("."));
		createService();
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);

		dServ.deploy(spec, "test", tempDir);

		verify(repoServ).fetchPackage(spec, tempDir);
		verify(unpackServ).unpack(downloadedFile, unpackDir);
		verify(configServ).configure(new File(artifactDir, "test"), "test", spec);
	}

	@Test
	public void shouldCreateCorrectDirectoryForUnpack() throws IOException {
		createMocks();
		createService();

		File zipFile = TestDataProvider.getZipFile(tempDir);
		mockDownload(zipFile);

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);

		dServ.deploy(spec, "test", tempDir);

		assertTrue(unpackDir.exists());
	}

	@Test
	public void shouldUpgradeDataBaseAfterUnpacking() {
		createMocks();
		createService();
		mockDownload(new File("."));

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		dServ.deploy(spec, "test", tempDir);

		verify(this.dbService).upgradeDatabase((File) anyObject());
	}

	@Test
	public void shouldContinueWithoutDBUpgradeIfNoPropertiesCouldBeLoaded() {
		createMocks();
		createService();
		mockDownload(new File("."));

		doThrow(new DatabaseInspectionException("Could not upgrade database")).when(dbService).upgradeDatabase(
				(File) anyObject());

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		dServ.deploy(spec, "test", tempDir);
		// Expect to continue
	}

	public void shouldInitiateConversionIfEnabled() {
		createMocks();
		createService();

		verify(resConvService).convert(unpackDir);
	}

	@After
	public void removeTempDir() throws IOException {
		FileUtils.deleteDirectory(tempDir);
	}

}
