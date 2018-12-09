/*
SQLyog Community v9.63 
MySQL - 5.0.67-community-nt : Database - catering_main
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`catering_main` /*!40100 DEFAULT CHARACTER SET latin1 */;

/*Table structure for table `appconfig` */

CREATE TABLE `appconfig` (
  `client_id` varchar(20) collate utf8_unicode_ci default NULL,
  `issue_date` date default NULL,
  `act_date` date default NULL,
  `due_date` date default NULL,
  `act_cd` varchar(20) collate utf8_unicode_ci default NULL,
  `license_no` varchar(20) collate utf8_unicode_ci default NULL,
  `email` varchar(50) collate utf8_unicode_ci default NULL,
  `server` varchar(100) collate utf8_unicode_ci default NULL,
  `act1` varchar(4) collate utf8_unicode_ci default '',
  `act2` varchar(4) collate utf8_unicode_ci default '',
  `act3` varchar(4) collate utf8_unicode_ci default '',
  `act4` varchar(4) collate utf8_unicode_ci default '',
  `user_nm` varchar(50) collate utf8_unicode_ci default NULL,
  `pass` varchar(20) collate utf8_unicode_ci default NULL,
  `status` int(11) default NULL,
  `months` int(11) default NULL,
  `days` int(11) default NULL,
  `haddr` varchar(20) collate utf8_unicode_ci default NULL,
  `iaddr` varchar(40) collate utf8_unicode_ci default NULL,
  `laststart` date default NULL,
  `def` varchar(20) collate utf8_unicode_ci default '',
  `app_limit` smallint(6) default '0',
  `cur_limit` smallint(6) default '0',
  `tmode` smallint(6) default '0',
  `proj_mode` smallint(6) default '0',
  `mlt_mac` smallint(6) default '0',
  `mac_lmt` int(10) default '1',
  `is_tax` smallint(6) default NULL,
  `theme_cd` int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `appconfig` */

insert  into `appconfig`(`client_id`,`issue_date`,`act_date`,`due_date`,`act_cd`,`license_no`,`email`,`server`,`act1`,`act2`,`act3`,`act4`,`user_nm`,`pass`,`status`,`months`,`days`,`haddr`,`iaddr`,`laststart`,`def`,`app_limit`,`cur_limit`,`tmode`,`proj_mode`,`mlt_mac`,`mac_lmt`,`is_tax`,`theme_cd`) values ('STOCK MANAGEMENT SOF','2017-01-26','2017-01-26','2019-05-10',NULL,'SMS-000001-15-06',NULL,NULL,'0000','1111','2222','3333','LG','LG123',-1,0,0,'90-4C-E5-07-79-87','192.168.0.108','2018-11-20','',0,0,0,1,0,1,0,4);

/*Table structure for table `change_themes` */

CREATE TABLE `change_themes` (
  `id` int(11) NOT NULL auto_increment,
  `theme_name` varchar(255) default NULL,
  `theme_link` varchar(500) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

/*Data for the table `change_themes` */

insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (1,'TextureLookAndFeel ','com.jtattoo.plaf.texture.TextureLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (2,'SmartLookAndFeel','com.jtattoo.plaf.smart.SmartLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (3,'NoireLookAndFeel','com.jtattoo.plaf.noire.NoireLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (4,'AcrylLookAndFeel','com.jtattoo.plaf.acryl.AcrylLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (5,'AeroLookAndFeel','com.jtattoo.plaf.aero.AeroLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (6,'AluminiumLookAndFeel','com.jtattoo.plaf.aluminium.AluminiumLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (7,'BernsteinLookAndFeel','com.jtattoo.plaf.bernstein.BernsteinLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (8,'FastLookAndFeel','com.jtattoo.plaf.fast.FastLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (9,'GraphiteLookAndFeel','com.jtattoo.plaf.graphite.GraphiteLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (10,'HiFiLookAndFeel','com.jtattoo.plaf.hifi.HiFiLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (11,'LunaLookAndFeel','com.jtattoo.plaf.luna.LunaLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (12,'McWinLookAndFeel ','com.jtattoo.plaf.mcwin.McWinLookAndFeel');
insert  into `change_themes`(`id`,`theme_name`,`theme_link`) values (13,'MintLookAndFeel','com.jtattoo.plaf.mint.MintLookAndFeel');

/*Table structure for table `db_master` */

CREATE TABLE `db_master` (
  `company_name` varchar(100) collate utf8_unicode_ci NOT NULL,
  `db_year` varchar(4) collate utf8_unicode_ci NOT NULL,
  `db_name` varchar(30) collate utf8_unicode_ci NOT NULL,
  `dbmonth` smallint(6) default '0',
  `dbname` varchar(30) collate utf8_unicode_ci default '',
  `dbyear` varchar(4) collate utf8_unicode_ci default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `db_master` */

insert  into `db_master`(`company_name`,`db_year`,`db_name`,`dbmonth`,`dbname`,`dbyear`) values ('Catering Management','2018','cat1801001',5,'cat1801001','2018');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
