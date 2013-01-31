package org.xmetdb.rest.endpoints;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.r.QueryReporter;

public class EnzymeJSONReporter extends QueryReporter<Enzyme, IQueryRetrieval<Enzyme>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected String comma = null;
	public EnzymeJSONReporter() {
		super();
	}
	
	@Override
	public void header(Writer output, IQueryRetrieval<Enzyme> query) {
		try {
			output.write("[");
		} catch (Exception x) {}
		
	}
	@Override
	public Object processItem(Enzyme item) throws Exception {
		try {
			if (comma!=null) getOutput().write(comma);
			item.toJSON(getOutput());
			getOutput().write(String.format(""));
			comma = ",";
		} catch (Exception x) {
			x.printStackTrace();
		}
		return item;
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<Enzyme> query) {
		try {
			output.write("\n]");
		} catch (Exception x) {}
	}
}
