#!/bin/sh
#
# The tests written for the deployer uses an ZIP of the
# example application. Because the dependencies between them
# in Maven seems impossible to express, this script will
# copy the latest version into the folders for the deployer.
#
#

pushd .
cd ../jettyserver-archetype
mvn clean install
popd
cp ../jettyserver-archetype/myapp-server/target/myapp-server-0.1-SNAPSHOT.zip src/test/resources/application.zip

echo
echo 'Application is updated! Please make sure that the tests in the deployer still runs!'
echo


