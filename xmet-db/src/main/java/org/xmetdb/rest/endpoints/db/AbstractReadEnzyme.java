package org.xmetdb.rest.endpoints.db;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.modbcum.q.query.AbstractQuery;

import org.xmetdb.rest.endpoints.Enzyme;

public abstract class AbstractReadEnzyme<T> extends AbstractQuery<T, Enzyme, StringCondition, Enzyme>  implements IQueryRetrieval<Enzyme> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3648848396511265333L;
	public AbstractReadEnzyme(Enzyme enzyme) {
		this(enzyme,null);
	}
	
	public AbstractReadEnzyme(Enzyme enzyme,T value) {
		super();
		setValue(enzyme);
		setFieldname(value);
	}	

	public Enzyme getObject(ResultSet rs) throws AmbitException {
		try {
			Enzyme result = new Enzyme();
			result.setId(rs.getInt("idtemplate"));
			result.setName(rs.getString("name"));
			result.setCode(rs.getString("code"));
			String uri = rs.getString("uri");
			if (uri==null) result.setUri(null);
			else
			try { result.setUri(new URI(uri));} catch (URISyntaxException x) { result.setUri(null);}
			try {
				String alleles = rs.getString("allele");
				if (alleles!=null) result.setAlleles(alleles.split(",")); else result.setAlleles(null);
			} catch (Exception x) {}
			return result;
		} catch (SQLException x) {
			throw new AmbitException(x);
		}
	}
}
