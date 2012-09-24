package org.xmetdb.rest.groups.resource;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.restnet.c.AbstractResource;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.xmetdb.rest.XmetdbHTMLReporter;
import org.xmetdb.rest.groups.DBGroup;
import org.xmetdb.rest.groups.IDBGroup;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.user.DBUser;

@Deprecated
public abstract class GroupHTMLReporter extends XmetdbHTMLReporter<IDBGroup, IQueryRetrieval<IDBGroup>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	protected DBUser user = null;
	
	protected boolean editable = false;
	
	public GroupHTMLReporter(Request baseRef, boolean collapsed,boolean editable,XmetdbHTMLBeauty htmlBeauty) {
		super(baseRef,collapsed,null,htmlBeauty);
		this.editable = editable;
	}
	
	
	public abstract String getBackLink();
	public abstract String getTitle();
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
	}
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		output.write("<div style='float:left;width:100%;align:center' >");
		output.write("<table class='datatable'  cellpadding='0' border='0' width='100%' cellspacing='0'>\n");
		if (!headless) {
			output.write("<thead>\n");	
			output.write(String.format("<th>%s</th>",DBGroup.fields.name.toString()));
			output.write("</thead>\n");
		}
		
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<IDBGroup> query) {
		try {
			if (printAsTable()) output.write("</table></div>\n");	
			if (htmlBeauty == null) htmlBeauty = new HTMLBeauty();
			htmlBeauty.writeHTMLFooter(output, "", uriReporter.getRequest());			

			output.flush();
		} catch (Exception x) {
			
		}
		
	}

	@Override
	protected void printPageNavigator(IQueryRetrieval<IDBGroup> query)
			throws Exception {

	}

	@Override
	public Object processItem(IDBGroup protocol) throws AmbitException  {
		try {
			String uri = uriReporter.getURI(protocol);
			if (collapsed) 
				printTable(output, uri,protocol);
			else  printTable(output, uri,protocol);// printForm(output,uri,protocol,false);
			record++;
		} catch (Exception x) {
			
		}
		return null;
	}
	
	protected void printForm(Writer output, String uri, IDBGroup group, boolean editable) {
		try {
			if (user!=null) {
				output.write("<tr>\n");	
				output.write(String.format("<th>%s URI</th><td align='left'><input name='%s_uri' value='' size='80'></td>\n",
									getTitle(),getTitle().toLowerCase()));
				output.write("</tr>\n");			
			} else
			for (DBGroup.fields field : DBGroup.fields.values()) {
				output.write("<tr>\n");	
				Object value = field.getValue(group);

				if (editable) {
					value = field.getHTMLField(group);
				} else 
					if (value==null) value = "";
							
				switch (field) {
				case idgroup: {
					if (!editable)
						output.write(String.format("<th>%s</th><td align='left'><a href='%s'>%s</a></td>\n",
							field.toString(),
							uri,
							uri));		
					break;
				}	
				default :
					output.write(String.format("<th>%s</th><td align='left'>%s</td>\n",
						field.toString(),value));
				}
							
				output.write("</tr>\n");				
			}
			output.flush();
		} catch (Exception x) {x.printStackTrace();} 
	}	
	
	protected void printTable(Writer output, String uri, IDBGroup item) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");		
			output.write(String.format("<td><a href='%s'>%s</a></td>",uri,item.getTitle()));
			output.write("</tr>\n");
		} catch (Exception x) {} 
	}

}


class GroupHTMLBeauty extends XmetdbHTMLBeauty {
	
	public GroupHTMLBeauty(String searchURI) {
		super(searchURI);
		setSearchTitle("Search for organisations");
		setSearchURI(searchURI);
	}
	@Override
	protected String searchMenu(Reference baseReference,Form form)  {
		String pageSize = "10";
		String userNameQuery = "";
		try {
			if ((form != null) && (form.size()>0)) {
				searchQuery = form.getFirstValue(AbstractResource.search_param)==null?"":form.getFirstValue(AbstractResource.search_param);
				pageSize = form.getFirstValue("pagesize")==null?"10":form.getFirstValue("pagesize");
				userNameQuery = form.getFirstValue("username")==null?"":form.getFirstValue("username");
			}
		} catch (Exception x) {
			searchQuery = "";
			pageSize = "10";
		}
		String hint = "Search for organisations by substring of name";
			return
		   String.format(		
		   "<div class='search ui-widget'>\n"+
		   "<p title='%s'>%s</p>\n"+
		   "<form method='GET' action='%s%s?pagesize=10'>\n"+
		   "<table width='200px'>\n"+
		   "<tr><td colspan='2'><input type='text' name='search' size='20' value='%s' tabindex='0' title='Search by first or last name'></td></tr>\n"+
		   "<tr><td colspan='2'><input type='text' name='ldapgroup' size='20' value='%s' tabindex='0' title='Search by group name'></td></tr>\n"+
		   "<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n"+
		   "<tr><td colspan='2' align='center'><input type='submit' id='submit' tabindex='4'  value='Search'/></td></tr>\n"+
		   "</table>\n"+			   
		   "</form> \n"+
		   "&nbsp;\n"+
		   "</div>\n",
		   hint,
		   getSearchTitle(),
		   baseReference,
		   getSearchURI(),
		   searchQuery==null?"":searchQuery,
		   userNameQuery==null?"":userNameQuery,				   
		   pageSize
		  
		   );
	}
}
