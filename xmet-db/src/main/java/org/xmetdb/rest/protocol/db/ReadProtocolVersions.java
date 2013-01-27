package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

public class ReadProtocolVersions extends ReadProtocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = -806521374100593321L;
	/*
	public ReadProtocolVersions(Integer id,Integer year) {
		super(id,-1,year);
	}
	*/
	public ReadProtocolVersions(String identifier) {
		super(identifier);
	}
	public String getSQL() throws AmbitException {
		if (getValue()!=null) {
			if (getValue().isValidIdentifier())
					return String.format(sql_withkeywords,"where",
						 String.format("idprotocol in (select idprotocol from protocol where qmrf_number=?)",fields.identifier.getCondition()));

		} 
		throw new AmbitException("No protocol identifier"); 
	}
	
	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params = null;
		if (getValue()!=null) {
			params = new ArrayList<QueryParam>();
			if (getValue().isValidIdentifier())
				params.add(fields.identifier.getParam(getValue()));
		}
		return params;
	}	
}
