package org.xmetdb.rest.xmet.curator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.opentox.cli.OTClient;
import net.idea.opentox.cli.structure.Substance;
import net.idea.opentox.cli.structure.SubstanceClient;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.user.DBUser;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.client.DefaultHttpClient;
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
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.UpdateObservationEntry;
import org.xmetdb.rest.protocol.resource.db.ProtocolDBResource;
import org.xmetdb.xmet.client.Resources;

public class UpdateObservationResource<Q extends IQueryRetrieval<DBProtocol>> extends ProtocolDBResource<Q> {
	UsernamePasswordCredentials creds = null;
	public UpdateObservationResource() {
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
		return null;
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
	
	protected Representation updateCuratedFlag(DBProtocol protocol,UpdateObservationEntry query, String value) {
		try {protocol.setSearchable(Boolean.parseBoolean(value));} catch (Exception x) {x.printStackTrace();}
		execUpdate(protocol, query);
		return new StringRepresentation(protocol.isSearchable()?"Curated":"Not curated",MediaType.TEXT_PLAIN);

	}
	protected UpdateObservationEntry getUpdateQuery()  {
		UpdateObservationEntry query = new UpdateObservationEntry();
		query.setSetCuratedFlag(false);
		return query;
	}
	@Override
	protected Representation put(Representation entity, Variant variant)
			throws ResourceException {
		if (MediaType.APPLICATION_WWW_FORM.equals(entity.getMediaType())) {
			Object key = getRequest().getAttributes().get(FileResource.resourceKey);
			if (key!=null) {
				DBProtocol protocol = new DBProtocol();
				protocol.setIdentifier(key.toString());
				UpdateObservationEntry query = getUpdateQuery();
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
						return updateCuratedFlag(protocol,query,value);
					case xmet_substrate_atoms: 
						try {
							String compoundURI = form.getFirstValue("compound_uri");
							if (compoundURI==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
							String url = updateSOM(compoundURI, key.toString(), value, queryService);
							System.out.println(url);
							return new StringRepresentation(value,MediaType.TEXT_PLAIN);
						} catch (ResourceException x) {
							throw x;
						} catch (Exception x) {
							throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
						}
					case xmet_product_atoms:
						try {
							String compoundURI = form.getFirstValue("compound_uri");
							if (compoundURI==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
							String url = updateSOM(compoundURI, key.toString(), value, queryService);
							return new StringRepresentation(value,MediaType.TEXT_PLAIN);
						} catch (ResourceException x) {
							throw x;
						} catch (Exception x) {
							throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
						}
					default:
					    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);
					}


				} catch (Exception x) {throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,id);}
			}
		}
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
	}
	
	protected String updateSOM(String compoundURI, String xmetdbid,String som,String queryService)throws Exception {
		String ambituser = ((TaskApplication)getApplication()).getProperty(Resources.AMBIT_LOCAL_USER);
		String ambitpass = ((TaskApplication)getApplication()).getProperty(Resources.AMBIT_LOCAL_PWD);
		final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(ambituser,ambitpass);
		final Reference uri = new Reference(queryService);
		OTClient cli=null;
		//PUT
		try {
			cli = new OTClient() {
				protected HttpClient createHTTPClient() {
					DefaultHttpClient  cli = new DefaultHttpClient();
					List<String> authpref = new ArrayList<String>();
					authpref.add(AuthPolicy.BASIC);
					cli.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
					cli.getCredentialsProvider().setCredentials(
					        new AuthScope(uri.getHostDomain(),uri.getHostPort()), 
					        creds);		
					return cli;
				};
			};
			SubstanceClient scli = cli.getSubstanceClient();
			Substance substance = new Substance();
			substance.setResourceIdentifier(new URL(compoundURI));
			RemoteTask task = scli.setSubstancePropertyAsync(new URL(queryService), substance,xmetdbid,som);
			task.waitUntilCompleted(500);
			if (task.getError()!=null) throw task.getError();
			return task.getResult().toExternalForm();
		} catch (Exception x) {
			//do smth
			throw x;
		} finally {
			try {cli.close();} catch (Exception x) {}
		}
		
	}
	@Override
	protected Map<String, Object> getMap(Variant variant)
			throws ResourceException {
		 Map<String, Object> map = super.getMap(variant);
		 map.put("xmet_updatemode", "update");
		 return map;
	}
	
}