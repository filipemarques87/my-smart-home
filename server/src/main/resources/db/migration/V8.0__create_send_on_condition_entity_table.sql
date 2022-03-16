CREATE TABLE send_on_condition
  (
     id        VARCHAR(120) NOT NULL,
     triggers  VARCHAR(150) NOT NULL,
     condition VARCHAR(150) NOT NULL,
     device_id VARCHAR(250) NOT NULL,
     PRIMARY KEY (id)
  );
