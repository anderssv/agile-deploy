package no.f12.agiledeploy.deployer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RepositoryRepoImpl implements RepositoryRepo {

	private URL repositoryURL;

	@Override
	public File fetchFile(String filePath, String fileName) {
		String fullFilePath = filePath + "/" + fileName;
		File resultingFile = null;
		try {
			URL fileUrl = new URL(repositoryURL, fullFilePath);
			FileOutputStream out = null;
			InputStream fileInputstream = null;
			try {
				URLConnection connection = fileUrl.openConnection();
				int contentLength = connection.getContentLength();
				checkContentType(connection, contentLength);

				byte[] data = new byte[contentLength];
				fileInputstream = readBytes(connection, contentLength, data);

				resultingFile = new File(fileName);
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
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Malformed URL: " + fullFilePath, e);
		} catch (IOException e) {
			throw new IllegalStateException("Could not retrieve file: " + fullFilePath, e);
		}
		return resultingFile;
	}

	private InputStream readBytes(URLConnection connection, int contentLength, byte[] data) throws IOException {
		InputStream fileInputstream;
		InputStream raw = connection.getInputStream();
		fileInputstream = new BufferedInputStream(raw);
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
			throw new IOException("This is not a binary file.");
		}
	}

	public void setRepositoryURL(URL url) {
		this.repositoryURL = url;
	}

}
