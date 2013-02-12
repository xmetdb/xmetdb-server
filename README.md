xmetdb-server
=============

The xmetdb web service/application

This is an ongoing implementation of XMETDB.  Almost fully functional, with some [issues](https://github.com/xmetdb/xmetdb-server/issues) still to be resolved!

--------
Build: 

    mvn package -P xmet

The default maven profile is -P xmet

Run: 

    mvn tomcat:run -P xmet

The tests assume
 
    CREATE USER 'guest'@'localhost' IDENTIFIED BY 'guest';
    GRANT ALL ON xmet_users.* TO 'guest'@'localhost';
    GRANT ALL ON xmetdb.* TO 'guest'@'localhost';
    GRANT ALL ON `xmetdb-test`.* TO 'guest'@'localhost';
    GRANT EXECUTE on procedure `xmetdb-test`.createProtocolVersion to 'guest'@'localhost';
    GRANT EXECUTE on procedure `xmetdb-test`.deleteProtocol to 'guest'@'localhost';
    

To change user name and password, modify the local maven profile or the properties under WEB-INF/classes/ambit2/rest/config

Test DB
    
    create database `xmetdb-test` character set utf8;
    GRANT ALL ON `xmetdb-test`.* TO 'guest'@'localhost';
    GRANT TRIGGER ON `xmetdb-test`.* TO 'guest'@'localhost';
    GRANT UPDATE,DROP,CREATE,SELECT,INSERT,EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE on `xmetdb-test`.* TO 'guest'@'localhost';
    GRANT UPDATE,DROP,CREATE,SELECT,INSERT,EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE on `xmetdb-test`.* TO 'guest'@'127.0.0.1';
    GRANT execute on PROCEDURE `ambit2-xmetdb-test`.findByProperty to guest@127.0.0.1;
    GRANT execute on PROCEDURE `ambit2-xmetdb-test`.findByProperty to guest@localhost;
