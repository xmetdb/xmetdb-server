package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.test.QueryTest;
import org.xmetdb.xmet.client.AtomUncertainty;
import org.xmetdb.xmet.client.ProductAmount;


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
			Assert.assertEquals(AtomUncertainty.Certain,protocol.getAtomUncertainty());
			Assert.assertEquals(ProductAmount.Major,protocol.getProductAmount());
			Assert.assertNotNull(protocol.getDataTemplate());
			Assert.assertEquals("TEST Reference",protocol.getDataTemplate().getTitle());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}
