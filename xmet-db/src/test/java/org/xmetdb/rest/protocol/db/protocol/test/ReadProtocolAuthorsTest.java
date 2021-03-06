package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.restnet.user.DBUser;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.test.CRUDTest;
import org.xmetdb.rest.protocol.db.test.QueryTest;
import org.xmetdb.rest.user.author.db.ReadAuthor;


public class ReadProtocolAuthorsTest extends QueryTest<ReadAuthor> {

	@Override
	protected ReadAuthor createQuery() throws Exception {
		return new ReadAuthor(new DBProtocol(CRUDTest.idxmet1),null);
	}

	@Override
	protected void verify(ReadAuthor query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBUser user = query.getObject(rs);
			Assert.assertEquals(2,user.getID());
			Assert.assertEquals("Guest",user.getFirstname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}