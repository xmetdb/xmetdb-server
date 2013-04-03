-- ----------------------------------------------------------
-- 2.14 to 2.15 CURATOR SUPPORT
-- ----------------------------------------------------------
ALTER TABLE `protocol` DROP COLUMN `summarySearchable` , 
CHANGE COLUMN `reference` `reference` VARCHAR(255) NULL COMMENT 'Reference'  , 
CHANGE COLUMN `published_status` `published_status` ENUM('draft','published','archived','deleted') NOT NULL DEFAULT 'draft'  , 
ADD COLUMN `curatedby` INT(10) NULL  AFTER `product_amount` , 
ADD COLUMN `curated` TINYINT(1) UNSIGNED NULL DEFAULT '0' COMMENT 'Curated - yes / no'  AFTER `curatedby` ;

ALTER TABLE `protocol` CHANGE COLUMN `curatedby` `curatedby` INT(10) UNSIGNED NULL  , 
	CHANGE COLUMN `curated` `curated` TINYINT(1) NULL DEFAULT '0' COMMENT 'Curated - yes / no'  , 
  ADD CONSTRAINT `FK_protocol_4`
  FOREIGN KEY (`curatedby` )
  REFERENCES `xmetdb-test`.`user` (`iduser` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_protocol_4_idx` (`curatedby` ASC) ;

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
    insert into protocol (idprotocol,version,title,qmrf_number,abstract,iduser,curated,idproject,idorganisation,filename,status,created,published_status,atom_uncertainty,product_amount)
    select null,1,title,concat("XMETDB",idprotocol,"v",now()),abstract,iduser,curated,idproject,idorganisation,filename,status,now(),'draft',atom_uncertainty,product_amount 
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
insert into version (idmajor,idminor,comment) values (2,15,"Curator support");