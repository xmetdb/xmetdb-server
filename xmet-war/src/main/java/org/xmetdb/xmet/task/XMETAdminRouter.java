package org.xmetdb.xmet.task;

import net.idea.restnet.aa.resource.AdminRouter;

import org.restlet.Context;
import org.xmetdb.rest.db.DatabaseResource;
import org.xmetdb.rest.user.alerts.notification.NotificationResource;

public class XMETAdminRouter extends AdminRouter  {

	public XMETAdminRouter(Context context) {
		super(context);
	}
	@Override
	protected void init() {
		attachDefault(XMETAdminResource.class);
		attach(String.format("/%s",DatabaseResource.resource),DatabaseResource.class);
		attach(NotificationResource.resourceKey, NotificationResource.class);
				
	}
}