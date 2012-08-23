package org.xmetdb.rest.protocol.db.group.test;

import java.sql.ResultSet;

import org.xmetdb.rest.groups.DBOrganisation;
import org.xmetdb.rest.groups.db.ReadOrganisation;
import org.xmetdb.rest.protocol.db.test.QueryTest;
import org.xmetdb.rest.user.DBUser;

import junit.framework.Assert;


public class ReadGroupByUserTest extends QueryTest<ReadOrganisation> {
	@Override
	protected ReadOrganisation createQuery() throws Exception {
		DBOrganisation p = new DBOrganisation();
		DBUser user = new DBUser(88);
		ReadOrganisation q = new ReadOrganisation(p);
		q.setFieldname(user);
		return q;
	}

	@Override
	protected void verify(ReadOrganisation query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBOrganisation group = query.getObject(rs);
			Assert.assertEquals(1,group.getID());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}