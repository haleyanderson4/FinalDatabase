-- Generation time: Wed, 28 Nov 2018 06:21:03 +0000
-- Host: mysql.hostinger.ro
-- DB name: u574849695_25
/*!40030 SET NAMES UTF8 */;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `Company`;
CREATE TABLE `Company` (
  `companyId` int(11) NOT NULL AUTO_INCREMENT,
  `companyName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `numEmployees` int(11) DEFAULT NULL,
  `yearlyRevenue` float(20,2) DEFAULT NULL,
  `stockPrice` float(20,2) DEFAULT NULL,
  PRIMARY KEY (`companyId`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `Company` VALUES ('1','Towne Inc','82418','5769749.00','1546.47'),
('2','Reichert-Harvey','42808','6004587.00','1645.60'),
('3','Brakus PLC','93042','1887925.50','1626.37'),
('4','Lehner-Cummerata','69836','9126112.00','922.17'),
('5','Gutmann-Kshlerin','91926','6920801.00','275.42'),
('6','Quigley, DuBuque and Johnston','85739','2216174.00','655.92'),
('7','Ruecker, Hermiston and Rath','5466','9445381.00','1633.51'),
('8','Kirlin, Hahn and Terry','1429','2990131.50','1618.36'),
('9','Sipes, Hayes and Feeney','76123','9681815.00','1734.84'),
('10','Rowe-Klein','65589','7699608.50','567.17'),
('11','Swift Inc','90506','2937160.75','1759.56'),
('12','Hahn-Koelpin','56399','1008608.56','720.70'),
('13','Hegmann PLC','18525','7163377.00','863.94'),
('14','Lehner, Hills and Kertzmann','33412','9566531.00','147.05'),
('15','Schowalter and Sons','97506','3963569.75','571.49'),
('16','Bergnaum Inc','21763','3499357.75','1859.27'),
('17','Crist, Hilll and Sanford','32917','249280.00','1766.06'),
('18','Walter LLC','55428','8628038.00','1064.08'),
('19','Stehr LLC','15924','5022907.00','1843.65'),
('20','Koepp, Klein and Pouros','60441','2494447.25','451.83'),
('21','Mann, Pouros and Stehr','45894','189414.66','915.70'),
('22','Wilderman-Olson','61837','1447478.25','175.29'),
('23','Bauch-Powlowski','48483','2809135.75','452.07'),
('24','Marks LLC','31402','8886866.00','1892.32'),
('25','Torp LLC','95240','9837077.00','1650.08'),
('26','Runolfsson-Satterfield','3438','6367606.00','1327.65'),
('27','Rohan, Littel and Swift','24711','1875141.00','1675.71'),
('28','Mills Ltd','80158','4153381.25','338.62'),
('29','Daugherty-Stokes','48135','3332354.00','1201.56'),
('30','Ullrich Group','22405','6209410.00','1847.27'),
('31','Cremin and Sons','34933','8786807.00','414.40'),
('32','Rath-Krajcik','82787','1592469.75','1300.46'),
('33','Sporer-Marquardt','52654','9757253.00','512.39'),
('34','Wiza-Wisozk','76377','3111354.25','1346.12'),
('35','Kris-Hirthe','44290','7498443.50','618.72'),
('36','Ruecker LLC','48119','4653488.00','158.68'),
('37','Cormier-Nikolaus','5309','9108211.00','920.85'),
('38','Bailey PLC','99230','4164170.00','1458.56'),
('39','Cormier PLC','84431','9247965.00','616.40'),
('40','Murray-Reynolds','10629','8910351.00','728.75'),
('41','Mayert PLC','96903','849834.31','1178.43'),
('42','Kunze-Feest','58658','8192257.00','327.23'),
('43','Howe-Heller','31911','5941914.50','180.44'),
('44','Hilll Ltd','81117','601788.00','348.77'),
('45','Grant Inc','44495','3516158.25','399.26'),
('46','Bartoletti LLC','48956','128278.11','645.07'),
('47','Gibson-Bahringer','28157','6433946.00','1076.02'),
('48','Beier-Ryan','16375','5319930.00','1631.52'),
('49','Hayes Ltd','19168','6649295.50','297.92'),
('50','Jaskolski, Strosin and Cremin','51304','3602093.00','1209.16'),
('51','Stroman, Heaney and Cassin','92513','533670.38','1517.91'),
('52','Reinger-Senger','76950','3645044.00','1681.02'),
('53','McGlynn Inc','211','8664452.00','1137.94'),
('54','Carter, Kerluke and Gleichner','9633','6896418.50','195.92'),
('55','Feeney, Bernier and Osinski','10200','650383.69','1365.95'),
('56','Ortiz, Considine and Rolfson','79102','1699049.25','1438.43'),
('57','Quitzon, Bogisich and Nicolas','99457','3210067.50','1739.54'),
('58','Welch-Strosin','34391','8614457.00','1373.51'),
('59','O\'Connell LLC','15193','7100628.50','1806.61'),
('60','Farrell, Dibbert and Rolfson','18009','876772.38','1824.52'),
('61','Lehner, Lebsack and Herman','74404','7083368.00','1953.10'),
('62','Friesen Group','36829','6157538.00','1371.95'),
('63','Feil Group','8139','7601616.50','1275.61'),
('64','Mitchell LLC','9147','5155717.50','447.16'),
('65','Ferry LLC','55479','4422934.00','1158.99'),
('66','Rutherford, Parker and Cummerata','39062','493054.72','587.77'),
('67','Bashirian-Emmerich','96761','281656.34','311.40'),
('68','Collins-Wolf','42600','9575011.00','1820.59'),
('69','Kirlin-Reichel','1476','1377663.00','1995.68'),
('70','Goodwin-Corwin','17540','9618847.00','970.73'),
('71','Larkin, Schumm and Daniel','8570','5397064.00','1119.55'),
('72','Trantow-Krajcik','10545','1560235.50','1833.10'),
('73','Cormier LLC','10349','7204273.50','1183.88'),
('74','Hyatt, Breitenberg and Mann','13895','559946.19','1948.50'),
('75','McDermott-Mosciski','23976','2776482.00','156.43'),
('76','Larson, Rath and Jaskolski','96066','1586816.25','1286.72'),
('77','Schinner-McClure','50647','784571.44','852.51'),
('78','Ruecker-Welch','68106','844046.44','105.19'),
('79','Shields, Schumm and Boyle','96234','2536380.50','1803.19'),
('80','Crona Ltd','83514','3481810.75','1794.09'),
('81','Mueller-Gulgowski','4998','6323529.50','926.52'),
('82','Romaguera, Lesch and Ritchie','47853','2137345.25','178.87'),
('83','Farrell and Sons','95923','5043628.00','1466.01'),
('84','Towne-Mohr','63262','1493425.62','772.22'),
('85','Hodkiewicz Group','53865','8536860.00','1716.45'),
('86','Bauch Inc','70934','8922416.00','1953.92'),
('87','Kohler PLC','83555','489362.34','1670.39'),
('88','Schultz, Hintz and Doyle','78181','3698462.00','1871.49'),
('89','Schultz-Weber','67858','1542223.88','1816.96'),
('90','Jones, Barton and Murazik','92852','8664298.00','1635.66'),
('91','Kunze LLC','65082','1680071.50','1646.95'),
('92','Langosh-Ullrich','62767','2966769.00','1937.60'),
('93','Wintheiser-Bogan','83626','6473211.50','1312.00'),
('94','Aufderhar-Reynolds','30266','6922977.00','1824.72'),
('95','Franecki-Kohler','14422','1460858.12','913.91'),
('96','Jacobi-Swaniawski','59295','6949300.50','1856.00'),
('97','Little Ltd','19239','9808050.00','314.47'),
('98','Goldner, Runolfsdottir and Windler','68316','8215373.50','1546.22'),
('99','Gulgowski-Hartmann','65141','1159612.38','391.83'),
('100','Beahan-Heidenreich','66885','3342559.00','316.95'); 




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

