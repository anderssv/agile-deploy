This is my preferred setup when it comes to creating
Java web applications. It handles several issues.

This is an early version and used for preparing for
my JavaZone 2009 talk on Agile Deployment.

It is loosely based around the following articles:
* http://blog.f12.no/wp/2009/03/27/agile-deployment-talk-retro/
* http://blog.f12.no/wp/2009/07/08/java-migrations-tools/
* http://blog.f12.no/wp/2009/01/24/the-new-guy-and-his-database/
* http://blog.f12.no/wp/2009/01/03/migrations-for-java/
* http://www.infoq.com/articles/deployment-is-the-goal

= Pre requisites =
Installed:
* Maven
* Java

== Configuration ==
To use the deployer you need a Maven repository.

To compile you must add a repository to your ~/.m2/settings.xml:

	<profile>
		<id>extra-repo</id>
		<activation>
			<activeByDefault>true</activeByDefault>
		</activation>
		<repositories>
			<repository>
				<id>dbdeploy</id>
				<url>http://dbdeploy.googlecode.com/svn/m2-repo/repository/</url>
				<layout>default</layout>
				<snapshotPolicy>always</snapshotPolicy>
			</repository>
		</repositories>
	</profile>


= How to use =
If you understand Maven it will be a lot easier to figure this out. :)

== Application template ==
The template is meant to be an example of how to create a light weight 
application with Jetty embedded. It is packaged as a ZIP, with .jar 
files included as well as some scripts to start the application. This is 
achieved by using the Maven app-assembler and assembly plugin. Have a 
look inside the target folder and the ZIP to figure out what is 
happening. 

You can use this for most webapplications. In my opinion you really 
don't need a big app server most of the time. A future version might
include an embedded version og Tomcat.

This would be suitable for a Maven Archetype, but I didn't find any good 
information on creating a multi project archetype. I'll have to look 
into it later. 

== Deployer ==
The deployer is packaged as a JAR with a manifest that says which class 
to execute. To launch the deployer enter the following on the command 
line: 

java -jar agile-deployer-0.1-SNAPSHOT.jar <env> <groupId> <artifactId> <version> 

By default it downloads artifacts from the Maven repository 
(http://repo1.maven.org/maven2), but you can change this in a property 
file called deploy.properties . By placing this file in the directory 
you are running the JAR from, and adding a property like below you can 
change the repo. 

repo.url=http://myrepo.myorg.com/maven2/ 

=== What it does ===
The deployer handles some basic things:
* Unpacking
* Finding the correct properties
* Symlinking necessary directories

In detail the deployer performs the following tasks:
* Downloads the specified artifact from the Maven repo
* Unpacks the zip into <artifactId>/<env>/current
* Copies settings (if they don't exist, no overwriting) from 
  <artifactId>/<env>/current/properties and 
  <artifactId>/<env>/current/properties/<env> into
  <artifactId>/<env>
* Creates symlink from <artifactId>/<env>/*.properties into
  <artifactId>/<env>/current (copy if not symlink capable)
* Creates symlink from <artifactId>/<env>/data into
  <artifactId>/<env>/current

= Tasks and features =

== DONE ==
* Packaging the application with all dependencies into
** A war
** A zip with all required JARs and WARs
* Installing
** Download release
** Unpacking
** Download snapshot
** Installing configuration
* Issues with clean up after run of deployer, some directories can not be deleted
* Default config for all environments, can be overrided by env
* Data directory in the environment dir that is not deleted on redeploy
* Sym link to data directory
* Copy files to env dir and sym link to current
* Correct permissions on execute scripts

== TODO ==
* Start/stop scripts

== FUTURE ==
* Stop before deploy
* Start after deploy 
* Upgrading the database
* Recommended practices
* Merge in new settins in properties files into existing file on disk?
* Run with daemon? JSW might have licence issues
* Separate SNAPSHOT and release repo
* Clean up exception handling. Way too many IllegalStateExceptions
* Separate log config for tests

= Finally =
Some of these parts should probably be in something like Scala or JRuby, 
but I'll have to take the time to learn that later. :) 



-- Anders Sveen <anders@f12.no>
