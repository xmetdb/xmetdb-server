package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocolByTextSearch;
import org.xmetdb.rest.protocol.db.test.QueryTest;

import junit.framework.Assert;

public class ReadProtocolByFreeTextTest  extends QueryTest<ReadProtocolByTextSearch> {

	@Override
	protected ReadProtocolByTextSearch createQuery() throws Exception {
		ReadProtocolByTextSearch query = new ReadProtocolByTextSearch();
		query.setFieldname("mutagenicity");
		return query;
	}

	@Override
	protected void verify(ReadProtocolByTextSearch query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			Assert.assertEquals(1,protocol.getID());
			Assert.assertNotNull(protocol.getOwner());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}