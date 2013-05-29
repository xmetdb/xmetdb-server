package org.xmetdb.rest.structure.resource;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.modbcum.p.QueryExecutor;
import net.idea.restnet.c.ChemicalMediaType;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.freemarker.FreeMarkerApplicaton;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.QueryResource;

import org.apache.xerces.impl.dv.util.Base64;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.DBRoles;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachment;
import org.xmetdb.xmet.client.Resources;
import org.xmetdb.xmet.client.XMETDBRoles;

public class StructureResource extends CatalogResource<Structure> {
	protected String queryService;
	protected boolean singleItem = false;
	protected HTMLBeauty htmlBeauty = null;
	protected DecimalFormat trf = new DecimalFormat( "#0.00" );
	
	public StructureResource() {
		super();
		queryService = 
			//"http://ambit.uni-plovdiv.bg:8080/qmrfdata";
			((TaskApplication) getApplication()).getProperty(Resources.Config.xmet_ambit_service.name());
		setHtmlbyTemplate(true);
	}

	@Override
	public boolean isHtmlbyTemplate() {
		return singleItem?false:headless?false:super.isHtmlbyTemplate();
	}
	public enum SearchMode {
		auto, similarity, smarts
	}
	
	
	@Override
	public String getTemplateName() {
		return "structures_body.ftl";
	}
	
	@Override
	public void configureTemplateMap(Map<String, Object> map) {
	    map.put("xmet_version_short",((FreeMarkerApplicaton)getApplication()).getVersionShort());
	    map.put("xmet_version_long",((FreeMarkerApplicaton)getApplication()).getVersionLong());
		StructureHTMLBeauty parameters = ((StructureHTMLBeauty)getHTMLBeauty());

		Reference query = getSearchReference(getContext(),getRequest(),getResponse(),parameters);
		

		Reference xmet_request = query.clone();
		map.put("xmet_request",xmet_request.toString());
		
		xmet_request.addQueryParameter("media",MediaType.TEXT_CSV.toString());
		map.put("xmet_request_csv",xmet_request.toString());
		
		xmet_request = query.clone();
		xmet_request.addQueryParameter("media",ChemicalMediaType.CHEMICAL_MDLSDF.toString());
		map.put("xmet_request_sdf",xmet_request.toString());		
		
		query.addQueryParameter("media",MediaType.APPLICATION_JAVASCRIPT.toString());
		map.put("xmet_request_jsonp",query.toString());

	
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
		map.put("creator","Ideaconsult Ltd.");
	    map.put("xmet_root",getRequest().getRootRef());
	    map.put("this_url",getRequest().getResourceRef());
	    map.put("searchURI",htmlBeauty==null || htmlBeauty.getSearchURI()==null?"":htmlBeauty.getSearchURI());
	    map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name()));
        map.put(Resources.Config.xmet_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_email.name()));
        map.put(Resources.Config.xmet_about.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_about.name()));
        map.put(Resources.Config.xmet_guide.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_guide.name()));

	    map.put("query", query2map(parameters));
	}
	
	
	protected Map<String,Object> query2map(StructureHTMLBeauty parameters) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			map.put("search",parameters.getSearchQuery());
		} catch (Exception x) {	}		
		try {
			map.put("option",parameters.getOption().name().toLowerCase());
		} catch (Exception x) {}
		try {
			map.put("threshold",parameters.getThreshold());
		} catch (Exception x) {}		
		try {
			map.put("dataset",parameters.getDatasets());
		} catch (Exception x) {}	
		
		try {
			map.put("pagesize",parameters.getPageSize());
		} catch (Exception x) {}		
		return map;
	}
	protected void parseParameters(Context context, Request request,Response response) throws ResourceException {
		Form form = request.getResourceRef().getQueryAsForm();
		
		StructureHTMLBeauty parameters = ((StructureHTMLBeauty)getHTMLBeauty());
		//parameters.setDatasets(form.getValuesArray("dataset"));
		String search = form.getFirstValue(QueryResource.search_param) == null ? ""
				: form.getFirstValue(QueryResource.search_param).trim();
		
		if ((search == null) || "".equals(search))	search="benzene"; //let's have a default
		
		String pagesize = form.getFirstValue("pagesize");
		String page = form.getFirstValue("page");
		try {
			int psize = Integer.parseInt(pagesize);
			if (psize > 100)parameters.setPageSize(psize);
			else parameters.setPageSize(psize);
		} catch (Exception x) {
			parameters.setPageSize(10);
		}
		try {
			int p = Integer.parseInt(page);
			if ((p < 0) || (p > 100))	parameters.setPage(0);
			else 			parameters.setPage(p);
		} catch (Exception x) {
			parameters.setPage(0);
		}
		String threshold = form.getFirstValue("threshold");
		try {
			parameters.setThreshold(Double.parseDouble(threshold));
		}catch (Exception x) {
			parameters.setThreshold(null);
		}
		SearchMode option = SearchMode.auto;
		try {
			option = SearchMode.valueOf(form.getFirstValue("option")
					.toLowerCase());
		} catch (Exception x) {
			option = SearchMode.auto;
		}
		parameters.setOption(option);
		
		if (search != null)
			search = search.replace("<", "").replace(">", "");
		parameters.setSearchQuery(search);
		
		try {
			String[] datasets = form.getValuesArray("dataset");
			parameters.setDatasets(verifyDataset(datasets));
		} catch (Exception x) { parameters.setDatasets(null); x.printStackTrace();}
		
		
		try {
			String type = form.getFirstValue("type");
			parameters.setMolFile(type==null?false:"b64mol".equals(type));
		} catch (Exception x) {  x.printStackTrace();}
		
		parameters.getFolder().clear();
		try {
			String folder = form.getFirstValue("search_substrates");
			if ("on".equals(folder))
				parameters.getFolder().add("substrate");
		} catch (Exception x) { }
		
		try {
			String folder = form.getFirstValue("search_products");
			if ("on".equals(folder))
				parameters.getFolder().add("product");
		} catch (Exception x) { }		
	}

	protected Reference getSearchReference(Context context, Request request,
			Response response,StructureHTMLBeauty parameters) throws ResourceException {
		parseParameters(context,request,response);
		Reference ref = null;
		try {
			ref = new Reference(String.format("%s/query/compound/search/all",
					queryService));
			switch (parameters.option) {
			case similarity: {
				ref = new Reference(String.format("%s/query/similarity?threshold=%3.2f", queryService,parameters.threshold));
				for (String f : parameters.getFolder())	ref.addQueryParameter("folder", f);
				break;
			}
			case smarts: {
				ref = new Reference(String.format("%s/query/smarts",queryService));
				for (String f : parameters.getFolder())	ref.addQueryParameter("folder", f);
				break;
			}
			}
			ref.addQueryParameter("pagesize", Long.toString(parameters.getPageSize()));
			ref.addQueryParameter("page", Integer.toString(parameters.getPage()));
			if (parameters.getSearchQuery() != null) {
				if (parameters.isMolFile()) {
					ref.addQueryParameter("b64search", parameters.getSearchQuery()); //already base64 encoded
					ref.addQueryParameter("type","mol");
				} else  if (parameters.getSearchQuery().indexOf("#")>=0) //this breaks jquery jsonp even if url encoded, use base64 as workaround
					ref.addQueryParameter("b64search", Base64.encode(parameters.getSearchQuery().getBytes()));
				else
					ref.addQueryParameter(QueryResource.search_param, parameters.getSearchQuery());
			}
			return ref;
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw createException(Status.CLIENT_ERROR_BAD_REQUEST,  parameters.getSearchQuery(),
					parameters.option, ref.toString(), x);
		}
	}
	
	@Override
	protected Iterator<Structure> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		StructureHTMLBeauty parameters = ((StructureHTMLBeauty)getHTMLBeauty());
		Reference ref = getSearchReference(context,request,response,parameters);
		try {
		

			try {
				List<Structure> records = Structure.retrieveStructures(
						queryService, ref.toString());
				return records.iterator();
			} catch (Exception x) {
				throw createException(Status.CLIENT_ERROR_BAD_REQUEST, parameters.getSearchQuery(),
						parameters.option, ref.toString(), x);
			}
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw createException(Status.CLIENT_ERROR_BAD_REQUEST,  parameters.getSearchQuery(),
					parameters.option, ref.toString(), x);
		}
	}

	protected ResourceException createException(Status status, String search,
			SearchMode option, String ref, Throwable x) {
		String message = String.format("Search query '%s' failed", search);
		switch (option) {
		case similarity: {
			message = String.format("SMILES or InChI expected instead of '%s'",
					search);
			break;
		}
		case smarts: {
			message = String.format("SMARTS expected instead of '%s'", search);
			break;
		}

		}
		throw new ResourceException(status.getCode(), message, String.format(
				"Error when contacting (%s) structure search service at %s",
				option.toString(), ref),
				"http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html", x);
	}

	@Override
	protected Reporter createHTMLReporter(boolean headles) {
		return null;
	}

	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty == null) htmlBeauty = new StructureHTMLBeauty(queryService);
		return htmlBeauty;
	}
	

	public String getConfigFile() {
		return "conf/xmetdb.pref";
	}
	
	protected String getAttachmentDir() {
		String dir = ((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_attachments_dir.name());
		return dir==null || "".equals(dir)?System.getProperty("java.io.tmpdir"):dir;
	}
	
	
	protected List<DBAttachment> verifyDataset(String[] attachmentKey) throws Exception {
		if (attachmentKey==null || attachmentKey.length==0) return null;
		List<DBAttachment> results = new ArrayList<DBAttachment>();
		Connection conn = null;
		QueryExecutor  exec = new QueryExecutor();
		try {
			ReadAttachment query;
			DBAttachment attachment = new DBAttachment();
			query = new ReadAttachment(null,getAttachmentDir());
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			exec.setConnection(conn);
			for (String aKey : attachmentKey) {
				if (aKey==null) continue;
				if (aKey.toString().startsWith("A")) {
					attachment.setID(new Integer(Reference.decode(aKey.toString().substring(1))));
					query.setFieldname(null);
					query.setValue(attachment);
				} else {
					query.setFieldname(new DBProtocol(Reference.decode(aKey.toString())));
					query.setValue(null);
				} 
				ResultSet rs = exec.process(query);
				while (rs.next()) {
					DBAttachment a = query.getObject(rs);
					if (a.isImported()) 
						results.add(a);
				}
				rs.close();

			}
			return results;
		} catch (NumberFormatException x) {
			return null;
		} catch (Exception x) {
			try { if (exec!=null) exec.close(); } catch (Exception xx) {}
			try { if (conn!=null) conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
		
	}
	
	public final static String algorithmURI = "/algorithm/fptanimoto";
	
	
}

class PropertiesIterator extends CSVFeatureValuesIterator<Structure> {
	protected Structure structure = null;

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public PropertiesIterator(String url) throws Exception {
		super(url);
	}

	@Override
	public Structure transformRawValues(List header, List values) {
		Structure r = structure == null ? new Structure() : structure;
		String value = null;
		for (int i = 0; i < header.size(); i++)
			try {
				value = values.get(i) == null ? "" : values.get(i).toString()
						.trim();
				if ("null".equals(value))
					value = "";
				if ("metric".equals(header.get(i))) {
					r.setSimilarity(value);
					continue;
				}
				Structure._titles title = Structure._titles.valueOf(header
						.get(i).toString()
						.replace("http://www.opentox.org/api/1.1#", ""));
				// String[] v = value.split("|");
				switch (title) {
				case Compound: {
					r.setResourceIdentifier(new URL(value));
					break;
				}
				case CASRN: {
					r.setCas(value);
					break;
				}
				case ChemicalName: {
					r.setName(value.replace("|", "<br>"));
					break;
				}
				case EINECS: {
					r.setEinecs(value);
					break;
				}
				case SMILES: {
					r.setSMILES(value);
					break;
				}
				case InChI_std: {
					r.setInChI(value);
					break;
				}
				case InChIKey_std: {
					r.setInChIKey(value);
					break;
				}
				}
			} catch (Exception x) {
				if (header.get(i).toString().toUpperCase().startsWith("CAS"))
					r.setCas(value);
				else if ("NAME".equals(header.get(i).toString().toUpperCase()))
					r.setName(value);
				else
					try {
						NumberFormat nf = java.text.DecimalFormat
								.getNumberInstance(Locale.ENGLISH);
						r.getProperties().put(header.get(i).toString(),
								nf.format(Double.parseDouble(value)));
					} catch (Exception e) {
						r.getProperties().put(header.get(i).toString(), value);
					}
			}

		return r;
	}
	
	
};
