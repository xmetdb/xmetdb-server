package org.xmetdb.rest.protocol.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

public class ReadProtocolByID extends ReadProtocolAbstract<String> implements IQueryRetrieval<DBProtocol>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 308150917300833831L;
	/**
	 * 
	 */

	protected static String sql = "select qmrf_number from protocol where idprotocol=? and version=?";

	public List<QueryParam> getParameters() throws AmbitException {
		if (getValue()==null) throw new AmbitException("Empty protocol");
		if (getValue().getID()<=0) throw new AmbitException("Invalid document ID");
		if (getValue().getVersion()<=0) throw new AmbitException("Invalid document version");
		List<QueryParam> params =  new ArrayList<QueryParam>();
		params.add(ReadProtocol.fields.idprotocol.getParam(getValue()));
		params.add(ReadProtocol.fields.version.getParam(getValue()));
		return params;
	}

	public String getSQL() throws AmbitException {
		return sql;

	}
	@Override
	public DBProtocol getObject(ResultSet rs) throws AmbitException {
		try {
			getValue().setIdentifier(rs.getString("qmrf_number"));
			return getValue();
		} catch (Exception x) {
			throw new AmbitException(x);
		}
	}
	
	
}