package org.xmetdb.rest.protocol.resource.db;

import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.idea.modbcum.i.IQueryCondition;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.modbcum.p.MasterDetailsProcessor;
import net.idea.modbcum.r.QueryReporter;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.xmetdb.rest.groups.DBOrganisation;
import org.xmetdb.rest.groups.DBProject;
import org.xmetdb.rest.groups.IDBGroup;
import org.xmetdb.rest.groups.resource.GroupQueryURIReporter;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.AttachmentURIReporter;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachment;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.resource.UserURIReporter;

public class ProtocolJSONReporter extends QueryReporter<DBProtocol, IQueryRetrieval<DBProtocol>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected String queryService;
	protected String comma = null;
	protected QueryURIReporter uriReporter;
	protected AttachmentURIReporter<IQueryRetrieval<DBAttachment>> attachmentReporter;
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
	
	public QueryURIReporter getUriReporter() {
		return uriReporter;
	}

	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;
	
	
	public ProtocolJSONReporter(Request request,String queryService) {
		super();
		this.queryService = queryService;
		uriReporter = new ProtocolQueryURIReporter<IQueryRetrieval<DBProtocol>>(request);
		groupReporter = new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
		userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
		attachmentReporter = new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(request);
		getProcessors().clear();
		IQueryRetrieval<DBAttachment> queryP = new ReadAttachment(null,null); 
		MasterDetailsProcessor<DBProtocol,DBAttachment,IQueryCondition> attachmentReader = new MasterDetailsProcessor<DBProtocol,DBAttachment,IQueryCondition>(queryP) {
			@Override
			protected DBProtocol processDetail(DBProtocol target, DBAttachment detail)
					throws Exception {
				
				detail.setResourceURL(new URL(attachmentReporter.getURI(detail)));
				target.getAttachments().add(detail);
				return target;
			}
		};
		getProcessors().add(attachmentReader);		
		processors.add(new DefaultAmbitProcessor<DBProtocol,DBProtocol>() {
			@Override
			public DBProtocol process(DBProtocol target) throws AmbitException {
				try {
				processItem(target);
				} catch (AmbitException x) {
					throw x;
				} catch (Exception x) {
					throw new AmbitException(x);
				}
				return target;
			};
		});						
	}
	
	@Override
	public void header(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("{\"observations\": [\n");
		} catch (Exception x) {}
		
	}
	
	private static String format = "\n{\n\t\"uri\":\"%s\",\n\t\"identifier\": \"%s\",\n\t\"title\": \"%s\",\n\t\"description\": \"%s\",\n\t\"atom_uncertainty\": \"%s\",\n\t\"product_amount\": \"%s\",\n\t\"enzyme\": {\n\t\t\"code\" :null, \"name\" :null\n\t},\n\t\"reference\": \"%s\",\n\t\"updated\": \"%s\",\n\t\"owner\": {\n\t\t\"uri\" :\"%s\",\n\t\t\"username\": \"%s\"\n\t}";
	private static String formatAttachments =  ",\n\t\"%s\": {\n\t\t\"dataset\": {\"uri\": \"%s/dataset/R%d\", \"structure\": null}\n\t}";
	private static String emptyAttachments =  ",\n\t\"%s\": {\n\t\t\"dataset\": {\"uri\": null, \"structure\": null}\n\t}";
		
	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {
			if (comma!=null) getOutput().write(comma);
			String uri = uriReporter.getURI(item);
		
			if ((item.getProject()!=null) && (item.getProject().getResourceURL()==null))
				item.getProject().setResourceURL(new URL(groupReporter.getURI((DBProject)item.getProject())));
			if ((item.getOrganisation()!=null) && (item.getOrganisation().getResourceURL()==null))
				item.getOrganisation().setResourceURL(new URL(groupReporter.getURI((DBOrganisation)item.getOrganisation())));
			if ((item.getOwner()!=null) && (item.getOwner().getResourceURL()==null))
				item.getOwner().setResourceURL(new URL(userReporter.getURI((DBUser)item.getOwner())));
			
			String reference = item.getReference();

			getOutput().write(String.format(format,
					uri,
					item.getVisibleIdentifier(),
					item.getTitle(),
					item.getAbstract(),
					item.getAtomUncertainty().name(),
					item.getProductAmount().name(),
					reference==null?"":reference,
					dateFormat.format(new Date(item.getTimeModified())),
					item.getOwner().getResourceURL(),
					item.getOwner().getUserName()
					));
			if ((item.getAttachments()!=null) && (item.getAttachments().size()>0))
				for (DBAttachment attachment : item.getAttachments()) {
					getOutput().write(String.format(formatAttachments,
							attachment.getType().toString(),
							queryService,
							attachment.getIdquerydatabase()));
				}		
			else {
				for (attachment_type at : attachment_type.values()) {
					getOutput().write(String.format(emptyAttachments,
							at.toString()));
				}
			}
		//	\"substrate\":{\"uri\":\"%s\"},\n\"product\":{\"uri\":%s%s%s}			
			getOutput().write("\n}");
			comma = ",";
		} catch (Exception x) {
			
		}
		return item;
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("\n]\n}");
		} catch (Exception x) {}
	}
}
