DROP TABLE IF EXISTS `recognition_data`;
CREATE TABLE `recognition_data` (
  `id` VARCHAR(32) NOT NULL,
  `class_code` VARCHAR(32) NOT NULL,
  `image_format` VARCHAR(32) NOT NULL,
  `image_width` INT NOT NULL,
  `image_height` INT NOT NULL,
  `image_size` INT NOT NULL,
  `image_content` LONGTEXT NOT NULL,
  `create_date` DATETIME NOT NULL,
  `create_by` VARCHAR(64) NOT NULL,
  `edit_date` DATETIME NOT NULL,
  `edit_by` VARCHAR(64) NOT NULL,
  `parent_image_id` VARCHAR(32) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_idx` (`id`),
  INDEX `class_idx` (`class_code`),
  INDEX `parent_idx` (`parent_image_id`)
);