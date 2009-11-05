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

		Collection<String> paths = new ArrayList<String>();
		paths.add("bin");

		ResourceConverterServiceImpl serviceImpl = new ResourceConverterServiceImpl();
		serviceImpl.setPaths(paths);
		serviceImpl.setSourceEncoding("UTF-8");
		serviceImpl.setTargetEncoding("Cp1047");

		ResourceConverterService service = serviceImpl;
		service.convert(target);

		File testFile = new File(target, "bin/myapp.bat");
		String wrong = FileUtil.readFile(testFile, "UTF-8");
		String right = FileUtil.readFile(testFile, "Cp1047");

		assertFalse(wrong.contains("Windows_NT"));
		assertTrue(right.contains("Windows_NT"));
	}
	
	@Test
	public void shouldNotDoConversionIfNoSourceEncodingIsGiven() throws IOException {
		File target = TestDataProvider.getDefaultTargetDirectory();
		TestDataProvider.unpackDefaultTestZip(target);
		
		Collection<String> paths = new ArrayList<String>();
		paths.add("bin");

		ResourceConverterServiceImpl serviceImpl = new ResourceConverterServiceImpl();
		serviceImpl.setPaths(paths);

		ResourceConverterService service = serviceImpl;
		service.convert(target);

		File testFile = new File(target, "bin/myapp.bat");
		String right = FileUtil.readFile(testFile, "UTF-8");
		String wrong = FileUtil.readFile(testFile, "Cp1047");

		assertFalse(wrong.contains("Windows_NT"));
		assertTrue(right.contains("Windows_NT"));
	}
	
	@Test
	public void shouldAcceptCommaDelimitedStringWithPaths() {
		ResourceConverterServiceImpl service = new ResourceConverterServiceImpl();
		service.setPathsString("bin;path/with/slashes; path/with/space ;hello");
		Collection<String> paths = service.getPaths();
		
		assertEquals(4, paths.size());
		assertTrue(paths.contains("bin"));
		assertTrue(paths.contains("path/with/space"));
	}

	@After
	public void removeTempDir() {
		FileUtil.deleteDir(TestDataProvider.getDefaultTempDir());
	}

}
