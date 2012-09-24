package org.xmetdb.rest.user.alerts.resource;

import java.io.Writer;
import java.sql.Date;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.xmetdb.rest.XmetdbHTMLReporter;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.alerts.db.DBAlert;
import org.xmetdb.xmet.client.Resources;

@Deprecated
public class AlertHTMLReporter extends XmetdbHTMLReporter<DBAlert, IQueryRetrieval<DBAlert>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	DBUser.fields[] entryFields = DBUser.fields.values();
	DBUser.fields[] displayFields = DBUser.fields.values();
	
	protected boolean editable = false;
	public AlertHTMLReporter() {
		this(null,true,false,new XmetdbHTMLBeauty(Resources.alert));
	}
	public AlertHTMLReporter(Request baseRef, boolean collapsed,boolean editable, HTMLBeauty htmlBeauty) {
		super(baseRef,collapsed,null,htmlBeauty);
		this.editable = editable;
		
	}
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new AlertURIReporter<IQueryRetrieval<DBAlert>>(request);
	}
	
	
	
	@Override
	protected boolean printAsTable() {
		return !collapsed;
	}
	@Override
	protected void printPageNavigator(IQueryRetrieval<DBAlert> query)
			throws Exception {
	
	}
	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		if (printAsTable()) {
			output.write("<div style='float:left; width:100%; align:center'>\n");
			output.write("<table class='datatable' cellpadding='0' border='0' width='100%' cellspacing='0'>\n");
			//output.write("<caption><h3>Users</h3></caption>\n");	
			output.write("<thead>\n");	
			output.write(String.format("<th>%s</th>", "Query"));
			output.write(String.format("<th>%s</th>", "Saved on"));
			output.write(String.format("<th>%s</th>", "Alert frequency"));
			output.write(String.format("<th>%s</th>", "Search last sent on"));
			output.write(String.format("<th>%s</th>", "Action"));
			output.write("</thead>\n");
			output.write("<tbody>\n");
		} 
	}
	@Override
	protected void printTable(Writer output, String uri, DBAlert alert) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");	
			output.write(renderItem(alert));			
			output.write("</tr>\n");
		} catch (Exception x) {
			x.printStackTrace();
		} 
	}
	
	public String renderItem(DBAlert alert) {
		StringBuilder rendering = new StringBuilder();

		rendering.append(String.format("<td>%s</td>",alert.getVisibleQuery()));
		rendering.append(String.format("<td>%s</td>",new Date(alert.getCreated())));
		rendering.append(String.format("<td>%s</td>",alert.getRecurrenceFrequency()));
		rendering.append(String.format("<td>%s</td>",alert.getSentAt()==0?"Never":new Date(alert.getSentAt())));
		rendering.append(String.format("<td>%s&nbsp;%s</td>",getRunLink(alert),getDeleteLink(alert)));
		return rendering.toString();
	}
	protected String getRunLink(DBAlert alert) {
		return String.format("<a href='%s%s'>Run search</a>",
					uriReporter.getBaseReference(),alert.getRunnableQuery());
	}
	protected String getDeleteLink(DBAlert alert) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<form action='%s%s%s/A%d?method=DELETE' method='POST'>");
		stringBuilder.append("<input type='hidden' name='username' value='%s'>");
		stringBuilder.append("<input title='Delete this alert' class='draw'");
		stringBuilder.append("type='image' src='%s/images/script_delete.png' value='Delete'>");
		stringBuilder.append("</form>");
		
		return String.format(stringBuilder.toString(), 
				uriReporter.getBaseReference(),
				Resources.myaccount,
				Resources.alert,
				alert.getID(),
				uriReporter.getRequest().getClientInfo().getUser().getIdentifier(),
				uriReporter.getBaseReference());
	}	
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBAlert> query) {
		try {
			if (printAsTable()) output.write("</tbody></table></div>\n");	
		} catch (Exception x) {}
		try {
		if (!headless) {
			if (htmlBeauty == null) htmlBeauty = new HTMLBeauty();
			htmlBeauty.writeHTMLFooter(output, "", uriReporter.getRequest());			

			output.flush();			
		}
		} catch (Exception x) {}
	}
	
	protected void printForm(Writer output, String uri, DBAlert alert, boolean editable) {
		try {
			output.write(
			getHtmlBeauty().printWidget(alert.getVisibleQuery(), 
					String.format("<table width='100%%'><tr><th width='25%%'>Saved on</th><td align='left' width='25%%'>%s</td><td align='right'>%s</td></tr><tr><th>Alert frequency</th><td >%s</td><td align='right'>%s</td></tr><tr><th>Search last sent on</th><td>%s</td></tr></table>",
							new Date(alert.getCreated()),
							getRunLink(alert),
							alert.getRecurrenceFrequency(),
							getDeleteLink(alert),
							alert.getSentAt()==0?"Never":new Date(alert.getSentAt())

							)));
			output.flush();
		} catch (Exception x) {x.printStackTrace();} 
	}	
	

	@Override
	protected HTMLBeauty createHTMLBeauty() {
		return new XmetdbHTMLBeauty(org.xmetdb.xmet.client.Resources.alert);
	}
}
