package org.xmetdb.rest.structure.resource;

import java.io.Writer;
import java.util.Enumeration;

import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.data.Reference;
import org.xmetdb.rest.xmet.admin.XmetdbCatalogHTMLReporter;
import org.xmetdb.xmet.client.Resources;

public class MoleculeHTMLReporter extends XmetdbCatalogHTMLReporter<Structure> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1154097135680514626L;
	protected String tabID = "Molecule";
	public MoleculeHTMLReporter(Request request, ResourceDoc doc,
			HTMLBeauty htmlbeauty) {
		super(request, doc, htmlbeauty,null);
		
	}
	
	
	@Override
	protected String printPageNavigator() {
		if (singleItem) return "";
		return super.printPageNavigator();
	}
	protected boolean printAsTable() {
		return false;
	}
	
	@Override
	public void processItem(Structure item, Writer output) {
		try {
			if (headless) output.write(renderItem(item));
			else {
				output.write(htmlBeauty.printWidget(
						item.cas!=null && !"".equals(item.cas)?item.cas:
						item.name!=null && !"".equals(item.name)?item.name:
						item.InChIKey!=null && !"".equals(item.InChIKey)?item.InChIKey:"Molecule",
						renderItem(item)));

			}	
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
				properties.append(String.format("<tr><th>%s</th><td>%s</td></tr>",key,item.getProperties().get(key)));

			}
		} catch (Exception x) {}
		//TODO smarts highlight
		String structure = 	String.format(
					"<div class='structureright'><a href='%s%s%s%s%s%s%s' >" +
					"<img src='%s?media=%s&w=150&h=150' alt='%s' title='%s' width='150' height='150'></a><br>%s\n</div>\n",
					baseReference,
					Resources.chemical,
					item.idchemical>0?"/":"",
					item.idchemical>0?Integer.toString(item.idchemical):"",
					item.idstructure>0?Resources.structure:"",
					item.idstructure>0?"/":"",
					item.idstructure>0?Integer.toString(item.idstructure):"",
					item.getResourceIdentifier(),
					Reference.encode("image/png"),
					item.cas==null?"":item.cas,
					item.name==null?"":item.name,
					item.cas==null?"":item.cas
					);
		
		StringBuilder rendering = new StringBuilder();
		
			if (!headless) rendering.append("<div >");
			//identifiers
			rendering.append(String.format(
			"<div id='%s' style='min-height:200px'>"+
			"%s\n"+ //structure
			"<span class='summary'><table>\n"+ 
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th></th><td>%s</td></tr>"+
			"<tr><th></th><td>%s</td></tr>"+
			"</table></span>"+
			"</div>",
			tabID,
			structure,
			(item.cas==null)||"".equals(item.cas)?"":"CAS RN",
			item.cas==null?"":item.cas,
			(item.name==null)||"".equals(item.name)?"":"Name",
			item.name==null?"":item.name,
			(item.SMILES==null)||"".equals(item.SMILES)?"":"SMILES",
			item.SMILES==null?"":item.SMILES,
			(item.InChI==null)||"".equals(item.InChI)?"":"InChI",
			item.InChI==null?"":item.InChI,
			(item.InChIKey==null)||"".equals(item.InChIKey)?"":"InChI Key",
			item.InChIKey==null?"":item.InChIKey,
			properties==null?"":properties,
			item.getSimilarity()==null?"":item.getSimilarity()
			));
			/*
			if (properties!=null)
				rendering.append(String.format(
						"<div id='Properties' style='min-height:200px'>"+
						"%s\n"+ //structure
						"<span class='summary'><table>%s</table></span>\n"+
						"</div>",
						structure,
						properties
						));
			*/
			
		
				
			if (!headless) rendering.append("</div>");
			return rendering.toString();
	}
}
