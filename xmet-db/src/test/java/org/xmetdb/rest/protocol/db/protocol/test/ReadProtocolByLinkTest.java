package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;

import org.dbunit.database.IDatabaseConnection;
import org.junit.Test;
import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.links.db.ReadObservationsByExternalIDentifier;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.test.QueryTest;

public class ReadProtocolByLinkTest extends
		QueryTest<ReadObservationsByExternalIDentifier> {

	@Override
	protected ReadObservationsByExternalIDentifier createQuery()
			throws Exception {
		ExternalIdentifier id = new ExternalIdentifier("type1", "12345");
		ReadObservationsByExternalIDentifier q = new ReadObservationsByExternalIDentifier(
				id);
		return q;
	}

	@Override
	protected void verify(ReadObservationsByExternalIDentifier query,
			ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			Assert.assertEquals(2, protocol.getID());
			Assert.assertEquals(1, protocol.getVersion());
			records++;
		}
		Assert.assertEquals(1, records);

	}

	protected ReadObservationsByExternalIDentifier createQueryLink()
			throws Exception {
		ExternalIdentifier id = new ExternalIdentifier(null, "12345");
		ReadObservationsByExternalIDentifier q = new ReadObservationsByExternalIDentifier(
				id);
		return q;
	}

	@Test
	public void testSelectByLinkOnly() throws Exception {
		setUpDatabase(getDbFile());
		IDatabaseConnection c = getConnection();
		ResultSet rs = null;
		try {
			executor.setConnection(c.getConnection());
			executor.open();
			ReadObservationsByExternalIDentifier q = createQueryLink();
			rs = executor.process(q);
			Assert.assertNotNull(rs);
			verify(q, rs);
		} finally {
			if (rs != null)
				rs.close();
			c.close();
		}
	}
}
