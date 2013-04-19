-- CREATE DATABASE `xmetdb` /*!40100 DEFAULT CHARACTER SET utf8 */;

-- -----------------------------------------------------
-- Users. If registered, 'username' points to xmet_users.users table
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE  `user` (
  `iduser` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL COMMENT 'OpenAM user name',
  `title` varchar(45) DEFAULT NULL,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `institute` varchar(128) DEFAULT NULL,
  `weblog` varchar(45) DEFAULT NULL,
  `homepage` varchar(45) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
   `keywords` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '""',
  `reviewer` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'true if wants to become a reviewer',
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `Index_2` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Organisation, project, linked to OpenAM groups
-- -----------------------------------------------------
DROP TABLE IF EXISTS `organisation`;
CREATE TABLE  `organisation` (
  `idorganisation` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `ldapgroup` varchar(128) DEFAULT NULL,
  `cluster` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`idorganisation`),
  UNIQUE KEY `Index_2` (`name`),
  KEY `Index_3` (`cluster`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Projects
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE  `project` (
  `idproject` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `ldapgroup` varchar(128) DEFAULT NULL,
  `cluster` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`idproject`),
  UNIQUE KEY `Index_2` (`name`),
  KEY `Index_3` (`cluster`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- -----------------------------------------------------
-- User affiliations 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_organisation`;
CREATE TABLE  `user_organisation` (
  `iduser` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idorganisation` int(10) unsigned NOT NULL,
  `priority` int(2) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`iduser`,`idorganisation`),
  KEY `FK_user_organisation_2` (`idorganisation`),
  KEY `Index_3` (`iduser`,`priority`),
  CONSTRAINT `FK_user_organisation_2` FOREIGN KEY (`idorganisation`) REFERENCES `organisation` (`idorganisation`) ON UPDATE CASCADE,
  CONSTRAINT `FK_user_organisation_1` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Projects the user is working on
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_project`;
CREATE TABLE  `user_project` (
  `iduser` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idproject` int(10) unsigned NOT NULL,
  `priority` int(2) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`iduser`,`idproject`),
  KEY `FK_user_project_2` (`idproject`),
  KEY `Index_3` (`iduser`,`priority`),
  CONSTRAINT `FK_user_project_2` FOREIGN KEY (`idproject`) REFERENCES `project` (`idproject`) ON UPDATE CASCADE,
  CONSTRAINT `FK_user_project_1` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Protocols metadata & placeholder for data templates. 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `protocol`;
CREATE TABLE `protocol` (
  `idprotocol` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `version` int(10) unsigned NOT NULL DEFAULT '1' COMMENT 'Version',
  `title` varchar(32) NOT NULL COMMENT 'Title',
  `qmrf_number` varchar(36) NOT NULL COMMENT 'QMRF Number',
  `abstract` varchar(255) DEFAULT NULL,
  `iduser` int(10) unsigned NOT NULL COMMENT 'Link to user table',
  `idproject` int(10) unsigned NOT NULL COMMENT 'Link to projects table',
  `idorganisation` int(10) unsigned NOT NULL COMMENT 'Link to org table',
  `filename` text COMMENT 'Path to file name',
  `reference` varchar(255) DEFAULT NULL COMMENT 'Reference',
  `status` enum('RESEARCH','SOP') NOT NULL DEFAULT 'RESEARCH' COMMENT 'Research or Standard Operating Procedure',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last updated',
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `published_status` enum('draft','published','archived','deleted') NOT NULL DEFAULT 'draft',
  `atom_uncertainty` enum('Certain','Uncertain') NOT NULL DEFAULT 'Uncertain',
  `product_amount` enum('Major','Minor','Unknown') NOT NULL DEFAULT 'Unknown',
  `curatedby` int(10) unsigned DEFAULT NULL,
  `curated` tinyint(1) DEFAULT '0' COMMENT 'Curated - yes / no',
  PRIMARY KEY (`idprotocol`,`version`) USING BTREE,
  UNIQUE KEY `qmrf_number` (`qmrf_number`),
  KEY `Index_3` (`title`),
  KEY `FK_protocol_1` (`idproject`),
  KEY `FK_protocol_2` (`idorganisation`),
  KEY `FK_protocol_3` (`iduser`),
  KEY `updated` (`updated`),
  KEY `Index_8` (`published_status`),
  KEY `Index_9` (`atom_uncertainty`),
  KEY `Index_10` (`product_amount`),
  KEY `Index_11` (`reference`),
  KEY `FK_protocol_4_idx` (`curatedby`),
  CONSTRAINT `FK_protocol_4` FOREIGN KEY (`curatedby`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_protocol_1` FOREIGN KEY (`idproject`) REFERENCES `project` (`idproject`),
  CONSTRAINT `FK_protocol_2` FOREIGN KEY (`idorganisation`) REFERENCES `organisation` (`idorganisation`),
  CONSTRAINT `FK_protocol_3` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Protocol authors
-- -----------------------------------------------------
DROP TABLE IF EXISTS `protocol_authors`;
CREATE TABLE  `protocol_authors` (
  `idprotocol` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL,
  `iduser` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idprotocol`,`version`,`iduser`) USING BTREE,
  KEY `FK_protocol_authors_2` (`iduser`),
  CONSTRAINT `FK_protocol_authors_1` FOREIGN KEY (`idprotocol`, `version`) REFERENCES `protocol` (`idprotocol`, `version`),
  CONSTRAINT `FK_protocol_authors_2` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Attachments 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `attachments`;
CREATE TABLE  `attachments` (
  `idattachment` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idprotocol` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` text NOT NULL,
  `type` enum('data_training','data_validation','document') NOT NULL DEFAULT 'document',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `format` varchar(32) NOT NULL DEFAULT 'txt',
  `original_name` text,
  `imported` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idattachment`) USING BTREE,
  UNIQUE KEY `Index_4` (`idprotocol`,`version`,`type`) USING BTREE,
  KEY `name` (`name`),
  KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Keywords. Want to do full text search, thus MyISAM. 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `keywords`;
CREATE TABLE  `keywords` (
  `idprotocol` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  `keywords` text NOT NULL COMMENT 'All keywords semicolon delimited',
  PRIMARY KEY (`idprotocol`,`version`) USING BTREE,
  FULLTEXT KEY `Index_2` (`keywords`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Enzymes 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE  `template` (
  `idtemplate` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(16) NOT NULL,
  `uniprot` varchar(16) DEFAULT NULL COMMENT 'UniProt ID',
  `alleles` text,
  PRIMARY KEY (`idtemplate`),
  UNIQUE KEY `template_code` (`code`,`name`) USING BTREE,
  UNIQUE KEY `template_uniprot` (`uniprot`) USING BTREE,
  KEY `template_alleles` (`alleles`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Endpoints related to the QMRF document
-- -----------------------------------------------------
DROP TABLE IF EXISTS `protocol_endpoints`;
CREATE TABLE  `protocol_endpoints` (
  `idprotocol` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL,
  `idtemplate` int(10) unsigned NOT NULL,
  `allele` varchar(15) NOT NULL,
  PRIMARY KEY (`idprotocol`,`version`,`idtemplate`) USING BTREE,
  KEY `FK_protocol_template_2` (`idtemplate`),
  KEY `Index_3` (`idtemplate`,`allele`),
  CONSTRAINT `FK_protocol_template_1` FOREIGN KEY (`idprotocol`, `version`) REFERENCES `protocol` (`idprotocol`, `version`),
  CONSTRAINT `FK_protocol_template_2` FOREIGN KEY (`idtemplate`) REFERENCES `template` (`idtemplate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Saved queries
-- -----------------------------------------------------
DROP TABLE IF EXISTS `alert`;
CREATE TABLE  `alert` (
  `idquery` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT 'query name',
  `query` text NOT NULL COMMENT 'query content',
  `qformat` enum('FREETEXT','SPARQL') NOT NULL DEFAULT 'FREETEXT' COMMENT 'query format',
  `rfrequency` enum('secondly','minutely','hourly','daily','weekly','monthly','yearly') NOT NULL DEFAULT 'weekly',
  `rinterval` int(10) unsigned NOT NULL DEFAULT '1',
  `iduser` int(10) unsigned NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sent` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`idquery`),
  KEY `FK_query_1` (`iduser`,`rfrequency`) USING BTREE,
  CONSTRAINT `FK_query_1` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- -----------------------------------------------------
-- DB schema version
-- -----------------------------------------------------
DROP TABLE IF EXISTS `version`;
CREATE TABLE  `version` (
  `idmajor` int(5) unsigned NOT NULL,
  `idminor` int(5) unsigned NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`idmajor`,`idminor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
insert into version (idmajor,idminor,comment) values (2,16,"XMETDB schema");

-- -----------------------------------------------------
-- Create new protocol version
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `createProtocolVersion`;
DELIMITER $$
CREATE PROCEDURE createProtocolVersion(
                IN protocol_qmrf_number VARCHAR(36),
                IN new_qmrf_number VARCHAR(36),
                IN title_new VARCHAR(255),
                IN abstract_new TEXT,
                OUT version_new INT)
begin
    DECLARE no_more_rows BOOLEAN;
    DECLARE pid INT;
    --
    DECLARE protocols CURSOR FOR
    	select max(version)+1,idprotocol from protocol where idprotocol in (select idprotocol from protocol where qmrf_number=protocol_qmrf_number) LIMIT 1;
    	
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_rows = TRUE;
    
    OPEN protocols;
    the_loop: LOOP

	  FETCH protocols into version_new,pid;
	  IF no_more_rows THEN
		  CLOSE protocols;
		  LEAVE the_loop;
  	END IF;
        
    -- update published status of the old version to archived

  	-- create new version
    insert into protocol (idprotocol,version,title,qmrf_number,abstract,iduser,curated,idproject,idorganisation,filename,status,created,published_status,atom_uncertainty,product_amount)
    select idprotocol,version_new,ifnull(title_new,title),concat("XMETDB",idprotocol,"v",version_new),ifnull(abstract_new,abstract),iduser,curated,idproject,idorganisation,filename,status,now(),'draft',atom_uncertainty,product_amount 
    from protocol where qmrf_number=protocol_qmrf_number;
      	
   	-- copy authors
    insert into protocol_authors (idprotocol,version,iduser)
    select idprotocol,version_new,protocol_authors.iduser from protocol_authors join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;

   	-- copy endpoints
    insert into protocol_endpoints (idprotocol,version,idtemplate,allele)
    select idprotocol,version_new,idtemplate,allele from protocol_endpoints join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;
    
   	-- copy keywords
    insert into keywords (idprotocol,version,keywords)
    select idprotocol,version_new,keywords from keywords join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;    
    
	-- Set the previous protocol status to archived
    update protocol set published_status='archived' where qmrf_number=protocol_qmrf_number;

	-- copy attachments
	insert into attachments (idprotocol,version,name,description,type,updated,format,original_name,imported)
	select p.idprotocol,version_new,name,description,type,now(),format,original_name,imported from attachments a, protocol p
	where p.idprotocol=a.idprotocol and p.version = a.version and p.qmrf_number = protocol_qmrf_number;
	
    END LOOP the_loop;

end $$

DELIMITER ;

DROP PROCEDURE IF EXISTS `createProtocolCopy`;
DELIMITER $$
CREATE PROCEDURE createProtocolCopy(
                IN protocol_qmrf_number VARCHAR(36),
                IN user_name VARCHAR(45),
                OUT new_qmrf_number VARCHAR(36))
begin
	DECLARE new_id INT;
	
  	-- create new version
    insert into protocol (idprotocol,version,title,qmrf_number,abstract,iduser,curated,idproject,idorganisation,filename,status,created,published_status,atom_uncertainty,product_amount)
    select null,1,protocol.title,concat("XMETDB",idprotocol,"v",now()),abstract,user.iduser,0,idproject,idorganisation,filename,status,now(),'draft',atom_uncertainty,product_amount 
    from protocol join user where qmrf_number=protocol_qmrf_number and username=user_name;
    
    SELECT LAST_INSERT_ID() INTO new_id;
	UPDATE protocol set qmrf_number=concat("XMETDB",new_id) where idprotocol=new_id and version=1;
	SET new_qmrf_number = concat("XMETDB",new_id);
	
   	-- copy authors
    insert into protocol_authors (idprotocol,version,iduser)
    select new_id,1,protocol_authors.iduser from protocol_authors join protocol using(idprotocol,version) where qmrf_number=protocol_qmrf_number;

   	-- copy endpoints
    insert into protocol_endpoints (idprotocol,version,idtemplate,allele)
    select new_id,1,idtemplate,allele from protocol_endpoints join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;
    
   	-- copy keywords
    insert into keywords (idprotocol,version,keywords)
    select new_id,1,concat('Copy of ',protocol_qmrf_number,' ',keywords) from keywords join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;    

	-- copy attachments
	insert into attachments (idprotocol,version,name,description,type,updated,format,original_name,imported)
	select new_id,1,name,description,type,now(),format,original_name,imported from attachments a, protocol p
	where p.idprotocol=a.idprotocol and p.version = a.version and p.qmrf_number = protocol_qmrf_number;
	
  
end $$

DELIMITER ;

-- -----------------------------------------------------
-- Only documents with 'deleted' status are physically deleted.
-- Documents with non-deleted status are only assigned 'deleted' status
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `deleteProtocol`;
DELIMITER $$
CREATE PROCEDURE deleteProtocol(IN protocol_qmrf_number VARCHAR(36))
begin
	delete a from protocol_endpoints a,protocol p where a.idprotocol=p.idprotocol and a.version=p.version and qmrf_number=protocol_qmrf_number and published_status='deleted';
	delete a from keywords a,protocol p where a.idprotocol=p.idprotocol and a.version=p.version and qmrf_number=protocol_qmrf_number and published_status='deleted';
	delete a from protocol_authors a,protocol p where a.idprotocol=p.idprotocol and a.version=p.version and qmrf_number=protocol_qmrf_number and published_status='deleted';
	delete a from attachments a, protocol p where a.idprotocol=p.idprotocol and a.version=p.version and qmrf_number=protocol_qmrf_number and published_status='deleted'; 
   	DELETE from protocol where qmrf_number=protocol_qmrf_number and published_status='deleted';
   
	-- otherwise	
   	UPDATE protocol set published_status='deleted' where qmrf_number=protocol_qmrf_number and published_status!='deleted';
end $$

DELIMITER ;

-- -----------------------------------------------------
-- Default set of enzymes
-- -----------------------------------------------------

LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;
INSERT INTO `template` VALUES (2,'cytochrome P450, family 3, subfamily A, polypeptide 4','CYP3A4','P08684','1A,1B,1C,1E,1F,1G,1H,1J,1K,1L,1M,1N,1P,1Q,1R,1S,1T,2,3,4,5,6,7,8,9,10,11,12,13,14,15A,15B,16A,16B,17,18A,18B,19,20,21,22'),(3,'cytochrome P450, family 2, subfamily E, polypeptide 1','CYP2E1','P05181','1A,1B,1C,1Cx2,2,3,4,5A,5B,6,7A,7B,7C'),(4,'cytochrome P450, family 2, subfamily D, polypeptide 6','CYP2D6','P10635','1A,1B,1C,1D,1E,1XN,2A,2B,2C,2D,2E,2F,2G,2H,2J,2K,2L,2M,2XN,3A,3B,4A,4B,4C,4D,4E,4F,4G,4H,4J,4K,4L,4M,4N,4X2,5,6A,6B,6C,6D,7,8,9,9x2,10A,10B,10C,10D,10X2,11,12,13,14A,14B,15,16,17,17XN,18,19,20,21A,21B,22,23,24,25,26,27,28,29,30,31,32,33,34,35A,35B,35X2,36,37,38,39,40,41,42,43,44,45A,45B,46,47,48,49,50,51,52,53,54,55,56A,56B,57,59,60,61,62,63,64,65,66,67,68A,68B,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84-101,102,103,104,105'),(5,'cytochrome P450, family 2, subfamily C, polypeptide 9','CYP2C9','P11712','1A,1B,1C,2A,2B,2C,3A,3B,4,5,6,7,8,9,10,11A,11B,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,34,33,34,35,36-56,57'),(6,'cytochrome P450, family 1, subfamily A, polypeptide 2','CYP1A2','P05177','1A,1B,1C,1D,1E,1F,1G,1H,1J,1K,1L,1M,1N,1P,1Q,1R,1S,1T,1U,1V,1W,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21'),(7,'cytochrome P450, family 2, subfamily C, polypeptide 19','CYP2C19','P33261','1A,1B,1C,2A,2B,2C,2D,3A,3B,4A,4B,5A,5B,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28'),(8,'cytochrome P450, family 1, subfamily A, polypeptide 1','CYP1A1','P04798','1,2A,2B,2C,3,4,5,6,7,8,9,10,11'),(9,'cytochrome P450, family 1, subfamily B, polypeptide 1','CYP1B1','Q16678','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26'),(10,'cytochrome P450, family 26, subfamily A, polypeptide 1','CYP26A1','O43174',''),(11,'cytochrome P450, family 26, subfamily B, polypeptide 1','CYP26B1','Q9NR63',NULL),(12,'cytochrome P450, family 2, subfamily S, polypeptide 1','CYP2S1','Q96SQ9','1A,1B,1C,1D,1E,1F,1G,1H,2,3,4,5A,5B'),(13,'cytochrome P450, family 4, subfamily B, polypeptide 1','CYP4B1','P13584','1,2A,2B,3,4,5,6,7'),(14,'cytochrome P450, family 2, subfamily W, polypeptide 1','CYP2W1','Q8TAV3','1A,1B,2,3,4,5,6'),(15,'cytochrome P450, family 2, subfamily C, polypeptide 8','CYP2C8','P10632','1A,1B,1C,2,3,4,5,6,7,8,9,10,11,12,13,14'),(16,'cytochrome P450, family 3, subfamily A, polypeptide 5','CYP3A5','P20815','1A,1B,1C,1D,1E,2,3A,3B,3C,3D,3E,3F,3G,3H,3I,3J,3K,3L,4,5,6,7,8,9,10,11'),(17,'Flavin-containing monooxygenase 2','FMO 2','Q99518','test,1,2,3'),(18,'Flavin-containing monooxygenase 1','FMO 1','Q01740','1'),(19,'Flavin-containing monooxygenase 5','FMO 5','P49326 ',NULL),(20,'cytochrome P450, family 2, subfamily A, polypeptide 6','CYP2A6','P11509','1A,1B1,1B2,1B3,1B4,1B5,1B6,1B7,1B8,1B9,1B10,1B11,1B12,1B13,1B14,1B15,1B16,1B17,1C,1D,1E,1F,1G,1H,1J,1K,1L,1X2A,1X2B,2,3,4A,4B,4C,4D,4E,4F,4G,4H,5,6,7,8,9A,9B,10,11,12A,12B,12C,13,14,15,16,17,18A,18B,18C,19,20,21,22,23,24A,24B,25,26,27,28A,28B,29,30,31A,31B,32,33,34,35A,35B,36,37'),(21,'cytochrome P450, family 2, subfamily A, polypeptide 13','CYP2A13','Q16696','1A,1B,1C,1D,1E,1F,1G,1H,1J,1K,1L,2A,2B,3,4,5,6,7,8,9,10'),(22,'Flavin-containing monooxygenase 4','FMO 4','P31512 ','1,2,3,4'),(25,'Flavin-containing monooxygenase 3','FMO 3','P31513','A,Ab,x'),(26,'Aldehyde oxidase','AOX1','Q06278',NULL),(27,'Amine oxidase A','MAOA','P21397',NULL),(28,'Amine oxidase B','MAOB','P27338',NULL),(29,'cytochrome P450, family 2, subfamily B, polypeptide 6','CYP2B6','P20813','1A,1B,1C,1D,1E,1F,1G,1H,1J,1K,1L,1M,1N,2A,2B,3,4A,4B,4C,4D,5A,5B,5C,6A,6B,6C,7A,7B,8,9,10,11A,11B,12,13A,13B,14,15A,15B,16,17A,17B,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37'),(30,'cytochrome P450, family 2, subfamily F, polypeptide 1','CYP2F1','Q32MN5','1,2A,2B,3,4,5A,5B,6'),(31,'cytochrome P450, family 2, subfamily J, polypeptide 2','CYP2J2','P51589','1,2,3,4,5,6,7,8,9,10'),(32,'cytochrome P450, family 2, subfamily R, polypeptide 1','CYP2R1','Q6VVX0','1,2'),(33,'cytochrome P450, family 3, subfamily A, polypeptide 7','CYP3A7','P24462','1A,1B,1C,1D,1E,2,3'),(34,'cytochrome P450, family 3, subfamily A, polypeptide 43','CYP3A43','Q9HB55','1A,1B,2A,2B,3'),(35,'cytochrome P450, family 4, subfamily A, polypeptide 11','CYP4A11','Q02928 ','1'),(36,'cytochrome P450, family 4, subfamily A, polypeptide 22','CYP4A22','Q5TCH4','1,2,3A,3B,3C,3D,3E,4,5,6,7,8,9,10,11,12A,12B,13A,13B,14,15'),(37,'cytochrome P450, family 4, subfamily F, polypeptide 2','CYP4F2','P78329','1,2,3'),(38,'cytochrome P450, family 5, subfamily A, polypeptide 1','CYP5A1','P24557','1A,1B,1C,1D,2,3,4,5,6,7,8,9'),(39,'cytochrome P450, family 8, subfamily A, polypeptide 1','CYP8A1','Q16647','1A,1B,1C,1D,1E,1F,1G,1H,1J,1K,1L,2,3,4'),(40,'cytochrome P450, family 19, subfamily A, polypeptide 1','CYP19A1','P11511','1,2,3,4,5'),(41,'cytochrome P450, family 21, subfamily A, polypeptide 2','CYP21A2','P08686','1A,1B,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20A,20B,20C,20D,20E,20F,20G,20H,20J,20K,20J,20K,20L,20M,20N,20P,20Q,20R,20S,20T,20U,20V,9+17'),(42,'cytochrome P450, family 17, subfamily A, polypeptide 1','CYP17A1','P05093',NULL),(43,'Glutathione S-transferase A1','GSTA1','P08263',NULL),(44,'Glutathione S-transferase A2','GSTA2','P09210',NULL),(45,'Glutathione S-transferase A3','GSTA3','Q16772',NULL),(46,'Glutathione S-transferase A4','GSTA4','O15217',NULL),(47,'Glutathione S-transferase A5','GSTA5','Q7RTV2',NULL),(48,'Glutathione S-transferase kappa 1','GSTK1','Q9Y2Q3',NULL),(49,'Glutathione S-transferase Mu 1','GSTM1','P09488',NULL),(50,'Glutathione S-transferase Mu 2','GSTM2','P28161',NULL),(51,'Glutathione S-transferase Mu 3','GSTM3','P21266',NULL),(52,'Glutathione S-transferase Mu 4','GSTM4','Q03013',NULL),(53,'Glutathione S-transferase Mu 5','GSTM5','P46439',NULL),(54,'Glutathione S-transferase omega-1','GSTO1','P78417',NULL),(55,'Glutathione S-transferase omega-2','GSTO2','Q9H4Y5',NULL),(56,'Glutathione S-transferase P','GSTP1','P09211',NULL),(57,'Glutathione S-transferase theta-1','GSTT1','P30711',NULL),(58,'Glutathione S-transferase theta-2','GSTT2','P0CG29',NULL),(59,'Glutathione S-transferase theta-4','GSTT4','A8MPT4',NULL),(60,'Microsomal glutathione S-transferase 3','MGST3','O14880',NULL),(61,'Microsomal glutathione S-transferase 2','MGST2','Q99735',NULL),(62,'Microsomal glutathione S-transferase 1','MGST1','P10620',NULL),(63,'Alcohol dehydrogenase 1A','ADH1A','P07327',NULL),(64,'Alcohol dehydrogenase 1B','ADH1B','P00325',NULL),(65,'Alcohol dehydrogenase 1C','ADH1C','P00326',NULL),(66,'Alcohol dehydrogenase 4','ADH4','P08319',NULL),(67,'Alcohol dehydrogenase 5','ADH5','P11766',NULL),(68,'Alcohol dehydrogenase 6','ADH6','P28332',NULL),(69,'Alcohol dehydrogenase 7','ADH7','P40394',NULL),(70,'Epoxide hydrolase 1','EPHX1','P07099',NULL),(71,'Epoxide hydrolase 2','EPHX2','P34913',NULL),(72,'Epoxide hydrolase 3','EPHX3','Q9H6B9',NULL),(73,'Epoxide hydrolase 4','EPHX4','Q8IUS5',NULL),(74,'UDP-glucuronosyltransferase 1-1','UGT1A1','P22309',NULL),(75,'UDP-glucuronosyltransferase 1-3','UGT1A3','P35503',NULL),(76,'UDP-glucuronosyltransferase 1-4','UGT1A4','P22310',NULL),(77,'UDP-glucuronosyltransferase 1-5','UGT1A5','P35504',NULL),(78,'UDP-glucuronosyltransferase 1-6','UGT1A6','P19224',NULL),(79,'UDP-glucuronosyltransferase 1-7','UGT1A7','Q9HAW7',NULL),(80,'UDP-glucuronosyltransferase 1-8','UGT1A8','Q9HAW9',NULL),(81,'UDP-glucuronosyltransferase 1-9','UGT1A9','O60656',NULL),(82,'UDP-glucuronosyltransferase 1-10','UGT1A10','Q9HAW8',NULL),(83,'UDP-glucuronosyltransferase 2B4','UGT2B4','P06133',NULL),(84,'UDP-glucuronosyltransferase 2B7','UGT2B7','P16662',NULL),(85,'UDP-glucuronosyltransferase 2B10','UGT2B10','P36537',NULL),(86,'UDP-glucuronosyltransferase 2B11','UGT2B11','O75310',NULL),(87,'UDP-glucuronosyltransferase 2B15','UGT2B15','P54855',NULL),(88,'Sulfotransferase 1A1','SULT1A1','P50225',NULL),(89,'Sulfotransferase 1A2','SULT1A2','P50226',NULL),(90,'Sulfotransferase 1A3/1A4','SULT1A3','P50224',NULL),(91,'Sulfotransferase 1B1','SULT1B1','O43704',NULL),(92,'Sulfotransferase 1C2','SULT1C2','O00338',NULL),(93,'Sulfotransferase 1C4','SULT1C4','O75897',NULL),(94,'Sulfotransferase 1E1','SULT1E1','P49888',NULL),(95,'Sulfotransferase 2A1','SULT2A1','Q06520',NULL),(96,'Sulfotransferase  2B1','SULT2B1','O00204',NULL),(97,'Sulfotransferase 4A1','SULT4A1','Q9BR01',NULL);
/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;

-- -----------------------------------------------------
-- Default set of users
-- -----------------------------------------------------
insert into `user` values 
(1,"admin","","Administrator","","","","","","","",true),
(2,"guest","","Guest","User","","","","","","",true);

-- -----------------------------------------------------
-- Default project
-- -----------------------------------------------------
insert into project values (1,"XMETDB","xmetdb",null);
insert into user_project values (1,1,1);

-- -----------------------------------------------------
-- Default organisation
-- -----------------------------------------------------
insert into organisation values (1,"XMETDB","xmetdb",null);
insert into user_organisation values (1,1,1);