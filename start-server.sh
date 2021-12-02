#!/bin/bash
# create folders if not exists
mkdir -p $DATA_FOLDER
mkdir -p CONFIG_FOLDER
mkdir -p LOGS_FOLDER
mkdir -p PLATFORMS_FOLDER

# create empty files if not exists
if [ ! -f "$CONFIG_FILE" ]; then
    touch "$CONFIG_FILE"
fi

if [ ! -f "$DEVICES_FILES" ]; then
    echo "[]" > "$DEVICES_FILES"
fi

# set INFO log level as default
if [[ -z "${LOG_LEVEL}" ]]; then
  LOG_LEVEL="INFO"
fi

# start java application
java, \
  -DappRoot="$APP_ROOT" \
  -DdataFolder="$DATA_FOLDER" \
  -DlogPath="$LOGS_FOLDER" \
  -DplatformsFolder="$PLATFORMS_FOLDER" \
  -DconfigFile="$CONFIG_FILE" \
  -DdevicesFile="$DEVICES_FILES" \
  -DfirebaseFcmFile="$FIREBASE_FCM_FILE" \
  -DdbHost=${DB_HOST} \
  -Dusername=${DB_USER} \
  -Dpassword=${DB_PASSWORD} \
  -DjdbcDriver=${DB_JDBC_DRIVER} \
  -DlogLevel=${LOG_LEVEL} \
  -jar /app/my-smart-home-server.jar
