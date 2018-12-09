/*
SQLyog Community v9.63 
MySQL - 5.0.67-community-nt : Database - cat1801001
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cat1801001` /*!40100 DEFAULT CHARACTER SET latin1 */;

/*Table structure for table `account_master` */

CREATE TABLE `account_master` (
  `id` varchar(7) NOT NULL,
  `name` varchar(255) default NULL,
  `fk_account_type_id` varchar(7) default NULL,
  `account_effect_rs` varchar(1) default NULL,
  `opening_rs` decimal(10,2) default '0.00',
  `maximum_rs` decimal(10,2) default '0.00',
  `minimum_rs` decimal(10,2) default '0.00',
  `lock_date` date default NULL,
  `mobile_no1` varchar(17) default NULL,
  `phone_no1` varchar(17) default NULL,
  `fax_no` varchar(17) default NULL,
  `email_id` varchar(100) default NULL,
  `address1` varchar(255) default NULL,
  `address2` varchar(255) default NULL,
  `contact_prsn` varchar(100) default NULL,
  `refby` varchar(100) default NULL,
  `shortname` varchar(5) default NULL,
  `gst_no` varchar(20) default NULL,
  `pan_no` varchar(10) default NULL,
  `err_cd` decimal(1,0) default NULL,
  `edit_no` decimal(3,0) NOT NULL default '0',
  `user_cd` decimal(3,0) default NULL,
  `time_stamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `account_master` */

insert  into `account_master`(`id`,`name`,`fk_account_type_id`,`account_effect_rs`,`opening_rs`,`maximum_rs`,`minimum_rs`,`lock_date`,`mobile_no1`,`phone_no1`,`fax_no`,`email_id`,`address1`,`address2`,`contact_prsn`,`refby`,`shortname`,`gst_no`,`pan_no`,`err_cd`,`edit_no`,`user_cd`,`time_stamp`) values ('AM00001','DSF','AT00001','0','0.00','0.00','0.00','2018-11-20','','','','','','','','','','','',NULL,'0','1','2018-11-20 23:23:55');

/*Table structure for table `account_type` */

CREATE TABLE `account_type` (
  `id` varchar(7) NOT NULL,
  `name` varchar(50) default NULL,
  `head` varchar(5) default NULL,
  `head_grp` varchar(7) default NULL,
  `acc_eff` int(5) default NULL,
  `side` int(5) default '2',
  `edit_no` decimal(3,0) NOT NULL default '0',
  `user_cd` decimal(3,0) default NULL,
  `time_stamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `account_type` */

insert  into `account_type`(`id`,`name`,`head`,`head_grp`,`acc_eff`,`side`,`edit_no`,`user_cd`,`time_stamp`) values ('AT00001','123','1','0',1,2,'2','1','2018-11-18 00:12:33');
insert  into `account_type`(`id`,`name`,`head`,`head_grp`,`acc_eff`,`side`,`edit_no`,`user_cd`,`time_stamp`) values ('AT00002','456','1','0',1,2,'2','1','2018-11-18 00:12:40');
insert  into `account_type`(`id`,`name`,`head`,`head_grp`,`acc_eff`,`side`,`edit_no`,`user_cd`,`time_stamp`) values ('AT00003','789','1','0',0,2,'2','1','2018-11-18 12:00:44');
insert  into `account_type`(`id`,`name`,`head`,`head_grp`,`acc_eff`,`side`,`edit_no`,`user_cd`,`time_stamp`) values ('AT00004','ER','1','0',0,2,'0','1','2018-11-20 23:23:46');

/*Table structure for table `bank_master` */

CREATE TABLE `bank_master` (
  `id` varchar(7) NOT NULL,
  `name` varchar(200) default NULL,
  `edit_no` decimal(3,0) NOT NULL default '0',
  `user_cd` decimal(3,0) default NULL,
  `time_stamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bank_master` */

insert  into `bank_master`(`id`,`name`,`edit_no`,`user_cd`,`time_stamp`) values ('BM00001','STATE BANK OF INDIA','0','1','2018-11-20 23:57:47');

/*Table structure for table `cheque_print` */

CREATE TABLE `cheque_print` (
  `id` varchar(7) NOT NULL,
  `party_name` varchar(255) default NULL,
  `cheque_date` date default NULL,
  `amount` decimal(10,3) default '0.000',
  `cheque_no` varchar(10) default NULL,
  `fk_bank_id` varchar(7) default NULL,
  `ac_pay` int(3) default NULL,
  `edit_no` decimal(3,0) NOT NULL default '0',
  `user_cd` decimal(3,0) default NULL,
  `time_stamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `cheque_print` */

/*Table structure for table `company_master` */

CREATE TABLE `company_master` (
  `id` varchar(7) NOT NULL,
  `company_code` varchar(2) default NULL,
  `branch_code` varchar(3) default NULL,
  `company_name` varchar(50) NOT NULL,
  `ac_year` varchar(4) NOT NULL,
  `mnth` varchar(2) NOT NULL,
  `sh_name` varchar(2) default NULL,
  `digit` decimal(1,0) NOT NULL,
  `invoice_type` decimal(1,0) default NULL,
  `image_path` varchar(255) default '',
  `add1` varchar(500) default '',
  `add2` varchar(500) default '',
  `corraddress1` varchar(500) default NULL,
  `corraddress2` varchar(500) default NULL,
  `mob_no` varchar(17) default '',
  `phone_no` varchar(17) default '',
  `licence_no` varchar(30) default '',
  `email` varchar(70) default '',
  `fax_no` varchar(17) default '',
  `pan_no` varchar(20) default '',
  `tin_no` varchar(20) default '',
  `cst_no` varchar(20) default '',
  `tax_no` varchar(20) default '',
  `bank_name` varchar(100) default '',
  `ac_no` varchar(20) default '',
  `branch_name` varchar(100) default '',
  `cash_ac_cd` varchar(7) default '',
  `lab_inc_ac` varchar(7) default '',
  `lab_exp_ac` varchar(7) default '',
  `sale_ac` varchar(7) default '',
  `purchase_ac` varchar(7) default '',
  `mypwd` varchar(10) default NULL,
  `contact_person` varchar(200) default '',
  `website` varchar(255) default '',
  `delete_pwd` varchar(30) default NULL,
  `ifsc_code` varchar(20) default NULL,
  `edit_no` decimal(3,0) NOT NULL default '0',
  `user_cd` decimal(3,0) NOT NULL,
  `time_stamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `company_master` */

insert  into `company_master`(`id`,`company_code`,`branch_code`,`company_name`,`ac_year`,`mnth`,`sh_name`,`digit`,`invoice_type`,`image_path`,`add1`,`add2`,`corraddress1`,`corraddress2`,`mob_no`,`phone_no`,`licence_no`,`email`,`fax_no`,`pan_no`,`tin_no`,`cst_no`,`tax_no`,`bank_name`,`ac_no`,`branch_name`,`cash_ac_cd`,`lab_inc_ac`,`lab_exp_ac`,`sale_ac`,`purchase_ac`,`mypwd`,`contact_person`,`website`,`delete_pwd`,`ifsc_code`,`edit_no`,`user_cd`,`time_stamp`) values ('C000001','01','001','SPECIFIC ELECTRONIC1','2017','01','SE','2','0','','B/51, Electronics Zone, G.I.D.C.,','Sector-25, Gandhinagar - 382025. dsdf sfdfsdf sdfsdf','B/51, Electronics Zone, G.I.D.C., rere','Sector-25, Gandhinagar - 382025. dsdf sfdfsdf sdfsdf','990-404-5566','7383149333','','sales@specificelectronics.com','','ACNFS5720N','24060305663ABC','24560305663123','','PUNJAB NATIONAL BANK','30425911901','BAPUNAGAR BR.','A000001','','','','','','Harsh Patel','http://www.specificelectronics.com','123','SBI29051693','0','1','2016-01-10 22:04:45');

/*Table structure for table `email_config` */

CREATE TABLE `email_config` (
  `id` int(1) NOT NULL default '1',
  `email` varchar(100) default NULL,
  `host` varchar(100) default NULL,
  `port` varchar(5) default NULL,
  `auth` varchar(5) default NULL,
  `smtp` varchar(50) default NULL,
  `protocol` varchar(100) default NULL,
  `socketFactoryPort` varchar(5) default NULL,
  `socketFactoryClass` varchar(255) default NULL,
  `socketFactoryFallback` varchar(5) default NULL,
  `debug` varchar(5) default NULL,
  `quitwait` varchar(255) default NULL,
  `userName` varchar(100) default NULL,
  `pwd` varchar(64) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `email_config` */

insert  into `email_config`(`id`,`email`,`host`,`port`,`auth`,`smtp`,`protocol`,`socketFactoryPort`,`socketFactoryClass`,`socketFactoryFallback`,`debug`,`quitwait`,`userName`,`pwd`) values (1,'shrikainfotech@mgail.com','gmail.',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `form_master` */

CREATE TABLE `form_master` (
  `id` decimal(5,0) NOT NULL,
  `name` varchar(255) default NULL,
  `fk_menu_id` decimal(5,0) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `form_master` */

insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('1','ACCOUNT TYPE','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('2','ACCOUNT MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('3','MAIN CATEGORY','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('4','SUB CATEGORY','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('5','ROW MATERIAL','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('6','MAIN CATEGORY','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('7','FOOD TYPE','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('8','FINISH MATERIAL','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('9','TIME MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('10','UNIT MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('11','OCCASION MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('12','MENU TYPE MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('13','MENU STEP MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('14','THEME MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('15','BANK MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('16','TAX MASTER','1');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('61','MAIN ORDER','2');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('62','ADD ORDER','2');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('63','ADD MULTIPLE ORDER','2');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('64','ORDER LIST','2');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('101','CHECK PRINT REPORT','3');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('102','ACCOUNT LIST','3');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('211','COMPANY SETTING','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('212','MANAGE USER','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('213','USER RIGHTS','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('214','MANAGE EMAIL','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('215','CHANGE PASSWORD','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('216','QUICK OPEN','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('217','BACK UP','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('218','RESET','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('219','EMAIL','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('220','CHECK PRINT','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('221','NEW YEAR','4');
insert  into `form_master`(`id`,`name`,`fk_menu_id`) values ('222','CHANGE THEMES','4');

/*Table structure for table `manage_email` */

CREATE TABLE `manage_email` (
  `manage_id` bigint(10) NOT NULL auto_increment,
  `manage_email` varchar(70) default NULL,
  `manage_pwd` varchar(70) default NULL,
  `manage_port` varchar(5) default NULL,
  `manage_host` longtext,
  `manage_mobno` varchar(20) default NULL,
  PRIMARY KEY  (`manage_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `manage_email` */

insert  into `manage_email`(`manage_id`,`manage_email`,`manage_pwd`,`manage_port`,`manage_host`,`manage_mobno`) values (1,'xyz11','xyz','587','smtp.mail.yahoo.com','7405116442');

/*Table structure for table `menu_master` */

CREATE TABLE `menu_master` (
  `id` decimal(5,0) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `menu_master` */

insert  into `menu_master`(`id`,`name`) values ('1','MASTER');
insert  into `menu_master`(`id`,`name`) values ('2','MENU ORDER');
insert  into `menu_master`(`id`,`name`) values ('3','REPORTS');
insert  into `menu_master`(`id`,`name`) values ('4','UTILITY');

/*Table structure for table `tax_master` */

CREATE TABLE `tax_master` (
  `id` varchar(7) NOT NULL,
  `name` varchar(50) default NULL,
  `tax` decimal(10,3) default NULL,
  `edit_no` decimal(3,0) NOT NULL default '0',
  `user_cd` decimal(3,0) default NULL,
  `time_stamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `tax_ac_cd` varchar(7) default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `tax_master` */

insert  into `tax_master`(`id`,`name`,`tax`,`edit_no`,`user_cd`,`time_stamp`,`tax_ac_cd`) values ('TAX0001','NO-TAX','0.000','0','1','2018-11-20 23:59:58','');

/*Table structure for table `unit_master` */

CREATE TABLE `unit_master` (
  `id` int(10) NOT NULL,
  `name` varchar(50) default NULL,
  `symbol` varchar(30) default NULL,
  `lower_cd` int(2) default '0',
  `upper_cd` int(2) default '0',
  `edit_no` decimal(3,0) NOT NULL default '0',
  `user_cd` decimal(3,0) default NULL,
  `time_stamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `unit_master` */

insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (1,'KILO','KG',2,0,'1','1','2016-02-09 19:09:39');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (2,'GRAM','GM',0,1,'0','1','2016-02-09 19:09:05');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (3,'LITER','LT',4,0,'0','1','2016-02-09 19:09:12');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (4,'MILILITER','ML',0,3,'0','1','2016-02-09 19:09:56');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (5,'NOS.','NOS.',0,0,'2','1','2016-03-06 16:55:07');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (6,'NONE','NONE',0,0,'0','1','2016-02-09 19:10:07');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (7,'GLASSE(S)','GL',0,0,'0','1','2016-02-09 19:10:14');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (8,'NO(S)','NO(S)',0,0,'0','1','2016-02-09 19:10:19');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (9,'PKT(S)','PKT(S)',0,0,'0','1','2016-02-09 19:10:26');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (10,'PCS(S)','PCS(S)',0,0,'0','1','2016-02-09 19:10:31');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (11,'PLT(S)','PLT(S)',0,0,'0','1','2016-02-09 19:15:33');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (12,'DOZEN(S)','DOZEN(S)',0,0,'0','1','2016-02-09 19:15:39');
insert  into `unit_master`(`id`,`name`,`symbol`,`lower_cd`,`upper_cd`,`edit_no`,`user_cd`,`time_stamp`) values (13,'MTR(S)','MTR(S)',0,0,'3','1','2018-11-18 12:02:06');

/*Table structure for table `user_master` */

CREATE TABLE `user_master` (
  `id` int(10) NOT NULL auto_increment,
  `username` varchar(20) default NULL,
  `password` varchar(20) default NULL,
  `user_photo` blob,
  `isactive` smallint(2) default '1',
  `edit_no` smallint(2) default '0',
  `modifiedby` int(5) default NULL,
  `time_stamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `user_master` */

insert  into `user_master`(`id`,`username`,`password`,`user_photo`,`isactive`,`edit_no`,`modifiedby`,`time_stamp`) values (1,'admin','',NULL,1,0,NULL,'2016-02-08 18:40:35');

/*Table structure for table `user_rights` */

CREATE TABLE `user_rights` (
  `user_cd` decimal(5,0) NOT NULL,
  `form_cd` decimal(5,0) NOT NULL,
  `views` decimal(1,0) default '0',
  `edit` decimal(1,0) default '0',
  `adds` decimal(1,0) default '0',
  `deletes` decimal(1,0) default '0',
  `print` decimal(1,0) default '0',
  `navigate_view` decimal(1,0) default '0',
  PRIMARY KEY  (`form_cd`,`user_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user_rights` */

insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','1','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','2','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','3','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','4','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','5','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','6','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','7','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','8','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','9','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','10','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','11','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','12','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','13','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','14','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','15','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','16','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','61','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','62','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','63','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','64','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','101','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','102','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','211','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','212','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','213','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','214','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','215','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','216','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','217','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','218','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','219','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','220','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','221','1','1','1','1','1','1');
insert  into `user_rights`(`user_cd`,`form_cd`,`views`,`edit`,`adds`,`deletes`,`print`,`navigate_view`) values ('1','222','1','1','1','1','1','1');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
