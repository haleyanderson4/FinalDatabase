-- Generation time: Wed, 28 Nov 2018 08:02:04 +0000
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

DROP TABLE IF EXISTS `Location`;
CREATE TABLE `Location` (
companyId INTEGER PRIMARY KEY AUTO_INCREMENT, 
FOREIGN KEY companyId(companyId) REFERENCES Company(companyId) ON DELETE CASCADE, 
street VARCHAR(100) not null, 
city VARCHAR(25) not null, 
state VARCHAR(2) not null
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `Location` VALUES ('1','9579 Durgan Overpass','Alysafort','Ar'),
('2','6451 Andrew Shoal Suite 564','Raphaelleview','Ve'),
('3','53148 Timmothy Canyon','Russburgh','Ve'),
('4','510 Durgan Rapids','Flatleyville','Ne'),
('5','29885 Eliane Passage','Rogahntown','Wa'),
('6','251 Shanahan Freeway Suite 816','Lefflerport','Ma'),
('7','21463 Macejkovic Ridges Suite 685','South Josianneside','Mi'),
('8','957 Corwin Forest','South Bianka','Wy'),
('9','14695 Von Locks Suite 210','Walshmouth','So'),
('10','9024 Quigley Plain','Trompfurt','In'),
('11','193 Price Estates Suite 574','North Maria','Te'),
('12','27969 Ramon Green Suite 478','West Elishaburgh','Ne'),
('13','925 Tamia Stravenue','South Paxton','Co'),
('14','49613 Lavada Green Apt. 757','Veumburgh','Mo'),
('15','359 Burley Trail Suite 868','East Rasheedtown','Mi'),
('16','2336 Waters Lodge Suite 666','Edgardomouth','Te'),
('17','045 Robel Well Apt. 777','Bayerside','Al'),
('18','70326 Wendy Views','Johnsbury','Mi'),
('19','813 Amelie Spurs Suite 679','East Kris','Mi'),
('20','1381 Ziemann Valleys','Port Leonard','Ne'),
('21','63000 Gusikowski Prairie','Lake Lucile','Mi'),
('22','350 Rosalee Lodge','Leoside','Wa'),
('23','344 Watsica Mountain Suite 509','Port Nicklausville','No'),
('24','49859 Jakubowski Ports Suite 722','Wisozkland','Ne'),
('25','6952 Lang Haven','West Kieran','Ma'),
('26','7809 Sawayn Fields Suite 852','Port Marielachester','Lo'),
('27','6349 Gunnar Key Apt. 981','New Nat','Ne'),
('28','7920 Brendan Valley Suite 424','Lake Eileen','Fl'),
('29','98248 Bartoletti Shores','Kuhnside','Di'),
('30','098 Leuschke Ville Apt. 850','North Zacheryhaven','Lo'),
('31','89184 Jordane Parks Suite 479','South Jessika','Ha'),
('32','7874 Bailey Squares Apt. 155','Lake Torreymouth','Ka'),
('33','4899 Lon Ramp','Gabefort','So'),
('34','59974 Wanda Spurs Suite 436','New Vern','Te'),
('35','6380 Gerard Junction Suite 097','Reynoldsland','Id'),
('36','03415 Grimes Vista Apt. 511','Lake Angelicaland','In'),
('37','469 Pat Haven Suite 680','North Valerie','Mi'),
('38','98291 Trystan Forges Suite 703','West Jordane','No'),
('39','527 Stroman Shoals Apt. 916','Lake Francesca','Il'),
('40','004 Kautzer Ways','North Coltenton','Ne'),
('41','37612 Leannon Loop','Darylland','Ka'),
('42','5171 Trantow Brooks Apt. 874','Port Nicholasborough','Io'),
('43','2891 Altenwerth Orchard Suite 668','Toyhaven','Ne'),
('44','4932 Mayer Springs Suite 162','Reillybury','Ne'),
('45','1828 Khalid Crossing Suite 207','New Raquel','Ne'),
('46','6094 Alford Road Suite 834','Port Rivertown','Ne'),
('47','14439 Maryse Mountains Apt. 075','Kuhicton','Mi'),
('48','680 Becker Hills Suite 422','Terranceville','Oh'),
('49','9144 Lenore Trail','South Giovanny','Ne'),
('50','81021 Clemens Knoll Apt. 543','New Wilhelmineside','Ma'),
('51','646 Mosciski Ports','Hellerbury','So'),
('52','46565 Abernathy Land','Sedrickland','Te'),
('53','70747 Kamren Curve','New Antonetown','Ne'),
('54','0165 Misael Walks Apt. 382','Kohlerview','Ke'),
('55','761 Emard Manors Suite 545','North Daisy','Te'),
('56','319 Holly Loop','New Sydneeburgh','Di'),
('57','82269 Beatty Overpass','Parkerhaven','Ut'),
('58','3676 Larson Wall Apt. 712','South Walter','Wi'),
('59','399 Hoeger Drive','Lake Lamar','Id'),
('60','02033 Theron Village Suite 781','New Richardfurt','Rh'),
('61','142 Grady Trace','Napoleonmouth','Wy'),
('62','62854 Nolan Cove','Ernestchester','Lo'),
('63','23043 Ivy Streets Suite 957','New Vitoview','Lo'),
('64','2548 Salvatore Ports Apt. 952','Hayesville','Ma'),
('65','96564 Brooke Garden','Ratkestad','Ar'),
('66','63874 Maribel Burgs','Smithchester','Co'),
('67','885 O\'Keefe Mews','Prestontown','Pe'),
('68','184 Jean Streets Apt. 049','Lake Lexie','Co'),
('69','74966 Pacocha Summit Apt. 325','Hiramborough','Oh'),
('70','1947 Stiedemann Ford','North Priscillabury','Mi'),
('71','48556 Isabella Drive','Edisonfurt','Fl'),
('72','583 Graham Villages Suite 577','Abshireberg','Vi'),
('73','9539 Schulist Dale Apt. 533','Johnsfurt','We'),
('74','5327 Spinka Locks Apt. 051','Dooleyborough','Id'),
('75','400 Gutmann Crest','Leslyview','No'),
('76','5076 McClure Falls Apt. 117','Lavonbury','Mi'),
('77','787 Eric Key','Prestonside','Te'),
('78','305 Damaris Estates Apt. 055','Lake Alfreda','Ma'),
('79','571 Harley Garden','Erdmanfurt','Wi'),
('80','02211 Dashawn Camp','Kayceeview','So'),
('81','073 Goodwin Inlet','North Pearline','Te'),
('82','2553 Joy Crossroad','Greenstad','Te'),
('83','96883 Isabel Villages Suite 100','East Seamus','We'),
('84','2413 Lakin Turnpike Suite 311','Kaylieville','Ne'),
('85','026 Maximilian Heights Suite 617','West Mara','Ar'),
('86','12407 Lisette Canyon','Grovermouth','Ne'),
('87','399 Terrill Plaza','Terrenceborough','Ne'),
('88','333 Zemlak Lodge Suite 959','West Ceceliashire','Ut'),
('89','148 Buckridge Spring Apt. 361','West Tomaston','Ne'),
('90','803 Wiegand Circle','Maudiefort','Ok'),
('91','011 Alice Prairie Apt. 822','Lake Carsonfort','Ne'),
('92','093 Kuhlman Lock','Sipeshaven','Vi'),
('93','092 Stark Plaza','South Brandy','Te'),
('94','556 Earline Pine Suite 039','West Wiley','Co'),
('95','70336 Koelpin Via Suite 949','Port Lavada','Ve'),
('96','328 Kling Knolls Suite 543','Port Xandermouth','No'),
('97','33407 Hyatt Drive Suite 587','Cristhaven','Ne'),
('98','256 Grady Fort Apt. 729','Port Hans','Ut'),
('99','77228 Sydnee Inlet','Langoshborough','Ha'),
('100','785 Akeem Groves','Port Libbie','Ok'); 




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

