package org.xmetdb.rest.protocol.attachments.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

public class UpdateAttachment<PROTOCOL extends Object> extends AbstractUpdate<PROTOCOL,DBAttachment> {
	public static final String[] create_sql = {
		"update attachments set imported =? where idattachment=?"
	};

	public UpdateAttachment(DBProtocol protocol,DBAttachment attachment) {
		super(attachment);
		//this.group = protocol;
	}
	
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params = new ArrayList<QueryParam>();
		IQueryUpdate<DBProtocol,DBAttachment> q = (IQueryUpdate<DBProtocol,DBAttachment>) this; 
		params.add(new QueryParam<Boolean>(Boolean.class, getObject().isImported()));
		if (!(getObject()!=null) && (getObject().getID()>0)) throw new AmbitException("Empty attachment id");
		params.add(new QueryParam<Integer>(Integer.class, getObject().getID()));
		return params;
	}

	public String[] getSQL() throws AmbitException {
		return create_sql;
	}
	public void setID(int index, int id) {
		
	}
	@Override
	public boolean returnKeys(int index) {
		return false;
	}
}