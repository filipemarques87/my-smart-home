CREATE TABLE notification
  (
     id        VARCHAR(120) NOT NULL,
     condition VARCHAR(150) NOT NULL,
     message   VARCHAR(150) NOT NULL,
     device_id VARCHAR(250) NULL,
     PRIMARY KEY (id)
  );  