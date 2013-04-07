package org.xmetdb.xmet.aa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import net.idea.modbcum.p.QueryExecutor;
import net.idea.restnet.db.DBConnection;
import net.toxbank.client.policy.AccessRights;
import net.toxbank.client.policy.PolicyRule;

import org.restlet.Request;
import org.restlet.Response;
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
 * 
 * Lets the user to read the protocol, if he is an owner.
 * Lets the user to update the protocol, if he is an owner and the protocol is not published.
 * Otherwise, we resort to the OpenSSO policy
 */
public class ProtocolAuthorizer  extends RoleAuthorizer {

	protected int maxDepth = Integer.MAX_VALUE;
	public int getMaxDepth() {
		return maxDepth;
	}
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	protected ReadProtocolAccessLocal query;
	protected QueryExecutor<ReadProtocolAccessLocal> executor;
	
	@Override
	public boolean authorize(Request request, Response response) {
		AccessRights policy = null;
		
		//first check if local access is allowed , e.g. same name
		Template template1 = new Template(String.format("%s%s/{%s}",request.getRootRef(),Resources.protocol,FileResource.resourceKey));
		Template template2 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.authors));
		Template template3 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.versions));
		Template template4 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.previous));
		//Template template5 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.curator));
		Template template6 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.protocol,FileResource.resourceKey,Resources.attachment));
		Map<String, Object> vars = new HashMap<String, Object>();
		Reference ref = request.getResourceRef().clone();
		ref.setQuery(null);
		template1.parse(ref.toString(),vars);
		template2.parse(ref.toString(),vars);
		template3.parse(ref.toString(),vars);
		template4.parse(ref.toString(),vars);
		//template5.parse(ref.toString(),vars);
		template6.parse(ref.toString(),vars);

		/**
		 * Try if there is a protocol identifier, or this is a top level query, in the later case, try the role AA
		 */
		if (vars.get(FileResource.resourceKey)!=null) { 
			
			String uri = String.format("%s%s/%s",request.getRootRef(),Resources.protocol,vars.get(FileResource.resourceKey));
			try {
				String identifier = vars.get(FileResource.resourceKey).toString();

				DBProtocol protocol = new DBProtocol(identifier);
				String username = request.getClientInfo().getUser().getIdentifier();

				policy = verify(protocol,username);
				/**
				 * The policy will let the user read the protocol, if he is an owner
				 * The policy will allow the user to update the protocol, if he is an owner and the protocol is not published
				 * Otherwise, we resort to OpenSSO policy
				 */
				if (policy !=null)
					for (PolicyRule rule : policy.getRules()) {
						Boolean allowed = rule.allows(request.getMethod().toString());
						if ((allowed!=null) && allowed) return true;
					}
				
			} catch (ResourceException x) {
				return super.authorize(request,response);
			}
		} else {
			//setPrefix(null);
		}
		return super.authorize(request,response);
	}

	public AccessRights verify(DBProtocol protocol, String username) throws ResourceException {
		//TODO make use of same connection for performance reasons
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
