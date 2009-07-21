package no.f12.agiledeploy.deployer;

public class DeployServiceImpl implements DeployService {

	private RepositoryService repositoryService;
	private UnpackerService unpackerService;

	public void setRepositoryService(RepositoryService repoServ) {
		this.repositoryService = repoServ;
	}

	public void setUnpackerService(UnpackerService unpackServ) {
		this.unpackerService = unpackServ;
	}

}
