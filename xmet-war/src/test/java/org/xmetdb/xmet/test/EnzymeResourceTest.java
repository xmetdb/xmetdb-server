package org.xmetdb.xmet.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;

import junit.framework.Assert;

import net.idea.opentox.cli.task.RemoteTask;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.xmetdb.xmet.client.Resources;

public class EnzymeResourceTest  extends ResourceTest {
		
		@Override
		public void setUp() throws Exception {
			super.setUp();
			setUpDatabase(dbFile);
			
		}
		@Override
		public String getTestURI() {
			return String.format("http://localhost:%d%s", port,Resources.enzyme);
		}
		
		@Test
		public void testURI() throws Exception {
			testGet(getTestURI(),MediaType.TEXT_URI_LIST);
		}
		/**
		 * 
		 */
		@Override
		public boolean verifyResponseURI(String uri, MediaType media, InputStream in)
				throws Exception {
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			String line = null;
			int count = 0;
			while ((line = r.readLine())!= null) {
				Assert.assertTrue(line.startsWith(String.format("http://localhost:%d%s/",port,Resources.enzyme)));
				count++;
			}
			return count==15;
		}	
		
		
		@Test
		public void testDelete() throws Exception {
			IDatabaseConnection c = getConnection();	
			ITable table = 	c.createQueryTable("EXPECTED","SELECT code FROM template where idtemplate=15");
			Assert.assertEquals("CYP2C8",table.getValue(0,"code"));
			c.close();		
			String org = String.format("http://localhost:%d%s/15", port,Resources.enzyme);
			RemoteTask task = testAsyncPoll(new Reference(org),
					MediaType.TEXT_URI_LIST, null,
					Method.DELETE);
			Assert.assertEquals(Status.SUCCESS_OK.getCode(), task.getStatus());
			//Assert.assertNull(task.getResult());
			c = getConnection();	
			table = 	c.createQueryTable("EXPECTED","SELECT code FROM template where idtemplate=15");
			Assert.assertEquals(0,table.getRowCount());
			c.close();			
		}
				
}
