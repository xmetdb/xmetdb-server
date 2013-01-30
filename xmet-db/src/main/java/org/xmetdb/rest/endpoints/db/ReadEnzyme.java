package org.xmetdb.rest.endpoints.db;

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

	private static final String sql_all = "SELECT code,name,uri,allele FROM template";
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
		return sql_all;
	}

	@Override
	public List<QueryParam> getParameters() throws AmbitException {
		return null;
	}

}
