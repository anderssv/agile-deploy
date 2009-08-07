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
	@Autowired
	private ConfigurationService configurationService;

	public void setRepositoryService(RepositoryService repoServ) {
		this.repositoryService = repoServ;
	}

	public void setUnpackerService(UnpackerService unpackServ) {
		this.unpackerService = unpackServ;
	}

	@Override
	public void deploy(PackageSpecification spec, String environment, File workingDirectory) {
		workingDirectory.mkdirs();

		File downloadedFile = repositoryService.fetchPackage(spec, workingDirectory);

		File deployDirectory = createUnpackDirectory(spec, environment, workingDirectory);
		unpackerService.unpack(downloadedFile, deployDirectory);
		removeArtifactAndVersion(deployDirectory, spec);
		configurationService.configure(deployDirectory, environment);

		downloadedFile.deleteOnExit();
	}

	public static void removeArtifactAndVersion(File deployDirectory, PackageSpecification spec) {
		FileUtil.moveOneUp(deployDirectory, spec.getArtifactFileName());
	}

	private File createUnpackDirectory(PackageSpecification spec, String environment, File workingDirectory) {
		File deployDirectory = new File(workingDirectory, spec.getArtifactId() + "/" + environment + "/current");
		FileUtil.deleteWithLogging(deployDirectory);
		if (!deployDirectory.mkdirs()) {
			throw new IllegalStateException("Could not create directory to deploy to: " + deployDirectory);
		}
		return deployDirectory;
	}

	@Override
	public void deploy(PackageSpecification ps, String environment) {
		deploy(ps, environment, workingDirectory);
	}

	public void setConfigurationService(ConfigurationService configServ) {
		this.configurationService = configServ;
	}

}
