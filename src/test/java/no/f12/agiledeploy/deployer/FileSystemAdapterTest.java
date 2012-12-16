package no.f12.agiledeploy.deployer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

import no.f12.agiledeploy.deployer.deploy.fs.FileUtil;

import org.junit.Test;

public class FileSystemAdapterTest extends AbstractFileSystemTest {

	@Test
	public void shouldPerformExpectedSubstitution() {
		assertEquals(String.format(FileUtil.DEFAULT_SYMLINKCOMMAND, "hello1", "hello2"),
				"ln -s hello1 hello2");
	}

	@Test
	public void shouldNotDeleteContentsMatchingAFilter() throws IOException {
		File artifactDir = getWorkingDirectory();
		File testDir = new File(artifactDir, "data");
		testDir.mkdirs();
		File testFile = new File(testDir, "testfile");
		TestDataProvider.writeContentToFile(testFile, "testing123");

		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (!pathname.getName().equals("data")) {
					return true;
				}
				return false;
			}
		};
		deleteDirectoryWithFilter(artifactDir, filter);
		assertTrue(Arrays.asList(testDir.listFiles()).contains(testFile));
		
		deleteDirectoryWithFilter(artifactDir, null);
	}
	
}
