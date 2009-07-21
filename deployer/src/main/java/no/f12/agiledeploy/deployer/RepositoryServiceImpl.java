package no.f12.agiledeploy.deployer;

public class RepositoryServiceImpl implements RepositoryService {

	private RepositoryRepo repositoryRepo;

	public void fetchPackage(PackageSpecification spec) {
		repositoryRepo.fetchFile(spec.getFullFilename() + ".zip");
	}

	public void setRepositoryRepo(RepositoryRepo repositoryRepo) {
		this.repositoryRepo = repositoryRepo;
	}

	
}
