package org.xmetdb.rest.links.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IParameterizedQuery;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.modbcum.q.query.AbstractQuery;

import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;

public class ReadObservationIdentifiers
		extends
		AbstractQuery<DBProtocol, ExternalIdentifier, EQCondition, ExternalIdentifier>
		implements IQueryRetrieval<ExternalIdentifier>,
		IParameterizedQuery<DBProtocol, ExternalIdentifier, EQCondition> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2268008311829764871L;
	private static String readSQL = "select idprotocol,version,type,id,qmrf_number from from protocol join protocol_links using(idprotocol,version) where idprotocol=? and version=?";
	private static String readSQL_byidentifier = "select idprotocol,version,type,id,qmrf_number from protocol join protocol_links using(idprotocol,version) where qmrf_number=?";

	@Override
	public String getSQL() throws AmbitException {
		if (getFieldname() == null)
			throw new AmbitException("Empty protocol");
		else if (getFieldname().getVisibleIdentifier() != null)
			return readSQL_byidentifier;
		else
			return readSQL;
	}

	public ReadObservationIdentifiers() {
		this(null);
	}

	public ReadObservationIdentifiers(DBProtocol protocol) {
		super();
		setFieldname(protocol);
	}

	public List<QueryParam> getParameters() throws AmbitException {
		if (getFieldname() == null)
			throw new AmbitException("Empty protocol");
		List<QueryParam> params = new ArrayList<QueryParam>();
		if (getFieldname().getVisibleIdentifier() != null) {
			params.add(ReadProtocol.fields.identifier.getParam(getFieldname()));
		} else {
			if (getFieldname().getID() <= 0)
				throw new AmbitException("Invalid document ID");
			if (getFieldname().getVersion() <= 0)
				throw new AmbitException("Invalid document version");
			params.add(ReadProtocol.fields.idprotocol.getParam(getFieldname()));
			params.add(ReadProtocol.fields.version.getParam(getFieldname()));
		}
		return params;
	}

	@Override
	public ExternalIdentifier getObject(ResultSet rs) throws AmbitException {
		try {
			return new ExternalIdentifier(rs.getString("qmrf_number"),
					rs.getString("type"),
					rs.getString("id"));
		} catch (Exception x) {
			throw new AmbitException(x);
		}
	}

	@Override
	public boolean isPrescreen() {
		return false;
	}

	@Override
	public double calculateMetric(ExternalIdentifier object) {
		return 1;
	}

}
