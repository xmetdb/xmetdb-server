package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;

import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.links.db.ReadObservationIdentifiers;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.test.QueryTest;

public class ReadProtocolLinksTest extends
		QueryTest<ReadObservationIdentifiers> {

	@Override
	protected ReadObservationIdentifiers createQuery() throws Exception {
		ReadObservationIdentifiers q = new ReadObservationIdentifiers();
		DBProtocol p = new DBProtocol();
		p.setID(2);
		p.setVersion(1);
		q.setFieldname(p);
		return q;
	}

	@Override
	protected void verify(ReadObservationIdentifiers query, ResultSet rs)
			throws Exception {
		int records = 0;
		while (rs.next()) {
			ExternalIdentifier ids = query.getObject(rs);
			Assert.assertTrue(ids.getSystemDesignator().startsWith("type"));
			Assert.assertNotNull(ids.getSystemIdentifier());
			records++;
		}
		Assert.assertEquals(2, records);

	}

}
