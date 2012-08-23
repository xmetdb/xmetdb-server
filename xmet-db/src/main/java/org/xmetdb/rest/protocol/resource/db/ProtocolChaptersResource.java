package org.xmetdb.rest.protocol.resource.db;

import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

import org.restlet.resource.ResourceException;
import org.xmetdb.xmet.client.Resources;

public class ProtocolChaptersResource extends SingleProtocolResource {

	public ProtocolChaptersResource() {
		super(Resources.chapter);
	}
	

	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException {
		XmetdbChaptersHTMLReporter rep = new XmetdbChaptersHTMLReporter(getRequest(),!singleItem,isEditable(),structure==null,details);
		rep.setHeadless(headless);
		rep.setHtmlBeauty(getHTMLBeauty());
		rep.setDtdresolver(((TaskApplication)getApplication()).getResolver());
		return rep;
	}
}	