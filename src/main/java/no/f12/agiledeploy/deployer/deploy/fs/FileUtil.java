package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileUtil {

	public static final String DEFAULT_SYMLINKCOMMAND = "ln -s %1$s %2$s";

	private static final Logger LOG = Logger.getLogger(FileUtil.class);

	public static void changePermissions(File file, String permission) {
		if (!hasSupportForUnixCommands()) {
			throw new IllegalStateException("No support for setting sym links on this os");
		}
		try {
			String command = String.format("chmod " + permission + " %1$s", file.getCanonicalPath());
			executeAndWait(command);
			LOG.debug("Changed permissions on " + file + " with command " + command);
		} catch (IOException e) {
			throw new IllegalStateException("Could not create symlink", e);
		}
	}

	public static void createSymbolicLink(File source, File symLink) {
		if (!hasSupportForUnixCommands()) {
			throw new IllegalStateException("No support for creating sym links on this os");
		}
		try {
			String command = String.format(DEFAULT_SYMLINKCOMMAND, getRelativePath(source, symLink), symLink
					.getCanonicalPath());
			executeAndWait(command);
			LOG.debug("Created symlink with command: " + command);
		} catch (IOException e) {
			throw new IllegalStateException("Could not create symlink", e);
		}
	}

	private static void executeAndWait(String command) throws IOException {
		try {
			Process proc = Runtime.getRuntime().exec(command);
			int returnCode = proc.waitFor();
			if (returnCode != 0) {
				throw new IllegalStateException("Could not execute " + command + ", process returned " + returnCode);
			}
		} catch (InterruptedException e) {
			throw new IllegalStateException("Could not create execute command " + command, e);
		}
	}

	public static String getRelativePath(File target, File relativeTo) {
		List<File> targetPaths = new ArrayList<File>();
		List<File> relativePaths = new ArrayList<File>();

		generateList(target, targetPaths);
		generateList(relativeTo, relativePaths);

		String result = "";

		List<File> namedPaths = new ArrayList<File>();
		namedPaths.addAll(targetPaths);
		namedPaths.removeAll(relativePaths);
		for (int ctr = 0; ctr < namedPaths.size(); ctr++) {
			result = namedPaths.get(ctr).getName() + "/" + result;
		}
		result = result.substring(0, result.length() - 1);

		List<File> levels = new ArrayList<File>();
		levels.addAll(relativePaths);
		levels.removeAll(targetPaths);
		for (int ctr = 0; ctr < levels.size() - 1; ctr++) {
			result = "../" + result;
		}

		return result;
	}

	private static void generateList(File currentFile, List<File> targetPaths) {
		while (currentFile != null) {
			targetPaths.add(currentFile);
			currentFile = currentFile.getParentFile();
		}
	}

	private static boolean hasSupportForUnixCommands() {
		if (isWindows())
			return false;
		return true;
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		// Mac
		return (os.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}

}
