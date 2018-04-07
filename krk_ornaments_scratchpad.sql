CREATE TABLE map_feature (
  id               INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  description      VARCHAR(512) NOT NULL,
  google_place_id  VARCHAR(512) NOT NULL,
  create_timestamp DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modify_timestamp DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
commit;

CREATE TABLE map_feature_photo (
  id               INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  map_feature_id INT NOT NULL,
  photo MEDIUMBLOB,
  create_timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (map_feature_id) REFERENCES map_feature(id)
);
SELECT VERSION();

delete FROM map_feature where id is not null;

delete from map_feature where id > 53;
delete from map_feature_photo where map_feature_id > 53;
SELECT * FROM map_feature;
SELECT * FROM map_feature_photo;

ALTER TABLE MAP_FEATURE_PHOTOS ADD COLUMN create_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

commit;

drop table map_feature_photo;

alter table map_feature_photo add COLUMN blob_name VARCHAR(512);

ALTER TABLE map_feature ADD COLUMN longitude DECIMAL(10,7) NOT NULL;
ALTER TABLE map_feature ADD COLUMN latitude DECIMAL(9, 6) NOT NULL;
