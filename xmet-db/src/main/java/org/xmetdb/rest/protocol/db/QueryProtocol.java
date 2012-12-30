package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

import org.restlet.data.Form;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.resource.db.ProtocolDBResource.SearchMode;

public class QueryProtocol extends ReadProtocolAbstract<Form>  implements IQueryRetrieval<DBProtocol> {
	protected static String sql_query = 
		"select idprotocol,protocol.version,protocol.title,qmrf_number,abstract as anabstract,iduser,summarySearchable,\n"+
		"idproject,idorganisation,user.username,user.firstname,user.lastname,\n"+
		"reference,updated,status,`created`,published_status,atom_uncertainty,product_amount,\n"+
		"template.code as enzymecode,template.name as enzymename,pe.allele\n"+
		"from protocol\n"+
		"left join protocol_endpoints pe using(idprotocol,version)\n"+
		"join template using(idtemplate)\n"+
		"join user using(iduser)\n";
	/**
	 * 
	 */
	private static final long serialVersionUID = 748004803718030032L;

	public QueryProtocol() {
		super();
	}
	
	@Override
	public String getSQL() throws AmbitException {
		StringBuilder number = null;
		StringBuilder exp = null;
		StringBuilder enz = null;
		StringBuilder allele = null;
		StringBuilder ref = null;
		for (SearchMode mode: SearchMode.values()) {
			String[] values = getFieldname().getValuesArray(mode.name());
			if ((values==null) || (values.length==0)) continue;
			for (String search:values)
				if (search==null || "".equals(search.trim())) continue;
				else
				switch (mode) {
				case xmet_number: {
					if (number==null) number = new StringBuilder(); else number.append(",");
					number.append("?");
					break;
				}
				case xmet_exp_enz: {
					if ("on".equals(search)) {
						if (exp==null) exp = new StringBuilder(); else exp.append(",");
						exp.append("?");
					}
					break;
				}
				case xmet_exp_hep: {
					if ("on".equals(search)) {
						if (exp==null) exp = new StringBuilder(); else exp.append(",");
						exp.append("?");
					}
					break;
				}
				case xmet_exp_ms: {
					if ("on".equals(search)) {
						if (exp==null) exp = new StringBuilder(); else exp.append(",");
						exp.append("?");
					}
					break;
				}			
				case xmet_enzyme: {
					if (enz==null) enz = new StringBuilder(); else enz.append(",");
					enz.append("?");
					break;
				}
				case xmet_allele: {
					if (allele==null) allele = new StringBuilder(); else allele.append(",");
					allele.append("?");
					break;
				}				
				case xmet_reference: {
					if (ref==null) ref = new StringBuilder(); else ref.append(" or ");
					ref.append("reference regexp ?");
					break;
				}		
				case modifiedSince: {
					break;
				}
				default: 
				}
		}
		List<String> b = new ArrayList<String>();
		if (number!=null) b.add(String.format("qmrf_number in (%s)", exp));
		if (exp!=null) b.add(String.format("protocol.title in (%s)", exp));
		if (enz!=null) b.add(String.format("template.code in (%s)", enz));
		if (allele!=null) b.add(String.format("pe.allele in (%s)", allele));
		if (ref!=null) b.add(String.format("(%s)", ref));
		StringBuilder sql = new StringBuilder();
		sql.append(sql_query);
		if (b.size()>0)
			sql.append("where\n");
		for (int i=0; i < b.size(); i++) {
			if (i>0) sql.append(" and ");
			sql.append(b.get(i));
		}
		return sql.toString();
	}

	@Override
	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		for (SearchMode mode: SearchMode.values()) {
			String[] values = getFieldname().getValuesArray(mode.name());
			if ((values==null) || (values.length==0)) continue;
			for (String search:values)
				if (search==null || "".equals(search.trim())) continue;
				else
				switch (mode) {
				case xmet_number: {
					params.add(new QueryParam<String>(String.class, search.trim()));
					break;
				}
				case xmet_exp_enz: {
					if ("on".equals(search)) 
						params.add(new QueryParam<String>(String.class, "ENZ"));
					break;
				}
				case xmet_exp_hep: {
					if ("on".equals(search)) 
						params.add(new QueryParam<String>(String.class, "HEP"));
					break;
				}
				case xmet_exp_ms: {
					if ("on".equals(search)) 
						params.add(new QueryParam<String>(String.class, "MS"));
					break;
				}				
				case xmet_allele: {
					params.add(new QueryParam<String>(String.class, search.trim()));
					break;
				}				
				case xmet_enzyme: {
					params.add(new QueryParam<String>(String.class, search.trim()));
					break;
				}
				case xmet_reference: {
					params.add(new QueryParam<String>(String.class, search.trim()));
					break;
				}		
				case modifiedSince: {
					break;
				}
				default: 
				}
		}
		return params;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (SearchMode mode: SearchMode.values()) {
			String[] values = getFieldname().getValuesArray(mode.name());
			if ((values==null) || (values.length==0)) continue;
			for (String search:values)
				if (search==null || "".equals(search.trim())) continue;
				else
				switch (mode) {
				case xmet_number: {
					b.append("&nbsp;");
					b.append(value);
					break;
				}
				case xmet_exp_enz: {
					if ("on".equals(search)) {
						b.append("&nbsp;ENZ");
					}
					break;
				}
				case xmet_exp_hep: {
					if ("on".equals(search)) {
						b.append("&nbsp;HEP");					}
					break;
				}
				case xmet_exp_ms: {
					if ("on".equals(search)) {
						b.append("&nbsp;MS");
					}
					break;
				}			
				case xmet_enzyme: {
					b.append("&nbsp;");
					b.append("Enzyme:");
					b.append(search);
					break;
				}
				case xmet_allele: {
					b.append(".");
					b.append(search);
					break;
				}				
				case xmet_reference: {
					b.append("&nbsp;Reference:");
					b.append(search);
					break;
				}		
				case modifiedSince: {
					break;
				}
				default: 
				}
		}
		if (b.length()==0) b.append("All observations");
		return b.toString();
	}

}
