package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Test;

public class CommandLineDeployerTest {

	File unpackDir = new File("temp/spring-core");
	File downloadedFile = new File("spring-core-2.5.6.jar");

	@Test(expected = IllegalArgumentException.class)
	public void shouldGiveErrorIfNotEnoughParameters() {
		CommandLineDeployer deployer = new CommandLineDeployer("classpath:spring/deployer-test-applicationContext.xml");
		deployer.execute(null);
	}

	@Test
	public void shouldStartUpGivenCorrectParameters() {
		CommandLineDeployer deployer = new CommandLineDeployer("classpath:spring/deployer-test-applicationContext.xml");
		String[] args = new String[] { "test", "org.springframework", "spring-core", "2.5.6", "jar" };
		deployer.execute(args);
		assertTrue(unpackDir.exists());
	}

	@After
	public void cleanupDir() {
		TestDataProvider.deleteDir(new File("temp"));
	}

}
