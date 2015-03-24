package org.xmetdb.rest.links.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;

public class UpdateObservationIdentifier extends
		AbstractUpdate<DBProtocol, ExternalIdentifier> {
	private static String[] insertSQL = { "insert ignore into protocol_links (idprotocol,version,type,id) select idprotocol,version,?,? from protocol where qmrf_number=?" };

	public UpdateObservationIdentifier(DBProtocol observation,
			ExternalIdentifier id) {
		super(id);
		setGroup(observation);
	}

	public UpdateObservationIdentifier() {
		this(null, null);
	}

	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getGroup() == null)
			throw new AmbitException("Empty observation");
		if (getGroup().getIdentifier() == null)
			throw new AmbitException("Invalid observation ID");

		List<QueryParam> params = new ArrayList<QueryParam>();
		if (getObject() != null) {
			params.add(new QueryParam<String>(String.class, getObject()
					.getSystemDesignator()));
			params.add(new QueryParam<String>(String.class, getObject()
					.getSystemIdentifier()));
			params.add(ReadProtocol.fields.identifier.getParam(getGroup()));
		}
		return params;
	}

	@Override
	public String[] getSQL() throws AmbitException {
		return insertSQL;
	}

	public void setID(int index, int id) {

	}
}
