package org.xmetdb.rest.protocol;

import net.idea.restnet.c.AbstractResource;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.xmetdb.xmet.client.Resources;

public class UserHTMLBeauty extends XmetdbHTMLBeauty {
	
	public UserHTMLBeauty(String searchURI) {
		super(searchURI);
		setSearchTitle("Search for users and authors");
	}
	
	public UserHTMLBeauty() {
		this(Resources.user);
	}
	
	@Override
	protected String searchMenu(Reference baseReference, Form form)  {
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
		
		String hint = "Search for users by first or last name or by user account name.";
		
		final String searchMenuTemplate =
			"<div class='search ui-widget'>\n" +
			"<p title='%s'>%s</p>\n" +
			"<form method='GET' action='%s%s>\n" +
			"<table width='200px'>\n" +
			"<tr><td colspan='2'><input type='text' name='search' size='20' value='%s' tabindex='0' title='Search by first or last name'></td></tr>\n" +
			"<tr><td colspan='2'><input type='text' name='username' size='20' value='%s' tabindex='0' title='Search by user name'></td></tr>\n" +
			"<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n" +
			"<tr><td colspan='2' align='center'><input id='submit' type='submit' tabindex='4'  value='Search'/></td></tr>\n" +
			"</table>\n" +			   
			"</form> \n" +
			"&nbsp;\n" +
			"</div>\n";
		   
		return String.format(
			searchMenuTemplate,
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
