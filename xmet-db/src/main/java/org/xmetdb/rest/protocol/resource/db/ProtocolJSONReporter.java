package org.xmetdb.rest.protocol.resource.db;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.r.QueryReporter;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.xmetdb.rest.groups.IDBGroup;
import org.xmetdb.rest.groups.resource.GroupQueryURIReporter;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.resource.UserURIReporter;

public class ProtocolJSONReporter extends QueryReporter<DBProtocol, IQueryRetrieval<DBProtocol>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected String comma = null;
	protected QueryURIReporter uriReporter;
	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;
	
	
	public ProtocolJSONReporter(Request request) {
		super();
		uriReporter = new ProtocolQueryURIReporter<IQueryRetrieval<DBProtocol>>(request);
		groupReporter = new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
		userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
	}
	
	@Override
	public void header(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("[");
		} catch (Exception x) {}
		
	}
	
	private static String format = "\n{\n\"uri\":\"%s\",\n\"identifier\":\"%s\",\n\"title\":\"%s\",\n\"enzyme\":\"%s\",\n\"updated\":\"%s\",\n\"owner\":\"{uri:\"%s\",username:\"%s\"}\n}";
	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {
			if (comma!=null) getOutput().write(comma);
			String uri = uriReporter.getURI(item);
			String user_uri = userReporter.getURI((DBUser)item.getOwner());
			getOutput().write(String.format(format,
					uri,
					item.getVisibleIdentifier(),
					item.getTitle(),
					item.getEndpoint(),
					item.getTimeModified(),
					user_uri,
					item.getOwner().getUserName()
					));
			comma = ",";
		} catch (Exception x) {
			
		}
		return item;
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("\n]");
		} catch (Exception x) {}
	}
}
