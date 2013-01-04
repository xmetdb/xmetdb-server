package org.xmetdb.rest.user.author.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;
import net.idea.restnet.user.DBUser;

import org.xmetdb.rest.db.exceptions.InvalidProtocolException;
import org.xmetdb.rest.protocol.DBProtocol;

/**
 * Removes an author of a given protocol. Does not delete the user itself.
 * @author nina
 *
 */
public class DeleteAuthor  extends AbstractUpdate<DBProtocol,DBUser> {
	protected static final String[] sql = new String[] {
		"DELETE from protocol_authors a, protocol p where p.idprotocol=a.idprotocol and p.version=a.version and qmrf_number=? and iduser=?"
	};
	protected static final String[] sql_all = new String[] {
		"DELETE from protocol_authors a, protocol p where p.idprotocol=a.idprotocol and p.version=a.version and qmrf_number=?"
	};
	public DeleteAuthor(DBProtocol protocol,DBUser author) {
		super(author);
		setGroup(protocol);
	}
	public DeleteAuthor() {
		this(null,null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getGroup()==null || !getGroup().isValidIdentifier()) throw new InvalidProtocolException();

		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<String>(String.class, getGroup().getIdentifier()));
		if ((getObject()!=null) && (getObject().getID()>0))
			params.add(new QueryParam<Integer>(Integer.class, getObject().getID()));
		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		return ((getObject()!=null) && (getObject().getID()>0))?sql:sql_all;
	}
	public void setID(int index, int id) {
			
	}
}