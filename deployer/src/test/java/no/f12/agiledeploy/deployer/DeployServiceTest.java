package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class DeployServiceTest {

	private RepositoryService repoServ;
	private UnpackerService unpackServ;
	private ConfigurationService configServ;
	private FileSystemAdapter fsAdapter;

	private DeployServiceImpl dServ;
	
	private File downloadedFile;
	private File tempDir = TestDataProvider.getDefaultTempDir();
	private File unpackDir = new File(tempDir, "spring-core/test/current");

	public void createMocks(File downloaded) {
		downloadedFile = downloaded;
		repoServ = mock(RepositoryService.class);
		unpackServ = mock(UnpackerService.class);
		configServ = mock(ConfigurationService.class);
		fsAdapter = mock(FileSystemAdapter.class);

		when(repoServ.fetchPackage((PackageSpecification) anyObject(), (File) anyObject())).thenReturn(downloaded);
	}

	public void createMocks() {
		createMocks(new File("."));
	}

	public void createService() {
		dServ = new DeployServiceImpl();
		dServ.setRepositoryService(repoServ);
		dServ.setUnpackerService(unpackServ);
		dServ.setConfigurationService(configServ);
		dServ.setFileSystemAdapter(fsAdapter);
	}

	@Test
	public void shouldDownloadAndThenUnpackAndConfigure() {
		createMocks();
		createService();
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);

		dServ.deploy(spec, "test", tempDir);

		verify(repoServ).fetchPackage(spec, tempDir);
		verify(unpackServ).unpack(downloadedFile, unpackDir);
		verify(configServ).configure(unpackDir, "test");
	}

	@Test
	public void shouldCreateCorrectDirectoryForUnpack() throws IOException {
		File zipFile = TestDataProvider.getZipFile(tempDir);
		createMocks(zipFile);
		createService();
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		
		dServ.deploy(spec, "test", tempDir);

		assertTrue(unpackDir.exists());
	}
	
	@After
	public void removeTempDir() {
		FileUtil.deleteDir(tempDir);
	}

}
