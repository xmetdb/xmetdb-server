package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.test.CRUDTest;
import org.xmetdb.rest.protocol.db.test.QueryTest;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.author.db.ReadAuthor;

import junit.framework.Assert;


public class ReadProtocolAuthorsTest extends QueryTest<ReadAuthor> {

	@Override
	protected ReadAuthor createQuery() throws Exception {
		return new ReadAuthor(new DBProtocol(CRUDTest.idxmet2),null);
	}

	@Override
	protected void verify(ReadAuthor query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBUser user = query.getObject(rs);
			Assert.assertEquals(10,user.getID());
			Assert.assertEquals("TestQMRF",user.getFirstname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}