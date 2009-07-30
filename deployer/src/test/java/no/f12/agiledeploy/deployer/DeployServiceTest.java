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
	private DeployServiceImpl dServ;
	private File downloadedFile;
	private File tempDir = TestDataProvider.getDefaultTempDir();

	public void createMocks(File downloaded) {
		downloadedFile = downloaded;
		repoServ = mock(RepositoryService.class);
		unpackServ = mock(UnpackerService.class);

		when(repoServ.fetchPackage((PackageSpecification) anyObject(), (File) anyObject())).thenReturn(downloaded);
	}

	public void createMocks() {
		createMocks(new File("."));
	}

	public void createService() {
		dServ = new DeployServiceImpl();
		dServ.setRepositoryService(repoServ);
		dServ.setUnpackerService(unpackServ);
	}

	@Test
	public void shouldDownloadAndThenUnpack() {
		createMocks();
		createService();
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);

		dServ.deploy(spec, "test", tempDir);

		verify(repoServ).fetchPackage(spec, tempDir);
		verify(unpackServ).unpack(downloadedFile, new File(tempDir, "spring-core/test"));
	}

	@Test
	public void shouldCreateCorrectDirectoryForUnpack() throws IOException {
		File zipFile = TestDataProvider.getZipFile();
		createMocks(zipFile);
		createService();
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		
		dServ.deploy(spec, "test", tempDir);

		File expectedDir = new File(tempDir, "spring-core/test");
		assertTrue(expectedDir.exists());
	}
	
	@After
	public void removeTempDir() {
		TestDataProvider.deleteDir(tempDir);
	}

}
