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

	public void deploy(PackageSpecification spec, String environment, File baseDirectory) {
		File downloadedFile = repositoryService.fetchPackage(spec);

		File deployDirectory = new File(baseDirectory, spec.getArtifactId() + "/" + environment);
		if (!deployDirectory.exists() && !deployDirectory.mkdirs()) {
			throw new IllegalStateException("Could not create directory to deploy to: " + deployDirectory);
		}
		unpackerService.unpack(downloadedFile);
	}

}
