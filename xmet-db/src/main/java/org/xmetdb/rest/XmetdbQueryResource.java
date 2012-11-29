package org.xmetdb.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.i.task.ICallableTask;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.i.task.Task;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

public abstract class XmetdbQueryResource<Q extends IQueryRetrieval<T>,T extends Serializable> extends QueryResource<Q,T>{

	protected boolean headless = false;
	protected XmetdbHTMLBeauty htmlBeauty;
	
	public XmetdbQueryResource() {
		super();
		
	}
	
	protected String getQueryService() {
		return ((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name());
	}
	protected String getAttachmentDir() {
		String dir = ((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_attachments_dir.name());
		return dir==null || "".equals(dir)?System.getProperty("java.io.tmpdir"):dir;
	}
	public boolean isHeadless() {
		return headless;
	}


	public void setHeadless(boolean headless) {
		this.headless = headless;
	}


	@Override
	public String getConfigFile() {
		return "conf/xmetdb.pref";
	}
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		headless = getHeadlessParam();
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty = new XmetdbHTMLBeauty(Resources.protocol);
		return htmlBeauty;
	}
	
	protected boolean getHeadlessParam() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		try {
			return Boolean.parseBoolean(form.getFirstValue("headless").toString());
		} catch (Exception x) {
			return false;
		}	
	}
	
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF(storage,getHTMLBeauty());
	}
	
	protected abstract QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException;
	
	@Override
	protected Task<Reference, Object> addTask(
			ICallableTask callable,
			T item,
			Reference reference) throws ResourceException {

			return ((TaskApplication)getApplication()).addTask(
				String.format("%s %s %s",
						callable.toString(),
						item==null?"":item.toString(),
						reference==null?"":" "),									
				callable,
				getRequest().getRootRef(),
				getToken());		
		
	}
	
	protected Map<String, Object> getMap(Variant variant) throws ResourceException {
		   Map<String, Object> map = new HashMap<String, Object>();

			map.put("managerRole", "false");
			map.put("editorRole", "false");
			if (getClientInfo()!=null) {
				if (getClientInfo().getUser()!=null)
					map.put("username", getClientInfo().getUser().getIdentifier());
				if (getClientInfo().getRoles()!=null) {
					if (getClientInfo().getRoles().indexOf(XmetdbHTMLReporter.managerRole)>=0)
						map.put("managerRole", "true");
					if (getClientInfo().getRoles().indexOf(XmetdbHTMLReporter.editorRole)>=0)
						map.put("editorRole", "true");
				}
			}

		        map.put("creator","IdeaConsult Ltd.");
		        map.put(Resources.Config.xmet_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_email.name()));
		        map.put(Resources.Config.xmet_about.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_about.name()));
		        map.put(Resources.Config.xmet_guide.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_guide.name()));
		        map.put("xmet_root",getRequest().getRootRef().toString());
		        getRequest().getResourceRef().addQueryParameter("media",MediaType.APPLICATION_JSON.toString());
		        map.put("xmet_request",getRequest().getResourceRef().toString());
		        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name()));

		        map.put("searchURI",htmlBeauty==null || htmlBeauty.getSearchURI()==null?"":htmlBeauty.getSearchURI());
		        
		        //remove paging
		        Form query = getRequest().getResourceRef().getQueryAsForm();
		        //query.removeAll("page");query.removeAll("pagesize");query.removeAll("max");
		        query.removeAll("media");
		        Reference r = getRequest().getResourceRef().clone();
		        r.setQuery(query.getQueryString());
		        map.put("xmet_request",r.toString()) ;
		        if (query.size()>0)
		        	map.put("xmet_query",query.getQueryString()) ;
		        //json
		        query.removeAll("media");query.add("media", MediaType.APPLICATION_JSON.toString());
		        r.setQuery(query.getQueryString());
		        map.put("xmet_request_json",r.toString());
		        //csv
		        query.removeAll("media");query.add("media", MediaType.TEXT_CSV.toString());
		        r.setQuery(query.getQueryString());
		        map.put("xmet_request_csv",r.toString());
		        return map;
	}
	
	@Override
	protected Representation getHTMLByTemplate(Variant variant) throws ResourceException {
		getHTMLBeauty();
        return toRepresentation(getMap(variant), getTemplateName(), MediaType.TEXT_PLAIN);
	}	

}
