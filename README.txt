This is my preferred setup when it comes to creating
Java applications. It handles several issues.

This is an early version and used for preparing for
my JavaZone 2009 talk on Agile Deployment.

It is loosely based around the following articles:
* http://blog.f12.no/wp/2009/03/27/agile-deployment-talk-retro/
* http://blog.f12.no/wp/2009/07/08/java-migrations-tools/
* http://blog.f12.no/wp/2009/01/24/the-new-guy-and-his-database/
* http://blog.f12.no/wp/2009/01/03/migrations-for-java/


= DONE =
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

= TODO =
* Installing the application by
** Replacing Symlinks
* Upgrading the database
* Run with daemon? JSW might have licence issues
* Start/stop scripts
* Recommended practices
* Correct permissions on execute scripts

Some of these parts should probably be in something
like Scala or JRuby, but I'll have to take the time
to learn that later. :)


-- Anders Sveen <anders@f12.no>
