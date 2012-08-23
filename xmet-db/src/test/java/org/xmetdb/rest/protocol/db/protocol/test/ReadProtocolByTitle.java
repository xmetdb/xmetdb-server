package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.test.QueryTest;

import junit.framework.Assert;

public class ReadProtocolByTitle  extends QueryTest<ReadProtocol> {

	@Override
	protected ReadProtocol createQuery() throws Exception {
		DBProtocol p = new DBProtocol();
		p.setTitle("mutagenicity");
		ReadProtocol query = new ReadProtocol(p);
		return query;
	}

	@Override
	protected void verify(ReadProtocol query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			Assert.assertEquals(83,protocol.getID());
			Assert.assertNotNull(protocol.getKeywords());
			Assert.assertEquals(4,protocol.getKeywords().size());
			Assert.assertNotNull(protocol.getOwner());
			//Assert.assertNotNull(protocol.getOwner().getFirstname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}