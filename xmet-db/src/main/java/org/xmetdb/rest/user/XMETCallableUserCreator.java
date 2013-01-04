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
		subject = "XMETDB User Confirmation";
		emailContent = 
			"Thank you for applying for user registration with the Xenobiotics Metabolism  Database.\n"+
			"\n"+
			"Please point your browser to the following URL in order to proceed with the registration of the \"%s\" user:\n"+
			"%s%s%s?code=%s\n"+
			"Please note that your registration will be cancelled automatically if it is not confirmed within 48 hours. If you miss this deadline you should start over the registration procedure and get a new confirmation code.\n"+
			"\n"+
			"If you change your mind and decide that you do NOT want to confirm the registration, then please discard this message and let the request expire on its own.\n"
			;			
	}

	
	@Override
	protected String getConfig() {
		return "config/xmetdb.properties";
	}
}