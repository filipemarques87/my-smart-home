FROM balenalib/armv7hf-openjdk:11-bookworm

# folders
ENV APP_ROOT "/app"
ENV DATA_FOLDER "$APP_ROOT/data"
ENV CONFIG_FOLDER "$APP_ROOT/config"
ENV LOGS_FOLDER "$APP_ROOT/logs"
ENV PLATFORMS_FOLDER "$APP_ROOT/platforms"

# files
ENV CONFIG_FILE "$CONFIG_FOLDER/config.properties"
ENV DEVICES_FILES "$CONFIG_FOLDER/devices.yaml"
ENV FIREBASE_FCM_FILE "$CONFIG_FOLDER/firebase-fcm.json"

# data config
ENV DB_HOST "$DB_HOST"
ENV DB_USER "$DB_USER"
ENV DB_PASSWORD "$DB_PASSWORD"
ENV DB_JDBC_DRIVER "$DB_JDBC_DRIVER"

# logging
ENV LOG_LEVEL "$LOG_LEVEL"

WORKDIR "$APP_ROOT"

# copy application and start script
COPY ./dist ./my-smart-home-server.jar
COPY ./start-server.sh ./start-server.sh

RUN chmod +x ./start-server.sh

CMD ["./start-server.sh"]
