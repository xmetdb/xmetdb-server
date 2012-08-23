package org.xmetdb.rest.user.author.resource;


import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.author.db.ReadAuthor;
import org.xmetdb.rest.user.db.ReadUser;
import org.xmetdb.rest.user.resource.UserDBResource;

public class ProtocolAuthorsResource extends UserDBResource<DBProtocol> {

	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
	}
	
	@Override
	protected ReadUser getUserQuery(Object key, String search_name,
			Object search_value) throws ResourceException {
		Object protocolKey = getRequest().getAttributes().get(FileResource.resourceKey);	
		if (protocolKey==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		DBProtocol protocol = new DBProtocol(Reference.decode(protocolKey.toString()));
		DBUser user = null;
		
		if (key!=null) {
			if (key.toString().startsWith("U")) {
				singleItem = true;
				user = new DBUser(new Integer(Reference.decode(key.toString().substring(1))));
			} else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}	
		return new ReadAuthor(protocol, user);
	}
		
}
