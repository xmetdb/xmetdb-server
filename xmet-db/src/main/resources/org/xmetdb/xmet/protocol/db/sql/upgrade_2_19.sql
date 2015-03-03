LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;
INSERT IGNORE INTO `template` VALUES (null,'Liver carboxylesterase 1','CES1','P23141',NULL),(null,'Cocaine esterase','CES2','O00748',NULL);
/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;

insert into version (idmajor,idminor,comment) values (2,19,"XMETDB schema");