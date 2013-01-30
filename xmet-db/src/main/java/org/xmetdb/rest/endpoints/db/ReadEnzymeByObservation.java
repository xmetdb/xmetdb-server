package org.xmetdb.rest.endpoints.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.protocol.DBProtocol;

/**
 * 
 * @author nina
 *
 */
public class ReadEnzymeByObservation  extends AbstractReadEnzyme<DBProtocol> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7623646134570258193L;
	private static final String sqlProtocol = 	
		"SELECT idprotocol,version,t.name,t.code,t.uri,t.allele FROM protocol\n"+
		"left join protocol_endpoints pe using(idprotocol,version)\n"+
		"left join template t using(idtemplate)\n"+
		"where qmrf_number=?";
	
	
	public ReadEnzymeByObservation(DBProtocol obs) {
		this(null,obs);
	}
	
	public ReadEnzymeByObservation(Enzyme enzyme,DBProtocol obs) {
		super(enzyme,obs);
	}

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
		return sqlProtocol;
	}

	@Override
	public List<QueryParam> getParameters() throws AmbitException {
		if ((getFieldname()==null) || (getFieldname().getIdentifier()==null))
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"No observation identifier!");
		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<String>(String.class, getFieldname().getIdentifier()));
		return params;
	}

}
