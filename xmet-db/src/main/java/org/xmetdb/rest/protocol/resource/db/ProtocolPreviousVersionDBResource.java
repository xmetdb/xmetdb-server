package org.xmetdb.rest.protocol.resource.db;


import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocolPreviousVersion;

/**
 * 
 * @author nina
 *
 * @param <Q>
 */
public class ProtocolPreviousVersionDBResource<Q extends ReadProtocol> extends ProtocolDBResource<Q> {

	@Override
	protected Q getProtocolQuery(Object key, int userID, Form form)
			throws ResourceException {
		version = true;
		if (key==null) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}			
		else {
			singleItem = false;
			return (Q)new ReadProtocolPreviousVersion(Reference.decode(key.toString()));
		}
	}
	
	@Override
	protected Q createUpdateQuery(Method method, Context context,
			Request request, Response response) throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}	
}