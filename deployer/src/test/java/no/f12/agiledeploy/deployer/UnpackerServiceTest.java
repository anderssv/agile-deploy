package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class UnpackerServiceTest extends AbstractFileSystemTest {

	File unpackDir = TestDataProvider.getDefaultTargetDirectory();

	@Test
	public void shouldUnpackCorrectly() throws IOException {
		TestDataProvider.unpackDefaultTestZip(unpackDir);

		assertTrue(new File(unpackDir, "bin").exists());
		assertTrue(new File(unpackDir, "repo").exists());
		assertTrue(new File(unpackDir, "bin/myapp").exists());
		assertTrue(new File(unpackDir, "bin/myapp.bat").exists());
	}
	
	@Test
	public void shouldUnpackContents() throws IOException {
		TestDataProvider.unpackDefaultTestZip(unpackDir);
		
		File fileToCheck = new File(unpackDir, "properties/system.properties");
		assertTrue(fileToCheck.exists());
		Resource propertyFile = new DefaultResourceLoader().getResource("file:" + fileToCheck.getAbsolutePath());
		Properties props = PropertiesLoaderUtils.loadProperties(propertyFile);
		assertTrue(props.propertyNames().hasMoreElements());
	}

}
