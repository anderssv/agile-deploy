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

}
