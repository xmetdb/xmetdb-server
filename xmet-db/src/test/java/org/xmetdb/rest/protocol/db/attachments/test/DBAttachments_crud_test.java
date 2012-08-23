package org.xmetdb.rest.protocol.db.attachments.test;

import java.io.File;
import java.net.URL;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
import org.xmetdb.rest.protocol.attachments.db.AddAttachment;
import org.xmetdb.rest.protocol.attachments.db.DeleteAttachment;
import org.xmetdb.rest.protocol.attachments.db.UpdateAttachment;
import org.xmetdb.rest.protocol.db.test.CRUDTest;

/**
 * @author nina
 *
 */
public class DBAttachments_crud_test  extends CRUDTest<DBProtocol,DBAttachment> {



	@Override
	protected IQueryUpdate<DBProtocol,DBAttachment> deleteQuery()
			throws Exception {
		return new DeleteAttachment(new DBAttachment(108),new DBProtocol(id83v1));
	}

	@Override
	protected void createVerify(IQueryUpdate<DBProtocol,DBAttachment> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idattachment,idprotocol,version,name,description,type,format,original_name,imported FROM attachments where idprotocol=2 and version=1");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("test",table.getValue(0,"name"));
		Assert.assertEquals("Description",table.getValue(0,"description"));
		Assert.assertEquals("",table.getValue(0,"format"));
		Assert.assertEquals(attachment_type.document.name(),table.getValue(0,"type"));
		c.close();
		
	}

	@Override
	protected IQueryUpdate<DBProtocol, DBAttachment> createQuery()
			throws Exception {
		DBProtocol protocol = new DBProtocol(id2v1);
		URL url = getClass().getClassLoader().getResource("org/xmetdb/xmet/test");
		DBAttachment attachment = DBAttachment.file2attachment(new File(url.getFile()), "Description", url.getFile(), attachment_type.document);
		return new AddAttachment(protocol, attachment);
	}

	@Override
	protected IQueryUpdate<DBProtocol, DBAttachment> createQueryNew()
			throws Exception {
		DBProtocol protocol = new DBProtocol(id2v1);
		URL url = getClass().getClassLoader().getResource("org/xmetdb/xmet/Training.sdf");
		DBAttachment attachment = DBAttachment.file2attachment(new File(url.getFile()), "Description", url.getFile(), attachment_type.data_training);
		return new AddAttachment(protocol, attachment);
	}

	@Override
	protected IQueryUpdate<DBProtocol, DBAttachment> updateQuery()
			throws Exception {
		return new UpdateAttachment(null,new DBAttachment(180));
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<DBProtocol, DBAttachment> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idattachment,idprotocol,version,name,description,type,format,original_name,imported FROM attachments where idprotocol=2 and version=1");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("Training",table.getValue(0,"name"));
		Assert.assertEquals("Description",table.getValue(0,"description"));
		Assert.assertEquals("sdf",table.getValue(0,"format"));
		Assert.assertEquals(attachment_type.data_training.name(),table.getValue(0,"type"));
		c.close();
	}

	@Override
	protected void updateVerify(IQueryUpdate<DBProtocol, DBAttachment> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idattachment,imported from attachments where idattachment=180");
		Assert.assertEquals(1,table.getRowCount());
		c.close();		
	}

	@Override
	protected void deleteVerify(IQueryUpdate<DBProtocol, DBAttachment> query)
			throws Exception {
	
		IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM attachments where idprotocol=83 and version=1 and idattachment=108");
		Assert.assertEquals(0,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT * FROM attachments where idprotocol=83 and version=1");
		Assert.assertEquals(3,table.getRowCount());		
	}

	
}
