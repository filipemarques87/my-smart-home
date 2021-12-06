#!/bin/bash

# volumes host path
APP_PATH=/home/pi/my-smart-home/

# configuration
DB_HOST=
DB_USER=
DB_PASSWORD=
DB_JDBC_DRIVER=

LOG_LEVEL=INFO

CONFIG_FOLDER=$APP_PATH/config
DATA_FOLDER=$APP_PATH/data
LOGS_FOLDER=$APP_PATH/logs
PLATFORMS_FOLDER=$APP_PATH/platforms
CONFIG_FILE=$APP_PATH/config/config.properties
DEVICES_FILES=$APP_PATH/config/devices.yaml
FIREBASE_FCM_FILE=$$APP_PATH/config/....json

# create folders if not exists
mkdir -p $DATA_FOLDER
mkdir -p $CONFIG_FOLDER
mkdir -p $LOGS_FOLDER
mkdir -p $PLATFORMS_FOLDER

# create empty files if not exists
if [ ! -f "$CONFIG_FILE" ]; then
    touch "$CONFIG_FILE"
fi

if [ ! -f "$DEVICES_FILES" ]; then
    echo "[]" > "$DEVICES_FILES"
fi

# start java application
java \
  -Xmx128m \
  -DdataFolder="$DATA_FOLDER" \
  -DlogPath="$LOGS_FOLDER" \
  -DplatformsFolder="$PLATFORMS_FOLDER" \
  -DconfigFile="$CONFIG_FILE" \
  -DdevicesFile="$DEVICES_FILES" \
  -DfirebaseFcmFile="$FIREBASE_FCM_FILE" \
  -DdbHost="$DB_HOST" \
  -DdbUserName="$DB_USER" \
  -DdbPassword="$DB_PASSWORD" \
  -DdbJdbcDriver="$DB_JDBC_DRIVER" \
  -DlogLevel="$LOG_LEVEL" \
  -jar $APP_PATH/my-smart-home-server.jar
