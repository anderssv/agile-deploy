package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	private static final Logger LOG = Logger.getLogger(ConfigurationServiceImpl.class);

	@Autowired(required = true)
	private FileSystemAdapter fileSystemAdapter;

	@Override
	public void configure(File environmentDirectory, String environment) {
		File propDir = getPropertiesDirectory(environmentDirectory);
		File environmentPropDir = getEnvironmentPropertiesDirectory(environment, environmentDirectory);

		LOG.info("Updating configuration");
		installProperties(environmentDirectory, environmentPropDir);
		installProperties(environmentDirectory, propDir);

		LOG.info("Creating links");
		createDirIfNotExists(getDataDirectory(environmentDirectory));
		createDirIfNotExists(getLogDirectory(environmentDirectory));
		createLinksToCurrent(environmentDirectory);
		
		updateBinPermissions(environmentDirectory);
	}

	private void createLinksToCurrent(File environmentDirectory) {
		final File installDirectory = getLatestVersionInstallationDirectory(environmentDirectory);
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
			linkInto(file, new File(environmentDirectory, "current"));
		}
	}

	private File getLogDirectory(File environmentDirectory) {
		return new File(environmentDirectory, "logs");
	}

	private void createDirIfNotExists(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	private void updateBinPermissions(File environmentDirectory) {
		File binDir = new File(environmentDirectory, "current/bin");
		if (binDir.exists()) {
			for (File binFile : binDir.listFiles()) {
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
			try {
				this.fileSystemAdapter.createSymbolicLink(realFile, link);
				LOG.info("Created link for " + realFile + " at " + link);
			} catch (IllegalStateException e) {
				LOG.warn("Could not create sym link to " + realFile + " at " + installationDirectory
						+ ". Please make sure your application does not write to any directories under your root.", e);
			}
		} else {
			try {
				this.fileSystemAdapter.createSymbolicLink(realFile, link);
				LOG.info("Created link for " + realFile + " at " + link);
			} catch (IllegalStateException e) {
				this.fileSystemAdapter.copyFile(realFile, new File(installationDirectory, realFile.getName()));
				LOG.info("Unable to link. Copied " + realFile);
			}
		}
	}

	private File getDataDirectory(File environmentDirectory) {
		return new File(environmentDirectory, "data");
	}

	private File getEnvironmentPropertiesDirectory(String environment, File environmentDirectory) {
		return new File(getPropertiesDirectory(environmentDirectory), environment);
	}

	private File getPropertiesDirectory(File environmentDirectory) {
		return new File(getLatestVersionInstallationDirectory(environmentDirectory), "properties");
	}

	private File getLatestVersionInstallationDirectory(File environmentDirectory) {
		return new File(environmentDirectory, "current");
	}

	private void installProperties(File environmentDirectory, File propDir) {
		if (propDir.exists()) {
			File[] files = propDir.listFiles();
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