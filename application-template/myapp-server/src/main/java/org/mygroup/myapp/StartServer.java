package org.mygroup.myapp;

import java.io.File;
import java.io.FilenameFilter;

import org.mygroup.jetty.WebServer;
import org.springframework.util.Assert;

public class StartServer {
	public static void main(String[] args) {
		WebServer server = new WebServer(8080);
		server.start(getFileForWar(new File("repo"), "org.mygroup", "myapp-war"), "/myapp");
	}

	public static File getFileForWar(File repoPath, String groupId, String artifactId) {
		File artifactPath = new File(repoPath, groupId.replaceAll("\\.", "/") + "/" + artifactId);

		String[] directories = artifactPath.list(createDirectoryFilter());
		Assert.isTrue(directories.length == 1);

		File artifactWithVersionPath = new File(artifactPath, directories[0]);

		String[] warfiles = artifactWithVersionPath.list(createWarFilenameFilter());
		Assert.isTrue(warfiles.length == 1);

		File warFile = new File(artifactWithVersionPath, warfiles[0]);

		return warFile;
	}

	private static FilenameFilter createWarFilenameFilter() {
		FilenameFilter warFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".war")) {
					return true;
				}
				return false;
			}
		};
		return warFilter;
	}

	private static FilenameFilter createDirectoryFilter() {
		FilenameFilter dirFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (new File(dir, name).isDirectory()) {
					return true;
				}
				return false;
			}
		};
		return dirFilter;
	}

}
