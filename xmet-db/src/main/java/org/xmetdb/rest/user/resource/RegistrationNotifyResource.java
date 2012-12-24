package org.xmetdb.rest.user.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.idea.restnet.i.task.ICallableTask;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.XmetdbHTMLReporter;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;

public class RegistrationNotifyResource  extends CatalogResource<String> {
	protected List<String> dummyuser = new ArrayList<String>();
	
	public RegistrationNotifyResource() {
		super();
		setHtmlbyTemplate(true);
		dummyuser.add(String.format("%s%s",Resources.register,Resources.notify));
	}
	
	@Override
	protected Iterator<String> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		return dummyuser.iterator();
	}

	@Override
	public String getTemplateName() {
		return "register_notify.ftl";
	}

	
	@Override
	public void configureTemplateMap(Map<String, Object> map) {
	
		map.put("searchURI",Resources.register);
		map.put("managerRole", "false");
		map.put("editorRole", "false");
		if (getClientInfo()!=null) {
			if (getClientInfo().getUser()!=null)
				map.put("username", getClientInfo().getUser().getIdentifier());
			if (getClientInfo().getRoles()!=null) {
				if (getClientInfo().getRoles().indexOf(XmetdbHTMLReporter.managerRole)>=0)
					map.put("managerRole", "true");
				if (getClientInfo().getRoles().indexOf(XmetdbHTMLReporter.editorRole)>=0)
					map.put("editorRole", "true");
			}
		}
		map.put("creator","Ideaconsult Ltd.");
	    map.put("xmet_root",getRequest().getRootRef());
	    map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name()));
        map.put(Resources.Config.xmet_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_email.name()));
        map.put(Resources.Config.xmet_about.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_about.name()));
        map.put(Resources.Config.xmet_guide.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_guide.name()));

	}
	
	@Override
	protected Reference getSourceReference(Form form, String model)
			throws ResourceException {
		return null;
	}
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation put(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation delete(Variant variant) throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected ICallableTask createCallable(Method method, Form form, String item)
			throws ResourceException {
		return null;
	}
	
	public String getConfigFile() {
		return "conf/xmetdb.pref";
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.notify);
	}
	
	
}
