package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.springframework.stereotype.Component;

@Component
public class ConfigurationServiceImpl implements ConfigurationService {

	@Override
	public void configure(File unpackDir, String environment) {
		File propDir = new File(unpackDir, "properties/" + environment);
		if (propDir.exists()) {
			File[] files = propDir.listFiles();
			for (File source : files) {
				File target = new File(unpackDir, source.getName());
				FileUtil.copyFile(source, target);
			}

		}
	}


}
