package org.xmetdb.rest.protocol.db.user.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;

import org.xmetdb.rest.protocol.db.test.QueryTest;


public class ReadUserTest  extends QueryTest<ReadUser> {

	@Override
	protected ReadUser createQuery() throws Exception {
		DBUser user = new DBUser(1);
		return new ReadUser(user);
	}

	@Override
	protected void verify(ReadUser query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBUser user = query.getObject(rs);
			Assert.assertEquals(1,user.getID());
			Assert.assertEquals("admin",user.getUserName());
			Assert.assertEquals("Administrator",user.getFirstname());
			Assert.assertEquals("",user.getLastname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}


}
