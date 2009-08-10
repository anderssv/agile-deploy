package no.f12.agiledeploy.deployer;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeployServiceImpl implements DeployService {

	@Autowired(required=true)
	private RepositoryService repositoryService;
	@Autowired(required=true)
	private UnpackerService unpackerService;
	@Autowired(required=true)
	private File workingDirectory = new File(".");
	@Autowired(required=true)
	private ConfigurationService configurationService;
	@Autowired(required=true)
	private FileSystemAdapter fileSystemAdapter;

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

		File deployDirectory = prepareDeployDirectory(workingDirectory, spec, environment);

		unpackerService.unpack(downloadedFile, deployDirectory);
		removeArtifactAndVersionFromPath(deployDirectory, spec);

		configurationService.configure(deployDirectory, environment);

		downloadedFile.deleteOnExit();
	}

	private File prepareDeployDirectory(File workingPath, PackageSpecification spec, String environment) {
		File deployDirectory = new File(workingPath, spec.getInstallationPath(environment));
		if (deployDirectory.exists()) {
			fileSystemAdapter.deleteDir(deployDirectory);
		} else if (!deployDirectory.mkdirs()) {
			throw new IllegalStateException("Could not create directory to deploy to: " + deployDirectory);
		}
		return deployDirectory;
	}

	public void removeArtifactAndVersionFromPath(File deployDirectory, PackageSpecification spec) {
		fileSystemAdapter.moveOneUp(new File(deployDirectory, spec.getArtifactFileName()));
	}

	@Override
	public void deploy(PackageSpecification ps, String environment) {
		deploy(ps, environment, workingDirectory);
	}

	public void setConfigurationService(ConfigurationService configServ) {
		this.configurationService = configServ;
	}

	public void setFileSystemAdapter(FileSystemAdapter adapter) {
		this.fileSystemAdapter = adapter;
	}

}
