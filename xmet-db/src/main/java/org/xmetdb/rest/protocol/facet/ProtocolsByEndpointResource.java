package org.xmetdb.rest.protocol.facet;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.facet.IFacet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ResourceException;

/**
 * Observations by enzyme
 * @author nina
 *
 */
public class ProtocolsByEndpointResource extends XMETFacetResource {
	public static final String resource = "/endpoint";
	
	public ProtocolsByEndpointResource() {
		super();
	}

	
	@Override
	protected IQueryRetrieval<IFacet<String>> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		return new EndpointProtocolFacetQuery(getResourceRef(getRequest()).toString());
	}
	
}