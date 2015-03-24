package org.xmetdb.rest.protocol.resource.db;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.r.QueryReporter;

import org.xmetdb.rest.links.ExternalIdentifier;

public class LinksJSONReporter extends QueryReporter<ExternalIdentifier, IQueryRetrieval<ExternalIdentifier>, Writer> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6247243997729783899L;
	protected String comma = "";
	protected String root = null;
	public LinksJSONReporter(String root) {
		super(); 
		this.root = root;
	}
	@Override
	public void footer(Writer arg0, IQueryRetrieval<ExternalIdentifier> arg1) {
		try {
			output.write("\n]\n}");
		} catch (Exception x) {
		}

	}

	@Override
	public void header(Writer arg0, IQueryRetrieval<ExternalIdentifier> arg1) {
		comma = "";
		try {
			output.write("{\"externalIdentifiers\": [\n");
		} catch (Exception x) {
		}
		
	}

	@Override
	public Object processItem(ExternalIdentifier item) throws Exception {
		getOutput().write(comma);
		getOutput().write(item.toString());
		comma = ",";
		return item;
	}

}
