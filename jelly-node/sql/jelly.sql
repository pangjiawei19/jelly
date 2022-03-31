# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.26)
# Database: jelly
# Generation Time: 2019-07-09 07:10:09 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table array
# ------------------------------------------------------------

DROP TABLE IF EXISTS `array`;

CREATE TABLE `array` (
  `id` bigint(30) NOT NULL AUTO_INCREMENT,
  `array` varchar(150) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table level
# ------------------------------------------------------------

DROP TABLE IF EXISTS `level`;

CREATE TABLE `level` (
  `level` tinyint(4) NOT NULL,
  `array` varchar(150) NOT NULL,
  PRIMARY KEY (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;

INSERT INTO `level` (`level`, `array`)
VALUES
	(1,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(2,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(3,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(4,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(5,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(6,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(7,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(8,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(9,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H'),
	(10,'8,8,B,H,H,B,S,H,H,H,8,H,S,H,H,H,B,B,B,8,S,H,H,S,H,H,H,H,8,B,H,S,B,S,S,S,B,8,H,H,H,H,B,H,H,H,8,S,H,H,H,B,B,S,B,8,H,H,S,B,H,H,S,H,8,S,H,H,H,H,H,B,H');

/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
