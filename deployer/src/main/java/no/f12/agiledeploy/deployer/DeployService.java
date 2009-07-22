package no.f12.agiledeploy.deployer;

import java.io.File;

public interface DeployService {

	public void deploy(PackageSpecification spec, String environment, File basePath);

}
