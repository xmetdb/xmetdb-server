package org.xmetdb.rest.user;

import java.sql.Connection;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.user.CallableUserCreator;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.data.Form;
import org.restlet.data.Method;

public class XMETCallableUserCreator extends CallableUserCreator {
	
	public XMETCallableUserCreator(Method method,DBUser item,
						UserURIReporter<IQueryRetrieval<DBUser>> reporter,
						Form input,
						String baseReference,
						Connection connection,
						String token,
						boolean passwordChange,
						String usersdbname)  {
		super(method,item,reporter, input,baseReference, connection,token,passwordChange,usersdbname);
		subject = "XMetDB User Confirmation";
	}

	
	@Override
	protected String getConfig() {
		return "config/xmetdb.properties";
	}

	@Override
	protected String getSender() {
		return "Xenobiotics Metabolism Database";
	}


	@Override
	protected String getSenderName() {
		return "The Support Team";
	}


	@Override
	protected String getSystemName() {
		return "XMetDB";
	}
}