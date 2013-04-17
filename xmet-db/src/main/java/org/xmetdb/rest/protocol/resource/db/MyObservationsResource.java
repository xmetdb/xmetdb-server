package org.xmetdb.rest.protocol.resource.db;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.db.ReadProtocolByUserName;

public class MyObservationsResource extends ProtocolDBResource<ReadProtocolByUserName> {

	public MyObservationsResource() {
		super();
	}
	
	@Override
	public String getTemplateName() {
		return "protocols_body.ftl";
	}
	@Override
	protected ReadProtocolByUserName createQuery(Context context, Request request, Response response)
			throws ResourceException {
		Object search_value = null;
		try {
			search_value = getClientInfo().getUser().getIdentifier();
		} catch (Exception x) {
			search_value = null;
			x.printStackTrace();
		}				
		if (search_value == null) throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED,"Not logged in!");		
		ReadProtocolByUserName query = new ReadProtocolByUserName();
		query.setFieldname(search_value.toString());
		return query;
	}
	@Override
	protected Representation put(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation put(Representation representation)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation post(Representation entity)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation delete() throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation delete(Variant variant) throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
}
