-- Added by Ramesh
-- changed id from int to varchar as per structure.
ALTER TABLE `cat1801001`.`food_type` CHANGE `id` `id` VARCHAR(11) NOT NULL;

-- Added by Rusi
-- changed id from int to varchar as per structure in raw main category and sub category.
ALTER TABLE `raw_material_main` CHANGE `id` `id` VARCHAR(7) NOT NULL;
ALTER TABLE `raw_material_sub` CHANGE `id` `id` VARCHAR(7) NOT NULL;
ALTER TABLE `raw_material_sub` CHANGE `fk_raw_material_main_id` `fk_raw_material_main_id` VARCHAR(7) NOT NULL;

-- Added by Rusi
-- changed id from int to varchar as per structure in raw material master.
ALTER TABLE `raw_material` CHANGE `id` `id` VARCHAR(7) NOT NULL, 
CHANGE `fk_raw_material_main_id` `fk_raw_material_main_id` VARCHAR(7) NULL, 
CHANGE `fk_raw_material_sub_id` `fk_raw_material_sub_id` VARCHAR(7) NULL;

-- Added by Rusi
-- Function master table created.
CREATE TABLE `function_master` 
( `id` VARCHAR(7) NOT NULL, `name_en` VARCHAR(50), `name_gu` VARCHAR(50), `name_hi` VARCHAR(50), 
`fk_status_id` TINYINT(4), `edit_no` DECIMAL(3,0) NOT NULL DEFAULT 0, 
`user_cd` DECIMAL(3,0), `time_stamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
PRIMARY KEY (`id`) ) 
ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_unicode_ci;
-- 1 row added in form_master
INSERT INTO `form_master` (`id`, `name`, `fk_menu_id`) VALUES ('15', 'FUNCTION MASTER', '1');