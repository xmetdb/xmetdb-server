package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.test.QueryTest;

import junit.framework.Assert;


public class ReadProtocolByTimeStampTest extends QueryTest<ReadProtocol> {

	@Override
	protected ReadProtocol createQuery() throws Exception {

		DBProtocol p = new DBProtocol();
		p.setTimeModified(1254757473000L);//in msec
		/**
		 * in DB: 1241795264, 1254757473 
		 */
		ReadProtocol q = new ReadProtocol(p);
		return q;
	}

	@Override
	protected void verify(ReadProtocol query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			//System.out.println(String.format("%d\t%s\t%s",protocol.getID(),protocol.getTimeModified(),new Date(protocol.getTimeModified())));
			//Assert.assertEquals(2,protocol.getID());
			//Assert.assertNotNull(protocol.getKeywords());
			//Assert.assertEquals(new Long(1326699051000L),protocol.getTimeModified());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}
	
}	
