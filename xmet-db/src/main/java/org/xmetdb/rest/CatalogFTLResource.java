package org.xmetdb.rest;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.CookieSetting;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.xmet.client.Resources;

import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.resource.CatalogResource;

public abstract class CatalogFTLResource<T> extends CatalogResource<T> {
	
	
	public String getTemplateName() {
		return null;
	}

	@Override
	protected Representation getHTMLByTemplate(Variant variant) throws ResourceException {

		        Map<String, Object> map = new HashMap<String, Object>();
		        if (getClientInfo().getUser()!=null) 
		        	map.put("username", getClientInfo().getUser().getIdentifier());
		        map.put("creator","IdeaConsult Ltd.");
		        map.put(Resources.Config.xmet_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_email.name()));
		        map.put(Resources.Config.xmet_about.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_about.name()));
		        map.put(Resources.Config.xmet_guide.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_guide.name()));
		        map.put("xmet_root",getRequest().getRootRef().toString());
		        getRequest().getResourceRef().addQueryParameter("media", Reference.encode(MediaType.APPLICATION_JSON.toString()));
		        map.put("xmet_request",getRequest().getResourceRef().toString());
		        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name()));
		        return toRepresentation(map, getTemplateName(), MediaType.TEXT_PLAIN);

	}
		

}
