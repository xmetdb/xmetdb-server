package org.xmetdb.rest.aa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import net.idea.modbcum.p.QueryExecutor;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.aalocal.DBRole;
import net.toxbank.client.policy.AccessRights;
import net.toxbank.client.policy.PolicyRule;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Template;
import org.restlet.security.RoleAuthorizer;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocolAccessLocal;
import org.xmetdb.xmet.client.Resources;


/**
 * All GETs are public
 * Adding new entries (/protocol POST) is allowed for anybody
 * Updating entries (/protocol/{id} PUT / POST / DELETE ) are allowed for admin role and for owners
 */
public class ProtocolAuthorizer  extends RoleAuthorizer {

	protected boolean skip = true;
	public ProtocolAuthorizer(boolean skip, DBRole... roles) {
		super();
		this.skip = skip;
		for (DBRole role : roles)
			getAuthorizedRoles().add(role);
	}

	protected ReadProtocolAccessLocal query;
	protected QueryExecutor<ReadProtocolAccessLocal> executor;
	
	@Override
	public boolean authorize(Request request, Response response) {
		//reading is public
		if (Method.GET.equals(request.getMethod()))	return true;
		if (skip) return true;
		if (Protocol.RIAP.equals(request.getProtocol())) return true;
		//writing denied for not logged in
		if ((request.getClientInfo() == null)
				|| (request.getClientInfo().getUser() == null)
				|| (request.getClientInfo().getUser().getIdentifier() == null))
			return false;
		//get the protocol id
		Object protocolid = getProtocol(request);
		if (protocolid==null) return true; //POST at the top level open to all users
		//check if admin
		if (super.authorize(request, response)) return true;
		//now check if owner
		AccessRights policy = null;
		try {
			String identifier = protocolid.toString();
			DBProtocol protocol = new DBProtocol(identifier);
			policy = verify(protocol,request.getClientInfo().getUser().getIdentifier());
			if (policy !=null)
				for (PolicyRule rule : policy.getRules()) {
					Boolean allowed = rule.allows(request.getMethod().toString());
					if ((allowed!=null) && allowed) return true;
				}
			return false;
		} catch (ResourceException x) {
			Context.getCurrentLogger().severe(x.getMessage());
			return false;
		}
	}

	protected Object getProtocol(Request request) {
		
		//first check if local access is allowed , e.g. same name
		Template template1 = new Template(String.format("%s%s/{%s}",request.getRootRef(),Resources.protocol,FileResource.resourceKey));
		Template template2 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.authors));
		Template template3 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.versions));
		Template template4 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.previous));
		Template template5 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.som));
		Template template6 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.attachment));
		Map<String, Object> vars = new HashMap<String, Object>();
		Reference ref = request.getResourceRef().clone();
		ref.setQuery(null);
		template1.parse(ref.toString(),vars);
		template2.parse(ref.toString(),vars);
		template3.parse(ref.toString(),vars);
		template4.parse(ref.toString(),vars);
		template5.parse(ref.toString(),vars);
		template6.parse(ref.toString(),vars);
		return vars.get(FileResource.resourceKey);
	}
	public AccessRights verify(DBProtocol protocol, String username) throws ResourceException {
		Connection c = null;
		ResultSet rs = null;
		try {
			if (query==null) query = new ReadProtocolAccessLocal();
			query.setFieldname(protocol);
			query.setValue(username);
			DBConnection dbc = new DBConnection(getApplication().getContext(),"conf/xmetdb.pref");
			c = dbc.getConnection();
			if (executor==null)  executor = new QueryExecutor<ReadProtocolAccessLocal>();
			executor.setConnection(c);
			rs = executor.process(query);
			AccessRights policy = null;
			while (rs.next()) {
				policy = query.getObject(rs);
				break;
			}
			return policy;
		} catch (ResourceException x) {
			throw x;			
		} catch (Exception x) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x.getMessage(),x);			

		} finally {
			try {executor.close();} catch (Exception x) {};
			try {if (rs!=null) rs.close();} catch (Exception x) {};
			try {if (c!=null) c.close();} catch (Exception x) {};
		}
		
	}
	
	
}
