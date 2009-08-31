package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class DeployServiceTest {

	private RepositoryService repoServ;
	private UnpackerService unpackServ;
	private ConfigurationService configServ;
	private FileSystemAdapter fsAdapter;
	private DataBaseService dbService;

	private DeployServiceImpl dServ;

	private File downloadedFile;
	private File tempDir = TestDataProvider.getDefaultTempDir();
	private File unpackDir = TestDataProvider.getDefaultTargetDirectory();
	private File artifactDir = TestDataProvider.getDefaultArtifactDirectory();

	public void createMocks() {
		repoServ = mock(RepositoryService.class);
		unpackServ = mock(UnpackerService.class);
		configServ = mock(ConfigurationService.class);
		fsAdapter = mock(FileSystemAdapter.class);
		dbService = mock(DataBaseService.class);
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
		verify(configServ).configure(new File(artifactDir, "test"), "test");
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

	
		doThrow(new IllegalStateException()).when(dbService).upgradeDatabase((File) anyObject());
		
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		dServ.deploy(spec, "test", tempDir);
		// Expect to continue
	}

	@After
	public void removeTempDir() {
		FileUtil.deleteDir(tempDir);
	}

}
