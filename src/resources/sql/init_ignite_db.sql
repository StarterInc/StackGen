CREATE DATABASE  IF NOT EXISTS `ignite` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ignite`;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acl`
--

DROP TABLE IF EXISTS `acl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `target_id` int(11) NOT NULL COMMENT 'foreign key id',
  `target_type` int(11) NOT NULL,
  `permission` int(11) NOT NULL,
  `principle_id` int(11) NOT NULL,
  `principle_type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq` (`target_id`,`target_type`,`permission`,`principle_id`,`principle_type`),
  KEY `perm` (`permission`) USING BTREE,
  KEY `target` (`target_id`) USING BTREE,
  KEY `principle_type` (`principle_type`) USING BTREE,
  KEY `acl_ibfk_3` (`principle_id`) USING BTREE,
  KEY `target_type` (`target_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11714 DEFAULT CHARSET=latin1 COMMENT='access control determines group sharing';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl`
--

LOCK TABLES `acl` WRITE;
/*!40000 ALTER TABLE `acl` DISABLE KEYS */;
REPLACE INTO `acl` VALUES (1293,1,0,3,2,0),(11713,1,0,3,2,1),(1333,2,0,3,2,0),(6713,2,0,10,2,0),(6714,2,5,10,2,0);
/*!40000 ALTER TABLE `acl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(100) DEFAULT NULL,
  `long_description` varchar(255) DEFAULT NULL,
  `suitability_rating` tinyint(4) DEFAULT NULL,
  `publishable` tinyint(4) DEFAULT NULL,
  `parent_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=latin1 COMMENT='basically categorizations of content';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content`
--

DROP TABLE IF EXISTS `content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `url` varchar(500) NOT NULL COMMENT 'can be remote or local content, regardless it is a URL',
  `mime_type` varchar(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `author` int(1) DEFAULT NULL COMMENT 'whether item is original content',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `repost_count` int(11) DEFAULT NULL COMMENT 'since a site share is limited to one group, cross posting me',
  `license` varchar(255) DEFAULT NULL,
  `copyright` varchar(255) DEFAULT NULL,
  `author_device` int(11) DEFAULT NULL,
  `width` decimal(10,0) DEFAULT NULL,
  `height` decimal(10,0) DEFAULT NULL,
  `post_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `flag` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `content_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1427 DEFAULT CHARSET=latin1 COMMENT='the main content items';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content`
--

LOCK TABLES `content` WRITE;
/*!40000 ALTER TABLE `content` DISABLE KEYS */;
/*!40000 ALTER TABLE `content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content_category_idx`
--

DROP TABLE IF EXISTS `content_category_idx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_category_idx` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqo` (`content_id`,`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2098 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_category_idx`
--

LOCK TABLES `content_category_idx` WRITE;
/*!40000 ALTER TABLE `content_category_idx` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_category_idx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content_rating`
--

DROP TABLE IF EXISTS `content_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_rating` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `value` decimal(10,0) DEFAULT NULL,
  `flag` int(11) DEFAULT '-1',
  `rater_id` int(11) DEFAULT NULL,
  `review` varchar(1000) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `content_id` (`content_id`),
  KEY `rater_id` (`rater_id`),
  CONSTRAINT `content_rating_ibfk_1` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`),
  CONSTRAINT `content_rating_ibfk_2` FOREIGN KEY (`rater_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30741 DEFAULT CHARSET=latin1 COMMENT='content objects in the system can have ratings';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_rating`
--

LOCK TABLES `content_rating` WRITE;
/*!40000 ALTER TABLE `content_rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `device_id` varchar(225) NOT NULL,
  `notification_type` int(10) unsigned zerofill NOT NULL,
  `flag` int(1) unsigned zerofill NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `uniqu1` (`user_id`,`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_word_gemerator`
--

DROP TABLE IF EXISTS `game_word_gemerator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_word_gemerator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_word_gemerator`
--

LOCK TABLES `game_word_gemerator` WRITE;
/*!40000 ALTER TABLE `game_word_gemerator` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_word_gemerator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `address_1` varchar(45) DEFAULT NULL,
  `address_2` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(2) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `locationcode` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_resource_idx`
--

DROP TABLE IF EXISTS `location_resource_idx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location_resource_idx` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location_id` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_resource_idx`
--

LOCK TABLES `location_resource_idx` WRITE;
/*!40000 ALTER TABLE `location_resource_idx` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_resource_idx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `remote_service`
--

DROP TABLE IF EXISTS `remote_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `remote_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(50) DEFAULT NULL,
  `auth_method` varchar(50) DEFAULT NULL,
  `auth_service_URL` varchar(255) DEFAULT NULL,
  `auth_service_params` varchar(255) DEFAULT NULL,
  `service_type` int(11) DEFAULT NULL COMMENT 'data, social, RPC etc.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='facebook, twitter ert.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `remote_service`
--

LOCK TABLES `remote_service` WRITE;
/*!40000 ALTER TABLE `remote_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `remote_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `remote_user`
--

DROP TABLE IF EXISTS `remote_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `remote_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `remote_service_id` int(11) DEFAULT NULL,
  `service_UID` varchar(255) DEFAULT NULL,
  `service_login_key` varchar(255) DEFAULT NULL,
  `login_method` int(11) DEFAULT NULL COMMENT 'this field determines type of 3rd party auth (ie: OAuth)',
  `permissions_granted` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `remote_user_ibfk_2` (`remote_service_id`),
  CONSTRAINT `remote_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `remote_user_ibfk_2` FOREIGN KEY (`remote_service_id`) REFERENCES `remote_service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='table tracks remote logins for 3rd party social networks and';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `remote_user`
--

LOCK TABLES `remote_user` WRITE;
/*!40000 ALTER TABLE `remote_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `remote_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `notes` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT 'NULL' COMMENT 'Every group has a name. Some more disparaging than others.',
  `system_role` binary(1) NOT NULL DEFAULT '0' COMMENT 'system groups',
  `owner_id` int(11) DEFAULT NULL,
  `size_max` int(11) DEFAULT NULL,
  `size_min` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=538 DEFAULT CHARSET=latin1 COMMENT='The ubiquitous role table. Boring, tired, yet invaluable.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
REPLACE INTO `role` VALUES (1,'administrator','?',0,5,1),(2,'everyone','?',0,1000000,1),(3,'starterPro','0',0,100,1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `roles_permissions`
--

DROP TABLE IF EXISTS `roles_permissions`;
/*!50001 DROP VIEW IF EXISTS `roles_permissions`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `roles_permissions` (
  `permission` tinyint NOT NULL,
  `role_name` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `seen`
--

DROP TABLE IF EXISTS `seen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seen`
--

LOCK TABLES `seen` WRITE;
/*!40000 ALTER TABLE `seen` DISABLE KEYS */;
/*!40000 ALTER TABLE `seen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syslog`
--

DROP TABLE IF EXISTS `syslog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `syslog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(200) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source_id` int(11) NOT NULL DEFAULT '0',
  `source_type` int(11) NOT NULL DEFAULT '0',
  `event_type` int(11) NOT NULL DEFAULT '0',
  `user_id` int(11) DEFAULT '-1',
  `response_code` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=337370 DEFAULT CHARSET=latin1 COMMENT='event based stats';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syslog`
--

LOCK TABLES `syslog` WRITE;
/*!40000 ALTER TABLE `syslog` DISABLE KEYS */;
REPLACE INTO `syslog` VALUES (337295,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-30 22:18:48',-1,-1,-1,-1,NULL),(337296,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-30 22:20:42',-1,-1,-1,-1,NULL),(337297,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, system, systemcheck]','2015-01-30 22:21:01',-1,-1,-1,-1,NULL),(337298,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, user, 1, password, reset]','2015-01-30 22:21:46',1,0,12,-1,NULL),(337299,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, user, -1, get]','2015-01-30 22:21:49',-1,0,10,-1,NULL),(337300,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, user, -1, get]','2015-01-30 22:21:54',-1,0,10,-1,NULL),(337301,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, -1, get]','2015-01-30 22:25:00',-1,0,10,-1,NULL),(337302,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-30 22:25:08',-1,-1,-1,-1,NULL),(337303,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, 1, password, reset]','2015-01-30 22:28:03',1,0,12,-1,NULL),(337304,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-30 22:28:08',1,0,12,-1,NULL),(337305,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-30 22:28:44',1,0,12,-1,NULL),(337306,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-30 22:29:44',1,0,12,-1,NULL),(337307,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-30 22:30:51',1,0,12,-1,NULL),(337308,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-30 22:32:35',1,0,12,-1,NULL),(337309,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-30 22:33:42',1,0,12,-1,NULL),(337310,'OK [0:0:0:0:0:0:0:1%0]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-30 22:34:37',1,0,12,-1,NULL),(337311,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 06:41:06',-1,-1,-1,-1,NULL),(337312,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 06:41:06',-1,-1,-1,-1,NULL),(337313,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 06:41:14',-1,-1,-1,-1,NULL),(337314,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 06:41:25',-1,-1,-1,-1,NULL),(337315,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 06:41:34',-1,-1,-1,-1,NULL),(337316,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 06:41:35',-1,-1,-1,-1,NULL),(337317,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 06:41:36',-1,-1,-1,-1,NULL),(337318,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-31 06:41:40',1,0,12,-1,NULL),(337319,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-31 06:44:50',1,0,12,-1,NULL),(337320,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-31 06:50:11',1,0,12,-1,NULL),(337321,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-31 06:50:13',1,0,12,-1,NULL),(337322,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-31 06:51:08',1,0,12,-1,NULL),(337323,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, user, -1, get]','2015-01-31 06:51:18',-1,0,10,-1,NULL),(337324,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, user, 1, get]','2015-01-31 06:51:33',1,0,12,-1,NULL),(337325,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, 2, get]','2015-01-31 06:53:20',2,0,12,-1,NULL),(337326,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, 2, get]','2015-01-31 06:54:34',2,0,12,-1,NULL),(337327,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, 2, get]','2015-01-31 06:54:35',2,0,12,-1,NULL),(337328,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, 2, get]','2015-01-31 06:55:07',2,0,12,-1,NULL),(337329,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, -1, get]','2015-01-31 06:55:08',-1,0,10,-1,NULL),(337330,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, -1, list]','2015-01-31 06:55:09',-1,0,10,-1,NULL),(337331,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, -1, list]','2015-01-31 06:55:23',-1,0,10,-1,NULL),(337332,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, -1, list]','2015-01-31 06:55:25',-1,0,10,-1,NULL),(337333,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, -1, list]','2015-01-31 06:55:32',-1,0,10,-1,NULL),(337334,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, user, -1, login]','2015-01-31 06:55:47',-1,0,10,-1,NULL),(337335,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 07:21:50',-1,-1,-1,-1,NULL),(337336,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, systemcheck]','2015-01-31 07:22:25',-1,-1,-1,-1,NULL),(337337,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 07:43:28',-1,-1,-1,-1,NULL),(337338,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 07:43:33',-1,-1,-1,-1,NULL),(337339,'OK [73.162.103.67]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 18:03:59',-1,-1,-1,-1,NULL),(337340,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, systemcheck]','2015-01-31 21:54:13',-1,-1,-1,-1,NULL),(337341,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, systemcheck]','2015-01-31 21:54:13',-1,-1,-1,-1,NULL),(337342,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-01-31 21:54:18',-1,-1,-1,-1,NULL),(337343,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 14:50:54',-1,-1,-1,-1,NULL),(337344,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:40:12',-1,-1,-1,-1,NULL),(337345,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:42:48',-1,-1,-1,-1,NULL),(337346,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:47:51',-1,-1,-1,-1,NULL),(337347,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:51:49',-1,-1,-1,-1,NULL),(337348,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:53:47',-1,-1,-1,-1,NULL),(337349,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:56:45',-1,-1,-1,-1,NULL),(337350,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:56:47',-1,-1,-1,-1,NULL),(337351,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:56:47',-1,-1,-1,-1,NULL),(337352,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, -1, status]','2015-02-01 15:57:05',-1,-1,-1,-1,NULL),(337353,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, -1, getstatus]','2015-02-01 15:57:09',-1,-1,-1,-1,NULL),(337354,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:57:23',-1,-1,-1,-1,NULL),(337355,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 15:57:24',-1,-1,-1,-1,NULL),(337356,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, user, 1, get]','2015-02-01 15:57:27',1,0,12,-1,NULL),(337357,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 16:05:36',-1,-1,-1,-1,NULL),(337358,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 16:06:26',-1,-1,-1,-1,NULL),(337359,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 17:38:43',-1,-1,-1,-1,NULL),(337360,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 17:39:41',-1,-1,-1,-1,NULL),(337361,'OK [127.0.0.1]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-01 17:46:48',-1,-1,-1,-1,NULL),(337362,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-02 01:47:46',-1,-1,-1,-1,NULL),(337363,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-02 01:47:51',-1,-1,-1,-1,NULL),(337364,'FAIL [104.11.208.113]  [-1:anon], [GET] [1.0, rating, 1, ratecontent]','2015-02-02 01:48:27',1,3,-1,-1,NULL),(337365,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, systemcheck]','2015-02-02 01:48:55',-1,-1,-1,-1,NULL),(337366,'OK [104.11.208.113]  [-1:anon], [GET] [1.0, system, apidocs]','2015-02-02 01:52:08',-1,-1,-1,-1,NULL),(337367,'OK [ email sent from: john@starter.io to: johnny_mcshreddy@starter.io]','2015-02-01 17:56:47',1,0,-2,1,NULL),(337368,'OK [ email sent from: johnny_mcshreddy@starter.io to: johnny_mcshreddy@starter.io]','2015-02-01 17:56:47',42,0,-2,42,NULL),(337369,'OK [ email sent from: johnny_mcshreddy@starter.io to: johnny_mcshreddy@starter.io]','2015-02-01 17:56:48',42,0,-2,42,NULL);
/*!40000 ALTER TABLE `syslog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL COMMENT 'Everyone has a first name!',
  `last_name` varchar(255) DEFAULT NULL COMMENT 'Not everyone has a last name. Sadly.',
  `email` varchar(255) NOT NULL COMMENT 'This seems old school. But we still believe in email. For now',
  `username` varchar(255) NOT NULL DEFAULT 'NULL' COMMENT 'Now things get interesting.  Add some constraints here.',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `password` varchar(100) NOT NULL,
  `password_salt` varchar(100) DEFAULT NULL,
  `avatar_image` varchar(255) DEFAULT NULL,
  `creation_source` int(11) DEFAULT '0' COMMENT 'Users can be created by Facebook or other logins, emails entered by friends',
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_unique` (`username`),
  UNIQUE KEY `email_unique` (`email`),
  KEY `username` (`username`),
  KEY `password` (`password`)
) ENGINE=InnoDB AUTO_INCREMENT=1652 DEFAULT CHARSET=latin1 COMMENT='this is the main users table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
REPLACE INTO `user` VALUES (-1,'anonymous','user','anonymous@ignite.com','anon','2015-01-30 00:00:00','021f3a24d5533498fa19485eab0d2a2a0944beae21262a01955eb841d8c2bbee',NULL,NULL,0,-4),(1,'Vroom','Admin','admin@ignite.com','administrator','2015-01-30 00:00:00','32a16353a840a07582f793a11fde1faa28c5f9d23e4df164e57bc32ce113a2fc',NULL,'',0,0),(2,'Chuck','Jonz','chuck@cointerlock.com','chuck','2015-01-30 00:00:00','32a16353a840a07582f793a11fde1faa28c5f9d23e4df164e57bc32ce113a2fc',NULL,NULL,0,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_location_idx`
--

DROP TABLE IF EXISTS `user_location_idx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_location_idx` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_location_idx`
--

LOCK TABLES `user_location_idx` WRITE;
/*!40000 ALTER TABLE `user_location_idx` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_location_idx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_preferences`
--

DROP TABLE IF EXISTS `user_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_preferences` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  `int_value` varchar(45) DEFAULT NULL,
  `target_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqu` (`name`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40298 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_preferences`
--

LOCK TABLES `user_preferences` WRITE;
/*!40000 ALTER TABLE `user_preferences` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_preferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role_idx`
--

DROP TABLE IF EXISTS `user_role_idx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role_idx` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT 'This is the user id. Obviously.',
  `role_id` int(11) NOT NULL COMMENT 'This is the group id. Duh.',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_group_unique` (`user_id`,`role_id`),
  KEY `user_id` (`user_id`),
  KEY `user_role_idx_ibfk_2` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9569 DEFAULT CHARSET=latin1 COMMENT='Here we go, putting users in roles.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role_idx`
--

LOCK TABLES `user_role_idx` WRITE;
/*!40000 ALTER TABLE `user_role_idx` DISABLE KEYS */;
REPLACE INTO `user_role_idx` VALUES (9563,-1,2,'2015-01-30 21:00:33'),(9564,1,1,'2015-01-31 06:51:03'),(9565,1,2,'2015-01-31 06:51:03'),(9566,2,1,'2015-01-31 06:53:13'),(9567,2,2,'2015-01-31 06:53:13'),(9568,2,3,'2015-01-31 06:54:26');
/*!40000 ALTER TABLE `user_role_idx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!50001 DROP VIEW IF EXISTS `user_roles`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `user_roles` (
  `role_name` tinyint NOT NULL,
  `username` tinyint NOT NULL,
  `role_id` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehicle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) DEFAULT NULL,
  `make` varchar(45) NOT NULL,
  `model` varchar(45) NOT NULL,
  `year` varchar(45) DEFAULT NULL,
  `vin` varchar(75) NOT NULL,
  `color` varchar(45) DEFAULT NULL,
  `notes` varchar(45) DEFAULT NULL,
  `last_service_date` timestamp NULL DEFAULT NULL,
  `next_service_date` timestamp NULL DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'ignite'
--

--
-- Final view structure for view `roles_permissions`
--

/*!50001 DROP TABLE IF EXISTS `roles_permissions`*/;
/*!50001 DROP VIEW IF EXISTS `roles_permissions`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`starter`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `roles_permissions` AS select distinct concat_ws(':',`acl`.`target_type`,`acl`.`permission`,`acl`.`target_id`) AS `permission`,concat_ws('-',`role`.`id`,`role`.`name`) AS `role_name` from ((`acl` join `user_role_idx`) join `role`) where ((`acl`.`principle_id` = `user_role_idx`.`role_id`) and (`user_role_idx`.`role_id` = `role`.`id`) and (`acl`.`principle_type` = 1)) union all select distinct concat_ws(':',`acl`.`target_type`,`acl`.`permission`,`acl`.`target_id`) AS `permission`,concat_ws('-',`user`.`id`,`acl`.`target_type`,`acl`.`target_id`) AS `role_name` from (`acl` join `user`) where ((`acl`.`principle_id` = `user`.`id`) and (`acl`.`principle_type` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `user_roles`
--

/*!50001 DROP TABLE IF EXISTS `user_roles`*/;
/*!50001 DROP VIEW IF EXISTS `user_roles`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`starter`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `user_roles` AS select distinct concat_ws('-',`role`.`id`,`role`.`name`) AS `role_name`,`user`.`id` AS `username`,`role`.`id` AS `role_id` from (((`acl` join `user_role_idx`) join `user`) join `role`) where ((`acl`.`principle_id` = `user_role_idx`.`role_id`) and (`user_role_idx`.`user_id` = `user`.`id`) and (`user_role_idx`.`role_id` = `role`.`id`) and (`acl`.`principle_type` = 1)) union all select distinct concat_ws('-',`user`.`id`,`acl`.`target_type`,`acl`.`target_id`) AS `role_name`,`user`.`id` AS `username`,`acl`.`target_id` AS `targetid` from (`acl` join `user`) where ((`acl`.`principle_id` = `user`.`id`) and (`acl`.`principle_type` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


