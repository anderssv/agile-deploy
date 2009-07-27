package no.f12.agiledeploy.deployer;

import java.io.File;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CommandLineDeployer {

	private DeployService deployService;
	private File workingDirectory;

	public CommandLineDeployer(String context) {
		BeanFactory factory = new ClassPathXmlApplicationContext(context);
		this.deployService = (DeployService) factory.getBean("deployServiceImpl");
		this.workingDirectory = (File) factory.getBean("workingDirectory");
	}

	public static void main(String[] args) {
		CommandLineDeployer deployer = new CommandLineDeployer("classpath:spring/deployer-applicationContext.xml");
		deployer.execute(args);
	}

	public void execute(String[] args) {
		checkCommandLine(args);
		PackageSpecification ps = parsePackageSpecification(args);
		String environment = parseEnvironment(args);

		deployService.deploy(ps, environment, workingDirectory);
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
}
