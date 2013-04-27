package org.xmetdb.rest.protocol.facet;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.DbAmbitException;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.modbcum.r.QueryReporter;

import org.restlet.Request;

public class FacetTXTReporter<Q extends IQueryRetrieval<IFacet>> extends QueryReporter<IFacet, Q, Writer> {

	public FacetTXTReporter(Request baseRef) {
		super();
	}
	@Override
	public void open() throws DbAmbitException {
		
	}

	@Override
	public void header(Writer output, Q query) {
	}

	@Override
	public void footer(Writer output, Q query) {
		try {
			output.flush();
			} catch (Exception x) {}
	}

	@Override
	public Object processItem(IFacet item) throws Exception {
		try {
			output.write(Integer.toString(item.getCount()));
		} catch (Exception x) {
			logger.error(x);
		}
		return item;
	}

}