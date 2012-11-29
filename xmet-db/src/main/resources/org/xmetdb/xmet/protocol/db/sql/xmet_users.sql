CREATE DATABASE IF NOT EXISTS `xmet_users`  /*!40100 DEFAULT CHARACTER SET utf8 */;

-- -----------------------------------------------------
-- User names 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE  `users` (
  `user_name` varchar(45) CHARACTER SET latin1 NOT NULL,
  `user_pass` varchar(32) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- User roles
-- -----------------------------------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE  `roles` (
  `role_name` varchar(16) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Roles assigned to users
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE  `user_roles` (
  `user_name` varchar(45) CHARACTER SET latin1 NOT NULL,
  `role_name` varchar(16) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`user_name`,`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

------------------------------------------
-- Registration status and confirmation codes
------------------------------------------
DROP TABLE IF EXISTS `user_registration`;
CREATE TABLE  `user_registration` (
  `user_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `confirmed` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `code` varchar(45) NOT NULL,
  `status` enum('disabled','commenced','confirmed') NOT NULL DEFAULT 'disabled',
  PRIMARY KEY (`user_name`),
  UNIQUE KEY `Index_2` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

------------------------------------------
-- Version
------------------------------------------
CREATE TABLE  `version` (
  `idmajor` int(5) unsigned NOT NULL,
  `idminor` int(5) unsigned NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`idmajor`,`idminor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
insert into version (idmajor,idminor,comment) values (2,0,"XMETDB users");

-- -----------------------------------------------------
-- Default user
-- -----------------------------------------------------
insert into users values("admin",MD5("admin"));
insert into roles values("xmetdb_editor");
insert into roles values("xmetdb_manager");
insert into roles values ("xmetdb_user");

insert into user_roles values("admin","xmetdb_editor");
insert into user_roles values("admin","xmetdb_manager");
insert into user_roles values("admin","xmetdb_user");
insert ignore into user_registration
SELECT user_name,now(),now(),concat("SYSTEM_",user_name),'confirmed' FROM users;


