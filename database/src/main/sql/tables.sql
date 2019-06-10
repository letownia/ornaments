DROP DATABASE map_decorator;
CREATE DATABASE IF NOT EXISTS map_decorator;

USE map_decorator;
CREATE TABLE map_feature (
  id               INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name             VARCHAR(512) NOT NULL,
  category         VARCHAR(12)  NOT NULL,
  longitude        DECIMAL(10,7) NOT NULL,
  latitude         DECIMAL(9, 6) NOT NULL,
  description      VARCHAR(512) NOT NULL,
  google_place_id  VARCHAR(512) NOT NULL,
  create_timestamp DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modify_timestamp DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE map_feature
  ADD CONSTRAINT check_category CHECK (category IN ('MYTHICAL', 'FISH', 'BIRDS', 'AMPHIBIANS', 'MAMMALS', 'REPTILES', 'INSECTS', 'ARACHNIDS'));

--
-- CREATE TABLE map_feature_photo_data (
--   id      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
--   data MEDIUMBLOB NOT NULL
-- );


CREATE TABLE map_feature_photo (
  id               INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  map_feature_id INT            NOT NULL,
  thumbnail_identifier VARCHAR(512)      NOT NULL,
  medium_photo_identifier  VARCHAR(512)  NOT NULL,
  create_timestamp   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (map_feature_id) REFERENCES map_feature(id)
);


CREATE TABLE map_decorator_log (
  insert_id VARCHAR(32) NOT NULL PRIMARY KEY,
  log_name VARCHAR(64) NOT NULL,
  receive_timestamp  DATETIME NOT NULL,
  create_timestamp DATETIME NOT NULL,
  db_insert_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
  textPayload VARCHAR(1024)
);