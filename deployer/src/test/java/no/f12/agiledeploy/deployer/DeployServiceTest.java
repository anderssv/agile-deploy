package no.f12.agiledeploy.deployer;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;

public class DeployServiceTest {

	private RepositoryService repoServ;
	private UnpackerService unpackServ;
	private DeployServiceImpl dServ;
	private File downloadedFile;

	public void createMocks() {
		repoServ = mock(RepositoryService.class);
		unpackServ = mock(UnpackerService.class);

		downloadedFile = new File(".");
		when(repoServ.fetchPackage((PackageSpecification) anyObject())).thenReturn(downloadedFile);
	}

	public void createService() {
		createMocks();
		dServ = new DeployServiceImpl();
		dServ.setRepositoryService(repoServ);
		dServ.setUnpackerService(unpackServ);
	}

	@Test
	public void shouldDownloadAndThenUnpack() {
		createService();
		PackageSpecification spec = PackageSpecificationTest.createDefaultSpect(false);

		dServ.deploy(spec);

		verify(repoServ).fetchPackage(spec);
		verify(unpackServ).unpack(downloadedFile);
	}

}
