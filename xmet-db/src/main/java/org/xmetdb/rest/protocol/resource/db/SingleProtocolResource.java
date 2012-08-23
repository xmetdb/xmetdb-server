package org.xmetdb.rest.protocol.resource.db;


import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.xmet.client.Resources;

public class SingleProtocolResource  extends ProtocolDBResource<ReadProtocol> {
	protected String suffix ;
	
	public SingleProtocolResource(String suffix) {
		super();
		this.suffix = suffix;
		singleItem = true;
	}

	@Override
	protected ReadProtocol createQuery(Context context, Request request, Response response)
			throws ResourceException {
		final Object key = request.getAttributes().get(FileResource.resourceKey);		
		try {
			if (key==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			else {
				return new ReadProtocol(Reference.decode(key.toString()));
			}
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new ResourceException(
					Status.CLIENT_ERROR_BAD_REQUEST,
					String.format("Invalid protocol id %d",key),
					x
					);
		}
	}
	

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) {
			htmlBeauty = new XmetdbHTMLBeauty(Resources.protocol,true);
		}
		return htmlBeauty;
	}
}
