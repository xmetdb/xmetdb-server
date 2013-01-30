package  org.xmetdb.rest.endpoints;

import java.net.URI;
import java.sql.Connection;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.p.UpdateExecutor;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.db.convertors.OutputWriterConvertor;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.XmetdbQueryResource;
import org.xmetdb.rest.endpoints.Enzyme.EnzymeFields;
import org.xmetdb.rest.endpoints.db.CreateEndpoint;
import org.xmetdb.rest.endpoints.db.ReadEnzyme;
import org.xmetdb.rest.endpoints.db.ReadEnzymeByObservation;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

/**
 * 
 * A resource wrapper fot {@link QueryOntology}
 * @author nina
 *
 */
public class EnzymesResource extends XmetdbQueryResource<IQueryRetrieval<Enzyme>, Enzyme> {
	
	public static String resource = "/catalog";
	public static String resourceParent = "subject";
	public static String resourceKey = "object";
	public static String resourceID = String.format("/{%s}/{%s}",resourceParent,resourceKey);
	public static String resourceTree = String.format("/{%s}/{%s}/view/{tree}",resourceParent,resourceKey);
	protected boolean isRecursive = false;
	
	public EnzymesResource() {
		super();
		setHtmlbyTemplate(true);
	}
	@Override
	public String getTemplateName() {
		return "s_endpoints_body.ftl";
	}
	public boolean isRecursive() {
		return isRecursive;
	}
	public void setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		customizeVariants(new MediaType[] {
				MediaType.TEXT_HTML,
				MediaType.TEXT_URI_LIST,
				MediaType.APPLICATION_JSON,
				MediaType.APPLICATION_RDF_XML,
				MediaType.APPLICATION_RDF_TURTLE,
				MediaType.TEXT_RDF_N3,
				MediaType.TEXT_RDF_NTRIPLES,				
				MediaType.APPLICATION_JAVA_OBJECT
				});

				
	}

	@Override
	public IProcessor<IQueryRetrieval<Enzyme>, Representation> createConvertor(
			Variant variant)
			throws net.idea.modbcum.i.exceptions.AmbitException,
			ResourceException {
		String filenamePrefix = getRequest().getResourceRef().getPath();
		/*
		if (variant.getMediaType().equals(MediaType.APPLICATION_RDF_XML) ||
					variant.getMediaType().equals(MediaType.APPLICATION_RDF_TURTLE) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_N3) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_NTRIPLES) ||
					variant.getMediaType().equals(MediaType.APPLICATION_JSON)
					) {
				return new RDFJenaConvertor<Property, IQueryRetrieval<Property>>(
						new TemplateRDFReporter<IQueryRetrieval<Property>>(
								getRequest(),getDocumentation(),variant.getMediaType(),isRecursive())
						,variant.getMediaType(),filenamePrefix);		
				
				
		} else 
		*/
		if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
				EnzymeURIReporter r = new EnzymeURIReporter(getRequest(),null);
				r.setDelimiter("\n");
				return new StringConvertor(	r,MediaType.TEXT_URI_LIST,filenamePrefix);
				
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
				EnzymeJSONReporter r = new EnzymeJSONReporter();
				return new StringConvertor(	r,MediaType.APPLICATION_JSON,filenamePrefix);
				
		} else 			
			return new OutputWriterConvertor(new EnzymeJSONReporter(),MediaType.TEXT_HTML);
	}


	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty = new XmetdbHTMLBeauty(Resources.endpoint);
		return htmlBeauty;
	}
	@Override
	protected IQueryRetrieval<Enzyme> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		/*
		String term = null;
		try {
			Form form = getResourceRef(getRequest()).getQueryAsForm();
			term = form.getFirstValue("term");
			if (term!=null) {
				QueryOntology q = new QueryOntology();
				q.setIncludeParent(RetrieveMode.all);
				q.setValue(new Enzyme(term,term));
				return q;
			}
		} catch (Exception x) {
			
		}			
		*/
		Object protocol = request.getAttributes().get(FileResource.resourceKey);
		if (protocol != null) {
			DBProtocol obs = new DBProtocol();
			obs.setIdentifier(protocol.toString());
			return new ReadEnzymeByObservation(obs);
		}
	
		Object key = request.getAttributes().get(resourceKey);
		if (key != null) {
			Enzyme enzyme = new Enzyme();
			enzyme.setCode(Reference.decode(key.toString()));
			return new ReadEnzyme(enzyme);
		} else {
			key =  request.getAttributes().get(resourceParent);
			return new ReadEnzyme();
		}

	}
	
	@Override
	protected Representation put(Representation entity, Variant variant)
			throws ResourceException {
		System.out.println(new Form(entity));
		return null;
	}
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		if (!MediaType.APPLICATION_WWW_FORM.equals(entity.getMediaType()))
			throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
		Connection conn = null;
		UpdateExecutor x = null;
		try {
			Form form = new Form(entity);
			Enzyme enzyme = new Enzyme();
			for (EnzymeFields field : EnzymeFields.values()) {
				String value = form.getFirstValue(field.name());
				switch (field) {
				case code: {
					if ((value==null)||("".equals(value))) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
					enzyme.setCode(value);break;
					}
				case name: {
					if ((value==null)||("".equals(value))) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
					enzyme.setName(value);
					break;
					}
				case uri: {
					if (value==null) enzyme.setUri(null);
					else enzyme.setUri(new URI(value));
					break;
					}
				case alleles: {
					if (value==null) 
						enzyme.setAlleles(null);
					else
						enzyme.setAlleles(value.split("\n")); 
					break;
				}
				}
			}
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			CreateEndpoint q = new CreateEndpoint(enzyme);
			x = new UpdateExecutor();
			x.setConnection(conn);
			x.process(q);
			return new StringRepresentation(enzyme.getCode());
		} catch (Exception e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,e.getMessage(),e);
		} finally {
			try { if (conn != null) conn.close(); } catch (Exception xx) {}
			try { if (x !=null) x.close(); } catch (Exception xx) {}
		}
	}
	
	/*

	protected Property create ObjectFromHeaders(Form requestHeaders,
			Representation entity) throws ResourceException {
		Object key = getRequest().getAttributes().get(resourceKey);
		if (key == null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		Object parent = getRequest().getAttributes().get(resourceParent);
		if (parent == null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		Dictionary dictionary = new Dictionary(parent.toString(),key.toString());
		return dictionary;
		
	}
	
	protected AbstractUpdate createDeleteObject(Property entry) throws ResourceException {
		if (entry instanceof Dictionary) {
			DeleteDictionary delete = new DeleteDictionary((Dictionary)entry);
			return delete;
		} else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
				String.format("Expected %s instead of %s",Dictionary.class.getName(),entry.getClass().getName()));
	};
	*/
	@Override
	protected QueryURIReporter<Enzyme, IQueryRetrieval<Enzyme>> getURUReporter(
			Request baseReference) throws ResourceException {
		return new EnzymeURIReporter(getRequest(),getDocumentation());
	}
}
