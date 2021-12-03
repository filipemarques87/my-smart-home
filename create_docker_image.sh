#!/bin/bash

DIST_DIR=dist

mvn clean package
ret=$?
if [[ $ret != 0 ]]; then
  exit 1
fi

if [ -d $DIST_DIR ]; then
  rm -r $DIST_DIR
fi
mkdir $DIST_DIR

cp ./server/target/server-1.0-SNAPSHOT-boot.jar $DIST_DIR/my-smart-home-server.jar

exit 0
echo "Building docker file"
docker build . -t ft2m/my-smart-home
if [[ $ret != 0 ]]; then
  exit 1
fi

echo "Pushing docker file"
docker push ft2m/my-smart-home:latest
