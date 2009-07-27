package no.f12.agiledeploy.deployer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class UnpackerServiceImpl implements UnpackerService {

	private static final Log LOG = LogFactory.getLog(UnpackerServiceImpl.class);
	
	@Override
	public void unpack(File downloadedFile) {
		unpack(downloadedFile, new File("."));
	}

	@Override
	public void unpack(File zipFile, File workingDirectory) {
		try {
			workingDirectory.mkdir();

			ZipFile zip = new ZipFile(zipFile);

			Enumeration zipFileEntries = zip.entries();
			while (zipFileEntries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

				if (entry.isDirectory()) {
					File destination = new File(workingDirectory, entry.getName());
					destination.mkdirs();
					LOG.debug(destination);
				} else {
					String currentEntry = entry.getName();

					File destFile = new File(workingDirectory, currentEntry);
					File destinationParent = destFile.getParentFile();

					// create the parent directory structure if needed
					destinationParent.mkdirs();
					writeFile(entry, zip, destFile);
					LOG.debug(destFile);
				}
			}
		} catch (ZipException e) {
			throw new IllegalStateException("Unable to extract zip: " + zipFile, e);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to access file: " + zipFile, e);
		}
	}

	private void writeFile(ZipEntry entry, ZipFile zip, File destination) throws IOException {
		BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));

		// establish buffer for writing file
		int BUFFER = 2048;
		byte data[] = new byte[BUFFER];

		// write the current file to disk
		FileOutputStream fos = new FileOutputStream(destination);
		BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

		int currentByte;
		// read and write until last byte is encountered
		while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
			dest.write(data, 0, currentByte);
		}
		dest.flush();
		dest.close();
		is.close();
	}

}
