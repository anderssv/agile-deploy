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

	public File getLastInstalledVersionDirectory() {
		return new File(getEnvironmentDirectory(this.packageSpecification, this.installBase, this.environment), "current");
	}

	public File getConfigurationDirectory() {
		return new File(getLastInstalledVersionDirectory(), "config");
	}

	public File getLogDirectory() {
		return new File(getEnvironmentDirectory(this.packageSpecification, this.installBase, this.environment), "logs");
	}

	public File getDataDirectory() {
		return new File(getEnvironmentDirectory(this.packageSpecification, this.installBase, this.environment), "data");
	}

	public File getBinDirectory() {
		return new File(getLastInstalledVersionDirectory(), "bin");
	}

	public static File getArtifactPath(PackageSpecification spec, File workingPath) {
		return new File(workingPath, spec.getArtifactId());
	}

	public File getArtifactPath() {
		return new File(installBase, this.packageSpecification.getArtifactId());
	}

	public static File getInstallDirectory(File environmentDirectory, PackageSpecification spec) {
		return new File(new File(environmentDirectory, "versions"), spec.getArtifactFileName());
	}
	
	public File getEnvironmentPropertiesDirectory() {
		return new File(getConfigurationDirectory(), environment);
	}


}
