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

import org.hsqldb.jdbcDriver;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/deployer-test-applicationContext.xml" })
public class DatabaseServiceTest {

	@Autowired
	private ConfigurationService configService;

	@Test
	public void shouldLoadPropertiesFromLocalDatasourceFile() throws IOException {
		File targetDirectory = unpackAndConfigure();

		DataBaseServiceImpl service = new DataBaseServiceImpl();
		service.loadSettings(targetDirectory);

		assertNotNull(service.getDatabaseUrl());
		assertEquals("jdbc:hsqldb:mem:test", service.getDatabaseUrl());
	}

	private File unpackAndConfigure() throws IOException {
		File targetDirectory = TestDataProvider.getDefaultTargetDirectory();

		TestDataProvider.unpackDefaultTestZip(targetDirectory);
		configService.configure(targetDirectory.getParentFile(), "test");
		return targetDirectory;
	}

	@Test
	public void shouldGenerateScriptForTestDatabase() throws IOException, SQLException {

		File targetDirectory = unpackAndConfigure();
		createVersionTable(targetDirectory);

		DataBaseServiceImpl service = new DataBaseServiceImpl();
		service.generateScripts(targetDirectory);

		File resultingFile = new File(targetDirectory, "db-upgrade.sql");
		assertTrue(resultingFile.exists());
		String fileContents = FileUtil.readToString(resultingFile);
		assertTrue(fileContents.contains("TEST1"));
		assertTrue(fileContents.contains("TEST2"));
	}

	private void createVersionTable(File targetDirectory) throws SQLException, FileNotFoundException, IOException {
		// This works because HSQLDB uses static variables for the memory DB.
		// Not something you really want. Should probably be put in the Spring
		// context instead.
		DriverManager.registerDriver(new jdbcDriver());
		DataSource ds = new SingleConnectionDataSource("jdbc:hsqldb:mem:test", "sa", "", false);
		JdbcTemplate template = new JdbcTemplate(ds);
		String tableSql = FileUtil.readToString(new File(targetDirectory,
				"db/dbdeploy/createSchemaVersionTable.hsql.sql"));
		template.execute(tableSql);
	}

	@After
	public void cleanUp() {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}
}
