package no.f12.agiledeploy.deployer;

public class TestDataProvider {

	public static PackageSpecification createDefaultSpec(boolean snapshot) {
		PackageSpecification spec = new PackageSpecification("group.g", "artifact", "version", snapshot);
		return spec;
	}

}
