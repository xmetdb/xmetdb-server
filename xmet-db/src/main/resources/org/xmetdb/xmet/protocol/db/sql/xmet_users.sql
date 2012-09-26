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
