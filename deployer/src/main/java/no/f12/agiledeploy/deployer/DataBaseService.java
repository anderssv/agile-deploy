package no.f12.agiledeploy.deployer;

import java.io.File;

public interface DataBaseService {

	void upgradeDatabase(File installationDirectory);

	void loadSettings(File targetDirectory);

}
