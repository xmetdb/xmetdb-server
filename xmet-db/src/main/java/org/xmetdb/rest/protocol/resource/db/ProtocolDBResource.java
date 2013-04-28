package org.xmetdb.rest.protocol.resource.db;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import net.idea.modbcum.i.IQueryObject;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.restnet.c.ChemicalMediaType;
import net.idea.restnet.c.PageParams;
import net.idea.restnet.c.RepresentationConvertor;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.c.task.TaskCreator;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.db.convertors.OutputWriterConvertor;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.db.convertors.RDFJenaConvertor;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;
import net.toxbank.client.io.rdf.TOXBANK;

import org.apache.commons.fileupload.FileItem;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.XmetdbQueryResource;
import org.xmetdb.rest.db.exceptions.MethodNotAllowedException;
import org.xmetdb.rest.protocol.CallableProtocolCopy;
import org.xmetdb.rest.protocol.CallableProtocolUpload;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.protocol.db.QueryProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocolByStructure;
import org.xmetdb.rest.structure.resource.Structure;
import org.xmetdb.rest.user.resource.UserDBResource;
import org.xmetdb.xmet.client.Resources;

/**
 * Protocol resource
 * @author nina
 *
 * @param <Q>
 */
public class ProtocolDBResource<Q extends IQueryRetrieval<DBProtocol>> extends XmetdbQueryResource<Q,DBProtocol> {
	public enum SearchMode {
		xmet_number,
		xmet_exp_ms,
		xmet_exp_hep,
		xmet_exp_enz,
		xmet_enzyme,
		xmet_allele,
		xmet_reference,
		modifiedSince
	}
	
	protected boolean singleItem = false;
	protected boolean version = false;
	protected Object structure;
	protected String visibleQuery = "Search results";

	public ProtocolDBResource() {
		super();
		setHtmlbyTemplate(true);
		
	}
	
	@Override
	public String getTemplateName() {
		return singleItem?"observation_body.ftl":"protocols_body.ftl";
	}
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		getVariants().clear();
		customizeVariants(new MediaType[] {
				MediaType.TEXT_HTML,
				MediaType.APPLICATION_JSON,
				MediaType.TEXT_URI_LIST,
				MediaType.TEXT_CSV,
				MediaType.APPLICATION_RDF_XML,
				MediaType.APPLICATION_RDF_TURTLE,
				MediaType.TEXT_RDF_N3,
				ChemicalMediaType.CHEMICAL_MDLSDF,
				MediaType.APPLICATION_JAVA_OBJECT,
				MediaType.APPLICATION_XML,
				GPML_WIKIPATHWAYS
		});		
	}

	@Override
	public RepresentationConvertor createConvertor(Variant variant)
			throws AmbitException, ResourceException {
		String filenamePrefix = getRequest().getResourceRef().getPath();
		if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
				return new StringConvertor(	
						new ProtocolQueryURIReporter(getRequest())
						,MediaType.TEXT_URI_LIST,filenamePrefix);
				
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
			String queryService = ((TaskApplication) getApplication())
									.getProperty(Resources.Config.xmet_ambit_service.name());
			ProtocolJSONReporter r = new ProtocolJSONReporter(getRequest(),queryService);
			return new OutputWriterConvertor(r,MediaType.APPLICATION_JSON,filenamePrefix);
		} else if (variant.getMediaType().equals(MediaType.TEXT_CSV)) {
			String queryService = ((TaskApplication) getApplication())
									.getProperty(Resources.Config.xmet_ambit_service.name());
			ProtocolCSVReporter r = new ProtocolCSVReporter(getRequest(),variant.getMediaType(),queryService);
			return new OutputWriterConvertor(r,MediaType.TEXT_CSV,filenamePrefix);
			
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_XML)||variant.getMediaType().equals(GPML_WIKIPATHWAYS)) {
			String queryService = ((TaskApplication) getApplication())
									.getProperty(Resources.Config.xmet_ambit_service.name());
			ProtocolGPMLReporter r = new ProtocolGPMLReporter(getRequest(),variant.getMediaType(),queryService);
			return new OutputWriterConvertor(r,MediaType.APPLICATION_XML,filenamePrefix);
			
		} else if (variant.getMediaType().equals(ChemicalMediaType.CHEMICAL_MDLSDF)) {
			String queryService = ((TaskApplication) getApplication())
									.getProperty(Resources.Config.xmet_ambit_service.name());
			ProtocolSDFReporter r = new ProtocolSDFReporter(getRequest(),variant.getMediaType(),queryService);
			return new OutputWriterConvertor(r,ChemicalMediaType.CHEMICAL_MDLSDF,filenamePrefix);					
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_RDF_XML) ||
					variant.getMediaType().equals(MediaType.APPLICATION_RDF_TURTLE) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_N3) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_NTRIPLES)
					
					) {
				return new RDFJenaConvertor<DBProtocol, IQueryRetrieval<DBProtocol>>(
						new ProtocolRDFReporter<IQueryRetrieval<DBProtocol>>(
								getRequest(),variant.getMediaType(),getDocumentation())
						,variant.getMediaType(),filenamePrefix) {
					@Override
					protected String getDefaultNameSpace() {
						return TOXBANK.URI;
					}					
				};
		} else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
			return new OutputWriterConvertor(createHTMLReporter(headless),MediaType.TEXT_HTML);
		} else {
				String queryService = ((TaskApplication) getApplication())
				.getProperty(Resources.Config.xmet_ambit_service.name());
				ProtocolJSONReporter r = new ProtocolJSONReporter(getRequest(),queryService);
				return new OutputWriterConvertor(r,MediaType.APPLICATION_JSON,filenamePrefix);
		}
	}
	
	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException {
		return null;
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) {
			htmlBeauty = new XmetdbHTMLBeauty(Resources.protocol);
		}
		return htmlBeauty;
	}
	

	protected Q getProtocolQuery(Object key,int userID,Form form) throws ResourceException {
		
		if (key==null) {
			if (form==null) {
				ReadProtocol dbQuery = new ReadProtocol();
				visibleQuery = "All observations";
				if (userID>0) {
					dbQuery.setFieldname(new DBUser(userID));
				} else dbQuery.setShowUnpublished(false);
				return (Q)dbQuery;
			} else {
				QueryProtocol dbQuery = new QueryProtocol();
				dbQuery.setFieldname(form);
				visibleQuery = dbQuery.toString();
				return (Q)dbQuery;				
			}
		} else {
			visibleQuery = Reference.decode(key.toString());
			singleItem = true;
			ReadProtocol dbQuery = new ReadProtocol(Reference.decode(key.toString()));
			dbQuery.setShowUnpublished(true);
			if (userID>0) dbQuery.setFieldname(new DBUser(userID));
			return (Q)dbQuery;
		}
	}
	

	@Override
	protected void setPaging(Form form, IQueryObject queryObject) {
		String max = form.getFirstValue(max_hits);
		String page = form.getFirstValue(PageParams.params.page.toString());
		String pageSize = form.getFirstValue(PageParams.params.pagesize.toString());
		if (max != null)
		try {
			queryObject.setPage(0);
			queryObject.setPageSize(Long.parseLong(form.getFirstValue(max_hits).toString()));
			return;
		} catch (Exception x) {
			
		}
		try {
			queryObject.setPage(Integer.parseInt(page));
		} catch (Exception x) {
			queryObject.setPageSize(0);
		}
		try {
			queryObject.setPageSize(Long.parseLong(pageSize));
		} catch (Exception x) {
			queryObject.setPageSize(1000);
		}			
	}
	
	@Override
	protected Q createQuery(Context context, Request request, Response response)
			throws ResourceException {
		
		Form form = request.getResourceRef().getQueryAsForm();
		/**
		 * Retrieve observation by structure
		 */
		structure = null;
		try {
			structure = form.getFirstValue("structure").toString();
			if ((structure!=null) && structure.toString().startsWith("http")) {
				IQueryRetrieval<DBProtocol> query = new ReadProtocolByStructure();
				Structure record = new Structure();
				record.setResourceIdentifier(new URL(structure.toString()));
				Object[] ids = record.parseURI(new Reference(getQueryService()));
				record.setIdchemical((Integer)ids[0]);
				record.setIdstructure((Integer)ids[1]);
				((ReadProtocolByStructure)query).setFieldname(record);
				singleItem = false;				
				return (Q)query;
			}			
		} catch (Exception x) {
			structure = null;
		}		
		/**
		 * Other types of search
		 */
		StringCondition c = StringCondition.getInstance(StringCondition.C_REGEXP);
		String param = getParams().getFirstValue(QueryResource.condition.toString());
		try {
			if (param != null)	{
				if ("startswith".equals(param.toLowerCase()))
					c= StringCondition.getInstance(StringCondition.C_STARTS_WITH);
				else
					c = StringCondition.getInstance(param);
			}
		} catch (Exception x) {}			
		Object key = request.getAttributes().get(FileResource.resourceKey);
		int userID = -1;
		try {
			Object userKey = request.getAttributes().get(UserDBResource.resourceKey);
			if (userKey!=null)
				userID = ReadUser.parseIdentifier(userKey.toString());
		} catch (Exception x) {}
		
		int queries = 0;
		DBProtocol query = new DBProtocol();
		for (SearchMode option: SearchMode.values()) {
			
			String search = form.getFirstValue(option.name());
			if ((search==null) || "".equals(search.trim())) continue;
			else search = search.trim();
			  
			switch (option) {
			case xmet_number: {
				singleItem = true;
				query.setIdentifier(search.toString());
				return (Q) new ReadProtocol(search);
			}
			case xmet_exp_enz: {
				if ("on".equals(search)) queries++;
				break;
			}
			case xmet_exp_hep: {
				if ("on".equals(search)) queries++;
				break;
			}
			case xmet_exp_ms: {
				if ("on".equals(search)) queries++;
				break;
			}			
			default: {
				queries ++;
			}
			}	
		}
		return getProtocolQuery(key, userID, queries>0?form:null);
	} 

	@Override
	protected QueryURIReporter<DBProtocol, Q> getURUReporter(
			Request baseReference) throws ResourceException {
		return new ProtocolQueryURIReporter(getRequest());
	}

	
	@Override
	protected boolean isAllowedMediaType(MediaType mediaType)
			throws ResourceException {
		return MediaType.MULTIPART_FORM_DATA.equals(mediaType) || MediaType.APPLICATION_WWW_FORM.equals(mediaType);
	}

	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			Form form, DBProtocol item) throws ResourceException {
		DBUser user = null;
		if ((getRequest().getClientInfo().getUser()==null) || (getRequest().getClientInfo().getUser().getIdentifier()==null)) {
			user = null;
			//this is protected by a filter, should not come here
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED.getCode(),
						"Not allowed",
						"Only logged in users may update observations",
						Status.CLIENT_ERROR_UNAUTHORIZED.getUri());
		} else {
			user = new DBUser();
			user.setUserName(getRequest().getClientInfo().getUser().getIdentifier());
		}	
		
		if (Method.DELETE.equals(method))
			return createCallable(method,(List<FileItem>) null, item);
		if (Method.POST.equals(method)) {
			Connection conn = null;
			try {
				DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
				conn = dbc.getConnection();
				return new CallableProtocolCopy(method,form,getRootRef().toString(),user,conn,getToken());
			} catch (Exception x) {
				try { conn.close(); } catch (Exception xx) {}
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
			}			
		} else throw new MethodNotAllowedException(getRequest().getResourceRef(),method);

	}
	

	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			List<FileItem> input, DBProtocol item) throws ResourceException {
		/*
		if ((getRequest().getClientInfo().getUser()==null) ||
				getRequest().getClientInfo().getUser().getIdentifier()==null)
				throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
			
		DBUser user = new DBUser();
		user.setUserName(getRequest().getClientInfo().getUser().getIdentifier());
		*/
		String xmetdbcookie = getRequest().getCookies().getFirstValue("xmetdb");
		DBUser user = null;
		
		if ((getRequest().getClientInfo().getUser()==null) || (getRequest().getClientInfo().getUser().getIdentifier()==null)) {
			user = null;
			
			//POST is protected by a filter, should not come here
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED.getCode(),
						"Upload not allowed",
						"Only logged in users may upload new observations",
						Status.CLIENT_ERROR_UNAUTHORIZED.getUri());
						
			//user = new DBUser();
			//user.setUserName("guest");
		} else {
			user = new DBUser();
			user.setUserName(getRequest().getClientInfo().getUser().getIdentifier());
		}	
		Connection conn = null;
		try {
			ProtocolQueryURIReporter r = new ProtocolQueryURIReporter(getRequest(),"");
			class TDBConnection extends DBConnection {
				public TDBConnection(Context context,String configFile) {
					super(context,configFile);
				}
				public String getDir() {
					loadProperties();
					return getAttachmentDir();
				}
			};
			TDBConnection dbc = new TDBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();

			String dir = dbc.getDir();
			if ("".equals(dir)) dir = null;
			
			String ambituser = ((TaskApplication)getApplication()).getProperty(Resources.AMBIT_LOCAL_USER);
			String ambitpass = ((TaskApplication)getApplication()).getProperty(Resources.AMBIT_LOCAL_PWD);
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(ambituser,ambitpass);
			
			return new CallableProtocolUpload(
						method,item,user,input,conn,r,xmetdbcookie,getRequest().getRootRef().toString(),
						dir==null?null:new File(dir),
						getQueryService(),creds);		
			
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}

	}
	
	protected String getQueryService() {
		return ((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name());
	}
	
	@Override
	protected Q createUpdateQuery(Method method, Context context,
			Request request, Response response) throws ResourceException {
		Object key = request.getAttributes().get(FileResource.resourceKey);
		if (Method.POST.equals(method)) {
			if (key==null) return null;//post allowed only on /protocol level, not on /protocol/id
		} else if (Method.DELETE.equals(method)) {
			if (key!=null) return super.createUpdateQuery(method, context, request, response);
		} else if (Method.PUT.equals(method)) {
			if (key!=null) {
				return (Q)new ReadProtocol(Reference.decode(key.toString()));
			}
		}
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);		
	}
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF(storage,getHTMLBeauty());
	}
	
	
	protected TaskCreator getTaskCreator(Form form, final Method method, boolean async, final Reference reference) throws Exception {
		if (Method.DELETE.equals(method) || Method.POST.equals(method)) {
			//POST with web form copies the observation into a new one
			TaskCreator taskCreator = super.getTaskCreator(form, method, async, reference);
			taskCreator.getProcessors().setAbortOnError(true);
			return taskCreator;
		} else
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Not multipart web form!");
	}
	
	@Override
	protected TaskCreator getTaskCreator(Representation entity,
			Variant variant, Method method, boolean async) throws Exception {
		TaskCreator taskCreator = super.getTaskCreator(entity,variant,method,async);
		taskCreator.getProcessors().setAbortOnError(true);
		return taskCreator;
	}
	
	@Override
	protected Map<String, Object> getMap(Variant variant)
			throws ResourceException {
		 Map<String, Object> map = super.getMap(variant);
		 map.put("xmet_breadcrumb", visibleQuery);
		 return map;
	}
}
