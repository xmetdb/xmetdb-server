package org.xmetdb.xmet.task;

import net.idea.restnet.aa.resource.AdminRouter;

import org.restlet.Context;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.xmet.curator.DraftObservationsResource;

public class XMETCuratorRouter  extends AdminRouter  {

	public XMETCuratorRouter(Context context) {
		super(context);
	}
	@Override
	protected void init() {
		attachDefault(DraftObservationsResource.class);
		attach(String.format("/{%s}",FileResource.resourceKey),DraftObservationsResource.class);
	}
}