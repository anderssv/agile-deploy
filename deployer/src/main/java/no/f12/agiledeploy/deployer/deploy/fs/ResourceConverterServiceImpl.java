package no.f12.agiledeploy.deployer.deploy.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import no.f12.agiledeploy.deployer.EncodingConversion;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ResourceConverterServiceImpl implements ResourceConverterService {

	private static final Logger LOG = Logger.getLogger(ResourceConverterServiceImpl.class);

	private Collection<EncodingConversion> conversions = new ArrayList<EncodingConversion>();

	public ResourceConverterServiceImpl() {
		this.conversions.add(new EncodingConversion("bin", "UTF-8", "Cp1047"));
	}

	public void setConversions(Collection<EncodingConversion> conversions) {
		this.conversions = conversions;
	}

	@Override
	public void convert(File target) {
		for (EncodingConversion conversion : conversions) {
			File currentFile = new File(target, conversion.getPath());
			if (!currentFile.exists()) {
				LOG.warn("Specified conversion does not exist: " + conversion);
				return;
			}

			try {
				convertFile(currentFile, conversion);
			} catch (FileNotFoundException e) {
				// Not possible
			} catch (IOException e) {
				throw new RuntimeException("Error converting file: " + currentFile);
			}
		}
	}

	private void convertFile(File file, EncodingConversion conversion) throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File subFile : files) {
				convertFile(subFile, conversion);
			}
		} else {
			String content = FileUtil.readFile(file, conversion.getSourceEncoding());
			FileUtil.writeStringToFile(file, conversion.getTargetEncoding(), content);
		}
	}

}
