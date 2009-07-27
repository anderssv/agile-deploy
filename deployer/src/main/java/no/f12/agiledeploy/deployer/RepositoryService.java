package no.f12.agiledeploy.deployer;

import java.io.File;

public interface RepositoryService {

	public File fetchPackage(PackageSpecification spec, File workingDirectory);

}
