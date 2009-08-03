package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Test;

public class CommandLineDeployerTest {

	String environment = "test";

	File workingDirectory = TestDataProvider.getDefaultTempDir();
	File unpackDir = new File(workingDirectory, "spring-core/" + environment + "/current");
	File downloadedFile = new File(workingDirectory, "spring-core-2.5.6.jar");

	@Test(expected = IllegalArgumentException.class)
	public void shouldGiveErrorIfNotEnoughParameters() {
		CommandLineDeployer deployer = new CommandLineDeployer("classpath:spring/deployer-test-applicationContext.xml");
		deployer.execute(null);
	}

	@Test
	public void shouldStartUpGivenCorrectParameters() {
		executeWithSpringStandard();
		assertTrue(unpackDir.exists());
	}

	@Test
	public void shouldCleanOutOldStuffFromDirectoryBeforeDeploy() {
		File extraDir = new File(unpackDir, "extra");
		extraDir.mkdirs();
		assertTrue(extraDir.exists());

		executeWithSpringStandard();

		assertTrue(!extraDir.exists());
	}

	private void executeWithSpringStandard() {
		CommandLineDeployer deployer = new CommandLineDeployer("classpath:spring/deployer-test-applicationContext.xml");
		String[] args = new String[] { environment, "org.springframework", "spring-core", "2.5.6", "jar" };
		deployer.execute(args);
	}

	@After
	public void cleanupDir() {
		FileUtil.deleteWithLogging(workingDirectory);
	}

}
