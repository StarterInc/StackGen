ALTER TABLE `BigDecimal`.`resource` 
ADD COLUMN `thumbnail_id` INT(11) NULL AFTER `location_id`;



UPDATE `BigDecimal`.`role` SET `system_role`='1' WHERE `id`='2';
UPDATE `BigDecimal`.`role` SET `system_role`='1' WHERE `id`='1';


ALTER TABLE `BigDecimal`.`resource` 
CHANGE COLUMN `notes` `notes` VARCHAR(255) NULL DEFAULT NULL ;


ALTER TABLE `BigDecimal`.`location` 
CHANGE COLUMN `name` `name` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `address_1` `address_1` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `address_2` `address_2` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `city` `city` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `state` `state` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `email` `email` VARCHAR(255) NULL DEFAULT NULL ;


ALTER TABLE `BigDecimal`.`location` 
CHANGE COLUMN `longitude` `longitude` DOUBLE NULL DEFAULT NULL ,
CHANGE COLUMN `latitude` `latitude` DOUBLE NULL DEFAULT NULL ;

CREATE TABLE `BigDecimal`.`location_category_idx` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `location_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `BigDecimal`.`location` 
ADD COLUMN `country` VARCHAR(45) NULL AFTER `postal_code`;

CREATE TABLE `content_resource_idx` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `content_id` INT NOT NULL,
  `resource_id` INT NOT NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `BigDecimal`.`content_location_idx` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `content_id` INT NOT NULL,
  `location_id` INT NOT NULL,
  PRIMARY KEY (`id`));

  
  CREATE TABLE `BigDecimal`.`category_resource_idx` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `category_id` INT NOT NULL,
  `resource_id` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));
