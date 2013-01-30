package org.xmetdb.rest.protocol.db.enzyme.test;


import java.net.URI;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.endpoints.db.CreateEndpoint;
import org.xmetdb.rest.protocol.db.test.CRUDTest;

public class Enzyme_crud_test  extends CRUDTest<Object,Enzyme>  {


	@Override
	protected IQueryUpdate<Object,Enzyme> createQuery() throws Exception {
		Enzyme enzyme = new Enzyme();
		enzyme.setCode("CYPXYZ");
		enzyme.setName("Cytochrome XYZ");
		enzyme.setUri(new URI("Q1234"));
		enzyme.setAlleles(new String[] {"A","B","C"});
		return new CreateEndpoint(enzyme);
	}

	@Override
	protected void createVerify(IQueryUpdate<Object,Enzyme> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT name,code,uri,allele from template where code='CYPXYZ'"));
		
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("Cytochrome XYZ",table.getValue(0,"name"));
		Assert.assertEquals("http://www.uniprot.org/uniprot/Q1234",table.getValue(0,"uri"));
		Assert.assertEquals("A,B,C",table.getValue(0,"allele"));
		
		c.close();
	}

	@Override
	public void testDelete() throws Exception {
	}
	@Override
	public void testUpdate() throws Exception {
	}
	@Override
	protected IQueryUpdate<Object,Enzyme> deleteQuery() throws Exception {
		return null;
	}

	@Override
	protected void deleteVerify(IQueryUpdate<Object,Enzyme> query)
			throws Exception {
	}

	@Override
	protected IQueryUpdate<Object,Enzyme> updateQuery() throws Exception {
		return null;
	}

	@Override
	protected void updateVerify(IQueryUpdate<Object,Enzyme> query)
			throws Exception {

	}

	@Override
	protected IQueryUpdate<Object,Enzyme> createQueryNew()
			throws Exception {
		return null;
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<Object,Enzyme> query)
			throws Exception {
		
		
	}
	@Override
	public void testCreateNew() throws Exception {
	}

}