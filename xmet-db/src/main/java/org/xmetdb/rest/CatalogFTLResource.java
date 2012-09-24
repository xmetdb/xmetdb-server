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
	protected boolean htmlbyTemplate = false;
	public boolean isHtmlbyTemplate() {
		return htmlbyTemplate;
	}

	public void setHtmlbyTemplate(boolean htmlbyTemplate) {
		this.htmlbyTemplate = htmlbyTemplate;
	}
	
	public String getTemplateName() {
		return null;
	}

	protected Representation getHTMLByTemplate(Variant variant) throws ResourceException {
		//	if (getRequest().getResourceRef().toString().equals(String.format("%s/",getRequest().getRootRef()))) {

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
		//	} else {
				//if no slash, all the styles etc. paths are broken...
			//	redirectSeeOther(String.format("%s/",getRequest().getRootRef()));
				//return null;
		//	}
		}
		

	    protected Representation toRepresentation(Map<String, Object> map,
	            String templateName, MediaType mediaType) {
	        
	        return new TemplateRepresentation(
	        		templateName,
	        		((FreeMarkerApplicaton)getApplication()).getConfiguration(),
	        		map,
	        		MediaType.TEXT_HTML);
	    }
	    
		@Override
		protected Representation get(Variant variant) throws ResourceException {
			if (htmlbyTemplate && MediaType.TEXT_HTML.equals(variant.getMediaType())) {
				CookieSetting cS = new CookieSetting(0, "subjectid", getToken());
				cS.setPath("/");
		        this.getResponse().getCookieSettings().add(cS);
		        return getHTMLByTemplate(variant);
	    	} else				
	    		return super.get(variant);
		}

}
