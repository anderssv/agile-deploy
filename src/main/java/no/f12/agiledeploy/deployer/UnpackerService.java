package no.f12.agiledeploy.deployer;

import java.io.File;

public interface UnpackerService {

	void unpack(File downloadedFile);
	
	void unpack(File downloadedFiles, File workingDirectory);

}
