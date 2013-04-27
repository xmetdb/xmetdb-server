ALTER TABLE `attachments` ADD COLUMN `newtype` ENUM('substrate','product') NOT NULL DEFAULT 'substrate'  AFTER `imported` ;
update attachments set newtype='product' where type='data_validation';
ALTER TABLE `attachments` 
DROP INDEX `type` 
, DROP INDEX `Index_4` ;
ALTER TABLE `attachments` DROP COLUMN `type` ;
ALTER TABLE `attachments` CHANGE COLUMN `newtype` `type` ENUM('substrate','product') NOT NULL DEFAULT 'substrate'  ;
ALTER TABLE `attachments` 
ADD UNIQUE INDEX `protocoltype` (`idprotocol` ASC, `version` ASC, `type` ASC) 
, ADD INDEX `type` (`type` ASC) ;
update `ambit2-xmetdb`.query q,xmetdb.attachments a set q.name =concat('XMETDB',idprotocol) where a.name=q.name;
insert into version (idmajor,idminor,comment) values (2,17,"Rename attachments type");