CREATE TABLE map_feature (
  id               INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  description      VARCHAR(512) NOT NULL,
  google_place_id  VARCHAR(512) NOT NULL,
  create_timestamp DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modify_timestamp DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
commit;

alter table map_feature add column name VARCHAR(512) after id;

update map_feature set .map_feature.APPROVED=true where description in ('Fat pigeon','Swan');
commit;

update map_feature_photo set approved =  true where map_feature_id in (select id from map_feature where approved = true);

commit;
select * FROM map_feature;
update map_feature set name = description where id is not null;
CREATE TABLE map_feature_photo (
  id               INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  map_feature_id INT NOT NULL,
  mediumIdentifier MEDIUMBLOB,
  create_timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (map_feature_id) REFERENCES map_feature(id)
);
SELECT VERSION();

delete from map_feature where approved = false;
delete from map_feature where id >= 113;

SELECT * FROM map_feature;

update map_feature set name='wrony' where id= 104;
commit;
SELECT * FROM map_feature_photo;

delete from map_feature_photo where map_feature_id = 92;
delete from map_feature where id = 92;
update map_feature set category = 'mammals' where id = 93;
commit;

delete from map_feature where id =83;

alter table map_feature_photo drop column category;
alter table map_feature_photo MODIFY thumbnail_identifier varchar(64) NOT NULL;


ALTER TABLE map_feature ADD COLUMN category enum('mythical', 'fish', 'birds', 'amphibians', 'mammals', 'reptiles', 'insects', 'arachnids');

select * From map_feature;

delete from map_feature where id in (88,89,91);

update map_feature set category = 'fish' where id in (87);

SELECT * FROM map_feature_photo;

update map_feature_photo set thumbnail_identifier  = medium_identifier where id is not null;

alter table map_feature_photo modify thumbnail_identifier varchar(512) not null;

ALTER TABLE MAP_FEATURE_PHOTOS ADD COLUMN create_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

commit;

drop table map_feature_photo;

alter table map_feature_photo add COLUMN blob_name VARCHAR(512);

ALTER TABLE map_feature ADD COLUMN longitude DECIMAL(10,7) NOT NULL;
ALTER TABLE map_feature ADD COLUMN latitude DECIMAL(9, 6) NOT NULL;
