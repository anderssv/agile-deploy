package no.f12.agiledeploy.deployer;

import static org.junit.Assert.*;

import org.junit.Test;

public class PackageSpecificationTest {

	@Test
	public void shouldResloveCorrectPathForArtifact() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		assertEquals("org/springframework/spring-core/2.5.6", spec.getArtifactPath());
	}

	@Test
	public void shouldResloveCorrectPathForArtifactIfSnapshot() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		assertEquals("org/springframework/spring-core/2.5.6-SNAPSHOT", spec.getArtifactPath());
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
	public void shouldResolveCorrectMetaDataFile() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		assertEquals("org/springframework/spring-core/2.5.6/maven-metadata.xml", spec.getMetadataFilename());
	}

	@Test
	public void shouldDetectSnapshot() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		assertTrue(spec.isSnapshot());
	}

}
