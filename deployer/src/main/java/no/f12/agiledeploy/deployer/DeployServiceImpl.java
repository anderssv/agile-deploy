package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileFilter;

import no.f12.agiledeploy.deployer.deploy.fs.ConfigurationService;
import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapter;
import no.f12.agiledeploy.deployer.deploy.fs.ResourceConverterService;
import no.f12.agiledeploy.deployer.repo.PackageSpecification;
import no.f12.agiledeploy.deployer.repo.RepositoryService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeployServiceImpl implements DeployService {

	private static final Logger LOG = Logger.getLogger(DeployServiceImpl.class);
	
	private static final FileFilter PROTECTED_FILES_FILTER = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			if (pathname.getName().equals("data") || pathname.getName().equals("logs")) {
				return false;
			}
			return true;
		}
	};
	
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
	@Autowired(required = true)
	private ResourceConverterService resourceConverterService;

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
		// TODO Because tests download a file without a directroy. Bad fix. We just support both.
		File artifactNamedDir = new File(installationDirectory, spec.getArtifactFileName());
		if (artifactNamedDir.exists()) {
			fileSystemAdapter.moveOneUp(artifactNamedDir);
		}
		
		resourceConverterService.convert(installationDirectory);

		configurationService.configure(environmentDirectory, environment, spec);

		try {
			databaseService.upgradeDatabase(installationDirectory);
		} catch (DatabaseInspectionException e) {
			// Not too happy about this one. Any suggestions?
			LOG.info("Could not inspect database for upgrade details, skipping. Increase logging for no.f12.agiledeploy.deployer.DataBaseServiceImpl to see details.");
		}
		
		downloadedFile.deleteOnExit();
	}

	private void prepareInstallationDirectory(File installationDirectory, PackageSpecification spec, String environment) {
		if (installationDirectory.exists()) {
			fileSystemAdapter.deleteDir(installationDirectory, PROTECTED_FILES_FILTER);
		} else if (!installationDirectory.mkdirs()) {
			throw new IllegalStateException("Could not create directory to deploy to: " + installationDirectory);
		}
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

	public void setResourceConverterService(ResourceConverterService resourceConverterService) {
		this.resourceConverterService = resourceConverterService;
	}

}
