package no.f12.agiledeploy.deployer;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationServiceImpl implements ConfigurationService {

	private static final Logger LOG = Logger.getLogger(ConfigurationServiceImpl.class);

	@Autowired(required = true)
	private FileSystemAdapter fileSystemAdapter;

	@Override
	public void configure(File environmentDirectory, String environment) {
		File currentInstallDir = new File(environmentDirectory, "current");
		File propDir = new File(currentInstallDir, "properties");
		File environmentPropDir = new File(propDir, environment);
		File dataDir = new File(environmentDirectory, "data");

		LOG.info("Updating configuration");
		installProperties(environmentDirectory, environmentPropDir);
		installProperties(environmentDirectory, propDir);

		LOG.info("Creating links");
		createLinks(environmentDirectory, dataDir);
	}

	private void createLinks(File environmentDirectory, File dataDir) {
		if (dataDir.exists()) {
			try {
				fileSystemAdapter.createSymbolicLink(dataDir, new File(environmentDirectory, "data"));
			} catch (IllegalStateException e) {
				LOG.warn("Could not create sym link to data directory at " + dataDir
						+ ". Please make sure your application does not write to a data dir under your root.", e);
			}
		}
	}

	private void installProperties(File unpackDir, File propDir) {
		if (propDir.exists()) {
			File[] files = propDir.listFiles();
			for (File source : files) {
				File target = new File(unpackDir, source.getName());
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