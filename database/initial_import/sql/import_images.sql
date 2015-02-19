DROP TABLE IF EXISTS recognition_data;
CREATE TABLE `hfr`.`recognition_data` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `class_code` VARCHAR(32) NOT NULL,
  `image_format` VARCHAR(32) NOT NULL,
  `image_width` INT NOT NULL,
  `image_height` INT NOT NULL,
  `image_size` INT NOT NULL,
  `image_content` LONGTEXT NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` VARCHAR(128) NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` VARCHAR(128) NOT NULL,
  `parent_image_id` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `parent_idx` (`parent_image_id`),
  INDEX `class_idx` (`class_code`)
);

CREATE TEMPORARY TABLE tmp(class VARCHAR(32), format VARCHAR(10), width INT, height INT, size INT, content LONGTEXT);
LOAD DATA INFILE 'c:/import_data/images.csv' INTO TABLE tmp
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';

INSERT INTO recognition_data(class_code, image_format, image_width, image_height, image_size,
                             image_content, create_date, create_by, edit_date, edit_by)
SELECT t.class, t.format, t.width, t.height, t.size, t.content, NOW(), 'kolexandr', NOW(), 'kolexandr' FROM tmp t;

DROP TEMPORARY TABLE tmp;