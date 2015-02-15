DROP TABLE IF EXISTS recognition_data_class;
CREATE TABLE `hfr`.`recognition_data_class` (
  `class_code` VARCHAR(32) NOT NULL,
  `type_code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`class_code`),
  UNIQUE INDEX `id_UNIQUE` (`class_code` ASC),
  INDEX `type_code_idx` (`type_code` ASC)
);

DROP TABLE IF EXISTS recognition_data_type;
CREATE TABLE `hfr`.`recognition_data_type` (
  `type_code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`type_code`),
  UNIQUE INDEX `id_UNIQUE` (`type_code` ASC)
);

DROP TABLE IF EXISTS users;
CREATE TABLE `hfr`.`users` (
  `username` VARCHAR(128) NOT NULL,
  `first_name` VARCHAR(128) NULL,
  `last_name` VARCHAR(128) NULL,
  `email` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `id_UNIQUE` (`username`),
  UNIQUE INDEX `email_idx` (`email`)
);

LOAD DATA INFILE 'c:/import_data/classes.csv' INTO TABLE recognition_data_class
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';