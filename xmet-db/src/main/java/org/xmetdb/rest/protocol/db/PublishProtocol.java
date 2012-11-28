package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

import org.xmetdb.rest.db.exceptions.InvalidProtocolException;
import org.xmetdb.rest.protocol.DBProtocol;

/**
 * Just sets the published flag
 * @author nina
 *
 */
public class PublishProtocol<T> extends AbstractUpdate<T,DBProtocol>{
	
	public static final String[] publish_sql = new String[] { 
		"update protocol set qmrf_number=?, published_status=? where idprotocol=? and version=?"		
	};
	public PublishProtocol(DBProtocol ref) {
		super(ref);
	}
		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params1 = new ArrayList<QueryParam>();
		if (getObject()==null || (getObject().getID()<=0) || (getObject().getVersion()<=0)) 
			throw new InvalidProtocolException();
	
		String id = String.format("XMETDB%d%s",getObject().getID(),getObject().getVersion()==1?"":"v"+Integer.toString(getObject().getVersion()));
		params1.add(new QueryParam<String>(String.class, id));
		params1.add(ReadProtocol.fields.published_status.getParam(getObject()));			
		params1.add(ReadProtocol.fields.idprotocol.getParam(getObject()));
		params1.add(ReadProtocol.fields.version.getParam(getObject()));
		
		return params1;
		
	}
	public String[] getSQL() throws AmbitException {
		
		return publish_sql;
	}
	public void setID(int index, int id) {
			
	}
}