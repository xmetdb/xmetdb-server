package org.xmetdb.xmet.aa;

import java.util.Map;

import net.idea.restnet.aa.local.UserLogoutPOSTResource;
import net.idea.restnet.c.freemarker.FreeMarkerApplicaton;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.security.User;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

public class XMETLogoutPOSTResource<U extends User> extends UserLogoutPOSTResource<U> {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.protocol);
	}
	
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
			
	     this.getResponse().redirectSeeOther(String.format("%s/",getRequest().getRootRef()));
	     return null;
	}
	
	@Override
	public void configureTemplateMap(Map<String, Object> map) {
		super.configureTemplateMap(map);
	    map.put("xmet_version_short",((FreeMarkerApplicaton)getApplication()).getVersionShort());
	    map.put("xmet_version_long",((FreeMarkerApplicaton)getApplication()).getVersionLong());
	}
}
