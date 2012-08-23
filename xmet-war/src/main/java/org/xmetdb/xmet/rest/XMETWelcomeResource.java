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
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

/**
 * A welcome/introductory page
 * @author nina
 *
 */
public class XMETWelcomeResource extends ServerResource {
	protected XmetdbHTMLBeauty htmlBeauty;

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		if (getRequest().getResourceRef().toString().equals(String.format("%s/",getRequest().getRootRef()))) {
	        Map<String, Object> map = new HashMap<String, Object>();
	        if (getClientInfo().getUser()!=null) 
	        	map.put("username", getClientInfo().getUser().getIdentifier());
	        map.put("creator","IdeaConsult Ltd.");
	        map.put(Resources.Config.xmet_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_email.name()));
	        map.put(Resources.Config.xmet_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_editor.name()));
	        map.put(Resources.Config.xmet_template.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_template.name()));
	        map.put(Resources.Config.xmet_manual.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_manual.name()));
	        map.put(Resources.Config.xmet_faq.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_faq.name()));
	        map.put(Resources.Config.xmet_theory.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_theory.name()));
	        map.put(Resources.Config.xmet_home.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_home.name()));
	        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name()));
	        return toRepresentation(map, "body-welcome.ftl", MediaType.TEXT_PLAIN);
		} else {
			//if no slash, all the styles etc. paths are broken...
			redirectSeeOther(String.format("%s/",getRequest().getRootRef()));
			return null;
		}
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
		return new XmetdbHTMLBeauty(Resources.welcome);
	}
	
	public XmetdbHTMLBeauty getHtmlBeauty() {
		if (htmlBeauty==null) htmlBeauty = createHtmlBeauty();
		return htmlBeauty;
	}

	public void setHtmlBeauty(XmetdbHTMLBeauty htmlBeauty) {
		this.htmlBeauty = htmlBeauty;
	}

}
