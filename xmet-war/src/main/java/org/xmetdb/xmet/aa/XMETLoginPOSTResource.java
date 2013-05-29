package org.xmetdb.xmet.aa;

import java.util.Map;

import net.idea.restnet.aa.local.UserLoginPOSTResource;
import net.idea.restnet.c.freemarker.FreeMarkerApplicaton;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.security.User;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

public class XMETLoginPOSTResource<U extends User> extends UserLoginPOSTResource<U> {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.protocol);
	}
	@Override
	public void configureTemplateMap(Map<String, Object> map) {
		super.configureTemplateMap(map);
	    map.put("xmet_version_short",((FreeMarkerApplicaton)getApplication()).getVersionShort());
	    map.put("xmet_version_long",((FreeMarkerApplicaton)getApplication()).getVersionLong());
	}
}
