DROP TABLE IF EXISTS recognition_data;
CREATE TABLE `hfr`.`recognition_data` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `class` VARCHAR(32) NOT NULL,
  `format` VARCHAR(32) NOT NULL,
  `width` INT NOT NULL,
  `height` INT NOT NULL,
  `size` INT NOT NULL,
  `content` BLOB NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` INT NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)
);

DROP TABLE IF EXISTS recognition_data_class;
CREATE TABLE `hfr`.`recognition_data_class` (
  `code` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `type`  VARCHAR(32) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`code`),
  UNIQUE INDEX `id_UNIQUE` (`code` ASC),
  INDEX `type_code_idx` (`type_code` ASC)
);

DROP TABLE IF EXISTS recognition_data_type;
CREATE TABLE `hfr`.`recognition_data_type` (
  `code` VARCHAR(32) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`code`),
  UNIQUE INDEX `id_UNIQUE` (`code` ASC)
);