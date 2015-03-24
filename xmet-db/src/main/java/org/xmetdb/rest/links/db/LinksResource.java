package org.xmetdb.rest.links.db;

import java.util.Map;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.XmetdbQueryResource;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.links.ExternalIdentifier.ExternalIdentifierFields;
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

	public static final String resourceKey = "idtemplate";
	public static final String resourceID = String.format("/{%s}", resourceKey);
	public static final String typeKey = "type";
	public static final String valueKey = "value";
	protected String xmetid = null;

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
		LinksJSONReporter r = new LinksJSONReporter(getRequest().getRootRef()
				.toString());
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
			xmetid = protocol.toString();
			obs.setIdentifier(protocol.toString());
			return new ReadObservationIdentifiers(obs);
		} else
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
	}

	@Override
	protected Representation put(Representation entity, Variant variant)
			throws ResourceException {
		try {
			Object protocol = getRequest().getAttributes().get(
					FileResource.resourceKey);
			if (protocol == null)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			xmetid = protocol.toString();
			DBProtocol p = new DBProtocol();
			p.setIdentifier(protocol.toString());

			Form form = new Form(entity);
			String value = form.getFirstValue("value");
			String id = form.getFirstValue("id");
			String columnName = form.getFirstValue("columnName");
			ExternalIdentifierFields field = ExternalIdentifierFields
					.valueOf(columnName.toLowerCase());
			ExternalIdentifier eid = new ExternalIdentifier();
			// enzyme.setId(Integer.parseInt(id));
			switch (field) {
			case type: {
				eid.setSystemDesignator(value);
				break;
			}
			case value: {
				eid.setSystemIdentifier(value);
				break;
			}

			}
			UpdateObservationIdentifier q = new UpdateObservationIdentifier(p,
					eid);
			execUpdate(eid, q);
			eid.setXmetid(xmetid);
			return new StringRepresentation(eid.toURI(), MediaType.TEXT_PLAIN);
		} catch (ResourceException x) {
			throw x;
		} catch (IllegalArgumentException x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, x);
		} catch (NullPointerException x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, x);
		} catch (Exception x) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, x);
		}
	}

	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		if (!MediaType.APPLICATION_WWW_FORM.equals(entity.getMediaType()))
			throw new ResourceException(
					Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
		try {
			Object protocol = getRequest().getAttributes().get(
					FileResource.resourceKey);
			if (protocol == null)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			xmetid = protocol.toString();
			DBProtocol p = new DBProtocol();
			p.setIdentifier(protocol.toString());

			Form form = new Form(entity);
			ExternalIdentifier eid = new ExternalIdentifier();
			for (ExternalIdentifierFields field : ExternalIdentifierFields
					.values()) {
				String value = form.getFirstValue(field.name());
				switch (field) {
				case type: {
					if ((value == null) || ("".equals(value)))
						throw new ResourceException(
								Status.CLIENT_ERROR_BAD_REQUEST);
					eid.setSystemDesignator(value);
					break;
				}
				case value: {
					if ((value == null) || ("".equals(value)))
						throw new ResourceException(
								Status.CLIENT_ERROR_BAD_REQUEST);
					eid.setSystemIdentifier(value);
					break;
				}
				/*
				 * case xmetid: { if ((value == null) || ("".equals(value)))
				 * throw new ResourceException(
				 * Status.CLIENT_ERROR_BAD_REQUEST); eid.setXmetid(value); if
				 * (!value.equals(xmetid)) throw new ResourceException(
				 * Status.CLIENT_ERROR_BAD_REQUEST); break; }
				 */
				}
			}
			UpdateObservationIdentifier q = new UpdateObservationIdentifier(p,
					eid);
			execUpdate(eid, q);
			eid.setXmetid(xmetid);
			return new StringRepresentation(eid.toURI(), MediaType.TEXT_PLAIN);
		} catch (Exception e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
					e.getMessage(), e);
		} finally {
		}
	}

	@Override
	protected Representation delete(Variant variant) throws ResourceException {
		Object protocol = getRequest().getAttributes().get(
				FileResource.resourceKey);
		Object type = getRequest().getAttributes().get(LinksResource.typeKey);
		Object value = getRequest().getAttributes().get(LinksResource.valueKey);
		if (protocol == null || value == null || type == null)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		xmetid = protocol.toString();
		try {
			DBProtocol p = new DBProtocol();
			p.setIdentifier(protocol.toString());
			ExternalIdentifier eid = new ExternalIdentifier(type.toString()
					.trim(), value.toString().trim());
			DeleteObservationIdentifier query = new DeleteObservationIdentifier(
					p, eid);
			execUpdate(eid, query);
			return new StringRepresentation("ok", MediaType.TEXT_PLAIN);
		} catch (Exception x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
					x.getMessage(), x);
		}
		/*
		 * if (key != null) try { Enzyme enzyme = new Enzyme();
		 * enzyme.setId(Integer.parseInt(key.toString())); DeleteEnzyme query =
		 * new DeleteEnzyme(enzyme); execUpdate(enzyme, query); return new
		 * StringRepresentation("ok", MediaType.TEXT_PLAIN); } catch (Exception
		 * e) { throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
		 * e.getMessage(), e); } finally { }
		 */

	}

	@Override
	protected Map<String, Object> getMap(Variant variant)
			throws ResourceException {
		Map<String, Object> map = super.getMap(variant);
		if (xmetid != null)
			map.put("xmetid", xmetid);
		return map;
	}
}
