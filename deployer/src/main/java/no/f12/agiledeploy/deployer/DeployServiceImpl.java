package no.f12.agiledeploy.deployer;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeployServiceImpl implements DeployService {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private UnpackerService unpackerService;
	@Autowired
	private File workingDirectory = new File(".");

	public void setRepositoryService(RepositoryService repoServ) {
		this.repositoryService = repoServ;
	}

	public void setUnpackerService(UnpackerService unpackServ) {
		this.unpackerService = unpackServ;
	}

	public void deploy(PackageSpecification spec, String environment, File workingDirectory) {
		workingDirectory.mkdirs();
		File downloadedFile = repositoryService.fetchPackage(spec, workingDirectory);

		File deployDirectory = new File(workingDirectory, spec.getArtifactId() + "/" + environment);
		if (!deployDirectory.exists() && !deployDirectory.mkdirs()) {
			throw new IllegalStateException("Could not create directory to deploy to: " + deployDirectory);
		}
		unpackerService.unpack(downloadedFile, deployDirectory);
	}

	@Override
	public void deploy(PackageSpecification ps, String environment) {
		deploy(ps, environment, workingDirectory);
	}

}
