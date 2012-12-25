package org.xmetdb.xmet.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.restnet.c.ChemicalMediaType;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.xmetdb.rest.protocol.ProtocolFactory;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachment;
import org.xmetdb.xmet.client.Resources;

public class AttachmentResourceTest extends ResourceTest {
	protected final static String idxmet1="XMETDB1";
	@Override
	public void setUp() throws Exception {
		super.setUp();
		setUpDatabase(dbFile);
		
	}
	@Override
	public String getTestURI() {
		return String.format("http://localhost:%d%s/%s%s", port,
				Resources.protocol,
				idxmet1,Resources.attachment);
	}
	
	@Test
	public void testURI() throws Exception {
		testGet(getTestURI(),MediaType.TEXT_URI_LIST);
	}
	/**
	 * The URI should be /protocol/XMETDB1/attachment
	 */
	@Override
	public boolean verifyResponseURI(String uri, MediaType media, InputStream in)
			throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line = null;
		int count = 0;
		while ((line = r.readLine())!= null) {
			System.out.println(line);
			Assert.assertTrue(line.startsWith(
					String.format("http://localhost:%d%s/%s%s",port,Resources.protocol,
							idxmet1,Resources.attachment)
							));
			count++;
		}
		return count==2;
	}	
	
	@Test
	public void testCreateEntryFromMultipartWeb() throws Exception {
		String url = createEntryFromMultipartWeb(new Reference(getTestURI()));
		
		Assert.assertTrue(url.startsWith(String.format("http://localhost:%d/protocol/%s/attachment",port,idxmet1)));
		System.out.println(url);
		
   	    IDatabaseConnection c = getConnection();	
		ITable  table = 	c.createQueryTable("EXPECTED","SELECT * FROM protocol");
		Assert.assertEquals(3,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT idattachment,idprotocol,version from attachments p where p.idprotocol=1 and p.version=1  order by idattachment desc limit 1");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals(new BigInteger("1"),table.getValue(0,"version"));
		Assert.assertEquals(new BigInteger("1"),table.getValue(0,"idprotocol"));
		String idattachment = table.getValue(0,"idattachment").toString();
		c.close();
		
		testImportAttachment(idattachment);
	}
	
	public void testImportAttachment(String attachmentID) throws Exception {
		String attachmentURL = String.format("http://localhost:%d/protocol/%s/attachment/A%s/dataset",port,idxmet1,attachmentID);
		Reference uri = new Reference(attachmentURL);
		IDatabaseConnection c = getConnection();	
		ITable  table = 	c.createQueryTable("EXPECTED","SELECT idattachment,imported,name FROM attachments where idattachment="+attachmentID + " order by idattachment desc");
		Assert.assertEquals(Boolean.FALSE,table.getValue(0,"imported"));
		c.close();
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("import",  "true"));

		
		RemoteTask task = testAsyncPoll(
				uri,
				MediaType.TEXT_URI_LIST, new UrlEncodedFormEntity(formparams,"UTF-8"),
				Method.POST);
		//wait to complete
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		if (!task.isCompletedOK())
			System.out.println(task.getError());
		Assert.assertEquals(uri.toString(),task.getResult().toString());

		c = getConnection();	
		 table = 	c.createQueryTable("EXPECTED","SELECT idattachment,imported,name FROM attachments where idattachment="+attachmentID);
		Assert.assertEquals(Boolean.TRUE,table.getValue(0,"imported"));
		c.close();

	}
	
	public String createEntryFromMultipartWeb(Reference uri) throws Exception {

		URL url = getClass().getClassLoader().getResource("org/xmetdb/xmet/sdf/100-04-9.sdf");
		Assert.assertNotNull(url);
		File file = new File(url.getFile());
		
		String[] names = new String[1];
		String[] values = new String[1];
		names[0] = ProtocolFactory.ObservationFields.user_uri.name();
		values[0] = String.format("http://localhost:%d%s/%s", port,	Resources.user, "U1");
		MultipartEntity rep = getMultipartWebFormRepresentation(names,values,
				ProtocolFactory.ObservationFields.xmet_substrate_upload.name(),file,ChemicalMediaType.CHEMICAL_MDLSDF.toString());
		
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM protocol");
		Assert.assertEquals(3,table.getRowCount());
		c.close();

		RemoteTask task = testAsyncPoll(uri,
				MediaType.TEXT_URI_LIST, rep,
				Method.POST);
		//wait to complete
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		if (!task.isCompletedOK())
			System.out.println(task.getError());
		
		Assert.assertTrue(task.getResult().toString().startsWith(String.format("http://localhost:%d/protocol/",port)));
		
		return task.getResult().toString();


	}		
	@Override
	public Object verifyResponseJavaObject(String uri, MediaType media,
			Representation rep) throws Exception {
		Object o = super.verifyResponseJavaObject(uri, media, rep);
		Assert.assertTrue(o instanceof ReadAttachment);

		return o;
	}
	
	
	
	
	
}
