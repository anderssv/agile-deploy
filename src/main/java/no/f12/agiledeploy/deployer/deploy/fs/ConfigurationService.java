package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;

import no.f12.agiledeploy.deployer.DeploymentSpecification;

public interface ConfigurationService {

	void configure(File environmentDirectory, String environment, DeploymentSpecification spec);

}
