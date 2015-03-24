package org.xmetdb.rest.protocol;

import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.endpoints.EnzymesResource;
import org.xmetdb.rest.links.db.LinksResource;
import org.xmetdb.rest.protocol.resource.db.ProtocolDBResource;
import org.xmetdb.rest.protocol.resource.db.ProtocolVersionDBResource;
import org.xmetdb.rest.user.author.resource.ProtocolAuthorsResource;
import org.xmetdb.rest.xmet.curator.UpdateObservationResource;
import org.xmetdb.xmet.client.Resources;

public class ProtocolRouter extends MyRouter {
	public ProtocolRouter(Context context) {
		super(context);
		attachDefault(ProtocolDBResource.class);
		attach(String.format("/{%s}",FileResource.resourceKey), ProtocolDBResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.versions), ProtocolVersionDBResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.authors), ProtocolAuthorsResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.endpoint), EnzymesResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.som), UpdateObservationResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.link), LinksResource.class);
		attach(String.format("/{%s}%s/{%s}",FileResource.resourceKey,Resources.link,LinksResource.typeKey), LinksResource.class);
		attach(String.format("/{%s}%s/{%s}/{%s}",FileResource.resourceKey,Resources.link,LinksResource.typeKey,LinksResource.valueKey), LinksResource.class);
		
	}
}
