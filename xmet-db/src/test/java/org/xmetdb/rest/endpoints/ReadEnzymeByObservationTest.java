package org.xmetdb.rest.endpoints;

import java.sql.ResultSet;

import junit.framework.Assert;

import org.xmetdb.rest.endpoints.db.ReadEnzymeByObservation;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.test.QueryTest;

public class ReadEnzymeByObservationTest extends QueryTest<ReadEnzymeByObservation> {

	
	@Override
	protected ReadEnzymeByObservation createQuery() throws Exception {
		DBProtocol p = new DBProtocol();
		p.setIdentifier("XMETDB2");
		return new ReadEnzymeByObservation(p);
	}

	@Override
	protected void verify(ReadEnzymeByObservation query, ResultSet rs) throws Exception {
		int count=0;
		while (rs.next()) {
			count++;
			Enzyme enzyme = query.getObject(rs);
			Assert.assertTrue(enzyme.getId()>0);
			Assert.assertNotNull(enzyme.getCode());
			Assert.assertNotNull(enzyme.getName());			
		}
		Assert.assertEquals(1,count);
	}


}