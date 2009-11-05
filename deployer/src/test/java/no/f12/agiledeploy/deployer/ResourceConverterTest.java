package no.f12.agiledeploy.deployer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import no.f12.agiledeploy.deployer.deploy.fs.FileUtil;
import no.f12.agiledeploy.deployer.deploy.fs.ResourceConverterService;
import no.f12.agiledeploy.deployer.deploy.fs.ResourceConverterServiceImpl;

import org.junit.After;
import org.junit.Test;

public class ResourceConverterTest {

	@Test
	public void shouldConvertFileCorrectly() throws IOException {
		File target = TestDataProvider.getDefaultTargetDirectory();
		TestDataProvider.unpackDefaultTestZip(target);

		EncodingConversion conversion = new EncodingConversion("bin", "UTF-8", "Cp1047");
		Collection<EncodingConversion> conversions = new ArrayList<EncodingConversion>();
		conversions.add(conversion);

		ResourceConverterServiceImpl serviceImpl = new ResourceConverterServiceImpl();
		serviceImpl.setConversions(conversions);

		ResourceConverterService service = serviceImpl;
		service.convert(target);

		File testFile = new File(target, "bin/myapp.bat");
		String wrong = FileUtil.readFile(testFile, "UTF-8");
		String right = FileUtil.readFile(testFile, "Cp1047");

		assertFalse(wrong.contains("Windows_NT"));
		assertTrue(right.contains("Windows_NT"));
	}

	@After
	public void removeTempDir() {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}

}
