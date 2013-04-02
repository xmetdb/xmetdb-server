package org.xmetdb.rest.endpoints.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

import org.xmetdb.rest.endpoints.Enzyme;

public class ReadEnzyme extends AbstractReadEnzyme<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5388155361346420917L;
	
	public ReadEnzyme() {
		this(null);
	}
	
	public ReadEnzyme(Enzyme enzyme) {
		super(enzyme);
	}

	private static final String sql_all = "SELECT idtemplate,code,name,uniprot,alleles FROM template";
	
	private static final String sql_byid = "SELECT idtemplate,code,name,uniprot,alleles FROM template where idtemplate=?";
	
	@Override
	public boolean isPrescreen() {
		return false;
	}

	@Override
	public double calculateMetric(Enzyme object) {
		return 1;
	}

	@Override
	public String getSQL() throws AmbitException {
		if ((getValue()==null) || (getValue().getID()<=0))
			return sql_all;
		else
			return sql_byid;
	}

	@Override
	public List<QueryParam> getParameters() throws AmbitException {
		if ((getValue()==null) || (getValue().getID()<=0))
			return null;
		else {
			List<QueryParam> params =  new ArrayList<QueryParam>();
			params.add(new QueryParam<Integer>(Integer.class,getValue().getID()));
			return params;
		}
	}

}
