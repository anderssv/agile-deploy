package no.f12.agiledeploy.deployer;

import static org.junit.Assert.*;

import org.junit.Test;

public class PackageSpecificationTest {
	
	@Test
	public void shouldResloveCorrectPathForArtifact() {
		PackageSpecification spec = createDefaultSpect(false);
		assertEquals("group/g/artifact/version", spec.getArtifactPath());
	}

	@Test
	public void shouldResloveCorrectPathForArtifactIfSnapshot() {
		PackageSpecification spec = createDefaultSpect(true);
		assertEquals("group/g/artifact/version-SNAPSHOT", spec.getArtifactPath());
	}

	@Test
	public void shouldResolveCorrectNameForFile() {
		PackageSpecification spec = createDefaultSpect(false);
		assertEquals("artifact-version", spec.getArtifactFileName());
	}
	
	@Test
	public void shouldResloveCorrectNameForFileWithSnapshot() {
		PackageSpecification spec = createDefaultSpect(true);
		assertEquals("artifact-version-SNAPSHOT", spec.getArtifactFileName());
	}
	
	@Test
	public void shouldResolveCorrectMetaDataFile() {
		PackageSpecification spec = createDefaultSpect(false);
		assertEquals("group/g/artifact/version/maven-metadata.xml", spec.getMetadataFilename());
	}
	
	public static PackageSpecification createDefaultSpect(boolean snapshot) {
		PackageSpecification spec = new PackageSpecification("group.g", "artifact", "version", snapshot);
		return spec;
	}
}
