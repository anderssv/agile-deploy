package no.f12.agiledeploy.deployer;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryServiceImpl implements RepositoryService {

	@Autowired
	private RepositoryRepo repositoryRepo;

	public File fetchPackage(PackageSpecification spec, File workingDirectory) {
		return repositoryRepo.fetchFile(spec.getArtifactPath(), spec.getArtifactFileName() + "." + spec.getPackageType(), workingDirectory);
	}

	public void setRepositoryRepo(RepositoryRepo repositoryRepo) {
		this.repositoryRepo = repositoryRepo;
	}

}
