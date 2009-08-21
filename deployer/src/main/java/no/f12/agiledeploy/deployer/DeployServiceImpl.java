package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeployServiceImpl implements DeployService {

	@Autowired(required = true)
	private RepositoryService repositoryService;
	@Autowired(required = true)
	private UnpackerService unpackerService;
	@Autowired(required = true)
	private ConfigurationService configurationService;
	@Autowired(required = true)
	private FileSystemAdapter fileSystemAdapter;
	@Autowired(required = true)
	private DataBaseService databaseService;

	public void setRepositoryService(RepositoryService repoServ) {
		this.repositoryService = repoServ;
	}

	public void setUnpackerService(UnpackerService unpackServ) {
		this.unpackerService = unpackServ;
	}

	@Override
	public void deploy(PackageSpecification spec, String environment, File workingDirectory) {
		File installationDirectory = spec.getFileSystemInformation().getArtifactInstallationDirectory(workingDirectory,
				environment);
		File environmentDirectory = spec.getFileSystemInformation().getArtifactEnvironmentDirectory(workingDirectory,
				environment);
		workingDirectory.mkdirs();

		File downloadedFile = repositoryService.fetchPackage(spec, workingDirectory);

		prepareInstallationDirectory(installationDirectory, spec, environment);

		unpackerService.unpack(downloadedFile, installationDirectory);
		removeArtifactAndVersionFromPath(installationDirectory, spec);

		configurationService.configure(environmentDirectory, environment);

		databaseService.upgradeDatabase(installationDirectory);
		
		downloadedFile.deleteOnExit();
	}

	private void prepareInstallationDirectory(File installationDirectory, PackageSpecification spec, String environment) {
		if (installationDirectory.exists()) {
			fileSystemAdapter.deleteDir(installationDirectory, new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if (pathname.getName().equals("data")) {
						return false;
					}
					return true;
				}
			});
		} else if (!installationDirectory.mkdirs()) {
			throw new IllegalStateException("Could not create directory to deploy to: " + installationDirectory);
		}
	}

	public void removeArtifactAndVersionFromPath(File deployDirectory, PackageSpecification spec) {
		fileSystemAdapter.moveOneUp(new File(deployDirectory, spec.getArtifactFileName()));
	}

	public void setConfigurationService(ConfigurationService configServ) {
		this.configurationService = configServ;
	}

	public void setFileSystemAdapter(FileSystemAdapter adapter) {
		this.fileSystemAdapter = adapter;
	}

	public void setDatabaseService(DataBaseService dbService) {
		this.databaseService = dbService;
	}

}
