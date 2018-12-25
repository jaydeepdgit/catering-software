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