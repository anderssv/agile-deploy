package no.f12.agiledeploy.deployer;

import java.io.File;
import java.util.Date;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
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

		ArgumentParser argparse = createArgumentParser();

		Namespace ns = null;
		try {			
			ns = argparse.parseArgs(args);
			System.out.println(ns.getAttrs());
			ps = new PackageSpecification(ns.getString("group_id"), ns.getString("artifact_id"), ns.getString("version"), ns.getString("packaging"));
			environment = ns.getString("environment");
		} catch (ArgumentParserException e) {
			argparse.handleError(e);
			System.exit(1);
		} catch (IllegalArgumentException e) {
			LOG.error(e.getMessage());
			System.exit(1);
		}

		LOG.info("Starting deploy: " + ps);
		deployService.deploy(ps, environment, workingDirectory);
	}

	protected static ArgumentParser createArgumentParser() {
		ArgumentParser argparse = ArgumentParsers.newArgumentParser("Agile Deployer").defaultHelp(true).description("Deploys lightweight Java applications");
		argparse.addArgument("-e", "--environment").required(true).help("The environment this deploy is for");
		argparse.addArgument("-g", "--group-id").required(true).help("The groupId of the artifact to deploy");
		argparse.addArgument("-a", "--artifact-id").required(true).help("The artifactID to deploy");
		argparse.addArgument("-v", "--version").required(true).help("The version to deploy");
		argparse.addArgument("-p", "--packaging").setDefault("zip").help("The package format");
		return argparse;
	}

	public File getWorkingDirectory() {
		return this.workingDirectory;
	}
}
