package no.f12.agiledeploy.deployer;

import java.io.File;
import java.util.Date;

import no.f12.agiledeploy.deployer.repo.PackageSpecification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CommandLineDeployer {

	private static final Logger LOG = Logger.getLogger(CommandLineDeployer.class);

	private DeployService deployService;
	private File workingDirectory = new File(".");

	public CommandLineDeployer(String context) {
		BeanFactory factory = new ClassPathXmlApplicationContext(context);
		this.deployService = (DeployService) factory.getBean("deployServiceImpl");
		this.workingDirectory = (File) factory.getBean("workingDirectory");
	}

	public static void main(String[] args) {
		LOG.info("Launched deploy at " + new Date());
		CommandLineDeployer deployer = new CommandLineDeployer("classpath:spring/deployer-applicationContext.xml");
		deployer.execute(args);
		LOG.info("Deploy ended at " + new Date());
	}

	public void execute(String[] args) {
		PackageSpecification ps = null;
		String environment = null;
		try {
			checkCommandLine(args);
			ps = parsePackageSpecification(args);
			environment = parseEnvironment(args);
		} catch (IllegalArgumentException e) {
			LOG.error(e.getMessage());
			System.exit(1);
		}

		LOG.info("Starting deploy: " + ps);
		deployService.deploy(ps, environment, workingDirectory);
	}

	private static String parseEnvironment(String[] args) {
		return args[0];
	}

	public static void checkCommandLine(String[] args) {
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

	public File getWorkingDirectory() {
		return this.workingDirectory;
	}
}
