package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.dbdeploy.DbDeploy;

@Component
public class DataBaseServiceImpl implements DataBaseService {

	private String databaseUrl;
	private String password;
	private String username;
	private String driver;
	private String dbms;

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
		this.password = (String) props.get("db.password");
		this.username = (String) props.get("db.username");
		this.driver = (String) props.get("db.driver");
		this.dbms = (String) props.get("db.dbms");
	}

	public String getDatabaseUrl() {
		return this.databaseUrl;
	}

	public void generateScripts(File targetDir) {
		loadSettings(targetDir);
		DbDeploy deployer = new DbDeploy();
		deployer.setOutputfile(new File(targetDir, "db-upgrade.sql"));
		deployer.setScriptdirectory(new File(targetDir, "db/migrations"));
		deployer.setUrl(this.databaseUrl);
		deployer.setPassword(this.password);
		deployer.setUserid(this.username);
		deployer.setDriver(this.driver);
		deployer.setDbms(this.dbms);

		try {
			deployer.go();
		} catch (Exception e) {
			throw new IllegalStateException("Could not execute DBDeploy", e);
		}

	}

}
