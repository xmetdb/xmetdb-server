package org.xmetdb.rest.endpoints;

import java.sql.ResultSet;

import junit.framework.Assert;

import org.xmetdb.rest.endpoints.db.ReadEnzyme;
import org.xmetdb.rest.protocol.db.test.QueryTest;

public class ReadEnzymeTest extends QueryTest<ReadEnzyme> {

	
	@Override
	protected ReadEnzyme createQuery() throws Exception {
		return new ReadEnzyme();
	}

	@Override
	protected void verify(ReadEnzyme query, ResultSet rs) throws Exception {
		int count=0;
		while (rs.next()) {
			count++;
			Enzyme enzyme = query.getObject(rs);
			Assert.assertTrue(enzyme.getId()>0);
			Assert.assertNotNull(enzyme.getCode());
			Assert.assertNotNull(enzyme.getName());
		}
		Assert.assertEquals(15,count);
	}


}