DROP TABLE IF EXISTS users;
CREATE TABLE `hfr`.`users` (
  `username` VARCHAR(128) NOT NULL,
  `first_name` VARCHAR(128) NULL,
  `last_name` VARCHAR(128) NULL,
  `email` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` VARCHAR(128) NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `id_UNIQUE` (`username`),
  UNIQUE INDEX `email_idx` (`email`)
);

INSERT INTO users(username, first_name, last_name, email, password, create_date, create_by, edit_date, edit_by)
VALUES('kolexandr', 'Oleksandr', 'Kucher', 'kolexandr.13@gmail.com', 'pass', NOW(), 'kolexandr', NOW(), 'kolexandr');


DROP TABLE IF EXISTS recognition_data_type;
CREATE TABLE `hfr`.`recognition_data_type` (
  `type_code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`type_code`),
  UNIQUE INDEX `id_UNIQUE` (`type_code` ASC)
);

INSERT INTO recognition_data_type(type_code, name, description)
VALUES('HMF', 'Human Face', 'Human Face Recognition images');


DROP TABLE IF EXISTS recognition_data_class;
CREATE TABLE `hfr`.`recognition_data_class` (
  `class_code` VARCHAR(32) NOT NULL,
  `type_code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` VARCHAR(128) NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`class_code`),
  UNIQUE INDEX `id_UNIQUE` (`class_code` ASC),
  INDEX `type_code_idx` (`type_code` ASC)
);

CREATE TEMPORARY TABLE tmp(class VARCHAR(32), type VARCHAR(32), name VARCHAR(128), description VARCHAR(255));
LOAD DATA INFILE 'c:/import_data/classes.csv' INTO TABLE tmp
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';

INSERT INTO recognition_data_class(class_code, type_code, name, description, create_date, create_by, edit_date, edit_by)
SELECT class, type, name, description, NOW(), 'kolexandr', NOW(), 'kolexandr' FROM tmp;

INSERT INTO recognition_data_class(class_code, type_code, name, description, create_date, create_by, edit_date, edit_by)
VALUES
  ('CRPD', 'HMF', 'CROPPED', 'Cropped images for human face recognition', NOW(), 'kolexandr', NOW(), 'kolexandr'),
  ('UPLD', 'HMF', 'UPLOADED', 'Uploaded images for human face recognition', NOW(), 'kolexandr', NOW(), 'kolexandr');

DROP TEMPORARY TABLE tmp;