package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileFilter;

import no.f12.agiledeploy.deployer.deploy.fs.ConfigurationService;
import no.f12.agiledeploy.deployer.deploy.fs.FileSystemAdapter;
import no.f12.agiledeploy.deployer.deploy.fs.ResourceConverterService;
import no.f12.agiledeploy.deployer.repo.PackageSpecification;
import no.f12.agiledeploy.deployer.repo.RepositoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeployServiceImpl implements DeployService {

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
	private ResourceConverterService resourceConverterService;

	public void setRepositoryService(RepositoryService repoServ) {
		this.repositoryService = repoServ;
	}

	public void setUnpackerService(UnpackerService unpackServ) {
		this.unpackerService = unpackServ;
	}

	@Override
	public void downloadAndDeploy(DeploymentSpecification ds) {
		File downloadedFile = repositoryService.fetchPackage(ds.getPackageSpecification(), ds.getInstallBase());
		downloadedFile.deleteOnExit();
		ds.setPackageFile(downloadedFile);
		
		deploy(ds);
	}
	
	@Override
	public void deploy(DeploymentSpecification ds) {
		PackageSpecification spec = ds.getPackageSpecification();
		File workingDirectory = ds.getInstallBase();
		String environment = ds.getEnvironment();
		File packageFile = ds.getPackageFile();
		
		File environmentDirectory = DeploymentSpecification.getEnvironmentDirectory(spec, workingDirectory, environment);
		File installationDirectory = DeploymentSpecification.getInstallDirectory(environmentDirectory, spec);
		workingDirectory.mkdirs();

		prepareInstallationDirectory(installationDirectory, spec, environment);

		unpackerService.unpack(packageFile, installationDirectory);
		// TODO Because tests download a file without a directroy. Bad fix. We
		// just support both.
		File artifactNamedDir = new File(installationDirectory, spec.getArtifactFileName());
		if (artifactNamedDir.exists()) {
			fileSystemAdapter.moveOneUp(artifactNamedDir);
		}

		resourceConverterService.convert(installationDirectory);

		configurationService.configure(environmentDirectory, environment, spec);
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

	public void setResourceConverterService(ResourceConverterService resourceConverterService) {
		this.resourceConverterService = resourceConverterService;
	}

}
