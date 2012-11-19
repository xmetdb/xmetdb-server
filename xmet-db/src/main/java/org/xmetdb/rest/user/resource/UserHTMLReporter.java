package org.xmetdb.rest.user.resource;

import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;

import net.idea.modbcum.i.IQueryCondition;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.modbcum.p.MasterDetailsProcessor;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;
import net.toxbank.client.resource.Organisation;

import org.restlet.Request;
import org.xmetdb.rest.XmetdbHTMLReporter;
import org.xmetdb.rest.groups.DBOrganisation;
import org.xmetdb.rest.groups.db.ReadOrganisation;
import org.xmetdb.rest.groups.resource.GroupQueryURIReporter;
import org.xmetdb.rest.protocol.UserHTMLBeauty;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.xmet.client.Resources;

public class UserHTMLReporter extends XmetdbHTMLReporter<DBUser, IQueryRetrieval<DBUser>> {
	
	private static final long serialVersionUID = -7959033048710547839L;
	DBUser.fields[] entryFields = DBUser.fields.values();
	protected GroupQueryURIReporter groupURIReporter ;
	final static DBUser.fields[] displayFields = {
		DBUser.fields.email,
		DBUser.fields.username,
		DBUser.fields.homepage,
		DBUser.fields.keywords,
		DBUser.fields.reviewer
	};
	
	public UserHTMLReporter() {
		this(null, true, false, new UserHTMLBeauty());
	}
	
	public UserHTMLReporter(Request request, boolean collapsed, boolean editable, UserHTMLBeauty htmlBeauty) {
		super(request, collapsed, null, htmlBeauty);
		setTitle("User");
		getProcessors().clear();
		groupURIReporter = new GroupQueryURIReporter(request);
		IQueryRetrieval<DBOrganisation> queryO = new ReadOrganisation(new DBOrganisation()); 
		
		MasterDetailsProcessor<DBUser, DBOrganisation, IQueryCondition> orgReader = new MasterDetailsProcessor<DBUser, DBOrganisation, IQueryCondition>(queryO) {
			@Override
			protected DBUser processDetail(DBUser target, DBOrganisation detail) throws Exception {
				detail.setResourceURL(new URL(groupURIReporter.getURI(detail)));
				target.addOrganisation(detail);
				return target;
			}
		};
		
		getProcessors().add(orgReader);
		processors.add(new DefaultAmbitProcessor<DBUser, DBUser>() {
			public DBUser process(DBUser target) throws AmbitException {
				processItem(target);
				return target;
			};
		});				
	}
	
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new UserURIReporter<IQueryRetrieval<DBUser>>(request);
	}
	
	@Override
	protected boolean printAsTable() {
		return collapsed;
	}

	@Override
	protected void printPageNavigator(IQueryRetrieval<DBUser> query)
		throws Exception {
		// print nothing, DataTables have their own paging
	}
	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		if (printAsTable()) {
			output.write("<div style='float:left; width:100%; align:center'>\n");
			output.write("<table class='datatable' cellpadding='0' border='0' width='100%' cellspacing='0'>\n");
			//output.write("<caption><h3>Users</h3></caption>\n");	
			output.write("<thead>\n");	
			output.write(String.format("<th>%s</th>", "Name"));
			output.write(String.format("<th>%s</th>", "Affiliation"));
			output.write(String.format("<th>%s</th>", "E-mail"));
			output.write(String.format("<th>%s</th>", "Keywords"));
			output.write(String.format("<th>%s</th>", "Reviewer"));
			output.write("</thead>\n");
			output.write("<tbody>\n");
		}
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBUser> query) {
		try {
			if (printAsTable()) output.write("</tbody></table></div>\n");	
		} catch (Exception x) {}
		if (!headless)
			super.footer(output, query);
	}
	
	@Override
	protected void printForm(Writer output, String uri, DBUser user, boolean editable) {
		try {
			StringBuilder rendering = new StringBuilder();
			
			String protocolURI = String.format(
					"<a href=\"%s%s/U%d%s?headless=true&details=false&media=text/html\" title=\"XMETDB documents\">XMETDB documents</a>",
					uriReporter.getRequest().getRootRef(),
					Resources.user,
					user.getID(),
					Resources.protocol);
			
			String alertURI = String.format(
					"<a href=\"%s%s/U%d%s?headless=true&details=false&media=text/html\" title=\"alerts\">Saved searches</a>",
					uriReporter.getRequest().getRootRef(),
					Resources.user,
					user.getID(),
					Resources.alert);

			
			// tab headers
			final String tabHeaders =
				"<div class='protocol'>\n"+					
				"<div class='tabs'>\n"+
				"<ul>\n"+
				"<li><a href='#tabs-id'>%s</a></li>\n"+
				"<li>%s<span></span></li>\n"+
				"<li>%s<span></span></li>\n"+
				"</ul>\n";
			rendering.append(String.format(tabHeaders, "User Profile", protocolURI,alertURI));
				
			// identifiers
			rendering.append("<div id='tabs-id'><span class='summary'>");
			rendering.append("<table width='80%%'>");
			rendering.append(String.format("<tr><th colspan='2'><a href='%s%s/U%d'>%s&nbsp;%s&nbsp%s</a></th></tr>",
							uriReporter.getRequest().getRootRef(),Resources.user,user.getID(),
							user.getTitle().trim(),user.getFirstname().trim(),user.getLastname().trim()));
			
			if (user.getOrganisations()!=null)
			for (Organisation org : user.getOrganisations())
				rendering.append(String.format("<tr><th>%s</th><td align='left'>%s</td></tr>", "Affiliation", org.getTitle()));					
			
			for (DBUser.fields field : displayFields) 
					rendering.append(String.format("<tr><th>%s</th><td align='left'>%s</td></tr>", 
							field.toString(),
							field.getValue(user)==null?"":field.getValue(user)
					));
	
			rendering.append("</table>");
			rendering.append("</span></div>");
			
			rendering.append(String.format("<div id='QMRF_documents'>%s</div>",protocolURI));
			rendering.append(String.format("<div id='alerts'>%s</div>",alertURI));

			rendering.append("</div>\n</div>\n"); // tabs, protocol

			output.write(rendering.toString());
		} catch (Exception x) {x.printStackTrace();} 
	}	

	@Override
	protected void printTable(Writer output, String uri, DBUser user) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");	
			output.write(renderItem(user));			
			output.write("</tr>\n");
		} catch (Exception x) {
			x.printStackTrace();
		} 
	}
	
	public String renderItem(DBUser item) {
		StringBuilder rendering = new StringBuilder();

		rendering.append(String.format("<td>%s %s %s %s</td>",
			item.getTitle(),
			item.getFirstname(),
			item.getLastname(),
			item.getUserName()==null?"":
				String.format("<br>[<a href='%s%s/U%d'>%s</a>]",
					uriReporter.getBaseReference(),
					Resources.user,
					item.getID(),
					item.getUserName()
				)
		));
		
		rendering.append(String.format("<td>%s%s</td>",
			item.getOrganisations()==null?"":item.getOrganisations().get(0).getTitle(),
			item.getHomepage()==null?"":
				String.format("<br><a href='%s'>%s</a>",
					item.getHomepage(),
					item.getHomepage()
				)							
		));

		rendering.append(String.format("<td>%s</td>",
			item.getEmail()==null?"":
				String.format("<a href='mailto:%s'>%s</a>",URLEncoder.encode(item.getEmail()), item.getEmail())
		));
		rendering.append(String.format("<td>%s</td>", item.getKeywords()==null?"":item.getKeywords()));
		rendering.append(String.format("<td>%s</td>", item.isReviewer()?"Yes":""));
		
		return rendering.toString();
	}
}

