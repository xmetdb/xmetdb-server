package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.ProtocolFactory;

/**
 * Only for updating fields by a curator
 * @author nina
 *
 */
public class UpdateObservationEntry extends AbstractUpdate<DBProtocol,ProtocolFactory.ObservationFields> {
	
	private static final String[] update_reference = 
	{"update protocol set updated=now(),reference=?,curated=1 where qmrf_number=?"};
	private static final String[] update_comments = 
	{"update keywords k,protocol p set keywords =?,curated=1,updated=now() where p.idprotocol=k.idprotocol and p.version=k.version and qmrf_number=?"};
	private static final String[] update_curated = 
	{"update protocol set updated=now(),curated=? where qmrf_number=?"};	
	@Override
	public String[] getSQL() throws AmbitException {
		switch (getObject()) {
		case xmet_reference: {
			return update_reference;
		}
		case xmet_comments: {
			return update_comments;
		}
		case curated: {
			return update_curated;
		}
		}
		return null;
	}

	@Override
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params1 = new ArrayList<QueryParam>();
		
		if (getGroup()==null) throw new AmbitException("Empty protocol");
		if (getGroup().getIdentifier()==null) throw new AmbitException("Invalid observation ID");
		
		if (getObject()==null) throw new AmbitException("Empty field");
		switch (getObject()) {
		case xmet_reference: {
			params1.add(new QueryParam<String>(String.class,getGroup().getReference()));
			break;
		}
		case xmet_comments: {
			List<String> comments = getGroup().getKeywords();
			params1.add(new QueryParam<String>(String.class,comments.size()==0?"":comments.get(0)));
			break;
		}
		case curated: {
			params1.add(new QueryParam<Boolean>(Boolean.class,getGroup().isSearchable()));
			break;
		}		
		}
		params1.add(new QueryParam<String>(String.class,getGroup().getIdentifier()));
		return params1;
	}

	@Override
	public void setID(int index, int id) {
	}

}
