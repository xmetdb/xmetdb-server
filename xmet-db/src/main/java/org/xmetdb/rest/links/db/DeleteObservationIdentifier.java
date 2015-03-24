package org.xmetdb.rest.links.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;

public class DeleteObservationIdentifier extends
		AbstractUpdate<DBProtocol, ExternalIdentifier> {
	private static String[] deleteSQL = { "delete L from protocol_links L, protocol p where L.idprotocol=p.idprotocol and L.version=p.version and qmrf_number=? and type=? and id=?" };

	public DeleteObservationIdentifier(DBProtocol observation,
			ExternalIdentifier id) {
		super(id);
		setGroup(observation);
	}

	public DeleteObservationIdentifier() {
		this(null, null);
	}

	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getGroup() == null)
			throw new AmbitException("Empty observation");
		if (getGroup().getIdentifier() == null)
			throw new AmbitException("Invalid observation ID");

		List<QueryParam> params = new ArrayList<QueryParam>();
		if (getObject() != null) {
			params.add(ReadProtocol.fields.identifier.getParam(getGroup()));			
			params.add(new QueryParam<String>(String.class, getObject()
					.getSystemDesignator()));
			params.add(new QueryParam<String>(String.class, getObject()
					.getSystemIdentifier()));
		} else throw new AmbitException("Invalid observation");
		return params;
	}

	@Override
	public String[] getSQL() throws AmbitException {
		return deleteSQL;
	}

	public void setID(int index, int id) {

	}
}
