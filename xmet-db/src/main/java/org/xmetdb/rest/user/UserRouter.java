package org.xmetdb.rest.user;

import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;
import org.xmetdb.rest.groups.OrganisationRouter;
import org.xmetdb.rest.groups.ProjectRouter;
import org.xmetdb.rest.protocol.ProtocolRouter;
import org.xmetdb.rest.protocol.resource.db.ProtocolDBResource;
import org.xmetdb.rest.user.alerts.resource.AlertRouter;
import org.xmetdb.rest.user.resource.UserDBResource;
import org.xmetdb.xmet.client.Resources;

public class UserRouter extends MyRouter {
	public UserRouter(Context context,ProtocolRouter protocols,OrganisationRouter orgRouter, 
				ProjectRouter projectRouter, AlertRouter alertRouter) {
		super(context);
		attachDefault(UserDBResource.class);
		attach(String.format("/{%s}",UserDBResource.resourceKey), UserDBResource.class);
		attach(String.format("/{%s}%s",UserDBResource.resourceKey,Resources.protocol), ProtocolDBResource.class);
		attach(String.format("/{%s}%s",UserDBResource.resourceKey,Resources.project),projectRouter);
		attach(String.format("/{%s}%s",UserDBResource.resourceKey,Resources.organisation), orgRouter);
		attach(String.format("/{%s}%s",UserDBResource.resourceKey,Resources.alert), alertRouter);
	}
}
