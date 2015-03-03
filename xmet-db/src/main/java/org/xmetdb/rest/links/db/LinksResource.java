package org.xmetdb.rest.links.db;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.XmetdbQueryResource;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.resource.db.LinksJSONReporter;

/**
 * 
 * A resource wrapper for {@link Enzyme} list
 * 
 * @author nina
 * @Path("/catalog")
 */

public class LinksResource
		extends
		XmetdbQueryResource<IQueryRetrieval<ExternalIdentifier>, ExternalIdentifier> {

	public static String resourceKey = "idtemplate";
	public static String resourceID = String.format("/{%s}", resourceKey);

	public LinksResource() {
		super();
		setHtmlbyTemplate(true);
	}

	@Override
	public String getTemplateName() {
		return "s_external_ids.ftl";
	}

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		customizeVariants(new MediaType[] { MediaType.TEXT_HTML,
				MediaType.APPLICATION_JSON, MediaType.APPLICATION_JAVA_OBJECT });

	}

	@Override
	public IProcessor<IQueryRetrieval<ExternalIdentifier>, Representation> createConvertor(
			Variant variant)
			throws net.idea.modbcum.i.exceptions.AmbitException,
			ResourceException {
		String filenamePrefix = getRequest().getResourceRef().getPath();
		/*
		 * if (variant.getMediaType().equals(MediaType.APPLICATION_RDF_XML) ||
		 * variant.getMediaType().equals(MediaType.APPLICATION_RDF_TURTLE) ||
		 * variant.getMediaType().equals(MediaType.TEXT_RDF_N3) ||
		 * variant.getMediaType().equals(MediaType.TEXT_RDF_NTRIPLES) ) { return
		 * new RDFJenaConvertor<Property, IQueryRetrieval<Property>>( new
		 * TemplateRDFReporter<IQueryRetrieval<Property>>(
		 * getRequest(),getDocumentation(),variant.getMediaType(),isRecursive())
		 * ,variant.getMediaType(),filenamePrefix);
		 * 
		 * 
		 * } else
		 */

		// if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
		LinksJSONReporter r = new LinksJSONReporter();
		return new StringConvertor(r, MediaType.APPLICATION_JSON,
				filenamePrefix);
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return null;
	}

	@Override
	protected IQueryRetrieval<ExternalIdentifier> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		Object protocol = request.getAttributes().get(FileResource.resourceKey);
		if (protocol != null) {
			DBProtocol obs = new DBProtocol();
			obs.setIdentifier(protocol.toString());
			return new ReadObservationIdentifiers(obs);
		} else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
	}

	
}
