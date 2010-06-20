package no.f12.agiledeploy.deployer;

import static org.junit.Assert.*;

import java.io.File;

import no.f12.agiledeploy.deployer.repo.PackageSpecification;

import org.junit.Test;

public class PackageSpecificationTest {

	@Test
	public void shouldResloveCorrectPathForArtifact() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		assertEquals("org/springframework/spring-core/2.5.6", spec.getRepositoryInformation().getArtifactPath());
	}

	@Test
	public void shouldResloveCorrectPathForArtifactIfSnapshot() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		assertEquals("org/springframework/spring-core/2.5.6-SNAPSHOT", spec.getRepositoryInformation()
				.getArtifactPath());
	}

	@Test
	public void shouldResolveCorrectNameForFile() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		assertEquals("spring-core-2.5.6", spec.getArtifactFileName());
	}

	@Test
	public void shouldResloveCorrectNameForFileWithSnapshot() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		assertEquals("spring-core-2.5.6-SNAPSHOT", spec.getArtifactFileName());
	}

	@Test
	public void shouldDetectSnapshot() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		assertTrue(spec.isSnapshot());
	}

	@Test
	public void shouldGiveCorrectArtifactPath() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		File workingPath = new File(".");
		assertEquals(new File(workingPath, "spring-core"), DirectoryRegistry.getArtifactPath(spec, workingPath));
	}

	@Test
	public void shouldGiveCorrectArtifactDataDirectory() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		File workingPath = new File(".");
		assertEquals(new File(workingPath, "spring-core/test/data"), DirectoryRegistry
				.getDataDirectory(DirectoryRegistry.getEnvironmentDirectory(spec, workingPath, "test")));
	}

	@Test
	public void shouldGiveCorrectPropertiesDirectory() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		File workingPath = new File(".");
		assertEquals(new File(workingPath, "spring-core/test/current/config"), DirectoryRegistry
				.getConfigurationDirectory(DirectoryRegistry.getEnvironmentDirectory(spec, workingPath, "test")));
	}

	@Test
	public void shouldGiveCorrectInstallationDirectory() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		File workingPath = new File(".");
		assertEquals(new File(workingPath, "spring-core/test/current"), DirectoryRegistry
				.getLastInstalledVersionDirectory(DirectoryRegistry.getEnvironmentDirectory(spec, workingPath, "test")));
	}

}
