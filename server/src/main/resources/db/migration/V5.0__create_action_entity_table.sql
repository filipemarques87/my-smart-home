CREATE TABLE action
  (
     id        VARCHAR(120) NOT NULL,
     TRIGGER   VARCHAR(150) NULL,
     target_id VARCHAR(150) NOT NULL,
     payload   VARCHAR(150) NULL,
     device_id VARCHAR(250) NULL,
     PRIMARY KEY (id)
  );