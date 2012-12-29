package org.xmetdb.rest.groups.resource;

import net.idea.restnet.c.AbstractResource;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;

public class GroupHTMLBeauty extends XmetdbHTMLBeauty {
	
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