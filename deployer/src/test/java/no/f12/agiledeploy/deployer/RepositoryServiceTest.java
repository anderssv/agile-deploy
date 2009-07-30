package no.f12.agiledeploy.deployer;

import java.io.File;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class RepositoryServiceTest {

	RepositoryServiceImpl repoService;

	@Test
	public void shouldFetchFileGivenCorrectSpec() {
		RepositoryRepo repo = createRepoServiceAndMockRepo();

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		repoService.fetchPackage(spec, new File("."));

		verify(repo).fetchFile("org/springframework/spring-core/2.5.6", "spring-core-2.5.6.zip", new File("."));
	}

	private RepositoryRepo createRepoServiceAndMockRepo() {
		repoService = new RepositoryServiceImpl();
		RepositoryRepo repo = mock(RepositoryRepo.class);
		repoService.setRepositoryRepo(repo);
		return repo;
	}

	@Test
	public void shouldFetchFileGivenArtifactType() {
		RepositoryRepo repo = createRepoServiceAndMockRepo();

		PackageSpecification spec = new PackageSpecification("org.springframework", "spring-core", "2.5.6", "jar");
		repoService.fetchPackage(spec, new File("."));

		verify(repo).fetchFile("org/springframework/spring-core/2.5.6", "spring-core-2.5.6.jar", new File("."));
	}

	private String createDefaultMetadataXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<metadata xsi:schemaLocation=\"http://maven.apache.org/METADATA/1.0.0 http://maven.apache.org/xsd/metadata-1.0.0.xsd\" xmlns=\"http://maven.apache.org/METADATA/1.0.0\"\n"
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + "<groupId>no.f12</groupId>\n"
				+ "<artifactId>artifact</artifactId>\n" + "<version>0.1-SNAPSHOT</version>\n" + "<versioning>\n"
				+ "  <snapshot>\n" + "    <timestamp>20090720.085251</timestamp>\n"
				+ "    <buildNumber>1</buildNumber>\n" + "  </snapshot>\n"
				+ "  <lastUpdated>20090720085428</lastUpdated>\n" + "</versioning>\n" + "</metadata>\n";
	}

}
