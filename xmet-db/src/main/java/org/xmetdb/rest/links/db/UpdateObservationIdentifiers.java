package org.xmetdb.rest.links.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;

public class UpdateObservationIdentifiers extends AbstractUpdate<DBProtocol,List<ExternalIdentifier>>  {
	private static String deleteSQL = "delete from protocol_links where idprotocol=? and version=?";
	private static String insertSQL = "insert ignore into protocol_links (idprotocol,version,type,id) values %s";
	
	public UpdateObservationIdentifiers(DBProtocol observation, List<ExternalIdentifier> ids) {
		super(ids);
		setGroup(observation);
	}
	
	public UpdateObservationIdentifiers() {
		this(null,null);
	}	
	
	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getGroup()==null) throw new AmbitException("Empty protocol");
		if (getGroup().getID()<=0) throw new AmbitException("Invalid document ID");
		if (getGroup().getVersion()<=0) throw new AmbitException("Invalid document version");
		
		List<QueryParam> params = new ArrayList<QueryParam>();
		switch (index) {
		case 0: {
			params.add(ReadProtocol.fields.idprotocol.getParam(getGroup()));
			params.add(ReadProtocol.fields.version.getParam(getGroup()));
			break;
		}
		case 1: {
			if (getObject()!=null)
				for (ExternalIdentifier id : getObject()) {
					params.add(ReadProtocol.fields.idprotocol.getParam(getGroup()));
					params.add(ReadProtocol.fields.version.getParam(getGroup()));
					params.add(new QueryParam<String>(String.class, id.getSystemDesignator()));
					params.add(new QueryParam<String>(String.class, id.getSystemIdentifier()));
				}
			break;
		}
		}
		return params;
	}

	@Override
	public String[] getSQL() throws AmbitException {

		if (getObject()==null || getObject().size()==0) return new String[] {deleteSQL};
		StringBuilder updateValues = null;	
		for (ExternalIdentifier id : getObject()) {
			if (updateValues==null) updateValues = new StringBuilder();
			else updateValues.append(",");
			updateValues.append("(?,?,?,?)");
		}	
		return new String[] {deleteSQL,String.format(insertSQL, updateValues.toString())};
	}
	public void setID(int index, int id) {
		
	}
}