package org.xmetdb.rest.protocol.db.attachments.test;

import java.net.URL;
import java.sql.ResultSet;

import junit.framework.Assert;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachment;
import org.xmetdb.rest.protocol.db.test.CRUDTest;
import org.xmetdb.rest.protocol.db.test.QueryTest;

/**
 * Attachments
 * @author nina
 *
 */

public class ReadAttachmentsTest  extends QueryTest<ReadAttachment> {

	@Override
	protected ReadAttachment createQuery() throws Exception {
		DBProtocol protocol = new DBProtocol(CRUDTest.idxmet2);
		return new ReadAttachment(protocol,System.getProperty("java.io.tmpdir"));
	}

	@Override
	protected void verify(ReadAttachment query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBAttachment attachment = query.getObject(rs);
			switch ( attachment.getID()) {
			case 3: break;
			case 4: break;
			default: Assert.fail(String.format("Wrong id %d", attachment.getID()));
			}

			Assert.assertNotNull(attachment.getResourceURL());
			URL expected = 
			new URL(String.format("file:/%s",System.getProperty("java.io.tmpdir")).replace("\\","/"));
			
			//if (attachment_type.document.equals(attachment.getType())) Assert.assertFalse(attachment.isImported());
			//else Assert.assertTrue(attachment.isImported());
			System.out.println(attachment.getResourceURL().toExternalForm());
			System.out.println(expected.toExternalForm());

			Assert.assertTrue(attachment.getResourceURL().toExternalForm().startsWith(expected.toExternalForm()));
			records++;
		}
		Assert.assertEquals(2,records);

	}

}
