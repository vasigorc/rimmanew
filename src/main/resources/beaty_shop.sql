-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: localhost    Database: beaty_shop
-- ------------------------------------------------------
-- Server version	5.7.17

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
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `important` mediumtext COLLATE utf8_unicode_ci,
  `no_show` tinyint(1) NOT NULL DEFAULT '0',
  `in_the_past` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `date` (`date`),
  KEY `in_the_past` (`in_the_past`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1,'2016-02-09','11:00:00','Varvara','massage','mynew@email.ca','mon numero est 514 000 0000',0,1),(26,'2016-01-28','13:00:00','Vasile','massage','vaasa@sad.ds','',0,1),(28,'2016-01-20','13:00:00','Tatiana','manicure','ajkfd@djfs.df','',0,1),(30,'2016-01-22','10:00:00','Elena','pedicure','adsds@asd.ds','',0,1),(32,'2016-10-22','10:00:00','Elena','pedicure','adsds@asd.ds','',0,1),(33,'2016-01-22','10:00:00','Elena','pedicure','adsds@asd.ds','',0,1),(35,'2016-07-28','15:00:00','Rimma','massage','valid@email.ca','any',0,0),(38,'2016-03-05','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(39,'2016-02-10','11:00:00','Liusea','waxing','liusea@somemail.qc.ca','Rappelez moi au cas ou',0,1),(40,'2016-02-03','11:00:00','Lyne','massage','lyne@yahoo.ca','massage please',0,1),(62,'2016-02-05','15:30:00','Egzbeta','massage','asdasdad@sdfsd.fd','mon numero est 514 000 0000',0,1),(64,'2016-02-19','11:00:00','Varvara','massage','variusha@canada.ca','',0,1),(65,'2016-02-19','10:00:00','Shawn','manicure','shawn@gmail.com','',0,1),(78,'2016-02-17','14:00:00','Vasilisa','manicure','courriel@yahoo.ca','',0,1),(79,'2016-03-10','11:00:00','Vasilisa','manicure','vasilisa@yahoo.de','',0,1),(82,'2016-03-18','09:00:00','Stephanie','manicure','sschifman@presagia.com','',0,1),(85,'2016-03-17','13:00:00','Eloise','manicure','eloise@netbeans.com','On the 13th of March',0,1),(86,'2016-04-27','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(90,'2016-03-31','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(91,'2016-03-15','13:00:00','Zinaida','massage','zinaida@mail.ro','Je pourrais arriver 10 minutes en retard',0,1),(92,'2016-03-30','15:00:00','Rimma','pedicure','rimma@aol.com','This is a test message',0,1),(96,'2016-06-02','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(97,'2016-04-16','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(98,'2016-05-10','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(111,'2016-05-18','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(112,'2016-07-06','15:00:00','Rimma','massage','valid@email.ca','any',0,0),(113,'2016-06-19','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(115,'2016-05-21','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(116,'2016-05-20','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(117,'2016-05-17','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(118,'2016-07-15','15:00:00','Rimma','massage','valid@email.ca','any',0,0),(119,'2016-06-23','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(120,'2016-05-22','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(121,'2016-05-25','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(122,'2016-06-05','15:00:00','Rimma','massage','valid@email.ca','any',0,1),(125,'2016-05-20','13:00:00','Alejandra','manicure','mdavila@presagia.com','',0,1),(126,'2016-05-19','16:13:15','agBeUZXW','massage','4wGdHdY1y9@Z7CYR.ca',NULL,0,1),(127,'2016-05-19','08:22:25','jfgFZmfN','manicure','Hq5srn1bPD@OfVOL.ca',NULL,0,1),(128,'2016-05-17','16:41:24','cFYPvwwr','massage','VuPJj9PsGx@HkQIg.ca',NULL,0,1),(129,'2016-05-19','04:18:36','gzszoWRZ','pedicure','cRbGUIRElT@sFF4f.ca',NULL,0,1),(130,'2016-05-17','18:13:26','AjnlogyS','manicure','6ygBscQIM6@WBTI6.ca',NULL,0,1),(131,'2016-05-18','08:33:02','bgxpPnBT','waxing','1FQQvtKgW8@hUZEy.ca',NULL,0,1),(133,'2016-05-18','02:55:21','jmtDjYkp','manicure','rjjPVLH8TR@rCeVO.ca',NULL,0,1),(135,'2016-05-27','14:00:00','Varvara','massage','varvara@gmail.com','',0,1),(136,'2016-05-31','11:00:00','Varvara','massage','asdasd@ada.ds','',0,1),(137,'2016-06-10','15:00:00','Alejandra','manicure','mdavila@presagia.com','One, two, three',0,1),(146,'2016-06-14','11:00:00','Varvara','massage','varea@gmail.com','ĞŸÑÑ‚Ğ¾Ğµ ÑĞ¾Ğ¾Ğ±Ñ‰ĞµĞ½Ğ¸Ğµ Ğ½Ğ° Ñ€ÑƒÑÑĞºĞ¾Ğ¼ ÑĞ·Ñ‹ĞºĞµ',0,1),(147,'2016-06-15','14:00:00','Lida','pedicure','asdasd@ada.ds','Ğ¨ĞµÑÑ‚Ğ¾Ğµ ÑĞ¾Ğ¾Ğ±Ñ‰ĞµĞ½Ğ¸Ğµ Ğ½Ğ° Ñ€ÑƒÑÑĞºĞ¾Ğ¼ ÑĞ·Ñ‹ĞºĞµ!',0,1),(148,'2016-06-23','11:00:00','Ğ•ĞºĞ°Ñ‚ĞµÑ€Ğ¸Ğ½Ğ°','massage','asdad@asda.ds','Ğ¡Ğ¾Ğ¾Ğ±Ñ‰ĞµĞ½Ğ¸Ğµ Ğ½Ğ° Ñ€ÑƒÑÑĞºĞ¾Ğ¼ ÑĞ·Ñ‹ĞºĞµ!',0,1),(150,'2016-06-30','13:00:00','Elena','manicure','elenatodorasco@gmail.com','',0,1),(153,'2016-08-01','16:30:00','Stephanie','manicure','sschiffman@presagia.com','',0,0),(155,'2016-07-23','15:00:00','Rimma','massage','valid@email.ca','any',0,0),(156,'2016-08-26','15:00:00','Rimma','massage','valid@email.ca','any',0,0),(157,'2016-08-20','16:00:00','Varvara','massage','varvara@gmal.com',NULL,0,0),(159,'2016-07-20','09:50:00','Ekaterina','massage','ekaterina@mail.qc.ca',NULL,0,0),(176,'2016-07-12','14:00:00','Elena','massage','elenatodorasco@gmail.com','',0,0),(179,'2016-08-18','13:00:00','Varvara','massage','vasigorc@gmail.com','Plus de dÃ©tails confirmÃ©',0,0),(180,'2016-08-24','13:00:00','Elena Todorasco','pedicure','elenatodorasco@gmail.com','514 000 0000',0,0),(181,'2016-09-15','11:00:00','Elena Todorasco','massage','elenatodorasco@gmail.com','',0,0),(182,'2016-10-11','09:57:00','Martine','pedicure','vasigorc@gmail.com','5147580674',0,0),(183,'2016-09-14','10:00:00','Varvara','manicure','varvaragorcinschi@gmail.com','detail detail detail detail detail ',0,0);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credential`
--

DROP TABLE IF EXISTS `credential`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credential` (
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `is_blocked` tinyint(1) DEFAULT '0',
  `created_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `passwd` blob NOT NULL,
  `is_suspended` tinyint(1) DEFAULT '0',
  `GROUP_group_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `firstname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lastname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `salt` blob NOT NULL,
  PRIMARY KEY (`username`),
  KEY `FK_credential_GROUP_group_name` (`GROUP_group_name`),
  CONSTRAINT `FK_credential_GROUP_group_name` FOREIGN KEY (`GROUP_group_name`) REFERENCES `groups` (`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credential`
--

LOCK TABLES `credential` WRITE;
/*!40000 ALTER TABLE `credential` DISABLE KEYS */;
INSERT INTO `credential` VALUES ('sample_user_nr3',0,'user_creator','2016-09-05 13:15:02','user_creator','2016-09-05 13:15:03','Jq\ß\ï­–®\ÃlOòÙ¡C·F\Çm8¬Ù§tÛ‡\Ë!ù$>õ,*l‰nf~\ÃZÛ¦Y\ç\ÊTr\äv³¶¿¢1',0,'admin',NULL,NULL,'\éB\Ü\×^\×\çøB \Ò'),('sample_user_two',0,'user_creator','2016-09-05 13:15:02','user_creator','2016-09-24 15:26:15','•‹\n7£¨¿¶q\Z\á¬İ®ğ‰\ê\Ë\Õ<Á\àÿÁ¥,°ı›¯ \ä«_÷\0´\ÙF|\İ—\Ü9Í¨\æf òs­',0,'admin','John','Smith',')5fM]\\´\İL\à:JQ'),('su-user',0,'Vasile','2016-09-24 15:26:14','Vasile','2016-09-24 15:26:14',';\ì*|®ööòŠV:²Æšiw¦¸¸Œ¥\Ë\Óv¶›¬Ö#¥\æ\Ğ\É\Ğ“*—2Ù\Ã~XÀ›ó‡	Ÿ&MiUL\Í',0,'su','John','Doe','3193kbo4ufab2q3paaik4ojb0q');
/*!40000 ALTER TABLE `credential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `group_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `created_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES ('admin','vgorcinschi','2016-08-21 15:14:41','junit test','2016-09-01 18:30:08'),('su','vgorcinschi','2016-09-01 18:30:08','vgorcinschi','2016-09-01 18:30:08');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `special_day`
--

DROP TABLE IF EXISTS `special_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `special_day` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `start_at` time DEFAULT NULL,
  `end_at` time DEFAULT NULL,
  `break_start` time DEFAULT NULL,
  `break_end` time DEFAULT NULL,
  `duration_per_appointment` int(11) DEFAULT NULL,
  `is_blocked` bit(1) NOT NULL,
  `message` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `date_UNIQUE` (`date`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `special_day`
--

LOCK TABLES `special_day` WRITE;
/*!40000 ALTER TABLE `special_day` DISABLE KEYS */;
INSERT INTO `special_day` VALUES (2,'2016-01-19','09:00:00','15:00:00','12:00:00','13:00:00',30,'\0','Short day'),(3,'2016-05-07','09:00:00','15:00:00','12:00:00','12:30:00',30,'\0','Short day'),(6,'2016-04-08','09:00:00','15:00:00','12:00:00','12:30:00',30,'\0','Short day'),(9,'2016-09-02',NULL,NULL,NULL,NULL,0,'',NULL),(10,'2016-09-18','12:00:00','17:00:00',NULL,NULL,30,'\0',NULL);
/*!40000 ALTER TABLE `special_day` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-26 10:14:37
