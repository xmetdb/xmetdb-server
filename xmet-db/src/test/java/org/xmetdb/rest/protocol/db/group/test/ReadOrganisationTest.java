package org.xmetdb.rest.protocol.db.group.test;

import java.sql.ResultSet;

import org.xmetdb.rest.groups.DBOrganisation;
import org.xmetdb.rest.groups.db.ReadOrganisation;
import org.xmetdb.rest.protocol.db.test.QueryTest;

import junit.framework.Assert;


public class ReadOrganisationTest extends QueryTest<ReadOrganisation> {
	@Override
	protected ReadOrganisation createQuery() throws Exception {
		DBOrganisation p = new DBOrganisation();
		p.setID(4);
		return new ReadOrganisation(p);
	}

	@Override
	protected void verify(ReadOrganisation query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBOrganisation group = query.getObject(rs);
			Assert.assertEquals(4,group.getID());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}
