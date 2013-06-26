package org.xmetdb.rest.structure.db;

import java.sql.ResultSet;

import junit.framework.Assert;

import org.xmetdb.rest.protocol.db.test.QueryTest;

public class ReadSOMTest extends QueryTest<ReadStructureSOM> {

	
	@Override
	protected ReadStructureSOM createQuery() throws Exception {
		return new ReadStructureSOM("XMETDB3");
	}

	@Override
	protected void verify(ReadStructureSOM query, ResultSet rs) throws Exception {
		int count=0;
		while (rs.next()) {
			count++;
			PropertyValueBuffer b = query.getObject(rs);
			System.out.println(b);
			
		}
		Assert.assertEquals(2,count);
	}


}