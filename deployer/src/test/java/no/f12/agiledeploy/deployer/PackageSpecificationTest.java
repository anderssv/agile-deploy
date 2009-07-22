package no.f12.agiledeploy.deployer;

import static org.junit.Assert.*;

import org.junit.Test;

public class PackageSpecificationTest {
	
	@Test
	public void shouldResloveCorrectPathForArtifact() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		assertEquals("group/g/artifact/version", spec.getArtifactPath());
	}

	@Test
	public void shouldResloveCorrectPathForArtifactIfSnapshot() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		assertEquals("group/g/artifact/version-SNAPSHOT", spec.getArtifactPath());
	}

	@Test
	public void shouldResolveCorrectNameForFile() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		assertEquals("artifact-version", spec.getArtifactFileName());
	}
	
	@Test
	public void shouldResloveCorrectNameForFileWithSnapshot() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		assertEquals("artifact-version-SNAPSHOT", spec.getArtifactFileName());
	}
	
	@Test
	public void shouldResolveCorrectMetaDataFile() {
		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		assertEquals("group/g/artifact/version/maven-metadata.xml", spec.getMetadataFilename());
	}
	
}
