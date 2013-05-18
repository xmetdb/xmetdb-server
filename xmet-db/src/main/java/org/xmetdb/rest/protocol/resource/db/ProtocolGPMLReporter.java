package org.xmetdb.rest.protocol.resource.db;

import java.io.Writer;
import java.util.List;

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
			String entryTitle = "GPMLExport"; // not sure how to get 'XMetDB1' 
			//This is a method to write a header.
			//to write an observation content , place the code in the processItem() method. Use getIdentifier() to retrieve XMETDB* id
			//There could be multiple observations serialized , and the header is common to all of them.
			//Not sure how this translates to GPML, so keeping it here for now
			output.write(String.format(gpml_pathway_start, entryTitle));
		} catch (Exception x) {}
		
	}
	
	
	private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private static String gpml_pathway_start = "<Pathway xmlns=\"http://pathvisio.org/GPML/2013a\" Name=\"%s\">";
	private static String gpml_comments = "<Comment Source=\"%s\">%s</Comment>";
	private static String gpml_graphics = "<Graphics BoardWidth=\"516.0\" BoardHeight=\"264.1\" />";
	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {
			List<String> comments = item.getKeywords();
			String uri = uriReporter.getURI(item);
			output.write(String.format(gpml_comments,"XMetDB Observation ID",item.getIdentifier()));
			output.write(String.format(gpml_comments,"XMetDB Observation URI",uri));
			output.write(String.format(gpml_comments,"XMetDB Reference",item.getReference()));
			output.write(String.format(gpml_comments,"XMetDB Experiment",String.format("%s (%s)",item.getAbstract(),item.getTitle())));
			output.write(String.format(gpml_comments,"XMetDB Product Ammount",item.getProductAmount()));
			output.write(String.format(gpml_comments,"XMetDB Comments",comments==null?"":comments.size()==0?"":comments.get(0)));
			output.write(String.format(gpml_comments,"XMetDB Status",item.isSearchable()?"Curated":"Not curated"));
			output.write(gpml_graphics);
			
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
			getOutput().write(
				String.format(structureNode, substrate.getIdchemical(), "d2e23", "125.0", "115.0", substrate.getInchi())
			);
			getOutput().write(String.format(enzymeNode,item.getEndpoint().getCode(),"af48a",item.getEndpoint().getUniprot_id()));
			getOutput().write(
				String.format(structureNode, product.getIdchemical(), "e427a", "285.0", "115.0", product.getInchi())
			);
			
			//infobox moved from the footer to the observation serialization
			output.write(gpml_interaction);
			output.write(gpml_infobox_start);

			/** TODO 
			 * below is the template, but if you have either a DOI or free text, it will probably not work well...
                            With a DOI you can actually get the fields, but that is too much to ask for now :)
                            See http://crosstech.crossref.org/2011/04/content_negotiation_for_crossr.html

			output.write("  <Biopax>");
			output.write("    <bp:PublicationXref xmlns:bp=\"http://www.biopax.org/release/biopax-level3.owl#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" rdf:id=\"ae3\">");
			output.write("      <bp:ID rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">17464936</bp:ID>");
			output.write("      <bp:DB rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">PubMed</bp:DB>");
			output.write("      <bp:TITLE rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">The emerging role of ACE2 in physiology and disease.</bp:TITLE>");
			output.write("      <bp:SOURCE rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">J Pathol</bp:SOURCE>");
			output.write("      <bp:YEAR rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">2007</bp:YEAR>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Hamming I</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Cooper ME</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Haagmans BL</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Hooper NM</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Korstanje R</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Osterhaus AD</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Timens W</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Turner AJ</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Navis G</bp:AUTHORS>");
			output.write("      <bp:AUTHORS rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">van Goor H</bp:AUTHORS>");
			output.write("    </bp:PublicationXref>");
			output.write("  </Biopax>");

			*/
			

		} catch (Exception x) {
			logger.error(x);
		}
		return item;
	}
	
	private static String structureNode =
		"<DataNode TextLabel=\"%d\" GraphId=\"%s\" Type=\"Metabolite\">\n"+
	    "<Graphics CenterX=\"%s\" CenterY=\"%s\" Width=\"80.0\" Height=\"20.0\" ZOrder=\"32768\" FontSize=\"10\" Valign=\"Middle\" Color=\"0000ff\"/>\n"+
	    //"<Xref Database=\"PubChem-compound\" ID=\"3033\"/>\n"+
	    "<Xref Database=\"InChI\" ID=\"%s\"/>\n"+ 
	    "</DataNode>";
	
	private static String enzymeNode = 
		"<DataNode TextLabel=\"%s\" GraphId=\"%s\" Type=\"Protein\">\n"+
    	"<Graphics CenterX=\"205.0\" CenterY=\"65.0\" Width=\"80.0\" Height=\"20.0\" ZOrder=\"32771\" FontSize=\"10\" Valign=\"Middle\"/>\n"+
    	"<Xref Database=\"Uniprot/TrEMBL\" ID=\"%s\"/>\n"+
    	"</DataNode>";

	private static String gpml_interaction = 
	"  <Interaction>" +
	"    <Graphics ZOrder=\"12288\" LineThickness=\"1.0\">" +
	"      <Point X=\"165.0\" Y=\"115.0\" GraphRef=\"d2e23\" RelX=\"1.0\" RelY=\"0.0\" />" +
	"      <Point X=\"245.0\" Y=\"115.0\" GraphRef=\"e427a\" RelX=\"-1.0\" RelY=\"0.0\" ArrowHead=\"Arrow\" />" +
	"      <Anchor Position=\"0.5\" Shape=\"None\" GraphId=\"baca7\" />" +
	"    </Graphics>" +
	"    <Xref Database=\"\" ID=\"\" />" +
	"  </Interaction>" +
	"  <Interaction>" +
	"    <Graphics ZOrder=\"12288\" LineThickness=\"1.0\">" +
	"      <Point X=\"205.0\" Y=\"75.0\" GraphRef=\"af48a\" RelX=\"0.0\" RelY=\"1.0\" />" +
	"      <Point X=\"205.0\" Y=\"115.0\" GraphRef=\"baca7\" RelX=\"0.0\" RelY=\"0.0\" ArrowHead=\"mim-catalysis\" />" +
	"    </Graphics>" +
	"    <Xref Database=\"\" ID=\"\" />" +
	"  </Interaction>";

	private static String gpml_infobox_start = "<InfoBox CenterX=\"0.0\" CenterY=\"0.0\" />";
	@Override
	public void footer(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("</Pathway>");
		} catch (Exception x) {
			logger.debug(x);
		} finally {
		}
		
	
	}
	
	@Override
	public String getFileExtension() {
		return "xml";
	}
}