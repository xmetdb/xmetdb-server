package org.xmetdb.rest.protocol.resource.db;

import java.io.Writer;
import java.text.SimpleDateFormat;

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
import org.xmetdb.rest.endpoints.Enzyme.EnzymeFields;
import org.xmetdb.rest.endpoints.db.ReadEnzymeByObservation;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachmentStructures;

import ambit2.base.interfaces.IStructureRecord;

public class ProtocolSDFReporter extends QueryReporter<DBProtocol, IQueryRetrieval<DBProtocol>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected MediaType mediaType;
	protected String queryService;
	protected QueryURIReporter uriReporter;
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
	
	public QueryURIReporter getUriReporter() {
		return uriReporter;
	}

	public ProtocolSDFReporter(Request request,MediaType mediaType,String queryService) {
		super();
		this.mediaType = mediaType;
		this.queryService = queryService;
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
		queryP.setRetrieveStructure(true);
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
	}
	
	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {
			String uri = uriReporter.getURI(item);
		
			IStructureRecord substrate = null;
			IStructureRecord product = null;
			String substrateSOM = null;
			String productSOM = null;
			for (DBAttachment attachment: item.getAttachments()) {
				switch (attachment.getType()) {
				case substrate: {
					substrate = attachment.getStructure();
					substrateSOM = attachment.getSom();
					break;				
				}
				case product: {
					product = attachment.getStructure();
					productSOM = attachment.getSom();
					break;
				}
				}
			}
			if (substrate!=null) {
				getOutput().write(substrate.getContent());
				writeProperty("XMETDB Observation",item.getVisibleIdentifier());
				writeProperty("XMETDB Observation URI",uri);
				writeProperty("XMETDB Status",item.isSearchable()?"Curated":"Not curated");
				writeProperty("XMETDB Entry","Substrate");
				writeProperty("XMETDB Experiment type",item.getAbstract());
				writeProperty("XMETDB Enzyme",String.format("%s\n%s\n%s",
							item.getEndpoint().getCode(),item.getEndpoint().getName(),item.getEndpoint().getUniprot_id()));
				writeProperty("XMETDB Alleles",EnzymeFields.alleles.getStringValue(item.getEndpoint(),""));
				writeProperty("XMETDB Reference",item.getReference());
				writeProperty("XMETDB Comments",item.getKeywords().toString());
				writeProperty("XMETDB Atoms",substrateSOM==null?"":substrateSOM);
				writeProperty("XMETDB Atoms Uncertainty",item.getAtomUncertainty().toString());
				getOutput().write("\n$$$$\n");
			}	
			if (product!=null) {
				getOutput().write(product.getContent());
				writeProperty("XMETDB Observation",item.getVisibleIdentifier());
				writeProperty("XMETDB Observation URI",uri);
				writeProperty("XMETDB Entry","Product");
				writeProperty("XMETDB Product amount",item.getProductAmount().toString());
				writeProperty("XMETDB Atoms",productSOM==null?"":productSOM);
				writeProperty("XMETDB Atoms Uncertainty",item.getAtomUncertainty().toString());
				getOutput().write("\n$$$$\n");
			}	
			
		} catch (Exception x) {
			x.printStackTrace();
		}
		return item;
	}
	protected void writeProperties(DBProtocol item) throws Exception {
		writeProperty("XMETDB Observation",item.getVisibleIdentifier());
		writeProperty("XMETDB Entry",item.getVisibleIdentifier());
	}
	
	protected void writeProperty(String name,String value) throws Exception {
			getOutput().write(String.format("\n> <%s>\n%s\n",name,value));
	}
	@Override
	public void footer(Writer output, IQueryRetrieval<DBProtocol> query) {
	}
	
	@Override
	public String getFileExtension() {
		return "sdf";
	}
}