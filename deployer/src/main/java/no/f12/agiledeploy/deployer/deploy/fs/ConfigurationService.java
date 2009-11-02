package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;

public interface ConfigurationService {

	void configure(File environmentDirectory, String environment);

}
