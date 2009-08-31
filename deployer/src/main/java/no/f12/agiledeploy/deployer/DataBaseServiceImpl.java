package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
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
		generateScripts(installationDirectory);
		executeScript(getScriptFile(installationDirectory));
	}

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
		try {
			loadSettings(targetDir);

			DbDeploy deployer = new DbDeploy();
			deployer.setOutputfile(getScriptFile(targetDir));
			deployer.setScriptdirectory(new File(targetDir, "db/migrations"));
			deployer.setUrl(this.databaseUrl);
			deployer.setPassword(this.password);
			deployer.setUserid(this.username);
			deployer.setDriver(this.driver);
			deployer.setDbms(this.dbms);

			deployer.go();
		} catch (Exception e) {
			throw new DatabaseInspectionException("Could not execute DBDeploy", e);
		}

	}

	File getScriptFile(File targetDir) {
		return new File(targetDir, "db-upgrade.sql");
	}

	public void executeScript(File dbScript) {
		registerDriver(this.driver);
		DataSource ds = new SingleConnectionDataSource(this.databaseUrl, this.username, this.password, false);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		try {
			jdbcTemplate.execute(FileUtil.readToString(dbScript));
		} catch (IOException e) {
			throw new IllegalStateException("Could not read file with SQL " + dbScript, e);
		}
	}

	private void registerDriver(String driver) {
		try {
			DriverManager.registerDriver((Driver) Class.forName(driver).newInstance());
		} catch (Exception e) {
			throw new IllegalStateException("Could not register driver", e);
		}
	}

}
