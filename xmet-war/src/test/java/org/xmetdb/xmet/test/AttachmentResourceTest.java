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
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
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
		
		Assert.assertEquals(String.format("http://localhost:%d/protocol/%s/attachment",port,idxmet1),url);

		
   	    IDatabaseConnection c = getConnection();	
		ITable  table = 	c.createQueryTable("EXPECTED","SELECT * FROM protocol");
		Assert.assertEquals(3,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT idattachment,idprotocol,version from attachments p where p.idprotocol=1 and p.version=1");
		Assert.assertEquals(3,table.getRowCount());
		Assert.assertEquals(new BigInteger("1"),table.getValue(0,"version"));
		Assert.assertEquals(new BigInteger("1"),table.getValue(0,"idprotocol"));
		c.close();
	}
	
	public String createEntryFromMultipartWeb(Reference uri) throws Exception {

		URL url = getClass().getClassLoader().getResource("org/xmetdb/xmet/protocol-sample.pdf");
		Assert.assertNotNull(url);
		File file = new File(url.getFile());
		
		String[] names = new String[0];
		String[] values = new String[0];
		MultipartEntity rep = getMultipartWebFormRepresentation(names,values,attachment_type.document.name(),file,MediaType.APPLICATION_PDF.toString());
		
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
		
		Assert.assertTrue(task.getResult().toString().startsWith(
					String.format("http://localhost:%d/protocol/",port)));
		
		return task.getResult().toString();


	}		
	@Override
	public Object verifyResponseJavaObject(String uri, MediaType media,
			Representation rep) throws Exception {
		Object o = super.verifyResponseJavaObject(uri, media, rep);
		Assert.assertTrue(o instanceof ReadAttachment);

		return o;
	}
	
	
	@Test
	public void testImportAttachment() throws Exception {
		Reference uri = new Reference(String.format("http://localhost:%d/protocol/%s/attachment/A1/dataset",port,idxmet1));

		IDatabaseConnection c = getConnection();	
		ITable  table = 	c.createQueryTable("EXPECTED","SELECT idattachment,imported,name FROM attachments where idattachment=1");
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
		 table = 	c.createQueryTable("EXPECTED","SELECT idattachment,imported,name FROM attachments where idattachment=1");
		Assert.assertEquals(Boolean.TRUE,table.getValue(0,"imported"));
		c.close();

	}
	
	
}
