package org.xmetdb.rest.protocol;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;


import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.xmetdb.rest.protocol.db.test.DbUnitTest;

/**
 * An utility to generate dump of the database in DbUnit format, to be used in tests
 * @author nina
 *
 */
public class ExtractData extends DbUnitTest {
	
    public static void main(String[] args) throws Exception {
    	ExtractData ed = new ExtractData();
    	ed.extract();
    }
    
    protected void extract() throws Exception {
    	IDatabaseConnection connection = null;
    	try {
	        Class driverClass = Class.forName("com.mysql.jdbc.Driver");
	        Connection jdbcConnection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/xmetdb", getUser(), getPWD());
	        connection = new DatabaseConnection(jdbcConnection);
	
	        // partial database export
	        QueryDataSet partialDataSet = new QueryDataSet(connection);
	        partialDataSet.addTable("user", "SELECT * FROM user where iduser in (10,88)");
	        partialDataSet.addTable("project", "SELECT * FROM project");
	        partialDataSet.addTable("organisation", "SELECT * FROM organisation where idorganisation in (1,5)");

	        partialDataSet.addTable("protocol", "SELECT * FROM protocol where idprotocol in (83,119,121,2)");
	        partialDataSet.addTable("attachments", "SELECT * FROM attachments where idprotocol in (83,119,121)");
	        partialDataSet.addTable("protocol_authors", "SELECT * FROM protocol_authors where idprotocol in (83,119,121)");

	        partialDataSet.addTable("keywords", "SELECT * FROM keywords");
	        partialDataSet.addTable("user_organisation", "SELECT * FROM user_organisation where iduser in (10,88)");
	        
	        partialDataSet.addTable("template", "SELECT * FROM template");
	        partialDataSet.addTable("dictionary", "SELECT * FROM dictionary");
	        
	        FlatDtdDataSet.write(partialDataSet, new FileOutputStream(
	        			"src/test/resources/org/xmetdb/xmet/partial-dataset.dtd"));
	        FlatXmlDataSet.write(partialDataSet, 
	        		new FileOutputStream("src/test/resources/org/xmetdb/xmet/partial-dataset.xml"));
	        
	        /*
	        // full database export
	        IDataSet fullDataSet = connection.createDataSet();
	        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full-dataset.xml"));
	        */
    	} finally {
    		try {connection.close(); } catch (Exception x) {}
    	}
    }
}
