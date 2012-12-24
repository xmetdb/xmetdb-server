package org.xmetdb.xmet.task;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.TaskResource;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

public class XMETTaskResource extends TaskResource<String> {

	public XMETTaskResource() {
		super();
		setHtmlbyTemplate(true);
	}
	@Override
	public String getTemplateName() {
		return "task_body.ftl";
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.protocol);
	}
	
	@Override
	protected void configureTemplateMap(Map<String, Object> map) {
        if (getClientInfo().getUser()!=null) 
        	map.put("username", getClientInfo().getUser().getIdentifier());
        map.put("creator","IdeaConsult Ltd.");
        map.put(Resources.Config.xmet_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_email.name()));
        map.put(Resources.Config.xmet_about.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_about.name()));
        map.put(Resources.Config.xmet_guide.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_guide.name()));
        map.put("xmet_root",getRequest().getRootRef().toString());

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
        
        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name()));
        Object taskid = getRequest().getAttributes().get(resourceKey);
        if (taskid!=null)
        	map.put("taskid",taskid.toString());
	}
	
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF<Object>(storage,getHTMLBeauty()) {
			@Override
			public synchronized Reporter<Iterator<UUID>, Writer> createTaskReporterHTML(
					Request request,ResourceDoc doc,HTMLBeauty htmlbeauty) throws AmbitException, ResourceException {
				return	new XMETTaskHTMLReporter<Object>(storage,request,doc,htmlbeauty);
			}			
		};
	}
}
