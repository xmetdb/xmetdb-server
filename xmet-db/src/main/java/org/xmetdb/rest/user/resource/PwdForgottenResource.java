package org.xmetdb.rest.user.resource;

import java.sql.Connection;

import net.idea.restnet.db.DBConnection;
import net.idea.restnet.i.task.ICallableTask;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.xmet.client.Resources.Config;

public class PwdForgottenResource extends RegistrationResource {

	@Override
	public String getTemplateName() {
		return "pwd_forgotten.ftl";
	}

	@Override
	protected ICallableTask createCallable(Method method, Form form, DBUser item)
			throws ResourceException {
		Connection conn = null;
		try {
			String usersdbname = getContext().getParameters().getFirstValue(Config.users_dbname.name());
			UserURIReporter reporter = new UserURIReporter(getRequest(),"");
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			return new CallablePasswordReset(method,null,reporter, form,getRequest().getRootRef().toString(),
					conn,getToken(),usersdbname==null?"xmet_users":usersdbname);
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	}
}
