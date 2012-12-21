package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.FileFilter;

import no.f12.agiledeploy.deployer.DeploymentSpecification;
import no.f12.agiledeploy.deployer.repo.PackageSpecification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	private static final Logger LOG = Logger.getLogger(ConfigurationServiceImpl.class);

	@Autowired(required = true)
	private FileSystemAdapter fileSystemAdapter;

	@Override
	public void configure(File environmentDirectory, String environment, DeploymentSpecification spec) {
		File configDir = spec.getConfigurationDirectory();
		File environmentConfigDir = spec.getEnvironmentPropertiesDirectory();

		LOG.info("Updating configuration");
		installConfigurationFromDirectory(environmentDirectory, environmentConfigDir);
		installConfigurationFromDirectory(environmentDirectory, configDir);

		LOG.info("Creating links");
		createDirIfNotExists(spec.getDataDirectory());
		createDirIfNotExists(spec.getLogDirectory());
		createLinksToCurrent(environmentDirectory, spec);

		updateBinPermissions(spec.getBinDirectory());
	}

	private void createLinksToCurrent(File environmentDirectory, DeploymentSpecification spec) {
		final File installDirectory = spec.getLastInstalledVersionDirectory();
		File[] entries = environmentDirectory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File path) {
				if (path.isDirectory() && path.equals(installDirectory)) {
					return false;
				}
				return true;
			}
		});

		for (File file : entries) {
			linkInto(file, spec.getLastInstalledVersionDirectory());
		}
	}

	private void createDirIfNotExists(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	private void updateBinPermissions(File binDirectory) {
		if (binDirectory.exists()) {
			for (File binFile : binDirectory.listFiles()) {
				try {
					fileSystemAdapter.changePermissionsOnFile(binFile, "u+x");
				} catch (IllegalStateException e) {
					LOG.warn("Could not set execute permissions on " + binFile, e);
				}
			}
		}
	}

	private void linkInto(File realFile, File installationDirectory) {
		File link = new File(installationDirectory, realFile.getName());
		if (link.exists()) {
			boolean deleted = link.delete();
			if (!deleted) {
				LOG.warn("Tried to create symlink, but it already existed and could not be deleted: " + link);
				return;
			}
		}

		if (realFile.isDirectory()) {
			createSymbolicLink(realFile, link, false);
		} else {
			createSymbolicLink(realFile, link, true);
		}
	}

	private void createSymbolicLink(File realFile, File link, boolean copyFailover) {
		try {
			this.fileSystemAdapter.createSymbolicLink(realFile, link);
			LOG.info("Created link for " + realFile + " at " + link);
		} catch (IllegalStateException e) {
			if (copyFailover && realFile.isFile()) {
				this.fileSystemAdapter.copyFile(realFile, link);
				LOG.info("Unable to link. Copied " + realFile);
			} else {
				LOG.warn("Could not create sym link to " + realFile + " at " + link
						+ ". Please make sure your application does not write to any directories under your root.", e);
			}
		}
	}

	private void installConfigurationFromDirectory(File environmentDirectory, File configDir) {
		if (configDir.exists()) {
			File[] files = configDir.listFiles();
			for (File source : files) {
				File target = new File(environmentDirectory, source.getName());
				if (!target.exists() && source.isFile()) {
					fileSystemAdapter.copyFile(source, target);
					LOG.info("Installed configuration file: " + source);
				} else {
					LOG.debug("File already exists, skipping " + source);
				}
			}
		}
	}

	public void setFileSystemAdapter(FileSystemAdapter fsAdapter) {
		this.fileSystemAdapter = fsAdapter;
	}

}