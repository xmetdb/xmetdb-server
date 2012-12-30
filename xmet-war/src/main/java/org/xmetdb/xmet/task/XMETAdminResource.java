package org.xmetdb.xmet.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.CatalogFTLResource;
import org.xmetdb.rest.db.DatabaseResource;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.rest.xmet.admin.XmetdbCatalogHTMLReporter;
import org.xmetdb.xmet.client.Resources;


public class XMETAdminResource extends CatalogFTLResource<AdminItem> {
	public static final String resource = "admin";
	protected List<AdminItem> topics = new ArrayList<AdminItem>();
	public XMETAdminResource() {
		super();
		topics.clear();

		topics.add(new AdminItem("Database admin",String.format("%s/%s",resource,DatabaseResource.resource),"Create or inspect database"));
		topics.add(new AdminItem("Users","user?pagesize=10","Browse registered users"));
		topics.add(new AdminItem("Organisations","organisation?pagesize=10","Browse users affiliations"));
		topics.add(new AdminItem("System jobs","task?pagesize=10","Browse jobs status"));
		setHtmlbyTemplate(true);
	}
	
	@Override
	public String getTemplateName() {
		return "admin_body.ftl";
	}
	@Override
	protected Iterator<AdminItem> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		return topics.iterator();
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.protocol);
	}
	
	protected Reporter createHTMLReporter(boolean headles) {
		return new XmetdbCatalogHTMLReporter<AdminItem>(getRequest(),getDocumentation(),getHTMLBeauty(),"XMETDB Administration action") {
			@Override
			public String renderItemTitle(AdminItem item) {
				return String.format("%d.%s",record+1,item.getName());
			}
			@Override
			public String renderItem(AdminItem item) {
				String uri = super.getURI(item).trim();
				return(String.format("<a href='%s'>%s</a>", uri,item.getDescription()));
			}			
			@Override
			protected String printPageNavigator() {
				return "";
			}
		};
	}
}

class AdminItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1844938624688575591L;
	protected String name;
	protected String relativeUri;
	protected String description;
	
	public AdminItem(String name,String relativeURI,String description) {
		setName(name);
		setRelativeUri(relativeURI);
		setDescription(description);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRelativeUri() {
		return relativeUri;
	}
	public void setRelativeUri(String relativeUri) {
		this.relativeUri = relativeUri;
	}
	@Override
	public String toString() {
		return getRelativeUri();
	}
}