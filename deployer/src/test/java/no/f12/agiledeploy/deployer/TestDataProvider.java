package no.f12.agiledeploy.deployer;

import org.junit.Test;

public class TestDataProvider {

	public static PackageSpecification createDefaultSpec(boolean snapshot) {
		PackageSpecification spec = new PackageSpecification("org.springframework", "spring-core", "2.5.6", snapshot);
		return spec;
	}
	


	@Test
	public void shouldNotFailMavenBuildJustBecauseThereIsNoTestsInThisClass() {

	}
}
