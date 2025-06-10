-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: agapaydb
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_log`
--

DROP TABLE IF EXISTS `admin_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `action_type` varchar(10) NOT NULL,
  `admin_id` int DEFAULT NULL,
  `adminfirstname` varchar(50) DEFAULT NULL,
  `adminlastname` varchar(50) DEFAULT NULL,
  `barangay_id` int DEFAULT NULL,
  `official_type` varchar(30) DEFAULT NULL,
  `contact` varchar(15) DEFAULT NULL,
  `gmail` varchar(100) DEFAULT NULL,
  `log_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `barangay_id` (`barangay_id`),
  CONSTRAINT `admin_log_ibfk_1` FOREIGN KEY (`barangay_id`) REFERENCES `barangays` (`barangay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_log`
--

LOCK TABLES `admin_log` WRITE;
/*!40000 ALTER TABLE `admin_log` DISABLE KEYS */;
INSERT INTO `admin_log` VALUES (1,'DELETE',8,'Mayleen Rose','Gonzales',2,'Barangay Treasurer','09235678901','mayleen.g@gmail.com','2025-06-07 16:28:38'),(2,'INSERT',9,'Mayleen Rose','Gonzales',2,'Barangay Treasurer','09235678901','mayleen.g@gmail.com','2025-06-07 16:29:48');
/*!40000 ALTER TABLE `admin_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_officials`
--

DROP TABLE IF EXISTS `admin_officials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_officials` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `adminfirstname` varchar(50) NOT NULL,
  `adminlastname` varchar(50) NOT NULL,
  `barangay_id` int NOT NULL,
  `official_type` enum('Barangay Captain','Barangay Kagawad','Barangay Secretary','Barangay Treasurer','SK Chairperson') NOT NULL,
  `adminpassword` varchar(50) NOT NULL,
  `contact` varchar(15) DEFAULT NULL,
  `gmail` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `contact` (`contact`),
  UNIQUE KEY `gmail` (`gmail`),
  KEY `barangay_id` (`barangay_id`),
  CONSTRAINT `admin_officials_ibfk_1` FOREIGN KEY (`barangay_id`) REFERENCES `barangays` (`barangay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_officials`
--

LOCK TABLES `admin_officials` WRITE;
/*!40000 ALTER TABLE `admin_officials` DISABLE KEYS */;
INSERT INTO `admin_officials` VALUES (1,'Ronen Kenneth','Pano',1,'Barangay Captain','pano@2004','09661234567','ronen@gmail.com','2025-06-07 16:07:25'),(2,'Tyrone Gabriel','Pascual',1,'Barangay Kagawad','pascual@1800','09458971235','tyrone@gmail.com','2025-06-07 16:07:25'),(3,'Clarisse Marie','Dela Cruz',1,'Barangay Secretary','clarisse@2024','09234561234','clarisse.aguho@gmail.com','2025-06-07 16:07:25'),(4,'Benedict John','Ramos',1,'Barangay Treasurer','benjohn@2024','09991239876','benedict.ramos@gmail.com','2025-06-07 16:07:25'),(5,'Dreig Rashid','Aguila',2,'Barangay Captain','aguila@2004','09375702698','dreig@gmail.com','2025-06-07 16:07:25'),(6,'Katrina Mae','Santos',2,'Barangay Kagawad','katrina@2024','09459873221','katrina.santos@gmail.com','2025-06-07 16:07:25'),(7,'Leonard Vincent','Javier',2,'Barangay Secretary','leo@2024','09187654321','leonard.javier@gmail.com','2025-06-07 16:07:25'),(9,'Mayleen Rose','Gonzales',2,'Barangay Treasurer','mayleen@2024','09235678901','mayleen.g@gmail.com','2025-06-07 16:29:48');
/*!40000 ALTER TABLE `admin_officials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_citizens`
--

DROP TABLE IF EXISTS `audit_citizens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_citizens` (
  `audit_id` int NOT NULL AUTO_INCREMENT,
  `citizen_id` int DEFAULT NULL,
  `action_type` enum('INSERT','DELETE') NOT NULL,
  `action_timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  `citizenlastname` varchar(100) DEFAULT NULL,
  `citizenfirstname` varchar(100) DEFAULT NULL,
  `contactNum` varchar(11) DEFAULT NULL,
  `valid_id_type` varchar(50) DEFAULT NULL,
  `valid_id_code` varchar(30) DEFAULT NULL,
  `barangay_id` int DEFAULT NULL,
  PRIMARY KEY (`audit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_citizens`
--

LOCK TABLES `audit_citizens` WRITE;
/*!40000 ALTER TABLE `audit_citizens` DISABLE KEYS */;
INSERT INTO `audit_citizens` VALUES (1,10,'INSERT','2025-06-08 22:55:15','Matibag','Josie','09453259911','Senior Citizen ID','184721-1746-0011',NULL),(2,10,'DELETE','2025-06-08 22:57:58','Matibag','Josie','09453259911','Senior Citizen ID','184721-1746-0011',NULL);
/*!40000 ALTER TABLE `audit_citizens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `barangays`
--

DROP TABLE IF EXISTS `barangays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `barangays` (
  `barangay_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `city_id` int NOT NULL,
  `barangay_symbol` varchar(100) DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`barangay_id`),
  KEY `city_id` (`city_id`),
  CONSTRAINT `barangays_ibfk_1` FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barangays`
--

LOCK TABLES `barangays` WRITE;
/*!40000 ALTER TABLE `barangays` DISABLE KEYS */;
INSERT INTO `barangays` VALUES (1,'Aguho',1,'barangay-1','1621'),(2,'Magtanggol',1,'barangay-2','1620');
/*!40000 ALTER TABLE `barangays` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cities` (
  `city_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `region_id` int NOT NULL,
  `city_symbol` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`city_id`),
  KEY `region_id` (`region_id`),
  CONSTRAINT `cities_ibfk_1` FOREIGN KEY (`region_id`) REFERENCES `regions` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cities`
--

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;
INSERT INTO `cities` VALUES (1,'Pateros',1,'city-1');
/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `citizens`
--

DROP TABLE IF EXISTS `citizens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citizens` (
  `citizen_id` int NOT NULL AUTO_INCREMENT,
  `citizenlastname` varchar(100) NOT NULL,
  `citizenfirstname` varchar(100) NOT NULL,
  `contactNum` varchar(11) NOT NULL,
  `marital_status` enum('Single','Married','Widowed','Divorced') NOT NULL,
  `household_no` int NOT NULL,
  `valid_id_type` varchar(50) NOT NULL,
  `valid_id_code` varchar(30) NOT NULL,
  `house_no` char(4) DEFAULT NULL,
  `barangay_id` int DEFAULT NULL,
  `city_id` int DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  `region_id` int DEFAULT NULL,
  `country_id` int DEFAULT NULL,
  PRIMARY KEY (`citizen_id`),
  UNIQUE KEY `contactNum` (`contactNum`),
  UNIQUE KEY `valid_id_code` (`valid_id_code`),
  KEY `barangay_id` (`barangay_id`),
  KEY `city_id` (`city_id`),
  KEY `region_id` (`region_id`),
  KEY `country_id` (`country_id`),
  CONSTRAINT `citizens_ibfk_1` FOREIGN KEY (`barangay_id`) REFERENCES `barangays` (`barangay_id`),
  CONSTRAINT `citizens_ibfk_2` FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`),
  CONSTRAINT `citizens_ibfk_3` FOREIGN KEY (`region_id`) REFERENCES `regions` (`region_id`),
  CONSTRAINT `citizens_ibfk_4` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `citizens`
--

LOCK TABLES `citizens` WRITE;
/*!40000 ALTER TABLE `citizens` DISABLE KEYS */;
INSERT INTO `citizens` VALUES (1,'Dela Cruz','Juan','09475709236','Married',5,'Philippine Passport','P1281567A','963',1,1,'1621',1,1),(2,'Reyes','Ana','09335721234','Single',3,'Driver’s License','N01-23-456789','2310',1,1,'1621',1,1),(3,'Santos','Mark','09871234325','Widowed',2,'SSS ID','01-2345678-9','1233',1,1,'1621',1,1),(4,'Torres','Maria','09753456547','Married',6,'UMID','01-2431478-9','9857',1,1,'1621',1,1),(5,'Garcia','Liza','09981234356','Single',1,'Voter’s ID','ABCDE1234567','1235',2,1,'1620',1,1),(6,'Fernandez','Leo','09957900057','Married',4,'PhilHealth ID','12-345698101-2','948',2,1,'1620',1,1),(7,'Rodriguez','Ella','09687546759','Divorced',2,'Senior Citizen ID','123456-7890-0000','5566',2,1,'1620',1,1),(8,'Lopez','Daniel','09857568182','Single',3,'PWD ID','PWD-AU-123456','6123',2,1,'1620',1,1);
/*!40000 ALTER TABLE `citizens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `countries` (
  `country_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `country_symbol` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `countries`
--

LOCK TABLES `countries` WRITE;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` VALUES (1,'Philippines','country-1');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regions`
--

DROP TABLE IF EXISTS `regions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `regions` (
  `region_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `country_id` int NOT NULL,
  `region_symbol` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`region_id`),
  KEY `country_id` (`country_id`),
  CONSTRAINT `regions_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regions`
--

LOCK TABLES `regions` WRITE;
/*!40000 ALTER TABLE `regions` DISABLE KEYS */;
INSERT INTO `regions` VALUES (1,'NCR',1,'region-1');
/*!40000 ALTER TABLE `regions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `valid_ids`
--

DROP TABLE IF EXISTS `valid_ids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `valid_ids` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `valid_ids`
--

LOCK TABLES `valid_ids` WRITE;
/*!40000 ALTER TABLE `valid_ids` DISABLE KEYS */;
INSERT INTO `valid_ids` VALUES (2,'Driver’s License'),(6,'PhilHealth ID'),(1,'Philippine Passport'),(7,'Postal ID'),(9,'PWD ID'),(8,'Senior Citizen ID'),(3,'SSS ID'),(4,'UMID'),(5,'Voter’s ID');
/*!40000 ALTER TABLE `valid_ids` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-09 13:03:06
