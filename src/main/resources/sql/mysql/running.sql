﻿# Host: localhost  (Version: 5.6.11)
# Date: 2015-06-05 18:21:25
# Generator: MySQL-Front 5.3  (Build 4.187)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "activity_info"
#

DROP TABLE IF EXISTS `activity_info`;
CREATE TABLE `activity_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuuid` varchar(36) NOT NULL DEFAULT '' COMMENT '活动uuid',
  `uuid` varchar(36) NOT NULL DEFAULT '' COMMENT '活动发起人uuid',
  `info` varchar(255) DEFAULT NULL COMMENT '活动信息',
  `time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '活动时间',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '活动地点',
  `longitude` varchar(32) NOT NULL DEFAULT '' COMMENT '经度',
  `latitude` varchar(32) NOT NULL DEFAULT '' COMMENT '纬度',
  `kilometer` int(11) NOT NULL DEFAULT '0' COMMENT '活动距离',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `last_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  `state` varchar(1) NOT NULL DEFAULT '1' COMMENT '活动有效为1，无效为0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `actuuid` (`actuuid`),
  KEY `uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='活动信息表';

#
# Data for table "activity_info"
#

INSERT INTO `activity_info` VALUES (7,'16c71b7d-a240-4ec0-9d5c-2be1df9b50b6','16c71b7d-a240-4ec0-9d5c-2be1df9b50b6','123456','2015-05-31 22:00:00','交大兴庆小区','108.990522','34.253745',10,'2015-05-31 10:47:42','2015-05-31 11:34:24','1'),(8,'a5679a02-f5db-4767-b838-b0eff99f110f','a5679a02-f5db-4767-b838-b0eff99f110f','123456','2015-05-31 21:00:00','清凉寺','108.937884','34.180789',10,'2015-05-31 10:53:13','2015-05-31 11:34:29','1');

#
# Structure for table "activity_participate"
#

DROP TABLE IF EXISTS `activity_participate`;
CREATE TABLE `activity_participate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuuid` varchar(36) DEFAULT NULL COMMENT '活动id',
  `uuid` varchar(36) NOT NULL DEFAULT '' COMMENT '活动参与者uuid',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `last_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `uuid` (`uuid`),
  KEY `activity_id` (`actuuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动参与表';

#
# Data for table "activity_participate"
#


#
# Structure for table "area_dict"
#

DROP TABLE IF EXISTS `area_dict`;
CREATE TABLE `area_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(16) DEFAULT NULL COMMENT '地区代码',
  `province` varchar(255) NOT NULL DEFAULT '' COMMENT '省份',
  `area` varchar(255) NOT NULL DEFAULT '' COMMENT '地区',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8 COMMENT='城市地区代码表';

#
# Data for table "area_dict"
#

INSERT INTO `area_dict` VALUES (1,'010','北京市','北京市'),(2,'021','上海市','上海市'),(3,'022','天津市','天津市'),(4,'023','重庆市','重庆市'),(5,'852','香港','香港'),(6,'310','河北省','邯郸市'),(7,'311','河北省','石家庄'),(8,'312','河北省','保定市'),(9,'313','河北省','张家口'),(10,'314','河北省','承德市'),(11,'315','河北省','唐山市'),(12,'316','河北省','廊坊市'),(13,'317','河北省','沧州市'),(14,'318','河北省','衡水市'),(15,'319','河北省','邢台市'),(16,'335','河北省','秦皇岛'),(17,'350','山西省','忻州市'),(18,'351','山西省','太原市'),(19,'352','山西省','大同市'),(20,'353','山西省','阳泉市'),(21,'354','山西省','榆次市'),(22,'355','山西省','长治市'),(23,'356','山西省','晋城市'),(24,'357','山西省','临汾市'),(25,'358','山西省','离石市'),(26,'359','山西省','运城市'),(27,'370','河南省','商丘市'),(28,'371','河南省','郑州市'),(29,'372','河南省','安阳市'),(30,'373','河南省','新乡市'),(31,'374','河南省','许昌市'),(32,'375','河南省','平顶山'),(33,'376','河南省','信阳市'),(34,'377','河南省','南阳市'),(35,'378','河南省','开封市'),(36,'379','河南省','洛阳市'),(37,'391','河南省','焦作市'),(38,'392','河南省','鹤壁市'),(39,'393','河南省','濮阳市'),(40,'394','河南省','周口市'),(41,'395','河南省','漯河市'),(42,'396','河南省','驻马店'),(43,'398','河南省','三门峡'),(44,'24','辽宁省','沈阳市'),(45,'410','辽宁省','铁岭市'),(46,'411','辽宁省','大连市'),(47,'412','辽宁省','鞍山市'),(48,'413','辽宁省','抚顺市'),(49,'414','辽宁省','本溪市'),(50,'415','辽宁省','丹东市'),(51,'416','辽宁省','锦州市'),(52,'417','辽宁省','营口市'),(53,'418','辽宁省','阜新市'),(54,'419','辽宁省','辽阳市'),(55,'421','辽宁省','朝阳市'),(56,'427','辽宁省','盘锦市'),(57,'429','辽宁省','葫芦岛'),(58,'431','吉林省','长春市'),(59,'432','吉林省','吉林市'),(60,'0433','吉林省','延吉市'),(61,'434','吉林省','四平市'),(62,'435','吉林省','通化市'),(63,'436','吉林省','白城市'),(64,'437','吉林省','辽源市'),(65,'438','吉林省','松原市'),(66,'439','吉林省','浑江市'),(67,'440','吉林省','珲春市'),(68,'450','黑龙江','阿城市'),(69,'451','黑龙江','哈尔滨'),(70,'452','黑龙江','齐齐哈尔'),(71,'453','黑龙江','牡丹江'),(72,'454','黑龙江','佳木斯'),(73,'455','黑龙江','绥化市'),(74,'456','黑龙江','黑河市'),(75,'457','黑龙江','加格达奇'),(76,'458','黑龙江','伊春市'),(77,'459','黑龙江','大庆市'),(78,'470','内蒙古','海拉尔'),(79,'471','内蒙古','呼和浩特'),(80,'472','内蒙古','包头市'),(81,'473','内蒙古','乌海市'),(82,'474','内蒙古','集宁市'),(83,'475','内蒙古','通辽市'),(84,'476','内蒙古','赤峰市'),(85,'477','内蒙古','东胜市'),(86,'478','内蒙古','临河市'),(87,'479','内蒙古','锡林浩特'),(88,'482','内蒙古','乌兰浩特'),(89,'483','内蒙古','阿拉善左旗'),(90,'25','江苏省','南京市'),(91,'510','江苏省','无锡市'),(92,'511','江苏省','镇江市'),(93,'512','江苏省','苏州市'),(94,'513','江苏省','南通市'),(95,'514','江苏省','扬州市'),(96,'515','江苏省','盐城市'),(97,'516','江苏省','徐州市'),(98,'517','江苏省','淮阴市'),(99,'517','江苏省','淮安市'),(100,'518','江苏省','连云港'),(101,'519','江苏省','常州市'),(102,'523','江苏省','泰州市'),(103,'530','山东省','菏泽市'),(104,'531','山东省','济南市'),(105,'532','山东省','青岛市'),(106,'533','山东省','淄博市'),(107,'534','山东省','德州市'),(108,'535','山东省','烟台市'),(109,'536','山东省','淮坊市'),(110,'537','山东省','济宁市'),(111,'538','山东省','泰安市'),(112,'539','山东省','临沂市'),(113,'550','安徽省','滁州市'),(114,'551','安徽省','合肥市'),(115,'552','安徽省','蚌埠市'),(116,'553','安徽省','芜湖市'),(117,'554','安徽省','淮南市'),(118,'555','安徽省','马鞍山'),(119,'556','安徽省','安庆市'),(120,'557','安徽省','宿州市'),(121,'558','安徽省','阜阳市'),(122,'559','安徽省','黄山市'),(123,'561','安徽省','淮北市'),(124,'562','安徽省','铜陵市'),(125,'563','安徽省','宣城市'),(126,'564','安徽省','六安市'),(127,'565','安徽省','巢湖市'),(128,'566','安徽省','贵池市'),(129,'570','浙江省','衢州市'),(130,'571','浙江省','杭州市'),(131,'572','浙江省','湖州市'),(132,'573','浙江省','嘉兴市'),(133,'574','浙江省','宁波市'),(134,'575','浙江省','绍兴市'),(135,'576','浙江省','台州市'),(136,'577','浙江省','温州市'),(137,'578','浙江省','丽水市'),(138,'579','浙江省','金华市'),(139,'580','浙江省','舟山市'),(140,'591','福建省','福州市'),(141,'592','福建省','厦门市'),(142,'593','福建省','宁德市'),(143,'594','福建省','莆田市'),(144,'595','福建省','泉州市'),(145,'595','福建省','晋江市'),(146,'596','福建省','漳州市'),(147,'597','福建省','龙岩市'),(148,'598','福建省','三明市'),(149,'599','福建省','南平市'),(150,'27','湖北省','武汉市'),(151,'710','湖北省','襄城市'),(152,'711','湖北省','鄂州市'),(153,'712','湖北省','孝感市'),(154,'713','湖北省','黄州市'),(155,'714','湖北省','黄石市'),(156,'715','湖北省','咸宁市'),(157,'716','湖北省','荆沙(江陵)'),(158,'717','湖北省','宜昌市'),(159,'718','湖北省','恩施市'),(160,'719','湖北省','十堰市'),(161,'0722','湖北省','随枣市'),(162,'724','湖北省','荆门市'),(163,'728','湖北省','江汉(仙桃)'),(164,'730','湖南省','岳阳市'),(165,'731','湖南省','长沙市'),(166,'732','湖南省','湘潭市'),(167,'733','湖南省','株州市'),(168,'734','湖南省','衡阳市'),(169,'735','湖南省','郴州市'),(170,'736','湖南省','常德市'),(171,'737','湖南省','益阳市'),(172,'738','湖南省','娄底市'),(173,'739','湖南省','邵阳市'),(174,'743','湖南省','吉首市'),(175,'744','湖南省','张家界'),(176,'745','湖南省','怀化市'),(177,'746','湖南省','永州冷'),(178,'20','广东省','广州市'),(179,'751','广东省','韶关市'),(180,'752','广东省','惠州市'),(181,'753','广东省','梅州市'),(182,'754','广东省','汕头市'),(183,'755','广东省','深圳市'),(184,'756','广东省','珠海市'),(185,'757','广东省','佛山市'),(186,'758','广东省','肇庆市'),(187,'759','广东省','湛江市'),(188,'760','广东省','中山市'),(189,'762','广东省','河源市'),(190,'763','广东省','清远市'),(191,'765','广东省','顺德市'),(192,'766','广东省','云浮市'),(193,'768','广东省','潮州市'),(194,'769','广东省','东莞市'),(195,'660','广东省','汕尾市'),(196,'661','广东省','潮阳市'),(197,'662','广东省','阳江市'),(198,'663','广东省','揭西市'),(199,'770','广西省','防城港'),(200,'771','广西省','南宁市'),(201,'772','广西省','柳州市'),(202,'773','广西省','桂林市'),(203,'774','广西省','梧州市'),(204,'775','广西省','玉林市'),(205,'776','广西省','百色市'),(206,'777','广西省','钦州市'),(207,'778','广西省','河池市'),(208,'779','广西省','北海市'),(209,'790','江西省','新余市'),(210,'791','江西省','南昌市'),(211,'792','江西省','九江市'),(212,'793','江西省','上饶市'),(213,'794','江西省','临川市'),(214,'795','江西省','宜春市'),(215,'796','江西省','吉安市'),(216,'797','江西省','赣州市'),(217,'798','江西省','景德镇'),(218,'799','江西省','萍乡市'),(219,'701','江西省','鹰潭市'),(220,'28','四川省','成都市'),(221,'810','四川省','涪陵市'),(222,'811','四川省','重庆市'),(223,'812','四川省','攀枝花'),(224,'813','四川省','自贡市'),(225,'814','四川省','永川市'),(226,'816','四川省','绵阳市'),(227,'817','四川省','南充市'),(228,'818','四川省','达县市'),(229,'819','四川省','万县市'),(230,'825','四川省','遂宁市'),(231,'826','四川省','广安市'),(232,'827','四川省','巴中市'),(233,'830','四川省','泸州市'),(234,'831','四川省','宜宾市'),(235,'832','四川省','内江市'),(236,'833','四川省','乐山市'),(237,'834','四川省','西昌市'),(238,'835','四川省','雅安市'),(239,'835','四川省','雅安市'),(240,'836','四川省','康定市'),(241,'837','四川省','马尔康'),(242,'838','四川省','德阳市'),(243,'839','四川省','广元市'),(244,'851','贵州省','贵阳市'),(245,'852','贵州省','遵义市'),(246,'853','贵州省','安顺市'),(247,'854','贵州省','都均市'),(248,'855','贵州省','凯里市'),(249,'856','贵州省','铜仁市'),(250,'857','贵州省','毕节市'),(251,'857','贵州省','毕节市'),(252,'859','贵州省','兴义市'),(253,'870','云南省','昭通市'),(254,'871','云南省','昆明市'),(255,'872','云南省','大理市'),(256,'873','云南省','个旧市'),(257,'874','云南省','曲靖市'),(258,'875','云南省','保山市'),(259,'876','云南省','文山市'),(260,'877','云南省','玉溪市'),(261,'878','云南省','楚雄市'),(262,'879','云南省','思茅市'),(263,'691','云南省','景洪市'),(264,'692','云南省','潞西市'),(265,'881','云南省','东川市'),(266,'883','云南省','临沧市'),(267,'886','云南省','六库市'),(268,'887','云南省','中甸市'),(269,'888','云南省','丽江市'),(270,'898','海南省','儋州'),(271,'898','海南省','海口市'),(272,'898','海南省','三亚市'),(273,'29','陕西省','西安市'),(274,'910','陕西省','咸阳市'),(275,'911','陕西省','延安市'),(276,'912','陕西省','榆林市'),(277,'913','陕西省','渭南市'),(278,'914','陕西省','商洛市'),(279,'915','陕西省','安康市'),(280,'916','陕西省','汉中市'),(281,'917','陕西省','宝鸡市'),(282,'919','陕西省','铜川市'),(283,'930','甘肃省','临夏市'),(284,'931','甘肃省','兰州市'),(285,'932','甘肃省','定西市'),(286,'933','甘肃省','平凉市'),(287,'934','甘肃省','西峰市'),(288,'935','甘肃省','武威市'),(289,'936','甘肃省','张掖市'),(290,'937','甘肃省','酒泉市'),(291,'938','甘肃省','天水市'),(292,'941','甘肃省','甘南州'),(293,'943','甘肃省','白银市'),(294,'891','西藏','拉萨市'),(295,'892','西藏','日喀则'),(296,'893','西藏','山南市'),(297,'951','宁夏','银川市'),(298,'952','宁夏','石嘴山'),(299,'953','宁夏','吴忠市'),(300,'954','宁夏','固原市'),(301,'971','青海省','西宁市'),(302,'972','青海省','海东市'),(303,'973','青海省','同仁市'),(304,'974','青海省','共和市'),(305,'975','青海省','玛沁市'),(306,'976','青海省','玉树市'),(307,'977','青海省','德令哈');

#
# Structure for table "gps_activity_info"
#

DROP TABLE IF EXISTS `gps_activity_info`;
CREATE TABLE `gps_activity_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuuid` varchar(36) NOT NULL DEFAULT '' COMMENT '唯一标示',
  `longitude` varchar(32) NOT NULL DEFAULT '' COMMENT '经度',
  `latitude` varchar(32) NOT NULL DEFAULT '' COMMENT '纬度',
  `geohash` varchar(128) NOT NULL DEFAULT '' COMMENT 'geohash',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `last_update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `actuuid` (`actuuid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='活动地理信息表';

#
# Data for table "gps_activity_info"
#

INSERT INTO `gps_activity_info` VALUES (16,'16c71b7d-a240-4ec0-9d5c-2be1df9b50b6','108.990522','34.253745','wqjdb5sqx0ze','2015-05-31 10:47:42','2015-05-31 11:17:10'),(17,'a5679a02-f5db-4767-b838-b0eff99f110f','108.937884','34.180789','wqj6qyqjs8vu','2015-05-31 10:53:13','2015-05-31 11:17:13');

#
# Structure for table "gps_runner_info"
#

DROP TABLE IF EXISTS `gps_runner_info`;
CREATE TABLE `gps_runner_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL DEFAULT '' COMMENT '唯一标示',
  `longitude` varchar(32) NOT NULL DEFAULT '' COMMENT '经度',
  `latitude` varchar(32) NOT NULL DEFAULT '' COMMENT '纬度',
  `geohash` varchar(128) NOT NULL DEFAULT '' COMMENT 'geohash',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `last_update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='地理信息表';

#
# Data for table "gps_runner_info"
#

INSERT INTO `gps_runner_info` VALUES (1,'c362342a-b6de-44d0-b5ec-91234a54e585','108.905907','34.250045','wqj6y5nbgx4h','2015-05-30 11:47:29','2015-06-05 15:42:27'),(7,'e9f0b704-8b52-4a8c-917d-6fa43dff7bda','108.933161','34.24063','wqj6yc6dnsh1','2015-05-30 12:31:53','2015-05-30 11:47:29'),(8,'16c71b7d-a240-4ec0-9d5c-2be1df9b50b6','108.874421','34.184215','wqj6mrpcmzs3','2015-05-30 12:33:14','2015-05-30 11:47:29'),(9,'a5679a02-f5db-4767-b838-b0eff99f110f','116.355071','39.898637','wx4dzxr4mtj2','2015-05-30 12:36:42','2015-05-30 11:47:29');

#
# Structure for table "runner_relationship"
#

DROP TABLE IF EXISTS `runner_relationship`;
CREATE TABLE `runner_relationship` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attention_uuid` varchar(36) NOT NULL DEFAULT '' COMMENT '关注者uuid',
  `passive_attention_uuid` varchar(36) NOT NULL DEFAULT '' COMMENT '被关注者uuid',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `last_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友关系';

#
# Data for table "runner_relationship"
#


#
# Structure for table "runner_user"
#

DROP TABLE IF EXISTS `runner_user`;
CREATE TABLE `runner_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL DEFAULT '' COMMENT '唯一标示',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '昵称',
  `login_name` varchar(64) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL DEFAULT '',
  `salt` varchar(64) NOT NULL DEFAULT '',
  `roles` varchar(255) NOT NULL DEFAULT '',
  `sex` int(11) NOT NULL DEFAULT '0' COMMENT '性别',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `signature` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `cloud_id` varchar(255) DEFAULT NULL COMMENT '第三方ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`),
  UNIQUE KEY `login_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='用户表';

#
# Data for table "runner_user"
#

INSERT INTO `runner_user` VALUES (1,'c31cfe4b-4c31-49ae-9e96-ffe239cdbe6c','admin','admin','dc9f459ceeb3448533253085e508c47bfc2f29bf','7faaa3e4dc341755','',0,100,NULL,NULL,'2015-06-04 17:35:49','2015-06-04 17:35:59'),(5,'c362342a-b6de-44d0-b5ec-91234a54e585','wangzhichao','wangzhichao','3092260d55392d7b58aeb64855dfb02bed0c5ae1','babf29af136e0f51','',0,22,NULL,'12345','2015-05-28 20:33:09','2015-06-02 18:11:44'),(16,'16c71b7d-a240-4ec0-9d5c-2be1df9b50b6','gaufy','gaufy','c38f4715e7e1bd56e2fd1858b1367469fa02e998','3c6524907953aea9','',0,30,NULL,'123456','2015-05-30 12:27:51','2015-05-30 20:54:40'),(17,'a5679a02-f5db-4767-b838-b0eff99f110f','line','line','7766f42cb79ee2e6304015695b1afe0ec8565340','9ba557379c500b1f','',1,27,NULL,'123456','2015-05-30 12:29:29','2015-05-30 12:29:29'),(18,'e9f0b704-8b52-4a8c-917d-6fa43dff7bda','grass','grass','858df5871d5306e7b941851e23110d6d6b699cdb','6c6efc6224bf4f5d','',1,24,NULL,'123456','2015-05-30 12:29:36','2015-05-30 12:29:36');
