#not use
FROM balenalib/armv7hf-openjdk:11-bookworm

# folders
ENV APP_ROOT "/app"
ENV DATA_FOLDER "/app/my-smart-home/data"
ENV CONFIG_FOLDER "/app/my-smart-home/config"
ENV LOGS_FOLDER "/app/my-smart-home/logs"
ENV PLATFORMS_FOLDER "/app/my-smart-home/platforms"

# files
ENV CONFIG_FILE "/app/my-smart-home/config/config.properties"
ENV DEVICES_FILES "/app/my-smart-home/config/devices.yaml"
ENV FIREBASE_FCM_FILE "/app/my-smart-home/config/firebase-fcm.json"

# data config
ENV DB_HOST "$DB_HOST"
ENV DB_USER "$DB_USER"
ENV DB_PASSWORD "$DB_PASSWORD"
ENV DB_JDBC_DRIVER "$DB_JDBC_DRIVER"

# logging
ENV LOG_LEVEL "$LOG_LEVEL"

WORKDIR "/app"

# copy application and start script
COPY ./my-smart-home-server.jar ./my-smart-home-server.jar
COPY ./start-server.sh ./start-server.sh

RUN chmod +x ./start-server.sh

CMD ["./start-server.sh"]
