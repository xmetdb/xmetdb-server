package org.xmetdb.xmet.task;

import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;

public class XMETTaskRouter extends MyRouter {
	public XMETTaskRouter(Context context) {
		super(context);
		attachDefault(XMETTaskResource.class);
		attach(XMETTaskResource.resourceID, XMETTaskResource.class);
	}
}
