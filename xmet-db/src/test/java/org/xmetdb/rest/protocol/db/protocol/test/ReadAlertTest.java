package org.xmetdb.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.alerts.db.DBAlert;
import net.idea.restnet.user.alerts.db.ReadAlert;

import org.xmetdb.rest.protocol.db.test.QueryTest;

/**
 * Test for {@link ReadAlert}
 * @author nina
 *
 */
public class ReadAlertTest  extends QueryTest<ReadAlert> {

	@Override
	protected ReadAlert createQuery() throws Exception {
		ReadAlert query = new ReadAlert(null);
		query.setFieldname(new DBUser(2));
		return query;
	}

	@Override
	protected void verify(ReadAlert query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBAlert alert = query.getObject(rs);
			Assert.assertEquals(2,alert.getUser().getID());
			Assert.assertNotNull(alert.getUser().getUserName());
			Assert.assertNotNull(alert.getSentAt());
			records++;
		}
		Assert.assertEquals(2,records);
	}

}
