package org.xmetdb.rest.protocol.db.protocol.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.links.db.UpdateObservationIdentifiers;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.test.CRUDTest;

public class ObservationIdentifiers_crud_test extends
		CRUDTest<DBProtocol, List<ExternalIdentifier>> {

	@Override
	protected IQueryUpdate<DBProtocol, List<ExternalIdentifier>> createQuery()
			throws Exception {
		DBProtocol c = new DBProtocol();
		c.setID(2);
		c.setVersion(1);
		List<ExternalIdentifier> ids = new ArrayList<ExternalIdentifier>();
		ids.add(new ExternalIdentifier("System1", "id1"));
		ids.add(new ExternalIdentifier("System2", "id2"));
		return new UpdateObservationIdentifiers(c, ids);
	}

	@Override
	protected void createVerify(
			IQueryUpdate<DBProtocol, List<ExternalIdentifier>> query)
			throws Exception {
		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED",
				"SELECT idprotocol,version,type,id FROM protocol_links where idprotocol=2 and version=1 order by type,id");
		Assert.assertEquals(2, table.getRowCount());
		Assert.assertEquals("System1", table.getValue(0, "type"));
		Assert.assertEquals("id1", table.getValue(0, "id"));
		Assert.assertEquals("System2", table.getValue(1, "type"));
		Assert.assertEquals("id2", table.getValue(1, "id"));
		// Assert.assertEquals("<?xml>".getBytes("UTF-8"),table.getValue(0,"content"));
		c.close();
	}

	@Override
	protected IQueryUpdate<DBProtocol, List<ExternalIdentifier>> deleteQuery()
			throws Exception {
		DBProtocol c = new DBProtocol();
		c.setID(2);
		c.setVersion(1);
		return new UpdateObservationIdentifiers(c, null);

	}

	@Override
	protected void deleteVerify(
			IQueryUpdate<DBProtocol, List<ExternalIdentifier>> query)
			throws Exception {
		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED",
				"SELECT * FROM protocol_links where idprotocol=2 and version=1 ");
		Assert.assertEquals(0, table.getRowCount());
		c.close();
	}

	@Override
	protected IQueryUpdate<DBProtocol, List<ExternalIdentifier>> updateQuery()
			throws Exception {
		return null;
	}

	@Override
	protected void updateVerify(
			IQueryUpdate<DBProtocol, List<ExternalIdentifier>> query)
			throws Exception {
	}

	@Override
	protected IQueryUpdate<DBProtocol, List<ExternalIdentifier>> createQueryNew()
			throws Exception {
		return null;
	}

	@Override
	protected void createVerifyNew(
			IQueryUpdate<DBProtocol, List<ExternalIdentifier>> query)
			throws Exception {

	}

	@Override
	public void testUpdate() throws Exception {
	}

	@Override
	public void testCreateNew() throws Exception {

	}
}
