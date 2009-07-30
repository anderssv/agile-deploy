package no.f12.agiledeploy.deployer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class RepositoryServiceImpl implements RepositoryService {

	@Autowired
	private RepositoryRepo repositoryRepo;

	public File fetchPackage(PackageSpecification spec, File workingDirectory) {
		if (spec.isSnapshot()) {
			File metadataFile = repositoryRepo.fetchFile(spec.getArtifactPath(), spec.getMetadataFilename(),
					workingDirectory);
			Document parsedDoc = parseXmlFile(metadataFile);

			String timestamp = extractElementValue(parsedDoc, "timestamp");
			String buildNumber = extractElementValue(parsedDoc, "buildNumber");

			File artifactFile = repositoryRepo.fetchFile(spec.getArtifactPath(), spec.getArtifactFilename(timestamp
					+ "-" + buildNumber)
					+ "." + spec.getPackageType(), workingDirectory);
			return artifactFile;
		} else {
			return repositoryRepo.fetchFile(spec.getArtifactPath(), spec.getArtifactFileName() + "."
					+ spec.getPackageType(), workingDirectory);
		}
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

}
