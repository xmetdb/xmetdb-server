package org.xmetdb.rest.protocol.attachments;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.r.QueryReporter;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.xmetdb.rest.protocol.ProtocolURIReporter;

public class AttachmentJSONReporter  extends QueryReporter<DBAttachment, IQueryRetrieval<DBAttachment>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected String comma = null;
	protected String queryService;
	protected ProtocolURIReporter protocolURIReporter;
	protected QueryURIReporter uriReporter;
	public QueryURIReporter getUriReporter() {
		return uriReporter;
	}
	
	public AttachmentJSONReporter(String queryService, Request request) {
		super();
		this.queryService = queryService;
		uriReporter = new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(request);
		protocolURIReporter = new ProtocolURIReporter(request,null);
	}
	

	
	@Override
	public void header(Writer output, IQueryRetrieval<DBAttachment> query) {
		try {
			output.write("{\"attachments\": [\n");
		} catch (Exception x) {}
		
	}
	
	private static String format = "\n{\n\"uri\":\"%s\",\n\"observation\":{\"uri\":\"%s\"},\n\"title\":\"%s\",\n\"description\":\"%s\",\n\"type\":\"%s\",\n\"format\":\"%s\",\n\"dataset\":{\"uri\":\"%s/dataset/R%s\"}\n}";
	@Override
	public Object processItem(DBAttachment item) throws Exception {
		try {
			if (comma!=null) getOutput().write(comma);
			String uri = uriReporter.getURI(item);
		
			getOutput().write(String.format(format,
					uri,
					item.getProtocol()==null?"":protocolURIReporter.getURI(item.getProtocol()),
					item.getTitle(),
					item.getDescription(),
					item.getType(),
					item.getFormat(),
					queryService,
					item.getIdquerydatabase()
					));
			comma = ",";
		} catch (Exception x) {
			
		}
		return item;
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBAttachment> query) {
		try {
			output.write("\n]\n}");
		} catch (Exception x) {}
	}
}
