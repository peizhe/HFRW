DROP TABLE IF EXISTS users;
CREATE TABLE `hfr`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(128) NOT NULL,
  `first_name` VARCHAR(128) NULL,
  `last_name` VARCHAR(128) NULL,
  `email` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` INT NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_idx` (`id`),
  UNIQUE INDEX `email_idx` (`email`),
  UNIQUE INDEX `username_idx` (`username`)
);

INSERT INTO users(username, first_name, last_name, email, password, create_date, create_by, edit_date, edit_by)
VALUES('kolexandr', 'Oleksandr', 'Kucher', 'kolexandr.13@gmail.com', 'pass', NOW(), 1, NOW(), 1);


DROP TABLE IF EXISTS recognition_data_type;
CREATE TABLE `hfr`.`recognition_data_type` (
  `code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`code`),
  UNIQUE INDEX `code_idx` (`code` ASC)
);

INSERT INTO recognition_data_type(code, name, description)
VALUES('HMF', 'Human Face', 'Human Face Recognition images');


DROP TABLE IF EXISTS recognition_data_class;
CREATE TABLE `hfr`.`recognition_data_class` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(32) NOT NULL,
  `type_code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` INT NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_idx` (`id`),
  UNIQUE INDEX `code_idx` (`code`),
  INDEX `type_code_idx` (`type_code`)
);

CREATE TEMPORARY TABLE tmp(class VARCHAR(32), type VARCHAR(32), name VARCHAR(128), description VARCHAR(255));
LOAD DATA INFILE 'c:/import_data/classes.csv' INTO TABLE tmp
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';

INSERT INTO recognition_data_class(code, type_code, name, description, create_date, create_by, edit_date, edit_by)
SELECT class, type, name, description, NOW(), 1, NOW(), 1 FROM tmp;

INSERT INTO recognition_data_class(code, type_code, name, description, create_date, create_by, edit_date, edit_by)
VALUES
  ('CRPD', 'HMF', 'CROPPED', 'Cropped images for human face recognition', NOW(), 1, NOW(), 1),
  ('UPLD', 'HMF', 'UPLOADED', 'Uploaded images for human face recognition', NOW(), 1, NOW(), 1),
  ('PCA', 'HMF', 'EigenFace', 'Components produced by PCA algorithm', NOW(), 1, NOW(), 1),
  ('LDA', 'HMF', 'FisherFace', 'Components produced by LDA algorithm', NOW(), 1, NOW(), 1),
  ('NBC', 'HMF', 'BayesFace', 'Components produced by Naive Bayes Classifier', NOW(), 1, NOW(), 1);

DROP TEMPORARY TABLE tmp;