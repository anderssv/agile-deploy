package no.f12.agiledeploy.deployer;

import java.io.File;

public class RepositoryServiceImpl implements RepositoryService {

	private RepositoryRepo repositoryRepo;

	public File fetchPackage(PackageSpecification spec) {
		repositoryRepo.fetchFile(spec.getFullFilename() + ".zip");
		return null;
	}

	public void setRepositoryRepo(RepositoryRepo repositoryRepo) {
		this.repositoryRepo = repositoryRepo;
	}

	
}
