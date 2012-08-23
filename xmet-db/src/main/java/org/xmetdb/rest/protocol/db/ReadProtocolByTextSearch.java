package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

/**
 * Free text search, as supported byMySQL full text search. Relies on table keywords to be populated.
 * @author nina
 *
 */
public class ReadProtocolByTextSearch extends ReadProtocolByEndpointString {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5128395204960444566L;
	protected static String sql = String.format(ReadProtocol.sql_withkeywords,
					"where ","published_status='published' and match (keywords.keywords) against (?)");

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null) 
			params.add(new QueryParam<String>(String.class, getFieldname()));
		else throw new AmbitException("No search query name!");
		return params;
	}

	public String getSQL() throws AmbitException {
		return sql;

	}

}
