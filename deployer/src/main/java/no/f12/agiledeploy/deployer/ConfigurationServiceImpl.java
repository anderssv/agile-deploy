package no.f12.agiledeploy.deployer;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationServiceImpl implements ConfigurationService {

	private static final Logger LOG = Logger.getLogger(ConfigurationServiceImpl.class);

	@Override
	public void configure(File unpackDir, String environment) {
		File propDir = new File(unpackDir, "properties");
		File environmentPropDir = new File(propDir, environment);
		LOG.info("Updating configuration");
		installProperties(unpackDir, environmentPropDir);
		installProperties(unpackDir, propDir);
	}

	private void installProperties(File unpackDir, File propDir) {
		if (propDir.exists()) {
			File[] files = propDir.listFiles();
			for (File source : files) {
				File target = new File(unpackDir, source.getName());
				if (!target.exists() && source.isFile()) {
					FileUtil.copyFile(source, target);
					LOG.info("Installed configuration file: " + source);
				} else {
					LOG.debug("File already exists, skipping " + source);
				}
			}

		}
	}


}
