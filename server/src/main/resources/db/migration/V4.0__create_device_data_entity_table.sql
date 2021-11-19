CREATE TABLE device_data
  (
     device_id  VARCHAR(120) NOT NULL,
     event_time BIGINT NOT NULL,
     data       VARCHAR(4000) NULL,
     type       VARCHAR(255) NULL,
     PRIMARY KEY (device_id)
  );