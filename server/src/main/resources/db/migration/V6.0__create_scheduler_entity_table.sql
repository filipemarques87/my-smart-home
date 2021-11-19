CREATE TABLE scheduler
  (
     id        VARCHAR(120) NOT NULL,
     TRIGGER   VARCHAR(150) NOT NULL,
     payload   VARCHAR(150) NULL,
     type      VARCHAR(255) NULL,
     device_id VARCHAR(250) NULL,
     PRIMARY KEY (id)
  );
