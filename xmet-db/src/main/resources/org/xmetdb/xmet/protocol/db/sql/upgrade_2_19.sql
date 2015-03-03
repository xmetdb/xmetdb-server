LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;
INSERT IGNORE INTO `template` VALUES (null,'Liver carboxylesterase 1','CES1','P23141',NULL),(null,'Cocaine esterase','CES2','O00748',NULL);
/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `protocol_links` (
  `idprotocol` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL,
  `type` varchar(64) NOT NULL,
  `id` varchar(64) NOT NULL,
  PRIMARY KEY (`idprotocol`,`version`,`type`,`id`) USING BTREE,
  KEY `link` (`id`),
  KEY `predicate` (`type`,`id`),
  CONSTRAINT `FK_protocol_links` FOREIGN KEY (`idprotocol`, `version`) REFERENCES `protocol` (`idprotocol`, `version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into version (idmajor,idminor,comment) values (2,19,"XMETDB schema");