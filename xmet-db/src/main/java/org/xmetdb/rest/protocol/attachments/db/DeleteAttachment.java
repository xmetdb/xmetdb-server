package org.xmetdb.rest.protocol.attachments.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

/**
 * Removes an author of a given protocol. Does not delete the user itself.
 * @author nina
 *
 */
public class DeleteAttachment  extends AbstractUpdate<DBProtocol,DBAttachment> {
	protected static final String sql = "DELETE from attachments where ";
	
	protected AttachmentFields[] ids = {AttachmentFields.idattachment,AttachmentFields.idprotocol,AttachmentFields.version};

	public DeleteAttachment(DBAttachment item,DBProtocol protocol) {
		super(item);
		setGroup(protocol);
	}
	public DeleteAttachment() {
		this(null,null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params = new ArrayList<QueryParam>();
		for (AttachmentFields p : ids) 
			if (p.isValid(this)) params.add(p.getParam(this));

		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		StringBuilder b = new StringBuilder();
		b.append(sql);
		String delimiter = "";
		for (AttachmentFields p : ids) 
			if (p.isValid(this)) {
				b.append(delimiter);
				b.append(p.getSQL());
				delimiter = " and ";
			}
		return new String[] {b.toString()};
	}
	public void setID(int index, int id) {
			
	}
}