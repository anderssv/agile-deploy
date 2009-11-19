package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import no.f12.agiledeploy.deployer.repo.PackageSpecification;
import no.f12.agiledeploy.deployer.repo.RepositoryRepoImpl;

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
	public void shouldDownloadFileAndWriteToDisk() throws MalformedURLException, FileNotFoundException {
		RepositoryRepoImpl repo = new RepositoryRepoImpl();
		repo.setRepositoryURL(repoUrl());

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		String fileName = spec.getArtifactFileName() + ".jar";

		File downloadedFile = repo.fetchFile(spec.getRepositoryInformation().getArtifactPath(), fileName,
				workingDirectory(), true);

		assertTrue(downloadedFile.exists());
		assertTrue(downloadedFile.getName().equals(fileName));
		downloadedFile.delete();
	}

	@Test
	public void shouldDownloadMetadataFileAndWriteToDisk() throws MalformedURLException, FileNotFoundException {
		RepositoryRepoImpl repo = new RepositoryRepoImpl();
		repo.setRepositoryURL(repoUrl());

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		String fileName = spec.getRepositoryInformation().getMetadataFilename();

		File downloadedFile = repo.fetchFile(spec.getRepositoryInformation().getMetadataPath(), fileName,
				workingDirectory(), false);

		assertTrue(downloadedFile.exists());
		assertTrue(downloadedFile.getName().equals(fileName));
		downloadedFile.delete();
	}

	@Test(expected = IllegalStateException.class)
	public void shouldFailCorrectlyWhenNoRepositoryIsSpecified() throws MalformedURLException, FileNotFoundException {
		RepositoryRepoImpl repo = new RepositoryRepoImpl();

		PackageSpecification spec = TestDataProvider.createDefaultSpec(false);
		String fileName = spec.getArtifactFileName() + ".jar";

		repo.fetchFile(spec.getRepositoryInformation().getArtifactPath(), fileName, workingDirectory(), true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldExceptionOnMissingFile() throws MalformedURLException, FileNotFoundException {
		RepositoryRepoImpl repo = new RepositoryRepoImpl();
		repo.setRepositoryURL(new URL("http://repo1.maven.org"));

		repo.fetchFile("somethingmissing", "alsomissing", workingDirectory(), true);
	}

	private File workingDirectory() {
		return new File(".");
	}

	private URL repoUrl() throws MalformedURLException {
		return new URL("http://repo1.maven.org/maven2/");
	}
}
