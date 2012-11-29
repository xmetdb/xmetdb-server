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