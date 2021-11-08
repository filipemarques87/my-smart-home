#!/bin/bash

DIST_DIR=./dist

mvn clean package
$ret=$?
if [ $ret ne 0 ]; then
  exit 1
fi

if [ -d $DIST_DIR ]; then
  rm -r $DIST_DIR
fi
mkdir $DIST_DIR

cp ./server/target/server-1.0-SNAPSHOT-boot.jar $DIST_DIR/my-smart-home-server.jar

docker build . --tag "my-smart-home-server"