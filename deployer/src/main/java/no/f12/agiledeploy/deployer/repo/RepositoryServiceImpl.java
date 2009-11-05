package no.f12.agiledeploy.deployer.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class RepositoryServiceImpl implements RepositoryService {

	@Autowired
	@Qualifier("release")
	private RepositoryRepo repositoryRepo;

	@Autowired
	@Qualifier("snapshot")
	private RepositoryRepo snapshotRepo;

	public File fetchPackage(PackageSpecification spec, File workingDirectory) {
		RepositoryRepo repo = repositoryRepo;
		String fileName = spec.getArtifactFileName() + "." + spec.getPackageType();

		// Change filename and repo if SNAPSHOT
		if (spec.isSnapshot()) {
			repo = snapshotRepo;
			Document parsedDoc = readMetadataContents(spec, repo, workingDirectory);

			String timestamp = extractElementValue(parsedDoc, "timestamp");
			String buildNumber = extractElementValue(parsedDoc, "buildNumber");

			fileName = spec.getArtifactFilename(timestamp + "-" + buildNumber) + "." + spec.getPackageType();
		}
		return repo.fetchFile(spec.getRepositoryInformation().getArtifactPath(), fileName, workingDirectory, true);
	}

	private Document readMetadataContents(PackageSpecification spec, RepositoryRepo repo, File workingDirectory) {
		File metadataFile = repo.fetchFile(spec.getRepositoryInformation().getMetadataPath(), spec
				.getRepositoryInformation().getMetadataFilename(), workingDirectory, false);
		Document parsedDoc = parseXmlFile(metadataFile);
		metadataFile.delete();
		return parsedDoc;
	}

	private Document parseXmlFile(File metadataFile) {
		Document parsedDoc;
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
			parsedDoc = documentBuilder.parse(metadataFile);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Could not read downloaded metadata file", e);
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException("Could not create XML parser", e);
		} catch (SAXException e) {
			throw new IllegalStateException("Error parsing XML", e);
		} catch (IOException e) {
			throw new IllegalStateException("Error parsing XML", e);
		}
		return parsedDoc;
	}

	public void setRepositoryRepo(RepositoryRepo repositoryRepo) {
		this.repositoryRepo = repositoryRepo;
	}

	private String extractElementValue(Document doc, String elementName) {
		NodeList list = doc.getElementsByTagName(elementName);
		Assert.isTrue(list.getLength() <= 1, "Should only be one element with name " + elementName);
		if (list.getLength() == 0) {
			return null;
		} else {
			String content = list.item(0).getTextContent();
			return content;
		}
	}

	public void setSnapshotRepositoryRepo(RepositoryRepo snapshotRepo) {
		this.snapshotRepo = snapshotRepo;
	}

}
