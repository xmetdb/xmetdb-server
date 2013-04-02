package org.xmetdb.rest.protocol.resource.db;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.user.DBUser;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.xmet.client.Resources;

public class DraftObservationsResource<Q extends IQueryRetrieval<DBProtocol>> extends ProtocolDBResource<Q> {
	
	@Override
	protected Q getProtocolQuery(Object key, int userID, Form form)
			throws ResourceException {
		
		if (key==null) {
			visibleQuery = "Not yet curated observations";
			ReadProtocol dbQuery = new ReadProtocol();
			dbQuery.setOnlyUnpublished(true);
			dbQuery.setShowUnpublished(true);
			dbQuery.setValue(null);
			if (userID>0) {
				dbQuery.setFieldname(new DBUser(userID));
			} 
			return (Q)dbQuery;
		}			
		else {
			singleItem = true;
			ReadProtocol dbQuery =  new ReadProtocol(Reference.decode(key.toString()));
			visibleQuery = Reference.decode(key.toString());
			dbQuery.setShowUnpublished(true);
			if (userID>0) dbQuery.setFieldname(new DBUser(userID));
			return (Q)dbQuery;
		}
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty = new XmetdbHTMLBeauty(Resources.draft,false);
		return htmlBeauty;
	}

}
