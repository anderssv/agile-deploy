package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;

import no.f12.agiledeploy.deployer.repo.PackageSpecification;

public interface ConfigurationService {

	void configure(File environmentDirectory, String environment, PackageSpecification spec);

}
