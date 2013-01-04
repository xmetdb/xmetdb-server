package org.xmetdb.rest.protocol.db.group.test;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.groups.IDBGroup;
import net.idea.restnet.groups.db.CreateGroup;
import net.idea.restnet.groups.db.DeleteGroup;
import net.idea.restnet.groups.db.UpdateGroup;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmetdb.rest.protocol.db.test.CRUDTest;

public class Group_crud_test  extends CRUDTest<Object,IDBGroup>  {
	String file = "http://localhost/1.pdf";

	@Override
	protected IQueryUpdate<Object,IDBGroup> createQuery() throws Exception {
		DBProject ref = new DBProject();
		ref.setGroupName("opentox");
		ref.setTitle("OpenTox");
		return new CreateGroup(ref);
	}

	@Override
	protected void createVerify(IQueryUpdate<Object,IDBGroup> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				"SELECT idproject,name,ldapgroup from project where name='OpenTox'");
		
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("opentox",table.getValue(0,"ldapgroup"));
		Assert.assertEquals("OpenTox",table.getValue(0,"name"));
		c.close();
	}

	@Override
	protected IQueryUpdate<Object,IDBGroup> deleteQuery() throws Exception {
		DBProject ref = new DBProject();
		ref.setID(2);
		return new DeleteGroup(ref);
	}

	@Override
	protected void deleteVerify(IQueryUpdate<Object,IDBGroup> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idproject FROM project where idproject=2");
		Assert.assertEquals(0,table.getRowCount());
		c.close();
		
	}


	@Override
	protected IQueryUpdate<Object,IDBGroup> updateQuery() throws Exception {
		DBProject ref = new DBProject();
		ref.setID(2);
		ref.setGroupName(null);
		ref.setTitle("ToxBank");
		return new UpdateGroup(ref);
	}

	@Override
	protected void updateVerify(IQueryUpdate<Object,IDBGroup> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT name,ldapgroup FROM project where idproject=2");
		Assert.assertEquals(1,table.getRowCount());

		Assert.assertEquals("ToxBank",table.getValue(0,"name"));
		Assert.assertNull(table.getValue(0,"ldapgroup"));
		
		c.close();
		
	}

	@Override
	protected IQueryUpdate<Object, IDBGroup> createQueryNew()
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<Object, IDBGroup> query)
			throws Exception {
		
		
	}
	@Override
	public void testCreateNew() throws Exception {
	}

}