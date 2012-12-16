package no.f12.agiledeploy.deployer;


public interface DeployService {

	public void downloadAndDeploy(DeploymentSpecification ds);
	
	public void deploy(DeploymentSpecification ds);

}
