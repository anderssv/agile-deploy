package no.f12.agiledeploy.deployer.repo;

import java.io.File;

public interface RepositoryRepo {

	/**
	 * Retrieves the given file from the repository
	 * 
	 * @param file The file to retireve
	 * @return The downloaded file
	 */
	File fetchFile(String filePath, String fileName, File workingDirectory, boolean binary);

}
