package org.xmetdb.rest.protocol.db.group.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.groups.db.ReadProject;

import org.xmetdb.rest.protocol.db.test.QueryTest;


public class ReadProjectTest  extends QueryTest<ReadProject> {

	@Override
	protected ReadProject createQuery() throws Exception {
		DBProject p = new DBProject();
		p.setID(1);
		return new ReadProject(p);
	}

	@Override
	protected void verify(ReadProject query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProject group = query.getObject(rs);
			Assert.assertEquals(1,group.getID());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}
