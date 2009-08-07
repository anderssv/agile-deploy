package no.f12.agiledeploy.deployer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ConfigurationServiceTest {
	
	@Test
	public void shouldCopyFilesForCurrentEnvironmentIfTheyDontExist() throws IOException {
		UnpackerService unpacker = new UnpackerServiceImpl();
		File workingDirectory = TestDataProvider.getDefaultTempDir();
		unpacker.unpack(TestDataProvider.getZipFile(), workingDirectory);
		FileUtil.moveOneUp(workingDirectory, "myapp-server-0.1-SNAPSHOT");
		
		ConfigurationServiceImpl configService = new ConfigurationServiceImpl();
		configService.configure(workingDirectory, "test");
		
		assertTrue(new File(workingDirectory, "datasource.properties").exists());
	}

}
