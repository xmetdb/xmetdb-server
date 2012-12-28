package org.xmetdb.rest.endpoints;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.r.QueryReporter;
import ambit2.base.data.Dictionary;

public class DictionaryJSONReporter<D extends Dictionary> extends QueryReporter<D, IQueryRetrieval<D>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected String comma = null;
	public DictionaryJSONReporter() {
		super();
	}
	
	@Override
	public void header(Writer output, IQueryRetrieval<D> query) {
		try {
			output.write("[");
		} catch (Exception x) {}
		
	}
	@Override
	public Object processItem(D item) throws Exception {
		try {
			String code = ((Enzyme)item).getCode()==null?"":((Enzyme)item).getCode();
			String name = item.getName()==null?"":item.getName();
			if (comma!=null) getOutput().write(comma);
			getOutput().write(String.format("\n{\"parentCode\":\"%s\",\"parentName\":\"%s\",\"code\":\"%s\",\"name\":\"%s\",\"label\":\"%s %s\"}",
					((Enzyme)item).getParentCode()==null?"":((Enzyme)item).getParentCode(),		
					item.getParentTemplate()==null?"":item.getParentTemplate(),			
					code,
					name,
					code,name
					));
			comma = ",";
		} catch (Exception x) {
			
		}
		return item;
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<D> query) {
		try {
			output.write("\n]");
		} catch (Exception x) {}
	}
}
