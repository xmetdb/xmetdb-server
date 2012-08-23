package org.xmetdb.rest.protocol.db.attachments.test;

import java.net.URL;
import java.sql.ResultSet;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachment;
import org.xmetdb.rest.protocol.db.test.CRUDTest;
import org.xmetdb.rest.protocol.db.test.QueryTest;

import junit.framework.Assert;

/**
 * Attachments
 * @author nina
 *
 */

public class ReadAttachmentsTest  extends QueryTest<ReadAttachment> {

	@Override
	protected ReadAttachment createQuery() throws Exception {
		DBProtocol protocol = new DBProtocol(CRUDTest.id83v1);
		return new ReadAttachment(protocol,System.getProperty("java.io.tmpdir"));
	}

	@Override
	protected void verify(ReadAttachment query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBAttachment attachment = query.getObject(rs);
			switch ( attachment.getID()) {
			case 108: break;
			case 109: break;
			case 110: break;
			case 111: break;
			default: Assert.fail(String.format("Wrong id %d", attachment.getID()));
			}

			Assert.assertNotNull(attachment.getResourceURL());
			URL expected = 
			new URL(String.format("file://%s",System.getProperty("java.io.tmpdir")).replace("\\","/"));
			
			//if (attachment_type.document.equals(attachment.getType())) Assert.assertFalse(attachment.isImported());
			//else Assert.assertTrue(attachment.isImported());
			System.out.println(attachment.getResourceURL().toExternalForm());
			System.out.println(expected.toExternalForm());

			Assert.assertTrue(attachment.getResourceURL().toExternalForm().startsWith(expected.toExternalForm()));
			records++;
		}
		Assert.assertEquals(4,records);

	}

}
