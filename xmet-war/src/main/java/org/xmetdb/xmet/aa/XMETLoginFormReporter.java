package org.xmetdb.xmet.aa;

import java.io.StringWriter;
import java.io.Writer;

import net.idea.restnet.aa.local.UserLoginHTMLReporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.data.Reference;
import org.restlet.security.Role;
import org.restlet.security.User;
import org.xmetdb.xmet.client.XMETDBRoles;

public class XMETLoginFormReporter<U extends User> extends UserLoginHTMLReporter<U> {
	
    public XMETLoginFormReporter(Request ref, ResourceDoc doc) {
    	this(ref,doc,null);
    }
	public XMETLoginFormReporter(Request ref, ResourceDoc doc,HTMLBeauty htmlBeauty) {
		super(ref,doc,htmlBeauty);
	}
	
	public void processItem(U item, Writer output) {
		
		try {
			String redirect = Reference.encode(String.format("%s/login",baseReference));
			String header = "";
			StringWriter writer = new StringWriter();
			writer.write("<table width='80%' id='users' border='0' cellpadding='0' cellspacing='1'>");
			writer.write("<tbody>");			
			if (item.getIdentifier()==null) {
				header = "Sign In";
	
				writer.write(String.format("<form method='post' action='%s/protected/signin?targetUri=%s'>",baseReference,redirect));
					
				writer.write(String.format("<tr><th align='right'>%s</th><td><input type='text' size='40' name='%s' value=''></td></tr>",
							"User name:&nbsp;","login"));
				writer.write(String.format("<tr><th align='right'>%s</th><td><input type='password' size='40' name='%s' value=''></td></tr>",
						"Password:&nbsp;","password"));
				writer.write("<tr><td></td><td><input align='bottom' type=\"submit\" value=\"Log in\"></td></tr>");
				//writer.write(String.format("<tr><th align='right'></th><td><input type='hidden' size='40' name='targetURI' value='%s/login'></td></tr>",baseReference));
				
				writer.write("</form>");

			} else {
				header = "&nbsp;";//String.format("Welcome, %s",item.getIdentifier());
				writer.write(String.format("<form method='post' action='%s/protected/signout?targetUri=%s'>",baseReference,redirect));
				writer.write(String.format("<tr><td width='25%%' align='right'>%s</td><th align='left'>%s</th></tr>","You are logged in as&nbsp;",item.getIdentifier()));
				
				for (Role role:getRequest().getClientInfo().getRoles()) {
					XMETDBRoles xmetdbrole = XMETDBRoles.valueOf(role.getName());
					if (xmetdbrole.getURI()==null) continue;
					writer.write(String.format("<tr><td align='right'>%s</td><th>%s&nbsp;(%s)</th></tr>","Role:&nbsp;",xmetdbrole.toString(),xmetdbrole.getHint()));
				}
				writer.write("<tr><td align='right'></td><td align='left'><input align='bottom' type=\"submit\" value=\"Log out\"></td></tr>");
				writer.write("</form>");
				
				writer.write("</tbody></table>");
				
		     }
			writer.write("</tbody></table>");
			output.write(htmlBeauty.printWidget(header, writer.toString()));
		} catch (Exception x) {
			x.printStackTrace();
		}
		
	};
	
	protected String myWorkspaceLinks() {
		return null;
	}	
}
