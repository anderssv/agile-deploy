package no.f12.agiledeploy.deployer;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryServiceImpl implements RepositoryService {

	@Autowired
	private RepositoryRepo repositoryRepo;

	public File fetchPackage(PackageSpecification spec) {
		return repositoryRepo.fetchFile(spec.getArtifactPath(), spec.getArtifactFileName() + "." + spec.getPackageType());
	}

	public void setRepositoryRepo(RepositoryRepo repositoryRepo) {
		this.repositoryRepo = repositoryRepo;
	}

}
