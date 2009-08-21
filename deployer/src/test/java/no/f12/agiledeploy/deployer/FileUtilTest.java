package no.f12.agiledeploy.deployer;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;


public class FileUtilTest {

	@Test
	public void shouldGenerateRelativePath() {
		File link = new File("target/temp/spring-core/test/current/datasource.properties");
		File target = new File("target/temp/spring-core/test/datasource.properties");
		
		assertEquals("../datasource.properties", FileUtil.getRelativePath(target, link));

		link = new File("target/temp/spring-core/test/current/thinglink");
		target = new File("target/temp/hello/thingdir");
		
		assertEquals("../../../hello/thingdir", FileUtil.getRelativePath(target, link));
	}

	
}
