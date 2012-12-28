/* CreateReference.java
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

package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;

import org.xmetdb.rest.protocol.DBProtocol;


public class CreateProtocol extends AbstractObjectUpdate<DBProtocol>{
	public static final String[] create_sql = {
		"insert into protocol (idprotocol,title,qmrf_number,abstract,iduser,summarySearchable,idproject,idorganisation,status,created,published_status,atom_uncertainty,product_amount,reference) " +
		"values (?,?,?,?,?,?,?,?,?,now(),?,?,?,?)",
		"insert into protocol_endpoints select idprotocol,version,idtemplate,? from protocol join template where code = ? and idprotocol=? and version=?",
		"update protocol set qmrf_number=? where idprotocol=? and version=?"
	};

	public CreateProtocol(DBProtocol ref) {
		super(ref);
	}
	public CreateProtocol() {
		this(null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params1 = new ArrayList<QueryParam>();
		switch (index) {
		case 0: {
			ReadProtocol.fields[] f = new ReadProtocol.fields[] {
					ReadProtocol.fields.idprotocol,
					ReadProtocol.fields.title,
					ReadProtocol.fields.identifier,
					ReadProtocol.fields.anabstract,
					ReadProtocol.fields.iduser,
					ReadProtocol.fields.summarySearchable,
					ReadProtocol.fields.idproject,
					ReadProtocol.fields.idorganisation,
					ReadProtocol.fields.status,
					ReadProtocol.fields.published_status,
					ReadProtocol.fields.atom_uncertainty,
					ReadProtocol.fields.product_amount,
					ReadProtocol.fields.reference
			};
			for (ReadProtocol.fields field: f) try {
				params1.add(field.getParam(getObject()));
			} catch (Exception x) {
				x.printStackTrace();
			}
			break;
		}
		case 1: {
			String allele = "";
			if (getObject().getEndpoint()!= null && getObject().getEndpoint().getAlleles()!=null && getObject().getEndpoint().getAlleles().length>0)
				allele = getObject().getEndpoint().getAlleles()[0];
			params1.add(new QueryParam<String>(String.class,allele));
			params1.add(new QueryParam<String>(String.class,getObject().getEndpoint()==null?"":getObject().getEndpoint().getCode()));
			params1.add(ReadProtocol.fields.idprotocol.getParam(getObject()));
			params1.add(ReadProtocol.fields.version.getParam(getObject()));
			break;
		}
		case 2: {
			
			params1.add(new QueryParam<String>(String.class,String.format("XMETDB%d",getObject().getID())));
			params1.add(ReadProtocol.fields.idprotocol.getParam(getObject()));
			params1.add(ReadProtocol.fields.version.getParam(getObject()));
			break;
		}		
		}
		return params1;
		
	}

	public String[] getSQL() throws AmbitException {
		return create_sql;
	}
	public void setID(int index, int id) {
		getObject().setID(id);
		getObject().setVersion(1);
	}
	@Override
	public boolean returnKeys(int index) {
		return true;
	}
}
