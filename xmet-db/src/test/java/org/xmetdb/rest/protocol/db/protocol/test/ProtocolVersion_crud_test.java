package org.xmetdb.rest.protocol.db.protocol.test;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.CreateProtocolCopy;
import org.xmetdb.rest.protocol.db.CreateProtocolVersion;
import org.xmetdb.rest.protocol.db.test.CRUDTest;

public class ProtocolVersion_crud_test<T extends Object>  extends CRUDTest<T,DBProtocol>  {
	DBProtocol copy = new DBProtocol(idxmet2);

	@Override
	protected IQueryUpdate<T,DBProtocol> createQuery() throws Exception {
		DBProtocol ref = new DBProtocol(idxmet2);
		ref.setAbstract("abstrakt");
		ref.setTitle("title");
		return (IQueryUpdate<T,DBProtocol>)new CreateProtocolVersion(DBProtocol.generateIdentifier(),ref);
	}

	@Override
	protected void createVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT idprotocol,version,published_status,title,idtemplate,allele FROM protocol left join protocol_endpoints using(idprotocol,version) where idprotocol=2"));
		
		Assert.assertEquals(2,table.getRowCount());
		//ensure endpoints are copied as well
		Assert.assertNotNull(table.getValue(0,"idtemplate"));
		Assert.assertNotNull(table.getValue(1,"idtemplate"));
		Assert.assertEquals("1A",table.getValue(0,"allele"));
		Assert.assertEquals("1A",table.getValue(1,"allele"));
		c.close();	
	}
	
	/**
	 * Create a version of a published protocol
	 */
	@Override
	protected IQueryUpdate<T, DBProtocol> createQueryNew() throws Exception {
		DBProtocol ref = new DBProtocol(idxmet2);
		ref.setAbstract("abstrakt");
		ref.setTitle("title");
		return (IQueryUpdate<T,DBProtocol>)new CreateProtocolVersion(DBProtocol.generateIdentifier(),ref);
	}


	@Override
	protected void createVerifyNew(IQueryUpdate<T, DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT idprotocol,version,qmrf_number,published_status,title FROM protocol where idprotocol=2 order by idprotocol,version"));
		
		Assert.assertEquals(2,table.getRowCount());
		Assert.assertEquals("draft",table.getValue(1,"published_status"));
		Assert.assertEquals("archived",table.getValue(0,"published_status"));
		Assert.assertEquals("XMETDB2",table.getValue(0,"qmrf_number"));
		Assert.assertEquals("XMETDB2v2",table.getValue(1,"qmrf_number"));
		c.close();	
		
	}


	@Override
	public void testDelete() throws Exception {
	
	}
	
	@Override
	protected IQueryUpdate<T, DBProtocol> updateQuery() throws Exception {
		copy = new DBProtocol(idxmet2);
		copy.setAbstract("abstrakt");
		copy.setTitle("title");
		return (IQueryUpdate<T,DBProtocol>)new CreateProtocolCopy(copy);	
	}

	@Override
	protected IQueryUpdate<T, DBProtocol> deleteQuery() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateVerify(IQueryUpdate<T, DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT idprotocol,version,qmrf_number,published_status,title FROM protocol where qmrf_number='"+copy.getIdentifier()+"'"));
		
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("draft",table.getValue(0,"published_status"));
		c.close();			
	}

	@Override
	protected void deleteVerify(IQueryUpdate<T, DBProtocol> query)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}