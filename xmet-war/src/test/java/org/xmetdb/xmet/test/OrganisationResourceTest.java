package org.xmetdb.xmet.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.restnet.groups.DBGroup;
import net.idea.restnet.groups.db.ReadOrganisation;
import net.toxbank.client.io.rdf.OrganisationIO;
import net.toxbank.client.resource.Organisation;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.xmetdb.xmet.client.Resources;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OrganisationResourceTest extends ResourceTest {
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		setUpDatabase(dbFile);
		
	}
	@Override
	public String getTestURI() {
		return String.format("http://localhost:%d%s", port,Resources.organisation);
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
			Assert.assertTrue(line.startsWith(String.format("http://localhost:%d%s/G",port,Resources.organisation)));
			count++;
		}
		return count==4;
	}	
	
	
	public void testTXT() throws Exception {
		testGet(getTestURI(),MediaType.TEXT_PLAIN);
	}
	/**
	 * Reading the template and ensuring the same garbage we put in is being read
	 */
	@Override
	public boolean verifyResponseTXT(String uri, MediaType media, InputStream in)
			throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line = null;
		int count = 0;
		while ((line = r.readLine())!= null) {
			Assert.assertEquals("ABCDEFGH", line);
			count++;
		}
		return count==1;
	}
	
	@Test
	public void testRDF() throws Exception {
		testGet(String.format("http://localhost:%d%s/G2", port,Resources.organisation),MediaType.APPLICATION_RDF_XML);
	}
	
	@Override
	public OntModel verifyResponseRDFXML(String uri, MediaType media,
			InputStream in) throws Exception {
		
		OntModel model = ModelFactory.createOntologyModel();
		model.read(in,null);
		
		OrganisationIO ioClass = new OrganisationIO();
		List<Organisation> orgs = ioClass.fromJena(model);
		Assert.assertEquals(1,orgs.size());
		Assert.assertEquals(String.format("http://localhost:%d%s/G2",port,Resources.organisation),
													orgs.get(0).getResourceURL().toString());
		Assert.assertEquals("ABC University", orgs.get(0).getTitle());
		return model;
	}	
	@Override
	public Object verifyResponseJavaObject(String uri, MediaType media,
			Representation rep) throws Exception {
		Object o = super.verifyResponseJavaObject(uri, media, rep);
		Assert.assertTrue(o instanceof ReadOrganisation);

		return o;
	}
	
	@Test
	public void testCreateEntryFromWebForm() throws Exception {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(DBGroup.fields.name.name(),  "organisation"));
		formparams.add(new BasicNameValuePair(DBGroup.fields.ldapgroup.name(), "xmetdb-new"));
		
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM organisation");
		Assert.assertEquals(4,table.getRowCount());
		c.close();

		RemoteTask task = testAsyncPoll(new Reference(String.format("http://localhost:%d%s", port,
				Resources.organisation)),
				MediaType.TEXT_URI_LIST,new UrlEncodedFormEntity(formparams, "UTF-8"),
				Method.POST);
		//wait to complete
	
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		
		Assert.assertTrue(task.getResult().toString().startsWith(String.format("http://localhost:%d/organisation/G",port)));

        c = getConnection();	
		table = 	c.createQueryTable("EXPECTED","SELECT * FROM organisation");
		Assert.assertEquals(5,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT idorganisation,name,ldapgroup from organisation where idorganisation>4");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("xmetdb-new",table.getValue(0,"ldapgroup"));
		Assert.assertEquals("organisation",table.getValue(0,"name"));
		

		String expectedURI = String.format("http://localhost:%d/organisation/G%s",port,table.getValue(0,"idorganisation"));
		Assert.assertEquals(expectedURI,task.getResult().toString());
		
		c.close();

	}
	
	
	@Test
	public void testUpdate() throws Exception {
		IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT name,ldapgroup FROM organisation where idorganisation=1");
		Assert.assertEquals("XMETDB",table.getValue(0,"name"));
		Assert.assertEquals("xmetdb",table.getValue(0,"ldapgroup"));
		c.close();		
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(DBGroup.fields.name.name(),  "XMETDB-new"));
		formparams.add(new BasicNameValuePair(DBGroup.fields.ldapgroup.name(), "xmetdb-new"));
		
		
		String org = String.format("http://localhost:%d%s/G1", port,Resources.organisation);
		RemoteTask task = testAsyncPoll(new Reference(org),
				MediaType.TEXT_URI_LIST,new UrlEncodedFormEntity(formparams, "UTF-8"),
				Method.PUT);
		Assert.assertEquals(Status.SUCCESS_OK.getCode(), task.getStatus());
		//Assert.assertNull(task.getResult());
		c = getConnection();	
		table = 	c.createQueryTable("EXPECTED","SELECT name,ldapgroup FROM organisation where idorganisation=1");
		Assert.assertEquals("XMETDB-new",table.getValue(0,"name"));
		Assert.assertEquals("xmetdb-new",table.getValue(0,"ldapgroup"));
		c.close();			
	}
	
	@Test
	public void testDelete() throws Exception {
		IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idorganisation FROM organisation where idorganisation=2");
		Assert.assertEquals(new BigInteger("2"),table.getValue(0,"idorganisation"));
		c.close();		
		String org = String.format("http://localhost:%d%s/G2", port,Resources.organisation);
		RemoteTask task = testAsyncPoll(new Reference(org),
				MediaType.TEXT_URI_LIST, null,
				Method.DELETE);
		Assert.assertEquals(Status.SUCCESS_OK.getCode(), task.getStatus());
		//Assert.assertNull(task.getResult());
		c = getConnection();	
		table = 	c.createQueryTable("EXPECTED","SELECT * FROM organisation where idorganisation=2");
		Assert.assertEquals(0,table.getRowCount());
		c.close();			
	}
	
	@Test
	public void testDeleteFromUserProfile() throws Exception {
		IDatabaseConnection c = getConnection();	
		String sql = "SELECT idorganisation FROM user_organisation where idorganisation=1 and iduser=1";
		ITable table = 	c.createQueryTable("EXPECTED",sql);
		Assert.assertEquals(1,table.getRowCount());
		c.close();		
		String org = String.format("http://localhost:%d%s/U1%s/G1", port,Resources.user,Resources.organisation);
		RemoteTask task = testAsyncPoll(new Reference(org),
				MediaType.TEXT_URI_LIST, null,
				Method.DELETE);
		Assert.assertEquals(Status.SUCCESS_OK.getCode(), task.getStatus());
		//Assert.assertNull(task.getResult());
		c = getConnection();	
		table = 	c.createQueryTable("EXPECTED",sql);
		Assert.assertEquals(0,table.getRowCount());
		c.close();			
	}	
	
	@Test
	public void testAddOrganisationToUserProfile() throws Exception {
		IDatabaseConnection c = getConnection();	
		String sql = "SELECT iduser FROM user_organisation where idorganisation=1 and iduser=1";
		ITable table = 	c.createQueryTable("EXPECTED",sql);
		Assert.assertEquals(1,table.getRowCount());
		c.close();		
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("organisation_uri",  String.format("http://localhost:%d%s/G4",port,Resources.organisation)));
				

		String org = String.format("http://localhost:%d%s/U1%s", port,Resources.user,Resources.organisation);
		RemoteTask task = testAsyncPoll(new Reference(org),
				MediaType.TEXT_URI_LIST, new UrlEncodedFormEntity(formparams, "UTF-8"),
				Method.POST);
		Assert.assertEquals(Status.SUCCESS_OK.getCode(), task.getStatus());
		//Assert.assertNull(task.getResult());
		c = getConnection();	
		sql = "SELECT iduser FROM user_organisation where idorganisation=4 and iduser=1";
		table = 	c.createQueryTable("EXPECTED",sql);
		Assert.assertEquals(1,table.getRowCount());
		c.close();			
	}	
}