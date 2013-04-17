package org.xmetdb.rest.protocol.resource.db;

import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;

import net.idea.modbcum.i.IQueryCondition;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.modbcum.p.MasterDetailsProcessor;
import net.idea.modbcum.r.QueryReporter;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.groups.IDBGroup;
import net.idea.restnet.groups.resource.GroupQueryURIReporter;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.Request;
import org.restlet.data.MediaType;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.endpoints.db.ReadEnzymeByObservation;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.AttachmentURIReporter;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachment;

public class ProtocolCSVReporter extends QueryReporter<DBProtocol, IQueryRetrieval<DBProtocol>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected MediaType mediaType;
	protected String queryService;
	protected QueryURIReporter uriReporter;
	protected AttachmentURIReporter<IQueryRetrieval<DBAttachment>> attachmentReporter;
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
	
	public QueryURIReporter getUriReporter() {
		return uriReporter;
	}

	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;
	
	
	public ProtocolCSVReporter(Request request,MediaType mediaType,String queryService) {
		super();
		this.mediaType = mediaType;
		this.queryService = queryService;
		uriReporter = new ProtocolQueryURIReporter<IQueryRetrieval<DBProtocol>>(request);
		groupReporter = new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
		userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
		attachmentReporter = new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(request);
		getProcessors().clear();
		IQueryRetrieval<Enzyme> queryE = new ReadEnzymeByObservation(null); 
		MasterDetailsProcessor<DBProtocol,Enzyme,IQueryCondition> enzymeReader = new MasterDetailsProcessor<DBProtocol,Enzyme,IQueryCondition>(queryE) {
			@Override
			protected DBProtocol processDetail(DBProtocol target, Enzyme detail)
					throws Exception {
				target.setEndpoint(detail);
				return target;
			}
		};
		getProcessors().add(enzymeReader);
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
			output.write(header);
		} catch (Exception x) {}
		
	}
	private static String header = "URI,Identifier,\"Product amount\",\"Experiment type\",Enzyme code,Enzyme name,UniProt,Reference,Comment\r\n";
	private static String format = "\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\"\r\n";
		
	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {
			String uri = uriReporter.getURI(item);
		
			if ((item.getProject()!=null) && (item.getProject().getResourceURL()==null))
				item.getProject().setResourceURL(new URL(groupReporter.getURI((DBProject)item.getProject())));
			if ((item.getOrganisation()!=null) && (item.getOrganisation().getResourceURL()==null))
				item.getOrganisation().setResourceURL(new URL(groupReporter.getURI((DBOrganisation)item.getOrganisation())));
			if ((item.getOwner()!=null) && (item.getOwner().getResourceURL()==null))
				item.getOwner().setResourceURL(new URL(userReporter.getURI((DBUser)item.getOwner())));
			

			getOutput().write(String.format(format,
					uri,
					item.getVisibleIdentifier(),
					item.getProductAmount(),
					item.getAbstract(),
					item.getEndpoint()==null?"":item.getEndpoint().toCSV(),
					item.getReference()==null?"":item.getReference(),
					item.getKeywords()
					));
		
		} catch (Exception x) {
			x.printStackTrace();
		}
		return item;
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBProtocol> query) {
	}
	
	@Override
	public String getFileExtension() {
		if (MediaType.APPLICATION_EXCEL.equals(mediaType)) return "xls";
		else if (MediaType.TEXT_CSV.equals(mediaType)) return "csv";
		return "csv";
	}
}