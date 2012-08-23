package org.xmetdb.rest.protocol.db.user.test;

import java.sql.ResultSet;

import org.xmetdb.rest.protocol.db.test.QueryTest;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.db.ReadUser;

import junit.framework.Assert;


public class ReadUserTest  extends QueryTest<ReadUser> {

	@Override
	protected ReadUser createQuery() throws Exception {
		DBUser user = new DBUser(3);
		return new ReadUser(user);
	}

	@Override
	protected void verify(ReadUser query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBUser user = query.getObject(rs);
			Assert.assertEquals(3,user.getID());
			Assert.assertEquals("guest",user.getUserName());
			Assert.assertEquals("abcdef",user.getFirstname());
			Assert.assertEquals("ABCDEF",user.getLastname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}


}
