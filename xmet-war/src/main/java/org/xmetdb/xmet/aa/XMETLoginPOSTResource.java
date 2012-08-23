package org.xmetdb.xmet.aa;

import net.idea.restnet.aa.local.UserLoginPOSTResource;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.security.User;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

public class XMETLoginPOSTResource<U extends User> extends UserLoginPOSTResource<U> {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.protocol);
	}
}
