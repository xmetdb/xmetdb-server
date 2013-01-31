package org.xmetdb.rest.protocol.db.enzyme.test;


import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.endpoints.db.CreateEndpoint;
import org.xmetdb.rest.endpoints.db.UpdateEnzyme;
import org.xmetdb.rest.protocol.db.test.CRUDTest;

public class Enzyme_crud_test  extends CRUDTest<Object,Enzyme>  {


	@Override
	protected IQueryUpdate<Object,Enzyme> createQuery() throws Exception {
		Enzyme enzyme = new Enzyme();
		enzyme.setCode("CYPXYZ");
		enzyme.setName("Cytochrome XYZ");
		enzyme.setUniprot_id("Q1234");
		enzyme.setAlleles(new String[] {"A","B","C"});
		return new CreateEndpoint(enzyme);
	}

	@Override
	protected void createVerify(IQueryUpdate<Object,Enzyme> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT name,code,uniprot,alleles from template where code='CYPXYZ'"));
		
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("Cytochrome XYZ",table.getValue(0,"name"));
		Assert.assertEquals("Q1234",table.getValue(0,"uniprot"));
		Assert.assertEquals("A,B,C",table.getValue(0,"alleles"));
		
		c.close();
	}

	@Override
	public void testDelete() throws Exception {
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
		Enzyme enzyme = new Enzyme();
		enzyme.setId(2);
		enzyme.setAlleles(new String[] {"A","B","C"});
		UpdateEnzyme q = new UpdateEnzyme();
		q.setObject(enzyme);
		return q;
	}

	@Override
	protected void updateVerify(IQueryUpdate<Object,Enzyme> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT name,code,uniprot,alleles from template where idtemplate=2"));
		
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("A,B,C",table.getValue(0,"alleles"));
		c.close();		

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