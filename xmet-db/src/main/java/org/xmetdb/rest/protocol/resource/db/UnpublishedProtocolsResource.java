package org.xmetdb.rest.protocol.resource.db;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.db.exceptions.InvalidXmetdbNumberException;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.db.ReadUser;
import org.xmetdb.rest.user.resource.UserDBResource;
import org.xmetdb.xmet.client.Resources;

public class UnpublishedProtocolsResource<Q extends IQueryRetrieval<DBProtocol>> extends ProtocolDBResource<Q> {

	@Override
	protected Q createQuery(Context context, Request request, Response response)
			throws ResourceException {
		
		Form form = request.getResourceRef().getQueryAsForm();
			
		Object modified = null;
		try {
			modified = form.getFirstValue("modifiedSince").toString();
		} catch (Exception x) {
			modified = null;
		}			

		Object key = request.getAttributes().get(FileResource.resourceKey);
		int userID = -1;
		try {
			Object userKey = request.getAttributes().get(UserDBResource.resourceKey);
			if (userKey!=null)
				userID = ReadUser.parseIdentifier(userKey.toString());
		} catch (Exception x) {}

	
		try {
			 return getProtocolQuery(key,userID,null,modified,false);
		}catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new InvalidXmetdbNumberException(key==null?"":key.toString());
		}
	} 
	
	@Override
	protected Q getProtocolQuery(Object key,int userID,Object search, Object modified, boolean showCreateLink) throws ResourceException {
		
		if (key==null) {
			ReadProtocol query = new ReadProtocol();
			query.setOnlyUnpublished(true);
			 query.setShowUnpublished(true);
			if (search != null) {
				DBProtocol p = new DBProtocol();
				p.setTitle(search.toString());
				query.setValue(p);
			} else if (modified != null) try {
				DBProtocol p = new DBProtocol();
				p.setTimeModified(Long.parseLong(modified.toString()));
				query.setValue(p);
			} catch (Exception x) {x.printStackTrace();}
//			query.setFieldname(search.toString());
			editable = showCreateLink;
			if (userID>0) {
				query.setFieldname(new DBUser(userID));
			} 
			return (Q)query;
		}			
		else {
			editable = showCreateLink;
			singleItem = true;
			ReadProtocol query =  new ReadProtocol(Reference.decode(key.toString()));
			query.setShowUnpublished(true);
			if (userID>0) query.setFieldname(new DBUser(userID));
			return (Q)query;
		}
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty = new XmetdbHTMLBeauty(Resources.unpublished,false);
		return htmlBeauty;
	}

}
