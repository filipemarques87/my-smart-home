#!/bin/bash

# volumes host path
HOST_APP_PATH=/home/pi/my-smart-home/

# configuration
DB_HOST=
DB_USER=
DB_PASSWORD=
DB_JDBC_DRIVER=

# create network if not exists
NETWORK_NAME=my-smart-home-network
res=$(docker network ls | grep "$NETWORK_NAME")
if [ -z "$res" ]; then
  echo "create network $NETWORK_NAME"
  docker network create $NETWORK_NAME
fi

docker run\
  -p 8080:8080 \
  --env TARGET_APP_PATH=$TARGET_APP_PATH \
  --env DB_HOST=$DB_HOST \
  --env DB_USER=$DB_USER \
  --env DB_PASSWORD=$DB_PASSWORD \
  --env DB_JDBC_DRIVER=$DB_JDBC_DRIVER \
  --env LOG_LEVEL=INFO \
  --volume $HOST_APP_PATH:/app/my-smart-home \
  --network my-smart-home-network \
  --name my-smart-home \
  ft2m/my-smart-home
