package no.f12.agiledeploy.deployer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepositoryServiceTest {

	RepositoryServiceImpl repoService;
	File tempDir = TestDataProvider.getDefaultTempDir();

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

	@Test
	public void shouldResolveCorrectFilenameForSnapshot() throws IOException {
		RepositoryRepo repo = createRepoServiceAndMockRepo();
		File resultingFile = createMavenMetadataFile();

		when(repo.fetchFile("org/springframework/spring-core/2.5.6-SNAPSHOT", "maven-metadata.xml", tempDir))
				.thenReturn(resultingFile);

		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		repoService.fetchPackage(spec, tempDir);

		verify(repo).fetchFile("org/springframework/spring-core/2.5.6-SNAPSHOT", "maven-metadata.xml", tempDir);
		verify(repo).fetchFile("org/springframework/spring-core/2.5.6-SNAPSHOT",
				"spring-core-2.5.6-20090720.085251-1.zip", tempDir);
	}
	
	@Before
	public void createTemp() {
		tempDir.mkdirs();
	}
	
	@After
	public void cleanupTemp() {
		TestDataProvider.deleteDir(tempDir);
	}

	/**
	 * Should just be a test resource?
	 */
	private File createMavenMetadataFile() throws IOException {
		File resultingFile = new File(tempDir, "maven-metadata.xml");
		FileWriter writer = new FileWriter(resultingFile);
		writer.write(createDefaultMetadataXML());
		writer.close();
		return resultingFile;
	}

	private String createDefaultMetadataXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<metadata xsi:schemaLocation=\"http://maven.apache.org/METADATA/1.0.0 http://maven.apache.org/xsd/metadata-1.0.0.xsd\" xmlns=\"http://maven.apache.org/METADATA/1.0.0\"\n"
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
				+ "<groupId>org.springframework</groupId>\n" + "<artifactId>spring-core</artifactId>\n"
				+ "<version>2.5.6-SNAPSHOT</version>\n" + "<versioning>\n" + "  <snapshot>\n"
				+ "    <timestamp>20090720.085251</timestamp>\n" + "    <buildNumber>1</buildNumber>\n"
				+ "  </snapshot>\n" + "  <lastUpdated>20090720085428</lastUpdated>\n" + "</versioning>\n"
				+ "</metadata>\n";
	}

}
