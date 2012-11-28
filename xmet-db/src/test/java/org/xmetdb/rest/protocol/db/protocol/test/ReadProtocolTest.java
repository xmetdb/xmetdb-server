package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.test.QueryTest;

import junit.framework.Assert;


public class ReadProtocolTest extends QueryTest<ReadProtocol> {

	@Override
	protected ReadProtocol createQuery() throws Exception {
		return new ReadProtocol("XMETDB3");
	}

	@Override
	protected void verify(ReadProtocol query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			Assert.assertEquals(3,protocol.getID());
			Assert.assertNotNull(protocol.getKeywords());
			Assert.assertEquals(1,protocol.getKeywords().size());
			Assert.assertNotNull(protocol.getOwner());
			//Assert.assertNotNull(protocol.getOwner().getFirstname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}
