package org.xmetdb.rest.protocol.resource.db;


import java.util.Map;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocolVersions;

public class ProtocolVersionDBResource<Q extends ReadProtocol> extends ProtocolDBResource<Q> {

	@Override
	public String getTemplateName() {
		return "protocols_body.ftl";
	}
	
	@Override
	protected Q getProtocolQuery(Object key, int userID, Form form)
			throws ResourceException {
		version = true;
		if (key==null) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}			
		else {
			singleItem = false;
			visibleQuery = Reference.decode(key.toString());
			return (Q)new ReadProtocolVersions(Reference.decode(key.toString()));
		}
	}
	
	@Override
	protected Q createUpdateQuery(Method method, Context context,
			Request request, Response response) throws ResourceException {
		Object key = request.getAttributes().get(FileResource.resourceKey);
		if (Method.POST.equals(method)) {
			if (key!=null) { //post allowed only on /protocol/id/versions
				return (Q)new ReadProtocol(Reference.decode(key.toString()));
			}
		} else {
			//if (key!=null) return super.createUpdateQuery(method, context, request, response);
		}
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}	

}
