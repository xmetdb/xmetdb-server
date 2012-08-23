-- CREATE DATABASE `tb` /*!40100 DEFAULT CHARACTER SET utf8 */;

-- -----------------------------------------------------
-- Users. If registered, 'username' points to tomcat_users table
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
  `title` varchar(255) NOT NULL COMMENT 'Title',
  `qmrf_number` varchar(36) NOT NULL COMMENT 'QMRF Number',
  `abstract` text,
  `summarySearchable` tinyint(1) NOT NULL DEFAULT '1',
  `iduser` int(10) unsigned NOT NULL COMMENT 'Link to user table',
  `idproject` int(10) unsigned NOT NULL COMMENT 'Link to projects table',
  `idorganisation` int(10) unsigned NOT NULL COMMENT 'Link to org table',
  `filename` text COMMENT 'Path to file name',
  `template` text COMMENT 'Data template',
  `status` enum('RESEARCH','SOP') NOT NULL DEFAULT 'RESEARCH' COMMENT 'Research or Standard Operating Procedure',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last updated',
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `published_status` enum('draft','submitted','under_review','returned_for_revision','review_completed','published','archived','deleted') NOT NULL DEFAULT 'draft',
  PRIMARY KEY (`idprotocol`,`version`) USING BTREE,
  UNIQUE KEY `qmrf_number` (`qmrf_number`),
  KEY `Index_3` (`title`),
  KEY `FK_protocol_1` (`idproject`),
  KEY `FK_protocol_2` (`idorganisation`),
  KEY `FK_protocol_3` (`iduser`),
  KEY `updated` (`updated`),
  KEY `Index_8` (`published_status`),
  CONSTRAINT `FK_protocol_1` FOREIGN KEY (`idproject`) REFERENCES `project` (`idproject`),
  CONSTRAINT `FK_protocol_2` FOREIGN KEY (`idorganisation`) REFERENCES `organisation` (`idorganisation`),
  CONSTRAINT `FK_protocol_3` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

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
CREATE TABLE  `attachments` (
  `idattachment` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idprotocol` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `description` text NOT NULL,
  `type` enum('data_training','data_validation','document') NOT NULL DEFAULT 'document',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `format` varchar(32) NOT NULL DEFAULT 'txt',
  `original_name` text,
  `imported` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idattachment`) USING BTREE,
  UNIQUE KEY `Index_4` (`idprotocol`,`version`,`name`) USING BTREE,
  KEY `name` (`name`),
  KEY `type` (`type`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
  `name` varchar(255) DEFAULT NULL COLLATE utf8_general_ci,
  `code` varchar(16) DEFAULT NULL COLLATE utf8_general_ci,
  `uri` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  PRIMARY KEY (`idtemplate`),
  UNIQUE KEY `template_list_index4157` (`name`,`code`) USING BTREE,
  KEY `Index_3` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Endpoints hierarchy
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE  `dictionary` (
  `idsubject` int(10) unsigned NOT NULL,
  `relationship` enum('is_a','is_part_of') COLLATE utf8_bin NOT NULL DEFAULT 'is_a',
  `idobject` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idsubject`,`relationship`,`idobject`),
  KEY `FK_dictionary_2` (`idobject`),
  CONSTRAINT `FK_dictionary_1` FOREIGN KEY (`idsubject`) REFERENCES `template` (`idtemplate`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_dictionary_2` FOREIGN KEY (`idobject`) REFERENCES `template` (`idtemplate`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- -----------------------------------------------------
-- Endpoints related to the QMRF document
-- -----------------------------------------------------
DROP TABLE IF EXISTS `protocol_endpoints`;
CREATE TABLE  `protocol_endpoints` (
  `idprotocol` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL,
  `idtemplate` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idprotocol`,`version`,`idtemplate`) USING BTREE,
  KEY `FK_protocol_template_2` (`idtemplate`),
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
insert into version (idmajor,idminor,comment) values (2,5,"QMRF schema");

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
    insert into protocol (idprotocol,version,title,qmrf_number,abstract,iduser,summarySearchable,idproject,idorganisation,filename,status,created,published_status)
    select idprotocol,version_new,ifnull(title_new,title),new_qmrf_number,ifnull(abstract_new,abstract),iduser,summarySearchable,idproject,idorganisation,null,status,now(),published_status 
    from protocol where qmrf_number=protocol_qmrf_number;
	
   	-- copy authors
    insert into protocol_authors (idprotocol,version,iduser)
    select idprotocol,version_new,protocol_authors.iduser from protocol_authors join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;

   	-- copy endpoints
    insert into protocol_endpoints (idprotocol,version,idtemplate)
    select idprotocol,version_new,idtemplate from protocol_endpoints join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;
    
   	-- copy keywords
    insert into keywords (idprotocol,version,keywords)
    select idprotocol,version_new,keywords from keywords join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;    
    
	-- move the qmrf number to the new version; replace the old one with qmrfnumber-vXX
    update protocol set published_status='archived',qmrf_number=concat(left(protocol_qmrf_number,36-(length(version)+2)),"-v",version) where qmrf_number=protocol_qmrf_number;
    update protocol set qmrf_number=protocol_qmrf_number where idprotocol=pid and version=version_new;


    END LOOP the_loop;

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