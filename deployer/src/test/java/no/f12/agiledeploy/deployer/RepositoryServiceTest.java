package no.f12.agiledeploy.deployer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import no.f12.agiledeploy.deployer.repo.PackageSpecification;
import no.f12.agiledeploy.deployer.repo.RepositoryRepo;
import no.f12.agiledeploy.deployer.repo.RepositoryServiceImpl;

import org.junit.Before;
import org.junit.Test;

public class RepositoryServiceTest extends AbstractFileSystemTest {

	private RepositoryServiceImpl repoService;
	private File tempDir = TestDataProvider.getDefaultTempDir();
	private RepositoryRepo snapshotRepo;
	private RepositoryRepo repo;

	@Test
	public void shouldFetchFileGivenCorrectSpec() {
		createRepoServiceAndMockRepo();

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		repoService.fetchPackage(spec, new File("."));

		verify(repo).fetchFile("org/springframework/spring-core/2.5.6", "spring-core-2.5.6.zip", new File("."), true);
	}

	private void createRepoServiceAndMockRepo() {
		repoService = new RepositoryServiceImpl();
		repo = mock(RepositoryRepo.class);
		repoService.setRepositoryRepo(repo);
		snapshotRepo = mock(RepositoryRepo.class);
		repoService.setSnapshotRepositoryRepo(snapshotRepo);
	}

	@Test
	public void shouldFetchFileGivenArtifactType() {
		createRepoServiceAndMockRepo();

		PackageSpecification spec = new PackageSpecification("org.springframework", "spring-core", "2.5.6", "jar");
		repoService.fetchPackage(spec, new File("."));

		verify(repo).fetchFile("org/springframework/spring-core/2.5.6", "spring-core-2.5.6.jar", new File("."), true);
	}

	@Test
	public void shouldResolveCorrectFilenameForSnapshotAndFetchFromCorrectRepo() throws IOException {
		createRepoServiceAndMockRepo();
		File resultingFile = createMavenMetadataFile();

		when(snapshotRepo.fetchFile("org/springframework/spring-core/2.5.6-SNAPSHOT", "maven-metadata.xml", tempDir, false))
				.thenReturn(resultingFile);

		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		repoService.fetchPackage(spec, tempDir);

		verify(snapshotRepo, atLeastOnce()).fetchFile("org/springframework/spring-core/2.5.6-SNAPSHOT", "maven-metadata.xml", tempDir, false);
		verify(snapshotRepo, atLeastOnce()).fetchFile("org/springframework/spring-core/2.5.6-SNAPSHOT",
				"spring-core-2.5.6-20090720.085251-1.zip", tempDir, true);
	}
	
	@Test(expected=IllegalStateException.class)
	public void shouldReportMissingRepoOnIllegalState() {
		createRepoServiceAndMockRepo();
		when(snapshotRepo.fetchFile(anyString(), anyString(), any(File.class), anyBoolean())).thenThrow(new IllegalStateException("No repo configured"));
		
		PackageSpecification spec = TestDataProvider.createDefaultSpec(true);
		this.repoService.fetchPackage(spec, tempDir);
	}
	
	@Before
	public void createTemp() {
		tempDir.mkdirs();
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
