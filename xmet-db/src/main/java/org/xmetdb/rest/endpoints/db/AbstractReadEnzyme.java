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
import org.xmetdb.rest.endpoints.Enzyme.EnzymeFields;

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
			result.setName(rs.getString(EnzymeFields.name.name()));
			result.setCode(rs.getString(EnzymeFields.code.name()));
			String uri = rs.getString(EnzymeFields.uniprot.name());
			result.setUniprot_id(uri);
			try {
				String alleles = rs.getString(EnzymeFields.alleles.name());
				if (alleles!=null) result.setAlleles(alleles.split(",")); else result.setAlleles(null);
			} catch (Exception x) {}
			return result;
		} catch (SQLException x) {
			throw new AmbitException(x);
		}
	}
}
