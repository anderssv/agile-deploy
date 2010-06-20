package no.f12.agiledeploy.deployer;

import java.io.File;

import no.f12.agiledeploy.deployer.repo.PackageSpecification;

public class DirectoryRegistry {

	public static File getEnvironmentDirectory(PackageSpecification spec, File workingDirectory, String environment) {
		return new File(getArtifactPath(spec, workingDirectory), environment);
	}

	public static File getLastInstalledVersionDirectory(File environmentDirectory) {
		return new File(environmentDirectory, "current");
	}

	public static File getConfigurationDirectory(File environmentDirectory) {
		return new File(getLastInstalledVersionDirectory(environmentDirectory), "config");
	}

	public static File getLogDirectory(File environmentDirectory) {
		return new File(environmentDirectory, "logs");
	}

	public static File getDataDirectory(File environmentDirectory) {
		return new File(environmentDirectory, "data");
	}

	public static File getBinDirectory(File environmentDirectory) {
		return new File(getLastInstalledVersionDirectory(environmentDirectory), "bin");
	}

	public static File getArtifactPath(PackageSpecification spec, File workingPath) {
		return new File(workingPath, spec.getArtifactId());
	}

}
