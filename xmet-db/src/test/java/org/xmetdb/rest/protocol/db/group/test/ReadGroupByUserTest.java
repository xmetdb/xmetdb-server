package org.xmetdb.rest.protocol.db.group.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.db.ReadOrganisation;
import net.idea.restnet.user.DBUser;

import org.xmetdb.rest.protocol.db.test.QueryTest;


public class ReadGroupByUserTest extends QueryTest<ReadOrganisation> {
	@Override
	protected ReadOrganisation createQuery() throws Exception {
		DBOrganisation p = new DBOrganisation();
		DBUser user = new DBUser(1);
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