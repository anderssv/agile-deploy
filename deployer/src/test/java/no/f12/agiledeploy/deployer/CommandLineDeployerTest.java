package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Test;

public class CommandLineDeployerTest {

	String environment = "test";

	CommandLineDeployer deployer;

	File workingDirectory;
	File unpackDir;
	File downloadedFile;

	@Test(expected = IllegalArgumentException.class)
	public void shouldGiveErrorIfNotEnoughParameters() {
		createDeployerAndSetupDir();
		deployer.execute(null);
	}

	private void createDeployerAndSetupDir() {
		deployer = new CommandLineDeployer("classpath:spring/deployer-test-applicationContext.xml");
		
		workingDirectory = deployer.getWorkingDirectory();
		unpackDir = new File(workingDirectory, "spring-core/" + environment + "/current");
		downloadedFile = new File(workingDirectory, "spring-core-2.5.6.jar");
	}

	@Test
	public void shouldStartUpGivenCorrectParameters() {
		createDeployerAndSetupDir();
		executeWithSpringStandard();
		assertTrue(unpackDir.exists());
	}

	@Test
	public void shouldCleanOutOldStuffFromDirectoryBeforeDeploy() {
		createDeployerAndSetupDir();

		File extraDir = new File(unpackDir, "extra");
		extraDir.mkdirs();
		assertTrue(extraDir.exists());

		executeWithSpringStandard();

		assertTrue(!extraDir.exists());
	}

	private void executeWithSpringStandard() {
		String[] args = new String[] { environment, "org.springframework", "spring-core", "2.5.6", "jar" };
		deployer.execute(args);
	}

	@After
	public void cleanupDir() {
		FileUtil.deleteDir(workingDirectory);
	}

}
