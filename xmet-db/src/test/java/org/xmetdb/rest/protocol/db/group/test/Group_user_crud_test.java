package org.xmetdb.rest.protocol.db.group.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmetdb.rest.groups.DBOrganisation;
import org.xmetdb.rest.groups.DBProject;
import org.xmetdb.rest.groups.IDBGroup;
import org.xmetdb.rest.groups.user.db.AddGroupsPerUser;
import org.xmetdb.rest.groups.user.db.DeleteGroupsPerUser;
import org.xmetdb.rest.protocol.db.test.CRUDTest;
import org.xmetdb.rest.user.DBUser;

public class Group_user_crud_test  extends CRUDTest<DBUser,List<IDBGroup>> {

	@Override
	protected IQueryUpdate<DBUser, List<IDBGroup>> createQuery()
			throws Exception {
		return new AddGroupsPerUser(new DBUser(1),new DBProject(2));
	}


	@Override
	protected void createVerify(IQueryUpdate<DBUser, List<IDBGroup>> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				"SELECT idproject from user_project where iduser=1 and idproject=2");
		
		Assert.assertEquals(1,table.getRowCount());
		c.close();
		
	}


	@Override
	protected IQueryUpdate<DBUser, List<IDBGroup>> createQueryNew()
			throws Exception {
		return new AddGroupsPerUser<IDBGroup>(new DBUser(1),new DBOrganisation(4));
	}

	
	@Override
	protected void createVerifyNew(IQueryUpdate<DBUser, List<IDBGroup>> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				"SELECT idorganisation from user_organisation where iduser=1 and idorganisation=4");
		
		Assert.assertEquals(1,table.getRowCount());
		c.close();
		
	}


	@Override
	protected IQueryUpdate<DBUser, List<IDBGroup>> updateQuery()
			throws Exception {
		return null;
	}

	
	@Override
	protected void updateVerify(IQueryUpdate<DBUser, List<IDBGroup>> query)
			throws Exception {
		
	}
	

	@Override
	protected IQueryUpdate<DBUser, List<IDBGroup>> deleteQuery()
			throws Exception {
		DBUser ref = new DBUser(1);
		List<IDBGroup> p = new ArrayList<IDBGroup>();
		p.add(new DBProject(1));
		DeleteGroupsPerUser q = new DeleteGroupsPerUser();
		q.setGroup(ref);
		q.setObject(p);
		return q;
	}

	@Override
	protected void deleteVerify(IQueryUpdate<DBUser, List<IDBGroup>> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				"SELECT idproject from user_project where iduser=1 and idproject=1");
		
		Assert.assertEquals(0,table.getRowCount());
		c.close();
	}

	@Override
	public void testUpdate() throws Exception {
	}
}
