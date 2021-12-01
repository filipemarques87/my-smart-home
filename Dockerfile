FROM balenalib/armv7hf-openjdk:11-bookworm

ENV DB_HOST "$DB_HOST"
ENV DB_USER "$DB_USER"
ENV DB_PASSWORD "$DB_PASSWORD"
ENV DB_JDBC_DRIVER "$DB_JDBC_DRIVER"
ENV LOG_LEVEL "$LOG_LEVEL"

COPY ./dist /app

CMD ["java", \
     "-DappFolder=/app" \
     "-DlogPath=/app/logs", \
     "-DdbHost=${DB_HOST}", \
     "-Dusername=${DB_USER}", \
     "-Dpassword=${DB_PASSWORD}", \
     "-DjdbcDriver=${DB_JDBC_DRIVER}" \
     "-DlogLevel=${LOG_LEVEL}", \
     "-jar", "/app/my-smart-home-server.jar"]
