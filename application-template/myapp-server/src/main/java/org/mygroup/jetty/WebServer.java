package org.mygroup.jetty;

import java.io.File;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.util.Assert;

public class WebServer {

	private Integer port;
	private Server server;

	public WebServer(int port) {
		this.port = port;
	}

	public void start(File webAppContextPath, String applicationContext) {
		server = startWebServer(webAppContextPath, applicationContext);
		port = getServerPort(server);
	}

	private Integer getServerPort(Server server) {
		return server.getConnectors()[0].getLocalPort();
	}

	private Server startWebServer(File webAppContextPath, String applicationContext) {
		Assert.isTrue(webAppContextPath.exists(), "The context path you have specified does not exist");
		Assert.notNull(applicationContext, "You must specify the context path of the application");

		Server server = new Server(this.port);
		try {
			server.addHandler(new WebAppContext(webAppContextPath.getCanonicalPath(), applicationContext));
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return server;
	}

	public Integer getPort() {
		Assert.notNull(port, "Server must be started before port can be determined");
		return this.port;
	}

	public void stop() {
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
