package org.xmetdb.xmet.task;

import net.idea.restnet.aa.resource.AdminRouter;

import org.restlet.Context;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.xmet.admin.XmetdbUploadUIResource;

public class XMETEditorRouter extends AdminRouter  {

	public XMETEditorRouter(Context context) {
		super(context);
	}
	@Override
	protected void init() {
		attachDefault(XmetdbUploadUIResource.class);
		attach(String.format("/{%s}",FileResource.resourceKey),XmetdbUploadUIResource.class);
	}
}