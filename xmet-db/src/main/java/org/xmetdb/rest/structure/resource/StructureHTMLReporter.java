package org.xmetdb.rest.structure.resource;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.xmetdb.rest.prediction.DBModel;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.structure.resource.StructureResource.SearchMode;
import org.xmetdb.rest.xmet.admin.XmetdbCatalogHTMLReporter;
import org.xmetdb.xmet.client.Resources;

@Deprecated
public class StructureHTMLReporter extends XmetdbCatalogHTMLReporter<Structure> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -244654733174669345L;
	
	public StructureHTMLReporter(Request request, ResourceDoc doc,
			HTMLBeauty htmlbeauty) {
		super(request, doc, htmlbeauty,null);
	}

	
	protected boolean printAsTable() {
		return false;
	}
	
	@Override
	public void processItem(Structure item, Writer output) {
		try {
			output.write(renderItem(item));
			record++;
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	
	public String renderItem(Structure item) {
		StringBuilder properties=null;
		try {
			Enumeration<String> keys = item.getProperties().keys();
			while (keys.hasMoreElements()) {
				if (properties==null) properties = new StringBuilder();
				String key = keys.nextElement();
				properties.append(String.format("<tr><th align='left' width='60%%'>%s</th><td align='center' width='40%%'>%s</td></tr>",key,item.getProperties().get(key)));

			}
		} catch (Exception x) {}
		

		String query = ((StructureHTMLBeauty)htmlBeauty).getSearchQuery();
		String smartsOption = ((StructureHTMLBeauty)htmlBeauty).getSmartsOption();
		//TODO smarts highlight

		
		String protocolURI = String.format(
				"<a href=\"%s%s?structure=%s&headless=true&details=false&media=text/html\" title=\"XMETDB observations\">XMETDB observations</a>",
				getRequest().getRootRef(),Resources.protocol,Reference.encode(item.getResourceIdentifier().toString()));
		
		String moleculeURI = String.format(
				"<a href=\"%s%s/%d?headless=true&details=false&media=text/html\" title=\"Molecule\">Molecule</a>",
				getRequest().getRootRef(),Resources.chemical,item.getIdchemical());
		
		StringBuilder rendering = new StringBuilder();
		rendering.append(String.format(
				"<div class='protocol'>\n"+					
				"<div class='tabs'>\n<ul>" +
				"<li>%s</li>\n" +
				"<li>%s</li>\n",
			    moleculeURI,protocolURI
				));
		

		if (htmlBeauty instanceof StructureHTMLBeauty) {
			rendering.append(renderAttachmentTab(item,((StructureHTMLBeauty)htmlBeauty).getAttachment()));
		
			if (((StructureHTMLBeauty)htmlBeauty).datasets !=null) {
			
				for (DBAttachment dataset: ((StructureHTMLBeauty)htmlBeauty).datasets)
					rendering.append(renderAttachmentTab(item,dataset));
			}
			if (((StructureHTMLBeauty)htmlBeauty).models !=null) {
				
				for (DBModel model: ((StructureHTMLBeauty)htmlBeauty).models)
					rendering.append(renderModelTab(item,model));
			}				
		}
	
		

		
		rendering.append("</ul></div>\n</div>\n");//tabs , protocol

		return rendering.toString();		

	}
	
	protected String renderAttachmentTab(Structure item, DBAttachment attachment) {
		if ((attachment!=null) && (attachment.getTitle()!=null)) {
			String datasetURI = String.format("%s%s/%d?dataset=A%d&headless=true&details=false&media=text/html", 
							getRequest().getRootRef(),Resources.chemical,item.getIdchemical(),attachment.getID());
			String uri = String.format(
					"<a href=\"%s\" title=\"%s\">%s&nbsp;%s</a>",
					datasetURI,attachment.getTitle(),attachment.getProtocol().getVisibleIdentifier(),attachment.getType().toString());
			return String.format("<li>%s<span></span></li>\n",uri);
		} else return "";
		
	}
	
	protected String renderModelTab(Structure item, DBModel model) {
		if (model!=null) {
			String datasetURI = String.format("%s%s/%d?model=M%d&headless=true&details=false&media=text/html", 
							getRequest().getRootRef(),Resources.chemical,item.getIdchemical(),model.getID());
			String uri = String.format(
					"<a href=\"%s\" title=\"%s\">%s</a>",
					datasetURI,model.getAlgorithm().getResourceIdentifier(),"Predictions");
			return String.format("<li>%s<span></span></li>\n",uri);
		} else return "";
		
	}
	
	@Override
	public void close() throws Exception {
		super.close();
	}
	@Override
	public HTMLBeauty getHtmlBeauty() {
		return htmlBeauty;
	}
	
	@Override
	public void footer(Writer output, Iterator<Structure> query) {
		try {
			if (printAsTable()) output.write("</table>\n");		

			if (record==(((XmetdbHTMLBeauty)htmlBeauty).getPage()*((XmetdbHTMLBeauty)htmlBeauty).getPageSize())) {
				if (((XmetdbHTMLBeauty)htmlBeauty).getSearchQuery()==null) {
					 output.write(((XmetdbHTMLBeauty)htmlBeauty).printWidget("You haven't specified a structure search query", "Please try the structure search menu."));
				} else  
					output.write(((XmetdbHTMLBeauty)htmlBeauty).printWidget(
							record==0?"Query returns no results":"No more results", 
							"Please try a different query"));
			}			
		} catch (Exception x) {}
		super.footer(output, query);
	}
	
	protected void printStructureDiagramEditor() throws Exception {
		
	}
	protected void printTableHeader(Writer output) throws Exception {
		output.write("<table width='90%'>\n");

		output.write("<tr bgcolor='FFFFFF' >\n");	
		output.write(String.format("<th width='50%%'>%s</th>","Chemical compound"));
		output.write(String.format("<th width='25%%'>%s</th>","Structure diagram"));
		output.write("</tr>\n");
		
	}
}


