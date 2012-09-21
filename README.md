xmetdb-server
=============

The xmetdb web service/application

This is an initial fork from qmrf.sf.net.  Don't consider it functional XMETDB application!

--------
Build: 

mvn package -P xmet

The default maven profile is -P xmet

The tests assume 
CREATE USER 'guest'@'localhost' IDENTIFIED BY 'guest';
GRANT ALL ON xmet_users.* TO 'guest'@'localhost';
GRANT ALL ON xmetdb.* TO 'guest'@'localhost';
GRANT ALL ON `xmetdb-test`.* TO 'guest'@'localhost';

GRANT TRIGGER ON xmetdb.* TO 'guest'@'localhost';
GRANT execute on `ambit2-xmet`.* to guest@localhost
GRANT execute on PROCEDURE `ambit2-xmet`.findByProperty to guest@127.0.0.1
GRANT execute on PROCEDURE `ambit2-xmet`.findByProperty to guest@localhost

To change user name and password, modify the local maven profile.

Test DB
create database `xmet-test` character set utf8;
GRANT ALL ON `xmet-test`.* TO 'guest'@'localhost';
GRANT TRIGGER ON `xmet-test`.* TO 'guest'@'localhost';
GRANT UPDATE,DROP,CREATE,SELECT,INSERT,EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE on `xmet-test`.* TO 'guest'@'localhost';
GRANT UPDATE,DROP,CREATE,SELECT,INSERT,EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE on `xmet-test`.* TO 'guest'@'127.0.0.1';
GRANT execute on PROCEDURE `ambit2-xmet-test`.findByProperty to guest@127.0.0.1
GRANT execute on PROCEDURE `ambit2-xmet-test`.findByProperty to guest@localhost