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

	public File getEnvironmentDirectory() {
		return new File(getArtifactPath(), this.environment);
	}

	public File getLastInstalledVersionDirectory() {
		return new File(getEnvironmentDirectory(), "current");
	}

	public File getConfigurationDirectory() {
		return new File(getLastInstalledVersionDirectory(), "config");
	}

	public File getLogDirectory() {
		return new File(getEnvironmentDirectory(), "logs");
	}

	public File getDataDirectory() {
		return new File(getEnvironmentDirectory(), "data");
	}

	public File getBinDirectory() {
		return new File(getLastInstalledVersionDirectory(), "bin");
	}

	public File getArtifactPath() {
		return new File(installBase, this.packageSpecification.getArtifactId());
	}

	public File getInstallDirectory() {
		return new File(new File(getEnvironmentDirectory(), "versions"),
				this.packageSpecification.getArtifactFileName());
	}

	public File getEnvironmentPropertiesDirectory() {
		return new File(getConfigurationDirectory(), environment);
	}

}
