package org.xmetdb.xmet.aa;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.restnet.aa.local.UserLoginFormResource;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.security.User;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;


public class XMETLoginFormResource extends UserLoginFormResource<User> {
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.login);
	}
	@Override
	protected Reporter createHTMLReporter(boolean headles) {
		return new XMETLoginFormReporter(getRequest(),getDocumentation(),getHTMLBeauty());
	}

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
			User user = getRequest().getClientInfo().getUser();
			if ((user!=null) && (user.getIdentifier()!=null)) {
				 this.getResponse().redirectSeeOther(String.format("%s%s",getRequest().getRootRef(),Resources.protocol));
				 return null;
			}	
		}
		return super.get(variant);
	}

}
