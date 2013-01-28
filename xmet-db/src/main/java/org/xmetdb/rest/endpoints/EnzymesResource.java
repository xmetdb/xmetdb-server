package  org.xmetdb.rest.endpoints;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.db.convertors.OutputWriterConvertor;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.XmetdbQueryResource;
import org.xmetdb.rest.endpoints.db.DictionaryObjectQuery;
import org.xmetdb.rest.endpoints.db.DictionaryQuery;
import org.xmetdb.rest.endpoints.db.QueryOntology;
import org.xmetdb.rest.endpoints.db.QueryOntology.RetrieveMode;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

import ambit2.base.data.Dictionary;

/**
 * 
 * A resource wrapper fot {@link QueryOntology}
 * @author nina
 *
 */
public class EnzymesResource<D extends Dictionary> extends XmetdbQueryResource<IQueryRetrieval<D>, D> {
	
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
	public IProcessor<IQueryRetrieval<D>, Representation> createConvertor(
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
	protected IQueryRetrieval<D> createQuery(Context context, Request request,
			Response response) throws ResourceException {
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
		Object protocol = request.getAttributes().get(FileResource.resourceKey);
		if (protocol != null) {
			QueryOntology q = new QueryOntology();
			q.setIncludeParent(RetrieveMode.protocol);
			q.setQmrf_number(protocol.toString());
			return q;
		}
		try {
			Object view = request.getAttributes().get("tree");
			setRecursive(view==null?false:"tree".equals(view));	
		} catch (Exception x) {
			setRecursive(false);
		}		
		Object key = request.getAttributes().get(resourceKey);
		if (key != null) {
			QueryOntology q = new QueryOntology();
			q.setIncludeParent(RetrieveMode.child);
			q.setValue(key==null?null:new Enzyme(Reference.decode(key.toString().replace("_", "/")),null));
			return q;
		} else {
			key =  request.getAttributes().get(resourceParent);
			DictionaryQuery qd = new DictionaryObjectQuery();
			qd.setCondition(StringCondition.getInstance(StringCondition.C_EQ));
			qd.setValue(key==null?null:Reference.decode(key.toString().replace("_", "/"),null));
			
			return qd;
		}

	}
	
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		// TODO Auto-generated method stub
		return super.post(entity, variant);
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
	protected QueryURIReporter<D, IQueryRetrieval<D>> getURUReporter(
			Request baseReference) throws ResourceException {
		return new EnzymeURIReporter(getRequest(),getDocumentation());
	}
}
