package org.xmetdb.rest.protocol.attachments.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;


public class AddAttachment extends AbstractUpdate<DBProtocol,DBAttachment>{
	public static final String[] create_sql = {
		"INSERT INTO attachments " +
		"SELECT null,idprotocol,version,?,?,?,now(),?,?,? from protocol where qmrf_number=?\n"+
		"ON DUPLICATE KEY UPDATE name=values(name),description=values(description),format=values(format),original_name=values(original_name)"
	};

	public AddAttachment(DBProtocol protocol,DBAttachment attachment) {
		super(attachment);
		setGroup(protocol);
	}
	
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params = new ArrayList<QueryParam>();
		for (AttachmentFields field : AttachmentFields.values()) 
			if (AttachmentFields.idattachment.equals(field)) continue;
			else if (AttachmentFields.idprotocol.equals(field)) continue;
			else if (AttachmentFields.version.equals(field)) continue;
			else params.add(field.getParam(this));
		params.add(new QueryParam<String>(String.class, getGroup().getIdentifier()));
		return params;
	}

	public String[] getSQL() throws AmbitException {
		return create_sql;
	}
	public void setID(int index, int id) {
		getObject().setID(id);
	}
	@Override
	public boolean returnKeys(int index) {
		return true;
	}
}