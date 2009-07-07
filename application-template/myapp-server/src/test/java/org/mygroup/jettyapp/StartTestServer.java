package org.mygroup.jettyapp;

import java.io.File;

import org.mygroup.jetty.WebServer;

/**
 * Starts a server pointing to a directory with hot-deploy
 */
public class StartTestServer {

	public static void main(String[] args) {
		WebServer server = new WebServer(8080);
		server.start(new File("../myapp-war/src/main/webapp"), "/myapp");
	}

}
