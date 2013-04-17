package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

import org.xmetdb.rest.protocol.DBProtocol;

public class ReadProtocolByUserName extends ReadProtocolAbstract<String> implements IQueryRetrieval<DBProtocol>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 308150917300833831L;
	/**
	 * 
	 */

	protected static String sql = String.format(sql_withkeywords," where ","username=?");

	public List<QueryParam> getParameters() throws AmbitException {
		if (getFieldname()==null) throw new AmbitException("Empty user");
		if ("".equals(getFieldname().trim())) throw new AmbitException("Empty user");
		List<QueryParam> params =  new ArrayList<QueryParam>();
		params.add(new QueryParam<String>(String.class,getFieldname().trim()));
		return params;
	}

	public String getSQL() throws AmbitException {
		return sql;

	}
	
}