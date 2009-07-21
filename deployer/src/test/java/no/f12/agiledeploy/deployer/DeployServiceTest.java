package no.f12.agiledeploy.deployer;

import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Test;

public class DeployServiceTest {

	@Test
	public void shouldDownloadAndThenUnpack() {
		DeployServiceImpl dServ = new DeployServiceImpl();
		RepositoryService repoServ = mock(RepositoryService.class);
		UnpackerService unpackServ = mock(UnpackerService.class);
		dServ.setRepositoryService(repoServ);
		dServ.setUnpackerService(unpackServ);
		
		
		//when(repoServ.fetchPackage((PackageSpecification) anyObject())).thenReturn(new File("."));
	}
	
}
