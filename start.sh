#!/bin/bash

# volumes host path
APP_PATH=/home/filipe/my-smart-home/

# configuration
DB_HOST=jdbc:h2:file:/app/config/database.db
DB_USER=sa
DB_PASSWORD=
DB_JDBC_DRIVER=
LOG_LEVEL=INFO

# create network if not exists
NETWORK_NAME=my-smart-home-network
res=$(docker network ls | grep "$NETWORK_NAME")
if [ -z "$res" ]; then
  echo "create network $NETWORK_NAME"
  docker network create $NETWORK_NAME
fi

docker run\
  -p 8080:8080 \
  --env DB_HOST=$DB_HOST \
  --env DB_USER=$DB_USER \
  --env DB_PASSWORD=$DB_PASSWORD \
  --env DB_JDBC_DRIVER=$DB_JDBC_DRIVER \
  --env LOG_LEVEL=$LOG_LEVEL \
  --volume $APP_PATH:/app \
  --network my-smart-home-network \
  ft2m/my-smart-home
