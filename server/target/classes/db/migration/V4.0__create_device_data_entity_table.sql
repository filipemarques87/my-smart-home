CREATE TABLE device_data
  (
     device_id  VARCHAR(120) NOT NULL,
     event_time BIGINT NOT NULL,
     data       VARCHAR(4000),
     PRIMARY KEY (device_id)
  );  