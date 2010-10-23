#!/bin/sh
#
# The tests written for the deployer uses an ZIP of the
# example application. Because the dependencies between them
# in Maven seems impossible to express, this script will
# copy the latest version into the folders for the deployer.
#
#

cd application-template
mvn clean install
cd ..
cp application-template/myapp-server/target/myapp-server-0.1-SNAPSHOT.zip deployer/src/test/resources/application.zip

echo
echo 'Application is updated! Please make sure that the tests in the deployer still runs!'
echo


