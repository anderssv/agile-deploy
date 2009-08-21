package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class DataBaseServiceImpl implements DataBaseService {

	private String databaseUrl;

	@Override
	public void upgradeDatabase(File installationDirectory) {
	}

	@Override
	public void loadSettings(File targetDirectory) {
		Properties props = new Properties();
		File propertyFile = new File(targetDirectory, "datasource.properties");
		try {
			props.load(new FileReader(propertyFile.getCanonicalPath()));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Could not load properties from file", e);
		} catch (IOException e) {
			throw new IllegalStateException("Could not load properties from file", e);
		}
		this.databaseUrl = (String) props.get("db.url");
	}

	public String getDatabaseUrl() {
		return this.databaseUrl;
	}

}
