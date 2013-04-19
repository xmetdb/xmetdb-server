-- ----------------------------------------------------------
-- 2.15 to 2.16 Copy procedure user parameter
-- ----------------------------------------------------------
-- -----------------------------------------------------
-- Create an observation copy
-- -----------------------------------------------------
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

insert into version (idmajor,idminor,comment) values (2,16,"Added copy procedure user parameter");