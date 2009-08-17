package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/blank-applicationContext.xml" })
@IfProfileValue(name = "test-group", values = { "external-integration-tests", "all" })
public class RepositoryRepoTest {

	@Test
	public void shouldDownloadFileAndWriteToDisk() throws MalformedURLException {
		RepositoryRepoImpl repo = new RepositoryRepoImpl();
		repo.setRepositoryURL(new URL("http://repo1.maven.org/maven2/"));

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		String fileName = spec.getArtifactFileName() + ".jar";

		File downloadedFile = repo
				.fetchFile(spec.getRepositoryInformation().getArtifactPath(), fileName, new File("."));

		assertTrue(downloadedFile.exists());
		assertTrue(downloadedFile.getName().equals(fileName));
		downloadedFile.delete();
	}
}
