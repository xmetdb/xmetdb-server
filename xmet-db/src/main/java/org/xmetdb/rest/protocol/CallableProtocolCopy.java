package org.xmetdb.rest.protocol;

import java.sql.Connection;

import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.restnet.db.update.CallableDBUpdateTask;
import net.idea.restnet.user.DBUser;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.db.CreateProtocolCopy;
import org.xmetdb.xmet.client.Resources;

public class CallableProtocolCopy extends CallableDBUpdateTask<DBProtocol,Form,String> {
	protected DBUser owner;
	public CallableProtocolCopy(Method method, Form input,String baseReference,DBUser user,
			Connection connection, String token) {
		super(method, input, baseReference, connection, token);
		this.owner = user;
	}


	@Override
	protected DBProtocol getTarget(Form input) throws Exception {
		String identifier = input.getFirstValue("source_uri");
		String prefix = String.format("%s%s/",baseReference, Resources.protocol);
		if ((identifier!=null) && identifier.startsWith(prefix)) {
			DBProtocol p = new DBProtocol();
			p.setIdentifier(identifier.substring(prefix.length()));
			return p;
		} else 
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Invalid value of 'observation_uri' form parameter.");
	}

	@Override
	protected IQueryUpdate<? extends Object, DBProtocol> createUpdate(DBProtocol target) throws Exception {
		CreateProtocolCopy q = new CreateProtocolCopy(owner,target);
		return q;
	}

	@Override
	protected String getURI(DBProtocol target) throws Exception {
		return String.format("%s%s/%s",baseReference, Resources.protocol,target.getIdentifier());
	}

}
