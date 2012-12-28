package org.xmetdb.rest.endpoints.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.db.exceptions.InvalidProtocolException;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.protocol.DBProtocol;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

/**
 * Removes an endpoint of a given protocol. Does not delete the endpoint itself.
 * @author nina
 *
 */
public class DeleteProtocolEndpoint  extends AbstractUpdate<DBProtocol,Enzyme> {
	protected static final String[] sql = new String[] {
		"DELETE from protocol_endpoints a, protocol p where p.idprotocol=a.idprotocol and p.version=a.version and qmrf_number=? and code=? and name=? "
	};
	protected static final String[] sql_all = new String[] {
		"DELETE from protocol_endpoint a, protocol p where p.idprotocol=a.idprotocol and p.version=a.version and qmrf_number=?"
	};
	public DeleteProtocolEndpoint(DBProtocol protocol,Enzyme endpoint) {
		super(endpoint);
		setGroup(protocol);
	}
	public DeleteProtocolEndpoint() {
		this(null,null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getGroup()==null || !getGroup().isValidIdentifier()) throw new InvalidProtocolException();

		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<String>(String.class, getGroup().getIdentifier()));
		if (getObject()==null || getObject().getCode()==null || getObject().getName()==null)  {
			params.add(new QueryParam<String>(String.class, getObject().getCode()));
			params.add(new QueryParam<String>(String.class, getObject().getName()));
		}
		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		return (getObject()==null || getObject().getCode()==null || getObject().getName()==null)?sql:sql_all;
	}
	
	public void setID(int index, int id) {
			
	}
}