-- ----------------------------------------------------------
-- 2.5 to 2.6 
-- ----------------------------------------------------------
ALTER TABLE `template` DROP INDEX `template_list_index4157`,
 ADD UNIQUE INDEX `template_list_index4157` USING BTREE(`name`),
 DROP INDEX `Index_3`,
 ADD UNIQUE INDEX `Index_3` USING BTREE(`code`);
insert into version (idmajor,idminor,comment) values (2,6,"Unique index for enzymes code");

-- ----------------------------------------------------------
-- 2.6 to 2.7 
-- ----------------------------------------------------------
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
    select idprotocol,version_new,ifnull(title_new,title),concat("XMETDB",idprotocol,"v",version_new),ifnull(abstract_new,abstract),iduser,summarySearchable,idproject,idorganisation,null,status,now(),published_status 
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
    
	-- Set the previous protocol status to archived
    update protocol set published_status='archived' where qmrf_number=protocol_qmrf_number;

    END LOOP the_loop;

end $$

DELIMITER ;

insert into version (idmajor,idminor,comment) values (2,7,"New protocol version stored proc");

-- ----------------------------------------------------------
-- 2.7 to 2.8 
-- ----------------------------------------------------------
ALTER TABLE `protocol` ADD COLUMN `atom_uncertainty` ENUM('Certain','Uncertain') NOT NULL DEFAULT 'Uncertain' AFTER `published_status`,
 ADD COLUMN `product_amount` ENUM('Major','Minor','Unknown') NOT NULL DEFAULT 'Unknown' AFTER `atom_uncertainty`,
 ADD INDEX `Index_9`(`atom_uncertainty`),
 ADD INDEX `Index_10`(`product_amount`);
 insert into version (idmajor,idminor,comment) values (2,8,"Atom uncertainty and product amount fields");
 
 -- ----------------------------------------------------------
-- 2.8 to 2.9 
-- ----------------------------------------------------------
ALTER TABLE `template` ADD COLUMN `allele` TEXT DEFAULT null AFTER `uri`;
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
ALTER TABLE `protocol_endpoints` ADD COLUMN `allele` VARCHAR(15) NOT NULL AFTER `idtemplate`,
 ADD INDEX `Index_3`(`idtemplate`, `allele`);
 
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
    select idprotocol,version_new,ifnull(title_new,title),concat("XMETDB",idprotocol,"v",version_new),ifnull(abstract_new,abstract),iduser,summarySearchable,idproject,idorganisation,null,status,now(),published_status 
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

    END LOOP the_loop;

end $$

DELIMITER ;
insert into version (idmajor,idminor,comment) values (2,9,"Alleles");

-- ----------------------------------------------------------
-- 2.9 to 2.10 
-- ----------------------------------------------------------
ALTER TABLE `protocol` MODIFY COLUMN `title` VARCHAR(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Title',
 MODIFY COLUMN `abstract` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 CHANGE COLUMN `template` `reference` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'Reference',
 ADD INDEX `Index_11`(`reference`);
insert into version (idmajor,idminor,comment) values (2,10,"References");

-- ----------------------------------------------------------
-- 2.10 to 2.11 
-- ----------------------------------------------------------
ALTER TABLE `attachments` DROP INDEX `Index_4`,
 ADD UNIQUE INDEX `Index_4` USING BTREE(`idprotocol`, `version`, `type`);
insert into version (idmajor,idminor,comment) values (2,11,"One attachment of a type");