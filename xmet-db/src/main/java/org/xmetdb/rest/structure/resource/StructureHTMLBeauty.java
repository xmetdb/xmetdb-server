package org.xmetdb.rest.structure.resource;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.xmetdb.rest.prediction.DBModel;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.structure.resource.StructureResource.SearchMode;
import org.xmetdb.xmet.client.Resources;

public class StructureHTMLBeauty extends XmetdbHTMLBeauty {
	protected List<String> folder = new ArrayList<String>();
	public List<String> getFolder() {
		return folder;
	}

	public void setFolder(List<String> folder) {
		this.folder = folder;
	}
	protected String queryService;
	protected List<DBModel> models;
	protected boolean molFile = false;
	public boolean isMolFile() {
		return molFile;
	}

	public void setMolFile(boolean molFile) {
		this.molFile = molFile;
	}

	public List<DBModel> getModels() {
		return models;
	}

	public void setModels(List<DBModel> models) {
		this.models = models;
	}
	protected DBAttachment attachment = null;
	
    public DBAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(DBAttachment attachment) {
		this.attachment = attachment;
	}
	protected List<DBAttachment> datasets = null;

	public List<DBAttachment> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DBAttachment> datasets) {
		this.datasets = datasets;
	}
	protected Double threshold; 
	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}
	protected SearchMode option;

	public SearchMode getOption() {
		return option;
	}

	public void setOption(SearchMode option) {
		this.option = option;
	}

	public StructureHTMLBeauty(String queryService) {
		super(Resources.chemical);
		this.queryService = queryService;
	}

	@Override
	public String getSearchTitle() {
		return "Structure search";
		
	}
	
	public String getSmartsOption() {
		return SearchMode.smarts.equals(option)?String.format("&smarts=%s", Reference.encode(searchQuery)):"";
	}


	@Override
	protected String searchMenu(Reference baseReference, Form form) {
		
		
		searchTitle = attachment==null?getSearchTitle():
				String.format("<a href='%s%s/%s' title='XMetDB document'>%s</a>&nbsp;<a href='%s%s/%s%s/A%d' title='%s'>%s</a>",
							baseReference,Resources.protocol,attachment.getProtocol().getIdentifier(),attachment.getProtocol().getVisibleIdentifier(),
							baseReference,Resources.protocol,attachment.getProtocol().getIdentifier(),Resources.attachment,attachment.getID(),
							attachment.getDescription(),attachment.getType().toString());
		/*already set by the resource
		searchQuery = form.getFirstValue(QueryResource.search_param);
		pageSize = 10;
		try { pageSize = Long.parseLong(form.getFirstValue("pagesize")); if ((pageSize<1) && (pageSize>100)) pageSize=10;} catch (Exception x) { pageSize=10;}
		page = 0;
		try { page = Integer.parseInt(form.getFirstValue("page")); if ((page<0) || (page>100)) page=0;} catch (Exception x) { page=0;}
		threshold = form.getFirstValue("threshold");
		option = SearchMode.auto;
		try {
			option = SearchMode.valueOf(form.getFirstValue("option").toLowerCase());
		} catch (Exception x) {
			option = SearchMode.auto;
		}
		*/
		String hint = "Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.";
		
		String imgURI = searchQuery==null?"":
				String.format("<img title='%s' alt='%s' border='1' width='150' height='150' src='%s/depict/cdk/any?search=%s&media=%s&w=150&h=150' onError=\"hideDiv('querypic')\">",
						getSearchQuery(),getSearchQuery(),
						 queryService,Reference.encode(searchQuery),Reference.encode("image/png"));
			return
		   String.format(		
		   "<div class='search ui-widget'>\n"+
		   "<p title='%s'>%s</p>\n"+
		   "<form method='GET' name='form' action='%s%s'>\n"+
		   "\n"+
		   "<input type='hidden' name='page' value='%s'>\n"+
		   "<input type='hidden' name='type' value='smiles'>\n"+
		   "<table width='100%%'>\n"+
		   "<tr><td colspan='2' align='center'><input type='button' class='draw' tabindex='0' value='Draw (sub)structure' title='Launches structure diagram editor' onClick='startEditor(\"%s\",'form');'></td></tr>\n"+
		   "<tr><td colspan='2' align='center'><input type='text' name='search' size='20' value='%s' tabindex='1' title='%s'></td></tr>\n"+
		   "<tr><td colspan='2'><input %s type='radio' value='auto' name='option' title='Exact structure or search by identifier' size='20'>Auto</td></tr>\n"+
		   "<tr><td><input %s type='radio' name='option' value='similarity' title='Enter SMILES or draw structure'>Similarity</td>\n"+
		   "<td align='left'>\n"+
		   "<select title ='Tanimoto similarity threshold' name='threshold'><option value='0.9' checked>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5'>0.5</option></select>\n"+
		   "</td></tr>\n"+
		   "<tr><td colspan='2'><input %s type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' size='20'>Substructure</td></tr>\n"+
		   "<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n"+
		   "<tr><td colspan='2' align='center'><input tabindex='2' id='submit' type='submit' value='Search'/></td></tr>\n"+		   
		   "</div>\n"+
		   "</table>\n"+
		   "</form> \n"+
		   "&nbsp;\n"+
		   "<div id='querypic' class='structureright'>%s</div>"+
		   "</div>\n",	
		   hint,
		   searchTitle,
		   baseReference,
		   getSearchURI(),
		   page,
			//JME		   
		   baseReference,		   
		   //search
		   searchQuery==null?"":searchQuery,
		   hint,
		   SearchMode.auto.equals(option)?"checked":"",
		   SearchMode.similarity.equals(option)?"checked":"",
		   SearchMode.smarts.equals(option)?"checked":"",
		   pageSize,
		   imgURI
		   );
	}
	@Override
	public String getPaging(int page, int start, int last, long pageSize) {

		// Having a constant number of pages display on top is convenient for the users and provides more consistent
		// overall look. But this would require the function to define different input parameters. In order to not
		// break it, implement a workaround, by calculating how many pages the caller (likely) intended to be shown.
		int total = last - start;

		// Normalization
		start = start<0?0:start; // don't go beyond first page
		last = start + total;

		String url = "<li><a class='%s' href='?page=%d&pagesize=%d%s%s%s'>%s</a></li>";

		StringBuilder b = new StringBuilder(); 
		b.append("<div><ul id='hnavlist'>");

		// Disable this for the time being as it seems to not fit well into the overall look.
		//b.append(String.format("<li><a href='#'>Pages:</a></li>"));

		// Display "first" and "previous" for the first page as inactive.
		String thresholdq = threshold==null?"":String.format("&threshold=%3.2f",threshold);
		String optionq = option==null?"":String.format("&option=%s",option.name());
		String searchq = searchQuery==null?"":String.format("&search=%s",searchQuery);
		if (page > 0) {
			b.append(String.format(url, "pselectable", 0, pageSize,
					searchq, optionq, thresholdq, "&lt;&lt;"));
			b.append(String.format(url, "pselectable", page-1, pageSize,
					searchq, optionq,	thresholdq, "&lt;"));
		} else {
			b.append(String.format("<li class='inactive'>&lt;&lt;</li>"));
			b.append(String.format("<li class='inactive'>&lt;</li>"));
		}

		// Display links to pages. Pages are counted from zero! Hence why we display "i+1".
		for (int i=start; i<= last; i++)
			b.append(String.format(url, i==page?"current":"pselectable", i, pageSize,
					searchq, optionq, thresholdq, i+1)); 
		b.append(String.format(url, "pselectable", page+1, pageSize,
					searchq, optionq, thresholdq,"&gt;"));
		// b.append(String.format("<li><label name='pageSize' value='%d' size='4' title='Page size'></li>",pageSize));
		b.append("</ul></div><br>");

		// Apply style for the hovered buttons sans (!) the currently selected one.
		// There are better ways to do it, but this should be okay for now.
		b.append(String.format(
			"<script>\n" +

			"$('a.pselectable').mouseover(function () { $(this).addClass('phovered');    } );\n" +
			"$('a.pselectable').mouseout(function  () { $(this).removeClass('phovered'); } );\n" +

			"</script>\n"
		));

		return b.toString();

	} // getPaging()

}