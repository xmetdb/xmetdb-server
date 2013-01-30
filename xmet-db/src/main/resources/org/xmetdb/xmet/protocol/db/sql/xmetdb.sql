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
CREATE TABLE  `protocol` (
  `idprotocol` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `version` int(10) unsigned NOT NULL DEFAULT '1' COMMENT 'Version',
  `title` varchar(32) NOT NULL COMMENT 'Title',
  `qmrf_number` varchar(36) NOT NULL COMMENT 'QMRF Number',
  `abstract` varchar(255) DEFAULT NULL,
  `summarySearchable` tinyint(1) NOT NULL DEFAULT '1',
  `iduser` int(10) unsigned NOT NULL COMMENT 'Link to user table',
  `idproject` int(10) unsigned NOT NULL COMMENT 'Link to projects table',
  `idorganisation` int(10) unsigned NOT NULL COMMENT 'Link to org table',
  `filename` text COMMENT 'Path to file name',
  `reference` varchar(255) DEFAULT NULL COMMENT 'Reference',
  `status` enum('RESEARCH','SOP') NOT NULL DEFAULT 'RESEARCH' COMMENT 'Research or Standard Operating Procedure',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last updated',
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `published_status` enum('draft','submitted','under_review','returned_for_revision','review_completed','published','archived','deleted') NOT NULL DEFAULT 'draft',
  `atom_uncertainty` enum('Certain','Uncertain') NOT NULL DEFAULT 'Uncertain',
  `product_amount` enum('Major','Minor','Unknown') NOT NULL DEFAULT 'Unknown',
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
-- Endpoints. 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE  `template` (
  `idtemplate` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(16) DEFAULT NULL,
  `uri` text,
  `allele` text,
  PRIMARY KEY (`idtemplate`),
  UNIQUE KEY `template_name` (`name`) USING BTREE,
  UNIQUE KEY `template_code` (`code`) USING BTREE
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
insert into version (idmajor,idminor,comment) values (2,13,"XMETDB schema");

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
    insert into protocol (idprotocol,version,title,qmrf_number,abstract,iduser,summarySearchable,idproject,idorganisation,filename,status,created,published_status,atom_uncertainty,product_amount)
    select idprotocol,version_new,ifnull(title_new,title),concat("XMETDB",idprotocol,"v",version_new),ifnull(abstract_new,abstract),iduser,summarySearchable,idproject,idorganisation,filename,status,now(),'draft',atom_uncertainty,product_amount 
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

-- -----------------------------------------------------
-- Create an observation copy
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `createProtocolCopy`;
DELIMITER $$
CREATE PROCEDURE createProtocolCopy(
                IN protocol_qmrf_number VARCHAR(36),
                OUT new_qmrf_number VARCHAR(36))
begin
	DECLARE new_id INT;
	
  	-- create new version
    insert into protocol (idprotocol,version,title,qmrf_number,abstract,iduser,summarySearchable,idproject,idorganisation,filename,status,created,published_status,atom_uncertainty,product_amount)
    select null,1,title,concat("XMETDB",idprotocol,"v",now()),abstract,iduser,summarySearchable,idproject,idorganisation,filename,status,now(),'draft',atom_uncertainty,product_amount 
    from protocol where qmrf_number=protocol_qmrf_number;
    
    
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

insert into template (idtemplate,name,code,uri) values
(1,null,null,null),
(null,"cytochrome P450, family 3, subfamily A, polypeptide 4","CYP3A4","http://www.uniprot.org/uniprot/P08684"),
(null,"cytochrome P450, family 2, subfamily E, polypeptide 1","CYP2E1","http://www.uniprot.org/uniprot/P05181"),
(null,"cytochrome P450, family 2, subfamily D, polypeptide 6","CYP2D6","http://www.uniprot.org/uniprot/P10635"),
(null,"cytochrome P450, family 2, subfamily C, polypeptide 9","CYP2C9","http://www.uniprot.org/uniprot/P11712"),
(null,"cytochrome P450, family 1, subfamily A, polypeptide 2","CYP1A2","http://www.uniprot.org/uniprot/P05177"),
(null,"cytochrome P450, family 2, subfamily C, polypeptide 19","CYP2C19","http://www.uniprot.org/uniprot/P33261"),
(null,"cytochrome P450, family 1, subfamily A, polypeptide 1","CYP1A1","http://www.uniprot.org/uniprot/P04798"),
(null,"cytochrome P450, family 1, subfamily B, polypeptide 1","CYP1B1","http://www.uniprot.org/uniprot/Q16678"),
(null,"cytochrome P450, family 26, subfamily A, polypeptide 1","CYP26A1","http://www.uniprot.org/uniprot/O43174"),

(null,"cytochrome P450, family 26, subfamily B, polypeptide 1","CYP26B1","http://www.uniprot.org/uniprot/Q9NR63"),
(null,"cytochrome P450, family 2, subfamily S, polypeptide 1","CYP2S1","http://www.uniprot.org/uniprot/Q96SQ9"),
(null,"cytochrome P450, family 4, subfamily B, polypeptide 1","CYP4B1","http://www.uniprot.org/uniprot/P13584"),
(null,"cytochrome P450, family 2, subfamily W, polypeptide 1","CYP2W1","http://www.uniprot.org/uniprot/Q8TAV3"),
(null,"cytochrome P450, family 2, subfamily C, polypeptide 8","CYP2C8","http://www.uniprot.org/uniprot/P10632"),
(null,"cytochrome P450, family 3, subfamily A, polypeptide 5","CYP3A5","http://www.uniprot.org/uniprot/P20815");

-- Alleles as per allele nomenclature at http://www.cypalleles.ki.se/
  
update template set allele="1A,1B,1C,1E,1F,1G,1H,1J,1K,1L,1M,1N,1P,1Q,1R,1S,1T,2,3,4,5,6,7,8,9,10,11,12,13,14,15A,15B,16A,16B,17,18A,18B,19,20,21,22"
where code='CYP3A4';
update template set allele="1A,1B,1C,1Cx2,2,3,4,5A,5B,6,7A,7B,7C" where code='CYP2E1';
update template set allele="1A,1B,1C,1D,1E,1XN,2A,2B,2C,2D,2E,2F,2G,2H,2J,2K,2L,2M,2XN,3A,3B,4A,4B,4C,4D,4E,4F,4G,4H,4J,4K,4L,4M,4N,4X2,5,6A,6B,6C,6D,7,8,9,9x2,10A,10B,10C,10D,10X2,11,12,13,14A,14B,15,16,17,17XN,18,19,20,21A,21B,22,23,24,25,26,27,28,29,30,31,32,33,34,35A,35B,35X2,36,37,38,39,40,41,42,43,44,45A,45B,46,47,48,49,50,51,52,53,54,55,56A,56B,57,59,60,61,62,63,64,65,66,67,68A,68B,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84-101,102,103,104,105"
where code='CYP2D6';
update template set allele="1A,1B,1C,2A,2B,2C,3A,3B,4,5,6,7,8,9,10,11A,11B,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,34,33,34,35,36-56,57"
where code='CYP2C9';
update template set allele="1A,1B,1C,1D,1E,1F,1G,1H,1J,1K,1L,1M,1N,1P,1Q,1R,1S,1T,1U,1V,1W,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21"
where code='CYP1A2';
update template set allele="1A,1B,1C,2A,2B,2C,2D,3A,3B,4A,4B,5A,5B,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28"
where code='CYP2C19';
update template set allele="1,2A,2B,2C,3,4,5,6,7,8,9,10,11"
where code='CYP1A1';
update template set allele="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26"
where code='CYP1B1';
update template set allele="1A,1B,1C,1D,1E,1F,1G,1H,2,3,4,5A,5B"
where code='CYP2S1';
update template set allele="1A,1B,1C,1D,1E,2,3A,3B,3C,3D,3E,3F,3G,3H,3I,3J,3K,3L,4,5,6,7,8,9,10,11"
where code='CYP3A5';
update template set allele="1A,1B,1C,2,3,4,5,6,7,8,9,10,11,12,13,14"
where code='CYP2C8';
update template set allele="1,2A,2B,3,4,5,6,7"
where code='CYP4B1';
update template set allele="1A,1B,2,3,4,5,6"
where code='CYP2W1';
update template set allele="1,2,3,4"
where code='CYP26A1';

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