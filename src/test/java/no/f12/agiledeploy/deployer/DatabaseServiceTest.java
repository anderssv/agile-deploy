package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import no.f12.agiledeploy.deployer.deploy.fs.ConfigurationService;

import org.apache.commons.io.FileUtils;
import org.hsqldb.jdbcDriver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/deployer-test-applicationContext.xml" })
public class DatabaseServiceTest extends AbstractFileSystemTest {

	@Autowired
	private ConfigurationService configService;
	private JdbcTemplate jdbcTemplate;

	@Test
	public void shouldLoadPropertiesFromLocalDatasourceFile() throws IOException {
		File targetDirectory = unpackAndConfigure();

		DataBaseServiceImpl service = new DataBaseServiceImpl();
		service.loadSettings(targetDirectory);

		assertNotNull(service.getDatabaseUrl());
		assertEquals("jdbc:hsqldb:mem:test", service.getDatabaseUrl());
	}

	private File unpackAndConfigure() throws IOException {
		File targetDirectory = TestDataProvider.getDefaultTargetDirectory(getWorkingDirectory());

		TestDataProvider.unpackDefaultTestZip(targetDirectory);
		configService.configure(targetDirectory.getParentFile(), "test", TestDataProvider.createDefaultSpec(false));
		return targetDirectory;
	}

	@Test
	public void shouldGenerateScriptForTestDatabase() throws IOException, SQLException {

		File targetDirectory = unpackAndConfigure();
		createVersionTable(new File("./src/main"));

		DataBaseServiceImpl service = new DataBaseServiceImpl();
		service.generateScripts(targetDirectory);

		File resultingFile = service.getScriptFile(targetDirectory);
		assertTrue(resultingFile.exists());
		String fileContents = FileUtils.readFileToString(resultingFile);
		assertTrue(fileContents, fileContents.contains("test1"));
		assertTrue(fileContents, fileContents.contains("test2"));
	}

	@Test
	public void shouldExecuteScriptThroughJdbc() throws IOException, SQLException {
		File targetDirectory = unpackAndConfigure();
		createVersionTable(new File("./src/main"));

		DataBaseServiceImpl service = new DataBaseServiceImpl();
		service.upgradeDatabase(targetDirectory);

		getJdbcTemplate().execute("select * from test1");
	}

	@Test(expected = DatabaseInspectionException.class)
	public void shouldThrowInspectionExceptionOnFailure() {
		DataBaseServiceImpl dbService = new DataBaseServiceImpl();
		dbService.upgradeDatabase(new File("."));
	}

	private void createVersionTable(File targetDirectory) throws SQLException, FileNotFoundException, IOException {
		// This works because HSQLDB uses static variables for the memory DB.
		// Not something you really want. Should probably be put in the Spring
		// context instead.
		JdbcTemplate template = getJdbcTemplate();
		boolean changelogExists = false;
		try {
			template.execute("select * from changelog");
			changelogExists = true;
		} catch (DataAccessException e) {
			// Do nothing
		}

		if (!changelogExists) {
			String tableSql = FileUtils.readFileToString(new File(targetDirectory,
					"db/dbdeploy/createSchemaVersionTable.hsql.sql"));
			template.execute(tableSql);
		}
	}

	private JdbcTemplate getJdbcTemplate() throws SQLException {
		if (this.jdbcTemplate == null) {
			DriverManager.registerDriver(new jdbcDriver());
			DataSource ds = new SingleConnectionDataSource("jdbc:hsqldb:mem:test", "sa", "", false);
			this.jdbcTemplate = new JdbcTemplate(ds);
		}
		return this.jdbcTemplate;
	}

}
