package no.f12.agiledeploy.deployer;

import java.io.File;

import no.f12.agiledeploy.deployer.repo.PackageSpecification;

public interface DeployService {

	public void downloadAndDeploy(PackageSpecification spec, String environment, File basePath);
	
	public void deploy(PackageSpecification spec, String environment, File basePath, File packageFile);

}
