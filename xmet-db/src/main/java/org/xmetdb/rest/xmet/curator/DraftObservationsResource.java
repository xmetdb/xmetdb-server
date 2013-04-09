package org.xmetdb.rest.xmet.curator;

import java.net.URL;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.opentox.cli.OTClient;
import net.idea.opentox.cli.structure.Substance;
import net.idea.opentox.cli.structure.SubstanceClient;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.user.DBUser;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.ProtocolFactory;
import org.xmetdb.rest.protocol.ProtocolFactory.ObservationFields;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.UpdateObservationEntry;
import org.xmetdb.rest.protocol.resource.db.ProtocolDBResource;
import org.xmetdb.xmet.client.Resources;

public class DraftObservationsResource<Q extends IQueryRetrieval<DBProtocol>> extends ProtocolDBResource<Q> {
	
	public DraftObservationsResource() {
		super();
		setHtmlbyTemplate(true);
	}
	@Override
	protected Q getProtocolQuery(Object key, int userID, Form form)
			throws ResourceException {
		
		if (key==null) {
			visibleQuery = "Not yet curated observations";
			ReadProtocol dbQuery = new ReadProtocol();
			dbQuery.setOnlyUnpublished(true);
			dbQuery.setShowUnpublished(true);
			dbQuery.setValue(null);
			if (userID>0) {
				dbQuery.setFieldname(new DBUser(userID));
			} 
			return (Q)dbQuery;
		}			
		else {
			singleItem = true;
			ReadProtocol dbQuery =  new ReadProtocol(Reference.decode(key.toString()));
			visibleQuery = Reference.decode(key.toString());
			dbQuery.setShowUnpublished(true);
			if (userID>0) dbQuery.setFieldname(new DBUser(userID));
			return (Q)dbQuery;
		}
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty = new XmetdbHTMLBeauty(Resources.curator,false);
		return htmlBeauty;
	}
	
	@Override
	public String getTemplateName() {
		return singleItem?"observation_curate.ftl":"protocols_body.ftl";
	}
	
	
	protected QueryHTMLReporter createHTMLReporter() throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
	}
	
	
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation post(Representation entity)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation put(Representation entity, Variant variant)
			throws ResourceException {
		if (MediaType.APPLICATION_WWW_FORM.equals(entity.getMediaType())) {
			Object key = getRequest().getAttributes().get(FileResource.resourceKey);
			if (key!=null) {
				DBProtocol protocol = new DBProtocol();
				protocol.setIdentifier(key.toString());
				UpdateObservationEntry query = new UpdateObservationEntry();
				query.setGroup(protocol);
				Form form = new Form(entity);
				String id = form.getFirstValue("id");
				String value = form.getFirstValue("value");
				String queryService = ((TaskApplication) getApplication()).getProperty(Resources.Config.xmet_ambit_service.name());
				try {
					ProtocolFactory.ObservationFields field = ProtocolFactory.ObservationFields.valueOf(id);
					query.setObject(field);
					switch (field) {
					case xmet_reference:
						protocol.setReference(value);
						execUpdate(protocol, query);
						return new StringRepresentation(value.toString(),MediaType.TEXT_PLAIN);
					case xmet_comments:
						protocol.getKeywords().add(value);
						execUpdate(protocol, query);
						return new StringRepresentation(value,MediaType.TEXT_PLAIN);
					case curated: 
						try {protocol.setSearchable(Boolean.parseBoolean(value));} catch (Exception x) {x.printStackTrace();}
						execUpdate(protocol, query);
						return new StringRepresentation(protocol.isSearchable()?"Curated":"Not curated",MediaType.TEXT_PLAIN);
					case xmet_substrate_atoms: 
						try {
							String compoundURI = form.getFirstValue("compound_uri");
							if (compoundURI==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
							String url = updateSOM(compoundURI, key.toString(), value, queryService);
							System.out.println(url);
						} catch (ResourceException x) {
							throw x;
						} catch (Exception x) {
							throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
						}
						break;						
					case xmet_product_atoms:
						try {
							String compoundURI = form.getFirstValue("compound_uri");
							if (compoundURI==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
							String url = updateSOM(compoundURI, key.toString(), value, queryService);
							System.out.println(url);
						} catch (ResourceException x) {
							throw x;
						} catch (Exception x) {
							throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
						}
						break;	
					default:
					    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
					}


				} catch (Exception x) {throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);}
			}
		}
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
	}
	
	protected String updateSOM(String compoundURI, String xmetdbid,String som,String queryService)throws Exception {
		OTClient cli=null;
		//PUT
		try {
			cli = new OTClient();
			SubstanceClient scli = cli.getSubstanceClient();
			Substance substance = new Substance();
			substance.setResourceIdentifier(new URL(compoundURI));
			RemoteTask task = scli.setSubstancePropertyAsync(new URL(queryService), substance,xmetdbid,som);
			task.waitUntilCompleted(500);
			return task.getResult().toExternalForm();
		} catch (Exception x) {
			//do smth
			throw x;
		} finally {
			try {cli.close();} catch (Exception x) {}
		}
		
	}

	
}
