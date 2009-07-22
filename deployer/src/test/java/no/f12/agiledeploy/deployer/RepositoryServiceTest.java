package no.f12.agiledeploy.deployer;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class RepositoryServiceTest {

	@Test
	public void shouldFetchFileGivenCorrectSpec() {
		RepositoryServiceImpl repoService = new RepositoryServiceImpl();
		RepositoryRepo repo = mock(RepositoryRepo.class);
		repoService.setRepositoryRepo(repo);

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		repoService.fetchPackage(spec);

		verify(repo).fetchFile("group/g/artifact/version/artifact-version.zip");
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
