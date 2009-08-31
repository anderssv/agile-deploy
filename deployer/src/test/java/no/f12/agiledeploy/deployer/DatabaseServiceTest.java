package no.f12.agiledeploy.deployer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Ignore("Need to add classes for HSQLDB")
	public void shouldGenerateScriptForTestDatabase() throws IOException {
		File targetDirectory = unpackAndConfigure();

		DataBaseServiceImpl service = new DataBaseServiceImpl();
		service.generateScripts(targetDirectory);

		assertTrue(new File(targetDirectory, "db-upgrade.sql").exists());
	}

	@After
	public void cleanUp() {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}
}
