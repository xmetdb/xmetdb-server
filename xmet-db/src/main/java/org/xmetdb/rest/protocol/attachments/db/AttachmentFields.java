package org.xmetdb.rest.protocol.attachments.db;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;

import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

public enum AttachmentFields {

	idattachment {
		@Override
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<Integer>(Integer.class, query.getObject().getID());
		}
		@Override
		public boolean isValid(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return (query.getObject()!=null) && (query.getObject().getID()>0);
		}			
	},
	idprotocol {
		@Override
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<Integer>(Integer.class, query.getGroup().getID());
		}
		@Override
		public boolean isValid(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return (query.getGroup()!=null) && (query.getGroup().getID()>0);
		}
		
	},
	version {
		@Override
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<Integer>(Integer.class, query.getGroup().getVersion());
		}	
		@Override
		public boolean isValid(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return (query.getGroup()!=null) && (query.getGroup().getVersion()>0);
		}
	},
	name {
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<String>(String.class, query.getObject().getTitle());
		}	
	},
	description {
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<String>(String.class, query.getObject().getDescription());
		}			
	},
	format {
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<String>(String.class, query.getObject().getFormat());
		}	
	},
	original_name {
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<String>(String.class, query.getObject().getOriginalFileName());
		}	
	},
	imported {
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<Boolean>(Boolean.class, query.getObject().isImported());
		}	
	},
	type {
		public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {
			return new QueryParam<String>(String.class, query.getObject().getType().name());
		}		
		
	};	
	public String getSQL() {
		return String.format("%s=?", name());
	}
	public QueryParam getParam(AbstractUpdate<DBProtocol,DBAttachment> query) {return null; }
	public boolean isValid(AbstractUpdate<DBProtocol,DBAttachment> query) { return false;}
}