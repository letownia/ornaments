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
  mediumIdentifier MEDIUMBLOB,
  create_timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (map_feature_id) REFERENCES map_feature(id)
);
SELECT VERSION();

delete from map_feature where id > 1;
delete from map_feature_photo where map_feature_id > 1;

SELECT * FROM map_feature;
SELECT * FROM map_feature_photo;

alter table map_feature_photo drop column photo;
alter table map_feature_photo add thumbnail_identifier varchar(512);

update map_feature_photo set thumbnail_identifier  = medium_identifier where id is not null;

alter table map_feature_photo modify thumbnail_identifier varchar(512) not null;

ALTER TABLE MAP_FEATURE_PHOTOS ADD COLUMN create_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

commit;

drop table map_feature_photo;

alter table map_feature_photo add COLUMN blob_name VARCHAR(512);

ALTER TABLE map_feature ADD COLUMN longitude DECIMAL(10,7) NOT NULL;
ALTER TABLE map_feature ADD COLUMN latitude DECIMAL(9, 6) NOT NULL;
