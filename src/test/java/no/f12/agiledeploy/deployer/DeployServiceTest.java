package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
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
		dServ.setResourceConverterService(resConvService);
	}
	
	@Test
	public void shouldAvoidDownloadingFileIfFileIsGiven() throws IOException {
		createMocks();
		createService();
		PackageSpecification spec = new PackageSpecification("no.f12", "testapp", "1.0-SNAPHOT", "zip");
		
		dServ.deploy(new DeploymentSpecification(spec, "test", tempDir, TestDataProvider.getZipFile(tempDir)));
	}

	@Test
	public void shouldDownloadAndThenUnpackAndConfigure() {
		createMocks();
		mockDownload(new File("."));
		createService();
		DeploymentSpecification ds = TestDataProvider.createDefaultDeploymentSpec(false, tempDir, null);
		
		dServ.downloadAndDeploy(ds);

		verify(repoServ).fetchPackage(ds.getPackageSpecification(), tempDir);
		verify(unpackServ).unpack(downloadedFile, unpackDir);
		verify(configServ).configure(new File(artifactDir, "test"), "test", ds);
	}

	@Test
	public void shouldCreateCorrectDirectoryForUnpack() throws IOException {
		createMocks();
		createService();

		File zipFile = TestDataProvider.getZipFile(tempDir);
		mockDownload(zipFile);

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);

		dServ.downloadAndDeploy(new DeploymentSpecification(spec, "test", tempDir, null));

		assertTrue(unpackDir.exists());
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
