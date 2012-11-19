package org.xmetdb.rest.user.resource;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.UserHTMLBeauty;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.db.ReadUser;
import org.xmetdb.xmet.client.Resources;

public class MyAccountResource<T> extends UserDBResource<T> {

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		editable = false;
		singleItem = true;
	}
	@Override
	protected ReadUser createQuery(Context context, Request request, Response response)
			throws ResourceException {
		String search_name = null;
		Object search_value = null;

		try {
			search_name = "username";
			search_value = getClientInfo().getUser().getIdentifier();
		} catch (Exception x) {
			search_value = null;
			x.printStackTrace();
		}				
		if (search_value == null) throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED,"Not logged in!");
		Object key = request.getAttributes().get(UserDBResource.resourceKey);		
		try {
			return getUserQuery(key,search_name,search_value);
		}catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new ResourceException(
					Status.CLIENT_ERROR_BAD_REQUEST,
					String.format("Invalid protocol id %d",key),
					x
					);
		}
	} 
	
	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless)
			throws ResourceException {
		UserHTMLReporter rep = new UserHTMLReporter(getRequest(),!singleItem,editable,(UserHTMLBeauty)getHTMLBeauty()) {
			@Override
			public String getTitle() {
				return null;
			}
			@Override
			protected void printPageNavigator(IQueryRetrieval<DBUser> query)
					throws Exception {
			}
		};
		
		rep.setHeadless(headless);
		return rep;
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new UserHTMLBeauty() {
			@Override
			public String getSearchURI() {
				return Resources.myaccount;
			}
		};
	}
	
}
