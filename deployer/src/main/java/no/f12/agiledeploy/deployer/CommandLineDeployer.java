package no.f12.agiledeploy.deployer;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CommandLineDeployer {

	public static void main(String[] args) {
		execute(args);
	}

	public static void execute(String[] args) {
		checkCommandLine(args);
		PackageSpecification ps = parsePackageSpecification(args);
		String environment = parseEnvironment(args);

		DeployService ds = wireApplication();
		ds.deploy(ps, environment);
	}

	private static String parseEnvironment(String[] args) {
		return args[0];
	}

	private static void checkCommandLine(String[] args) {
		if (args == null || args.length < 4) {
			throw new IllegalArgumentException(
					"Not enough parameters. Usage: CommandLineDeployer environment groupId artifactId version");
		}
	}

	private static PackageSpecification parsePackageSpecification(String[] args) {
		if (args.length == 4) {
			return new PackageSpecification(args[1], args[2], args[3]);
		} else {
			return new PackageSpecification(args[1], args[2], args[3], args[4]);
		}
	}

	private static DeployService wireApplication() {
		BeanFactory factory = new ClassPathXmlApplicationContext("classpath:spring/deployer-applicationContext.xml");
		return (DeployService) factory.getBean("deployServiceImpl");
	}

}
