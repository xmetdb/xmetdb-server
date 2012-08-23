package org.xmetdb.rest.protocol.attachments;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.xmetdb.xmet.client.Resources;

public class AttachmentURIReporter <Q extends IQueryRetrieval<DBAttachment>> extends QueryURIReporter<DBAttachment, Q> {
	String prefix = "";


	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8868430033131766579L;
	public AttachmentURIReporter(Request baseRef,String prefix) {
		super(baseRef,null);	
		this.prefix = prefix;
	}
	public AttachmentURIReporter(Request baseRef) {
		this(baseRef,"");
	}
	public AttachmentURIReporter() {
		this(null);
	}	

	@Override
	public String getURI(String ref, DBAttachment item) {
		return String.format("%s%s%s/A%d",ref,prefix,Resources.attachment,item.getID());
	}

}