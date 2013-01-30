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
			StringBuilder alleles = new StringBuilder();
			alleles.append("[");
			String delimiter = "";
			if (item.getAlleles()!=null) 
				for (String allele: item.alleles) {
					alleles.append(delimiter);
					alleles.append("\"");
					alleles.append(allele);
					alleles.append("\"");
					delimiter = ",";
				}
			alleles.append("]");
			String code = ((Enzyme)item).getCode()==null?"":((Enzyme)item).getCode();
			String name = item.getName()==null?"":item.getName();
			if (comma!=null) getOutput().write(comma);
			getOutput().write(String.format("\n{\"parentCode\":\"%s\",\"parentName\":\"%s\",\"code\":\"%s\",\"name\":\"%s\",\"label\":\"%s %s\",\"uri\":\"%s\",\"alleles\":%s}",
					((Enzyme)item).getParentCode()==null?"":((Enzyme)item).getParentCode(),		
					item.getParentTemplate()==null?"":item.getParentTemplate(),			
					code,
					name,
					code,name,
					item.getUri()==null?"":item.getUri(),
					alleles
					));
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
