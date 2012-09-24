package org.xmetdb.rest.xmet.admin;

import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.toxbank.client.resource.Organisation;
import net.toxbank.client.resource.Project;
import net.toxbank.client.resource.User;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CookieSetting;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.FreeMarkerApplicaton;
import org.xmetdb.rest.db.exceptions.InvalidXmetdbNumberException;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty.update_mode;
import org.xmetdb.xmet.client.Resources;

/**
 * @TODO Rewrite to use the .ftl template only 
 */
public class XmetdbUploadUIResource extends CatalogResource<DBProtocol> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1658864353613787327L;
	protected List<DBProtocol> items = new ArrayList<DBProtocol>();
	protected DBProtocol protocol = null;
	protected update_mode mode = update_mode.newdocument;
	protected boolean htmlbyTemplate = true;

	public String getTemplateName() {
		return "submit_body.ftl";
	}

	public boolean isHtmlbyTemplate() {
		return htmlbyTemplate;
	}

	public void setHtmlbyTemplate(boolean htmlbyTemplate) {
		this.htmlbyTemplate = htmlbyTemplate;
	}

	
	public XmetdbUploadUIResource() {
		super();
	}
	@Override
	protected Iterator<DBProtocol> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		try {
			mode = update_mode.valueOf(request.getResourceRef().getQueryAsForm().getFirstValue("mode").toString());
		} catch (Exception x) {mode = update_mode.newdocument;}		
		try { 
			Object key = request.getAttributes().get(FileResource.resourceKey);
			if (key!=null)  
			try { //add attachments to a protocol
				protocol = new DBProtocol(key.toString());
				protocol.setResourceURL(new URL(String.format("%s%s/%s", getRequest().getRootRef(),Resources.protocol, protocol.getIdentifier())));
				if (update_mode.newdocument.equals(mode)) mode = update_mode.attachments;
			} catch (Exception x) {
				throw new InvalidXmetdbNumberException(key.toString());
			}
			else { protocol=null; mode = update_mode.newdocument;}
			//else //new protocol
		} catch (Exception x) {}


		return items.iterator();
	}
	protected Reporter createHTMLReporter(boolean headles) {
		return new XmetdbCatalogHTMLReporter<DBProtocol>(getRequest(),getDocumentation(),getHTMLBeauty(),null) {
			@Override
			public void header(Writer w, Iterator<DBProtocol> query) {
				super.header(w, query);
				String uri = protocol==null?String.format("%s%s",getRequest().getRootRef().toString(), Resources.protocol):
					String.format("%s%s",protocol.getResourceURL(),Resources.attachment);
				boolean attachments = true;
				try {
					if (protocol==null) {

						protocol= new DBProtocol();
						protocol.setPublished(true);
						protocol.setOrganisation(new Organisation(new URL(String.format("%s%s",getRequest().getRootRef(),
						((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_default_organisation.name())))));
						protocol.setProject(new Project(new URL(String.format("%s%s",getRequest().getRootRef(),
								((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_default_project.name())))));
						protocol.setOwner(new User(new URL(String.format("%s%s",getRequest().getRootRef(),
								((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_default_owner.name())))));	
					}
				} catch (Exception x) {
					logger.debug(x);
					protocol = null;
				}
				try {
					w.write(((XmetdbHTMLBeauty)htmlBeauty).printUploadForm(uri,uri, protocol,mode,request.getRootRef().toString()));
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
			@Override
			protected String printPageNavigator() {
				// want to mimic the paging style, probably could be done in a better way
				return	String.format(
						"<div style='background-color: #109DFF;font-weight:bold;border: 1px solid #109DFF;'><p style='color:#ffffff;'>%s QMRF document</p></div>\n",
						mode.toString());
			
			}
		};
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.editor);
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
}
