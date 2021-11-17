#!/bin/bash

# configuration
DB_HOST=jdbc:h2:file:/app/config/database.db
DB_USER=sa
DB_PASSWORD=
LOG_LEVEL=INFO

# volumes host path
CONFIG_PATH=/home/filipe/projects/my-smart-home
PLUGINS_PATH=/home/filipe/projects/my-smart-home/server-app/plugins

docker run\
  -p 8080:8080 \
  --env DB_HOST=$DB_HOST \
  --env DB_USER=$DB_USER \
  --env DB_PASSWORD=$DB_PASSWORD \
  --env LOG_LEVEL=$LOG_LEVEL \
  --volume $CONFIG_PATH:/app/config \
  --volume $PLUGINS_PATH:/app/plugins \
  --network my-smart-home-network \
  my-smart-home-server 
