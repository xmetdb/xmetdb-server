package org.xmetdb.rest.user.author.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.db.exceptions.InvalidProtocolException;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.db.ReadUser;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;


public class ReadAuthor extends ReadUser<DBProtocol> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2677470406987442304L;
	private static final String join = "\njoin protocol_authors using(iduser)\njoin protocol using(idprotocol,version) where ";
	public ReadAuthor(DBProtocol protocol,DBUser user) {
		super(user);
		setFieldname(protocol);
	}
	

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params = null;
		if (getFieldname()==null || getFieldname().getIdentifier()==null) throw new InvalidProtocolException();
		params = new ArrayList<QueryParam>();
		params.add(ReadProtocol.fields.identifier.getParam(getFieldname()));
		if ((getValue()!=null) && getValue().getID()>0)
			params.add(fields.iduser.getParam(getValue()));
		
		return params;
	}

	public String getSQL() throws AmbitException {
		if ((getValue()!=null) && (getValue().getID()>0))
			return String.format(sql,join,
				   String.format("%s and %s",
						   ReadProtocol.fields.identifier.getCondition(),
						   fields.iduser.getCondition()));
		else 
			return String.format(sql,join,
					String.format("%s ",
							ReadProtocol.fields.identifier.getCondition()
							));
			
	}	
}
