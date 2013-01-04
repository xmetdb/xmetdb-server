package org.xmetdb.rest.endpoints;

import java.sql.ResultSet;

import junit.framework.Assert;

import org.xmetdb.rest.endpoints.db.QueryOntology;
import org.xmetdb.rest.endpoints.db.QueryOntology.RetrieveMode;
import org.xmetdb.rest.protocol.db.test.QueryTest;

import ambit2.base.data.Dictionary;

public class QueryOntologyTest extends QueryTest<QueryOntology> {

	@Override
	protected QueryOntology createQuery() throws Exception {
		QueryOntology q = new QueryOntology();
		q.setIncludeParent(RetrieveMode.childandarent);
		q.setValue(new Dictionary(null,null));
		return q;
	}

	@Override
	protected void verify(QueryOntology query, ResultSet rs) throws Exception {
		int count=0;
		while (rs.next()) {
			count++;
		}
		Assert.assertEquals(15,count);
	}

}
