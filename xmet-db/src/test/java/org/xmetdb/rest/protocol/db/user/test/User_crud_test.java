/* ReferenceCRUDTest.java
 * Author: nina
 * Date: Mar 28, 2009
 * Revision: 0.1 
 * 
 * Copyright (C) 2005-2009  Ideaconsult Ltd.
 * 
 * Contact: nina
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

package org.xmetdb.rest.protocol.db.user.test;

import java.sql.Statement;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.restnet.u.UserCredentials;
import net.idea.restnet.u.UserRegistration;

import org.apache.commons.lang.RandomStringUtils;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.xmetdb.rest.protocol.db.test.CRUDTest;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.db.CreateUser;
import org.xmetdb.rest.user.db.DeleteUser;
import org.xmetdb.rest.user.db.UpdateCredentials;
import org.xmetdb.rest.user.db.UpdateUser;

public final class User_crud_test<T extends Object>  extends CRUDTest<T,DBUser>  {

	protected final String code = RandomStringUtils.randomAlphanumeric(45);
	@Override
	protected IQueryUpdate<T,DBUser> createQuery() throws Exception {
        IDatabaseConnection c = getConnection();	
        Statement st = c.getConnection().createStatement();
        st.addBatch("USE aalocal_test;");
        st.addBatch("DELETE FROM aalocal_test.users;");
        st.addBatch("DELETE FROM aalocal_test.user_registration;");
        st.addBatch("DELETE FROM aalocal_test.user_roles;");
        st.executeBatch();
		ITable table = 	c.createQueryTable("EXPECTED",String.format("SELECT user_name from aalocal_test.users"));
		Assert.assertEquals(0,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED",String.format("SELECT user_name from aalocal_test.user_registration"));
		Assert.assertEquals(0,table.getRowCount());	
		table = 	c.createQueryTable("EXPECTED",String.format("SELECT role_name from aalocal_test.user_roles"));
		Assert.assertEquals(0,table.getRowCount());		
		st.close();
		c.close();
		
		DBUser user = new DBUser();
		user.setFirstname("QWERTY");
		user.setLastname("ASDFG");
		user.setUserName("testuser");
		user.setCredentials(new UserCredentials(null,"test"));
		return (IQueryUpdate<T,DBUser>)new CreateUser(user,new UserRegistration(code),"aalocal_test");
	}

	@Override
	protected void createVerify(IQueryUpdate<T,DBUser> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT iduser,username,firstname,lastname from user where firstname='QWERTY' and username='testuser'"));
		Assert.assertEquals(1,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED",
					"SELECT user_name  from aalocal_test.users where user_name='testuser'");		
		Assert.assertEquals(1,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT user_name,code,status  from aalocal_test.user_registration where user_name='testuser' and status='commenced' and code='%s'",code));		
		Assert.assertEquals(1,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED",
					"SELECT user_name,role_name from aalocal_test.user_roles where user_name='testuser' and role_name='user'");		
		Assert.assertEquals(1,table.getRowCount());		
		c.close();
	}

	@Override
	protected IQueryUpdate<T,DBUser> deleteQuery() throws Exception {
		DBUser ref = new DBUser(4);
		return (IQueryUpdate<T,DBUser>)new DeleteUser(ref);
	}

	@Override
	protected void deleteVerify(IQueryUpdate<T,DBUser> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT iduser FROM user where iduser=4");
		Assert.assertEquals(0,table.getRowCount());
		c.close();
		
	}

	@Override
	protected IQueryUpdate<T,DBUser> updateQuery() throws Exception {
		DBUser ref = new DBUser();
		ref.setLastname("NEW");
		ref.setID(3);

		return (IQueryUpdate<T,DBUser>) new UpdateUser(ref);
	}

	@Override
	protected void updateVerify(IQueryUpdate<T,DBUser> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT lastname FROM user where iduser=3");
		Assert.assertEquals(1,table.getRowCount());

		Assert.assertEquals("NEW",table.getValue(0,"lastname"));
		
		c.close();
		
	}

	@Override
	protected IQueryUpdate<T, DBUser> createQueryNew()
			throws Exception {
		return null;
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<T, DBUser> query)
			throws Exception {
		
		
	}
	@Override
	public void testCreateNew() throws Exception {
	}

	@Test
	public void testUpdatePassword() throws Exception {
		DBUser user = new DBUser();
		user.setUserName("test");
		IDatabaseConnection c = getConnection();
		java.sql.Statement st = c.getConnection().createStatement();
		st.executeUpdate("insert into aalocal_test.users values ('test',md5('test')) on duplicate key update  user_pass=values(user_pass)");

		//String md = org.apache.commons.codec.digest.DigestUtils.md5Hex("test");
		IQueryUpdate query = new UpdateCredentials(
					new UserCredentials("test", "newpwd"), 
					user,"aalocal_test");
		setUpDatabase(dbFile);
		executor.setConnection(c.getConnection());
		executor.open();
		Assert.assertTrue(executor.process(query)>=1);
		
		ITable table = 	c.createQueryTable("EXPECTED","SELECT user_pass FROM `aalocal_test`.users where user_name='test'");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals(org.apache.commons.codec.digest.DigestUtils.md5Hex("newpwd"),table.getValue(0,"user_pass"));
		c.close();
	}
}
