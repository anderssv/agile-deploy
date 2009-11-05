package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class ResourceConverterServiceImpl implements ResourceConverterService {

	private static final Logger LOG = Logger.getLogger(ResourceConverterServiceImpl.class);

	private Collection<String> paths;
	private String sourceEncoding;
	private String targetEncoding;

	@Override
	public void convert(File target) {
		if (sourceEncoding == null || sourceEncoding.trim().equals("")) {
			return;
		}
		LOG.info("Starting conversion of resource encoding from " + sourceEncoding + " to " + targetEncoding);
		
		for (String path : paths) {
			File currentFile = new File(target, path);
			if (!currentFile.exists()) {
				LOG.warn("Specified conversion path does not exist: " + path);
				return;
			}

			try {
				convertFile(currentFile);
			} catch (FileNotFoundException e) {
				// Not possible
			} catch (IOException e) {
				throw new RuntimeException("Error converting file: " + currentFile);
			}
		}
	}

	private void convertFile(File file) throws IOException {
		if (file.isDirectory()) {
			LOG.info("Converting " + file);
			File[] files = file.listFiles();
			for (File subFile : files) {
				convertFile(subFile);
			}
		} else {
			LOG.debug("Converting " + file);
			String content = FileUtil.readFile(file, this.getSourceEncoding());
			FileUtil.writeStringToFile(file, this.getTargetEncoding(), content);
		}
	}

	private String getTargetEncoding() {
		return this.targetEncoding;
	}

	private String getSourceEncoding() {
		return this.sourceEncoding;
	}

	public void setPaths(Collection<String> paths) {
		this.paths = paths;
	}

	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}

	public void setTargetEncoding(String targetEncoding) {
		this.targetEncoding = targetEncoding;
	}

	public void setPathsString(String string) {
		paths = new HashSet<String>();
		StringTokenizer tokenizer = new StringTokenizer(string, ";");
		while (tokenizer.hasMoreElements()) {
			String element = tokenizer.nextToken();
			paths.add(element.trim());
		}
	}

	public Collection<String> getPaths() {
		return this.paths;
	}

}
