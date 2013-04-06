package org.xmetdb.xmet.rest;

import java.util.HashMap;
import java.util.Map;

import net.idea.restnet.c.TaskApplication;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.xmetdb.rest.DBRoles;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;
import org.xmetdb.xmet.client.XMETDBRoles;

/**
 * A welcome/introductory page
 * @author nina
 *
 */
public class XMETWelcomeResource extends ServerResource {

	
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		//if (getRequest().getResourceRef().toString().equals(String.format("%s/",getRequest().getRootRef()))) {
	        Map<String, Object> map = new HashMap<String, Object>();
	        if (getClientInfo().getUser()!=null) 
	        	map.put("username", getClientInfo().getUser().getIdentifier());
	        map.put("creator","IdeaConsult Ltd.");

	        map.put(XMETDBRoles.xmetdb_admin.name(), Boolean.FALSE);
			map.put(XMETDBRoles.xmetdb_curator.name(), Boolean.FALSE);
			if (getClientInfo()!=null) {
				if (getClientInfo().getUser()!=null)
					map.put("username", getClientInfo().getUser().getIdentifier());
				if (getClientInfo().getRoles()!=null) {
					if (DBRoles.isAdmin(getClientInfo().getRoles()))
						map.put(XMETDBRoles.xmetdb_admin.name(),Boolean.TRUE);
					if (DBRoles.isCurator(getClientInfo().getRoles()))
						map.put(XMETDBRoles.xmetdb_curator.name(), Boolean.TRUE);
					if (DBRoles.isUser(getClientInfo().getRoles()))
						map.put(XMETDBRoles.xmetdb_user.name(), Boolean.TRUE);	
				}
			}
	        
	        map.put("xmet_root",getRequest().getRootRef().toString());
	        map.put("xmet_request",getRequest().getResourceRef().toString());
	        map.put(Resources.Config.xmet_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_email.name()));
	        map.put(Resources.Config.xmet_about.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_about.name()));
	        map.put(Resources.Config.xmet_guide.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_guide.name()));
	        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name()));
	        return toRepresentation(map, "/search_body.ftl", MediaType.TEXT_PLAIN);
		//} else {
			//if no slash, all the styles etc. paths are broken...
			//redirectSeeOther(String.format("%s/",getRequest().getRootRef()));
			//return null;
		//}
	}
	

    protected Representation toRepresentation(Map<String, Object> map,
            String templateName, MediaType mediaType) {
        
        return new TemplateRepresentation(
        		templateName,
        		((XMETApplication)getApplication()).getConfiguration(),
        		map,
        		MediaType.TEXT_HTML);
    }
    
	@Override
	protected Representation get() throws ResourceException {
		return this.get(null);
	}
	protected XmetdbHTMLBeauty createHtmlBeauty() {
		return new XmetdbHTMLBeauty(Resources.search);
	}
	
	public XmetdbHTMLBeauty getHtmlBeauty() {
		return null;
	}

	public void setHtmlBeauty(XmetdbHTMLBeauty htmlBeauty) {

	}

}
