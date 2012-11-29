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

package org.xmetdb.rest.protocol.db.protocol.test;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.toxbank.client.resource.Protocol.STATUS;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.xmetdb.rest.endpoints.EndpointTest;
import org.xmetdb.rest.groups.DBOrganisation;
import org.xmetdb.rest.groups.DBProject;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.CreateProtocol;
import org.xmetdb.rest.protocol.db.DeleteProtocol;
import org.xmetdb.rest.protocol.db.PublishProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.UpdateProtocol;
import org.xmetdb.rest.protocol.db.test.CRUDTest;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.author.db.AddAuthor;
import org.xmetdb.xmet.client.AtomUncertainty;
import org.xmetdb.xmet.client.ProductAmount;
import org.xmetdb.xmet.client.PublishedStatus;

public final class Protocol_crud_test<T extends Object>  extends CRUDTest<T,DBProtocol>  {
	
	
	@Override
	protected IQueryUpdate<T,DBProtocol> createQuery() throws Exception {

		DBProtocol ref = new DBProtocol(); //id2v1);
		ref.setID(3);
		ref.setVersion(1);
		//ref.setAbstract(IOUtils.toString(in, "UTF-8"));
		ref.setTitle("HEP");
		ref.setAbstract("Hepatocytes");
		ref.addKeyword("hepatocyte");
		ref.setPublished(true);
		return (IQueryUpdate<T,DBProtocol>)new UpdateProtocol(ref);

	}

	@Override
	protected void createVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT idprotocol,version,published_status,title,abstract,qmrf_number FROM protocol where idprotocol=3 and version=1"));
		
		Assert.assertEquals(1,table.getRowCount());
		//we ignore the published flag here! There is a special PublishProtocol query
		Assert.assertEquals(Boolean.FALSE,
					(Boolean)(PublishedStatus.published==table.getValue(0,ReadProtocol.fields.published_status.name()))
					);
		Assert.assertEquals("HEP",table.getValue(0,"title"));
		Assert.assertEquals("Hepatocytes",table.getValue(0,"abstract"));
		Assert.assertEquals("XMETDB3",table.getValue(0,"qmrf_number"));
		c.close();	
	}
	
	@Test
	public void testMarkAsDeleted() throws Exception {
		IQueryUpdate<T,DBProtocol> query = markAsDeletedQuery();
		setUpDatabase(dbFile);
		IDatabaseConnection c = getConnection();
		executor.setConnection(c.getConnection());
		executor.open();
		Assert.assertTrue(executor.process(query)>=1);
		markAsDeleteVerify(query);
		c.close();
	}

	protected IQueryUpdate<T,DBProtocol> markAsDeletedQuery() throws Exception {
		DBProtocol ref = new DBProtocol(idxmet2);
		return ( IQueryUpdate<T,DBProtocol>)new DeleteProtocol(ref);
	}
	
	protected void markAsDeleteVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idprotocol,published_status FROM protocol where idprotocol=2 and version=1");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals(PublishedStatus.deleted.name(),table.getValue(0,ReadProtocol.fields.published_status.name()));
		c.close();
		
	}	
	@Override
	protected IQueryUpdate<T,DBProtocol> deleteQuery() throws Exception {
		DBProtocol ref = new DBProtocol(idxmet3);
		return ( IQueryUpdate<T,DBProtocol>)new DeleteProtocol(ref);
	}

	@Override
	protected void deleteVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idprotocol FROM protocol where idprotocol=3 and version=1");
		Assert.assertEquals(0,table.getRowCount());
		c.close();
		
	}
	
	/**
	 * Adds authors to a protocol
	 */
	@Override
	protected IQueryUpdate<T,DBProtocol> updateQuery() throws Exception {
		DBProtocol ref = new DBProtocol(idxmet2);
		//ref.addAuthor(new DBUser(3));
		//ref.addAuthor(new DBUser(10));

		IQueryUpdate<T,DBProtocol> q = (IQueryUpdate<T,DBProtocol>)new AddAuthor(new DBUser(3),ref);
		return q;
	}

	@Override
	protected void updateVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idprotocol,version,iduser FROM protocol_authors where idprotocol=2");
		Assert.assertEquals(1,table.getRowCount());
		c.close();
	}

	@Override
	protected IQueryUpdate<T, DBProtocol> createQueryNew()
			throws Exception {
		DBProtocol ref = new DBProtocol();
		DBUser user = new DBUser();
		user.setID(3);
		ref.setOwner(user);
		ref.setProject(new DBProject(1));	
		ref.setOrganisation(new DBOrganisation(1));
		ref.setSearchable(true);
		ref.setStatus(STATUS.SOP);
		ref.setPublished(false);
		ref.setIdentifier("");
		ref.setEndpoint(new EndpointTest(null,null));
		ref.getEndpoint().setCode("CYP3A4");
		ref.setAbstract("Hepatocytes");
		ref.setTitle("HEP");
		ref.setProductAmount(ProductAmount.Major);
		ref.setAtomUncertainty(AtomUncertainty.Certain);
		return (IQueryUpdate<T, DBProtocol>)new CreateProtocol(ref);
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<T, DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT idprotocol,qmrf_number,summarySearchable,status,atom_uncertainty,product_amount FROM protocol where title='HEP' and abstract =  'Hepatocytes' and iduser='3' and idproject=1 and idorganisation=1"));
		
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals(Boolean.TRUE,table.getValue(0,"summarySearchable"));
		Assert.assertEquals(STATUS.SOP.toString(),table.getValue(0,"status"));
		Assert.assertEquals(AtomUncertainty.Certain.toString(),table.getValue(0,"atom_uncertainty"));
		Assert.assertEquals(ProductAmount.Major.toString(),table.getValue(0,"product_amount"));
		Assert.assertTrue(table.getValue(0,"qmrf_number").toString().startsWith("XMETDB"));
		c.close();		
	}


	@Test
	public void testPublish() throws Exception {
		IQueryUpdate<T,DBProtocol> query = publishQuery();
		setUpDatabase(dbFile);
		IDatabaseConnection c = getConnection();
		executor.setConnection(c.getConnection());
		executor.open();
		Assert.assertTrue(executor.process(query)>=1);
		publishVerify(query);
		c.close();
	}
	
	protected IQueryUpdate<T,DBProtocol> publishQuery() throws Exception {
		DBProtocol ref = new DBProtocol(); 
		ref.setID(3);
		ref.setVersion(1);
		ref.setPublished(true);
		return (IQueryUpdate<T,DBProtocol>)new PublishProtocol(ref);
	}

	protected void publishVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
					"SELECT idprotocol,version,qmrf_number,published_status FROM protocol where idprotocol=3 and version=1");
		
		Assert.assertEquals(PublishedStatus.published.name(),table.getValue(0,ReadProtocol.fields.published_status.name()));
		Assert.assertEquals("XMETDB3",table.getValue(0,"qmrf_number"));
		c.close();
	}
}
