DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` VARCHAR(64) NOT NULL,
  `first_name` VARCHAR(128) NULL,
  `last_name` VARCHAR(128) NULL,
  `email` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` VARCHAR(64) NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `email_idx` (`email`),
  UNIQUE INDEX `username_idx` (`username`)
);

INSERT INTO users(username, first_name, last_name, email, password, create_date, create_by, edit_date, edit_by)
VALUES('kolexandr', 'Oleksandr', 'Kucher', 'kolexandr.13@gmail.com', 'pass', NOW(), 'kolexandr', NOW(), 'kolexandr');


DROP TABLE IF EXISTS `recognition_data_type`;
CREATE TABLE `recognition_data_type` (
  `code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`code`),
  UNIQUE INDEX `code_idx` (`code` ASC)
);

INSERT INTO recognition_data_type(code, name, description)
VALUES('HMF', 'Human Face', 'Human Face Recognition images');


DROP TABLE IF EXISTS `recognition_data_class`;
CREATE TABLE `recognition_data_class` (
  `code` VARCHAR(32) NOT NULL,
  `type_code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` VARCHAR(64) NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` VARCHAR(64)NOT NULL,
  `for_recognition` BIT NOT NULL,
  PRIMARY KEY (`code`),
  UNIQUE INDEX `code_idx` (`code`),
  INDEX `type_code_idx` (`type_code`)
);

CREATE TEMPORARY TABLE tmp(class VARCHAR(32), type VARCHAR(32), name VARCHAR(128), description VARCHAR(255));
LOAD DATA INFILE 'c:/import_data/classes.csv' INTO TABLE tmp
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';

INSERT INTO recognition_data_class(code, type_code, name, description, create_date, create_by, edit_date, edit_by, for_recognition)
SELECT class, type, name, description, NOW(), 'kolexandr', NOW(), 'kolexandr', 1 FROM tmp;

INSERT INTO recognition_data_class(code, type_code, name, description, create_date, create_by, edit_date, edit_by, for_recognition)
VALUES
  ('CRPD', 'HMF', 'CROPPED', 'Cropped images for human face recognition', NOW(), 'kolexandr', NOW(), 'kolexandr', 0),
  ('UPLD', 'HMF', 'UPLOADED', 'Uploaded images for human face recognition', NOW(), 'kolexandr', NOW(), 'kolexandr', 0),
  ('PCA', 'HMF', 'Eigen Face', 'Components produced by PCA algorithm', NOW(), 'kolexandr', NOW(), 'kolexandr', 0),
  ('LDA', 'HMF', 'Fisher Face', 'Components produced by LDA algorithm', NOW(), 'kolexandr', NOW(), 'kolexandr', 0),
  ('NBC', 'HMF', 'Bayes Face', 'Components produced by Naive Bayes Classifier', NOW(), 'kolexandr', NOW(), 'kolexandr', 0),
  ('LPP', 'HMF', 'Laplacian Face', 'Components produced by LPP classifier', NOW(), 'kolexandr', NOW(), 'kolexandr', 0);

DROP TEMPORARY TABLE tmp;