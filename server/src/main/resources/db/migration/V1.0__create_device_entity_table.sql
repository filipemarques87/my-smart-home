CREATE TABLE device
  (
     device_id       VARCHAR(120) NOT NULL,
     platform        VARCHAR(150) NOT NULL,
     name            VARCHAR(150) NOT NULL,
     additional_info VARCHAR(4000) NOT NULL,
     type            VARCHAR(150) NOT NULL,
     device_group_id VARCHAR(150) NOT NULL,
     PRIMARY KEY (device_id)
  );  