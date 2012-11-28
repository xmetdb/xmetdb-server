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

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmetdb.rest.endpoints.EndpointTest;
import org.xmetdb.rest.endpoints.db.AddProtocolEndpoint;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.test.CRUDTest;

public final class ProtocolEndpoint_crud_test<T extends Object>  extends CRUDTest<T,DBProtocol>  {
	
	@Override
	public void testCreate() throws Exception {
	
	}
	@Override
	protected IQueryUpdate<T,DBProtocol> createQuery() throws Exception {
		return null;
	}

	@Override
	protected void createVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
      
	}
	@Override
	public void testDelete() throws Exception {

	}

	@Override
	protected IQueryUpdate<T,DBProtocol> deleteQuery() throws Exception {
		return null;
	}

	@Override
	protected void deleteVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
	
	}
	
	/**
	 * Adds authors to a protocol
	 */
	@Override
	protected IQueryUpdate<T,DBProtocol> updateQuery() throws Exception {
		DBProtocol protocol = new DBProtocol(idxmet1);
		EndpointTest endpoint = new EndpointTest("UV-VIS Adsorption Spectra",null);
		endpoint.setCode("101");
		IQueryUpdate<T,DBProtocol> q = (IQueryUpdate<T,DBProtocol>)new AddProtocolEndpoint(endpoint,protocol);
		return q;
	}

	@Override
	protected void updateVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				"SELECT idprotocol,version,idtemplate,code,name FROM  protocol_endpoints join template using(idtemplate) where idprotocol=2");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("101",table.getValue(0, "code"));
		Assert.assertEquals("UV-VIS Adsorption Spectra",table.getValue(0, "name"));

		c.close();
	}

	protected static String newQMRFNumber = DBProtocol.generateIdentifier(); 
	@Override
	protected IQueryUpdate<T, DBProtocol> createQueryNew()
			throws Exception {
		return null;
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<T, DBProtocol> query)
			throws Exception {

	}
	
	@Override
	public void testCreateNew() throws Exception {
	
	}

}
