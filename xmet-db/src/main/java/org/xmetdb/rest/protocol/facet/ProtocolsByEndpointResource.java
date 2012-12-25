package org.xmetdb.rest.protocol.facet;

import java.util.Map;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.db.facet.FacetResource;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

/**
 * Observations by enzyme
 * @author nina
 *
 */
public class ProtocolsByEndpointResource extends FacetResource<IQueryRetrieval<IFacet<String>>> {
	public static final String resource = "/endpoint";
	
	public ProtocolsByEndpointResource() {
		super();
		setHtmlbyTemplate(true);
	}
	@Override
	public String getTemplateName() {
		return "facet_body.ftl";
	}
	
	@Override
	protected IQueryRetrieval<IFacet<String>> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		return new EndpointProtocolFacetQuery(getResourceRef(getRequest()).toString());
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.protocol);
	}
	
	@Override
	public String getConfigFile() {
		return "conf/xmetdb.pref";
	}
	@Override
	protected boolean isAllowedMediaType(MediaType mediaType)
			throws ResourceException {
		return false;
	}
	
	@Override
	protected QueryHTMLReporter getHTMLReporter(Request request) {
		XmetdbHTMLBeauty hb = (XmetdbHTMLBeauty)getHTMLBeauty();
		hb.setSearchURI(Resources.endpoint);
		return new XmetdbHTMLFacetReporter(request,true,null,hb);
	}
	@Override
	public void configureTemplateMap(Map<String, Object> map) {
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

	}
}