package org.xmetdb.rest.xmet.curator;

import java.util.Map;

import net.idea.modbcum.i.IQueryRetrieval;

import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.UpdateObservationEntry;

public class DraftObservationsResource<Q extends IQueryRetrieval<DBProtocol>> extends UpdateObservationResource<Q> {
	
	public DraftObservationsResource() {
		super();
	}
	@Override
	protected UpdateObservationEntry getUpdateQuery()  {
		UpdateObservationEntry query = new UpdateObservationEntry();
		query.setSetCuratedFlag(true);
		return query;
	}

	@Override
	protected Map<String, Object> getMap(Variant variant)
			throws ResourceException {
		 Map<String, Object> map = super.getMap(variant);
		 map.put("xmet_updatemode", "curate");
		 return map;
	}
	
}
