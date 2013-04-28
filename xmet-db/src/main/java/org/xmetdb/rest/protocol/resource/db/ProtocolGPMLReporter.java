package org.xmetdb.rest.protocol.resource.db;

import java.io.Writer;

import net.idea.modbcum.i.IQueryCondition;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.modbcum.p.MasterDetailsProcessor;
import net.idea.modbcum.r.QueryReporter;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.MediaType;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.endpoints.db.ReadEnzymeByObservation;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachmentStructures;

import ambit2.base.interfaces.IStructureRecord;

/**
 * GPML being the XML behind WikiPathways:
 * http://www.plosbiology.org/article/info%3Adoi%2F10.1371%2Fjournal.pbio.0060184
 * https://github.com/xmetdb/xmetdb-server/issues/33
 * @author nina
 *
 */
public class ProtocolGPMLReporter  extends QueryReporter<DBProtocol, IQueryRetrieval<DBProtocol>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected MediaType mediaType;
	protected QueryURIReporter uriReporter;
	
	public QueryURIReporter getUriReporter() {
		return uriReporter;
	}

	
	public ProtocolGPMLReporter(Request request,MediaType mediaType,String queryService) {
		super();
		this.mediaType = mediaType;
		uriReporter = new ProtocolQueryURIReporter<IQueryRetrieval<DBProtocol>>(request);
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
		ReadAttachmentStructures queryP = new ReadAttachmentStructures(null);
		queryP.setRetrieveStructure(false);
		MasterDetailsProcessor<DBProtocol,DBAttachment,IQueryCondition> attachmentReader = new MasterDetailsProcessor<DBProtocol,DBAttachment,IQueryCondition>(queryP) {
			@Override
			protected DBProtocol processDetail(DBProtocol target, DBAttachment detail)
					throws Exception {
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
			output.write("<Pathway>");
			
		} catch (Exception x) {}
		
	}
	
	
	private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		
	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {
			String uri = uriReporter.getURI(item);
		
			IStructureRecord substrate = null;
			IStructureRecord product = null;
			for (DBAttachment attachment: item.getAttachments()) {
				switch (attachment.getType()) {
				case substrate: {
					substrate = attachment.getStructure();
					break;				
				}
				case product: {
					product = attachment.getStructure();
					break;
				}
				}
			}
			getOutput().write(String.format(structureNode,substrate.getIdchemical(),substrate.getIdchemical(),substrate.getInchi()));
			getOutput().write(String.format(enzymeNode,item.getEndpoint().getCode(),item.getEndpoint().getCode(),item.getEndpoint().getUniprot_id()));
			getOutput().write(String.format(structureNode,product.getIdchemical(),product.getIdchemical(),product.getInchi()));
			
		
		} catch (Exception x) {
			x.printStackTrace();
		}
		return item;
	}
	
	private static String structureNode =
		"<DataNode TextLabel=\"%d\" GraphId=\"%d\" Type=\"Metabolite\">\n"+
	    "<Graphics CenterX=\"289.19315954771645\" CenterY=\"224.11050289701976\" Width=\"80.0\" Height=\"20.0\" ZOrder=\"32768\" FontSize=\"10\" Valign=\"Middle\" Color=\"0000ff\"/>\n"+
	    //"<Xref Database=\"PubChem-compound\" ID=\"3033\"/>\n"+
	    "<Xref Database=\"InChI\" ID=\"%s\"/>\n"+ 
	    "</DataNode>";
	
	private static String enzymeNode = 
		"<DataNode TextLabel=\"%s\" GraphId=\"%s\" Type=\"Protein\">\n"+
    	"<Graphics CenterX=\"378.781450992011\" CenterY=\"85.36340595214881\" Width=\"80.0\" Height=\"20.0\" ZOrder=\"32771\" FontSize=\"10\" Valign=\"Middle\"/>\n"+
    	"<Xref Database=\"Uniprot/TrEMBL\" ID=\"%s\"/>\n"+
    	"</DataNode>";
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("</Pathway>");
			
		} catch (Exception x) {}
	
	}
	
	@Override
	public String getFileExtension() {
		return "xml";
	}
}