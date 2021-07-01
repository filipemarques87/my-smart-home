FROM openjdk:11-jre-slim-stretch

ARG DB_HOST
ARG DB_USER
ARG DB_PASSWORD

VOLUME /app/config
VOLUME /app/plugins

COPY ./dist /app

CMD ["java", \
     "-DdeviceFile=/app/config/devices.yaml", \
     "-DconfigurationFile=/app/config/configuration.properties", \
     "-DdbHost=${DB_HOST}", \
     "-Dusername=${DB_USER}", \
     "-Dpassword=${DB_PASSWORD}", \
     "-DlogLevel=${LOG_LEVEL}", \
     "-DlogPath=/app/logs", \
     "-DfirebaseConfigFile=/", \
     "-jar", "server-1.0-SNAPSHOT.jar"]