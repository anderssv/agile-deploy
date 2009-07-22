package no.f12.agiledeploy.deployer;

import org.junit.Test;

public class TestDataProvider {

	public static PackageSpecification createDefaultSpec(boolean snapshot) {
		PackageSpecification spec = new PackageSpecification("group.g", "artifact", "version", snapshot);
		return spec;
	}

	@Test
	public void shouldNotFailMavenBuildJustBecauseThereIsNoTestsInThisClass() {

	}
}
