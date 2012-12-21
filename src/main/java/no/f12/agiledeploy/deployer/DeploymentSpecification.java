package no.f12.agiledeploy.deployer;

import java.io.File;

import no.f12.agiledeploy.deployer.repo.PackageSpecification;

public class DeploymentSpecification {

	private PackageSpecification packageSpecification;
	private String environment;
	private File installBase;
	private File packageFile;

	public DeploymentSpecification(PackageSpecification ps, String environment, File workingDirectory, File packageFile) {
		this.packageSpecification = ps;
		this.environment = environment;
		this.installBase = workingDirectory;
		this.packageFile = packageFile;
	}

	public PackageSpecification getPackageSpecification() {
		return this.packageSpecification;
	}

	public File getInstallBase() {
		return this.installBase;
	}

	public String getEnvironment() {
		return this.environment;
	}

	public File getPackageFile() {
		return this.packageFile;
	}

	public void setPackageFile(File downloadedFile) {
		this.packageFile = downloadedFile;
	}
	
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

	public static File getInstallDirectory(File environmentDirectory, PackageSpecification spec) {
		return new File(new File(environmentDirectory, "versions"), spec.getArtifactFileName());
	}


}
