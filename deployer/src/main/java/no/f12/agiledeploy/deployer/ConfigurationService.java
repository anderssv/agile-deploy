package no.f12.agiledeploy.deployer;

import java.io.File;

public interface ConfigurationService {

	void configure(File environmentDirectory, String environment);

}
