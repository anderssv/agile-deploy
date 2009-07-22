package no.f12.agiledeploy.deployer;

import java.io.File;

public class DeployServiceImpl implements DeployService {

	private RepositoryService repositoryService;
	private UnpackerService unpackerService;

	public void setRepositoryService(RepositoryService repoServ) {
		this.repositoryService = repoServ;
	}

	public void setUnpackerService(UnpackerService unpackServ) {
		this.unpackerService = unpackServ;
	}

	public void deploy(PackageSpecification spec) {
		File downloadedFile = repositoryService.fetchPackage(spec);
		unpackerService.unpack(downloadedFile);
	}

}
