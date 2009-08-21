package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

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
	@Ignore
	public void shouldLoadPropertiesFromLocalDatasourceFile() throws IOException {
		File targetDirectory = TestDataProvider.getDefaultTargetDirectory();
		
		TestDataProvider.unpackDefaultTestZip(targetDirectory);
		configService.configure(targetDirectory.getParentFile(), "test");
		
		DataBaseServiceImpl service = new DataBaseServiceImpl();
		service.loadSettings(targetDirectory);
		
		assertNotNull(service.getDatabaseUrl());
		assertEquals("test", service.getDatabaseUrl());
	}
	
	//@After
	public void cleanUp() {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}
}
