package no.f12.agiledeploy.deployer.repo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class RepositoryRepoImpl implements RepositoryRepo {

	private static final Logger LOG = Logger.getLogger(RepositoryRepoImpl.class);

	private URL repositoryURL;

	@Override
	public File fetchFile(String filePath, String fileName, File workingDirectory, boolean binary) {
		if (this.repositoryURL == null) {
			throw new IllegalStateException("No repository specified for this repo");
		}

		String fullFilePath = filePath + "/" + fileName;
		File resultingFile = null;

		URL fileUrl;
		try {
			fileUrl = new URL(repositoryURL, fullFilePath);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Malformed URL: " + fullFilePath, e);
		}

		LOG.info("Downloading package from " + fileUrl);
		try {
			resultingFile = downloadFile(fileName, workingDirectory, binary, resultingFile, fileUrl);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Could not download file " + fileUrl, e);
		}
		return resultingFile;
	}

	private File downloadFile(String fileName, File workingDirectory, boolean binary, File resultingFile, URL fileUrl)
			throws FileNotFoundException {
		try {
			FileOutputStream out = null;
			InputStream fileInputstream = null;
			try {
				HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
				if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
					throw new FileNotFoundException(fileUrl + " not found (404)");
				}
				int contentLength = connection.getContentLength();
				if (binary) {
					checkContentType(connection, contentLength);
				}

				byte[] data = new byte[contentLength];
				fileInputstream = readBytes(connection, contentLength, data);

				resultingFile = new File(workingDirectory, fileName);
				out = new FileOutputStream(resultingFile);
				out.write(data);
			} finally {
				if (fileInputstream != null)
					fileInputstream.close();
				if (out != null) {
					out.flush();
					out.close();
				}
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw new IllegalStateException("Could not retrieve file: " + fileUrl, e);
		}
		return resultingFile;
	}


	private InputStream readBytes(URLConnection connection, int contentLength, byte[] data) throws IOException {
		InputStream raw = connection.getInputStream();
		InputStream fileInputstream = new BufferedInputStream(raw);

		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = fileInputstream.read(data, offset, data.length - offset);
			if (bytesRead == -1)
				break;
			offset += bytesRead;
		}

		if (offset != contentLength) {
			throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
		}
		return fileInputstream;
	}

	private void checkContentType(URLConnection connection, int contentLength) throws IOException {
		String contentType = connection.getContentType();
		if (contentType.startsWith("text/") || contentLength == -1) {
			throw new IOException("This is not a binary file: " + contentType);
		}
	}

	public void setRepositoryURL(URL url) {
		this.repositoryURL = url;
	}

}
