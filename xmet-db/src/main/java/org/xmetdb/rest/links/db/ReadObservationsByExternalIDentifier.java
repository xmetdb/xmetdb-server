package org.xmetdb.rest.links.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.toxbank.client.resource.Protocol;

import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.protocol.db.ReadProtocolAbstract;


public class ReadObservationsByExternalIDentifier extends ReadProtocolAbstract<ExternalIdentifier> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5352529378365800440L;
	private Protocol record = new Protocol();
	
	private static String sql_byexternaltype_and_id =
			String.format(ReadProtocolAbstract.sql_withkeywords, "join protocol_links using(idprotocol,version)", "\nwhere type=? and id=?");
	
	private static String sql_byexternalid =
			String.format(ReadProtocolAbstract.sql_withkeywords, "join protocol_links using(idprotocol,version)", "\nwhere id=?");

	public ReadObservationsByExternalIDentifier(ExternalIdentifier id) {
		super();
		setFieldname(id);
	}	
	@Override
	public String getSQL() throws AmbitException {
		if (getFieldname()==null) throw new AmbitException("ID not defined");
		if (getFieldname().getSystemDesignator()!=null && getFieldname().getSystemIdentifier()!=null)
			return sql_byexternaltype_and_id;
		else if (getFieldname().getSystemIdentifier()!=null)
			return sql_byexternalid;
		else throw new AmbitException("ID not defined");
	}

	@Override
	public List<QueryParam> getParameters() throws AmbitException {
		if (getFieldname()==null) throw new AmbitException("ID not defined");
		List<QueryParam> params = new ArrayList<QueryParam>();
		if (getFieldname().getSystemDesignator()!=null)
			params.add(new QueryParam<String>(String.class, getFieldname().getSystemDesignator()));
		params.add(new QueryParam<String>(String.class, getFieldname().getSystemIdentifier()));
		return params;
	}

	public Protocol getRecord() {
		return record;
	}
	
	@Override
	public String toString() {
		return String.format("%s=%s", getFieldname()==null?"null":getFieldname(),getValue()==null?"null":getValue());
	}

}
