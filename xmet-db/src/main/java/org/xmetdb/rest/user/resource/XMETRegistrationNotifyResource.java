package org.xmetdb.rest.user.resource;

import java.util.Map;

import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.user.resource.RegistrationNotifyResource;

import org.xmetdb.rest.DBRoles;
import org.xmetdb.rest.protocol.XmetdbHTMLBeauty;
import org.xmetdb.xmet.client.Resources;
import org.xmetdb.xmet.client.XMETDBRoles;

public class XMETRegistrationNotifyResource  extends RegistrationNotifyResource {
	
	public XMETRegistrationNotifyResource() {
		super();
	}

	@Override
	public String getTemplateName() {
		return "register_notify.ftl";
	}

	
	@Override
	public void configureTemplateMap(Map<String, Object> map) {
		map.put("searchURI",Resources.register);
		map.put(XMETDBRoles.xmetdb_admin.name(), Boolean.FALSE);
		map.put(XMETDBRoles.xmetdb_curator.name(), Boolean.FALSE);
		if (getClientInfo()!=null) {
			if (getClientInfo().getUser()!=null)
				map.put("username", getClientInfo().getUser().getIdentifier());
			if (getClientInfo().getRoles()!=null) {
				if (DBRoles.isAdmin(getClientInfo().getRoles()))
					map.put(XMETDBRoles.xmetdb_admin.name(), Boolean.TRUE);
				if (DBRoles.isCurator(getClientInfo().getRoles()))
					map.put(XMETDBRoles.xmetdb_curator.name(), Boolean.TRUE);
				if (DBRoles.isUser(getClientInfo().getRoles()))
					map.put(XMETDBRoles.xmetdb_user.name(), Boolean.TRUE);				
			}
		}
		map.put("creator","Ideaconsult Ltd.");
	    map.put("xmet_root",getRequest().getRootRef());
	    map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_ambit_service.name()));
        map.put(Resources.Config.xmet_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_email.name()));
        map.put(Resources.Config.xmet_about.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_about.name()));
        map.put(Resources.Config.xmet_guide.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.xmet_guide.name()));

	}
	
	public String getConfigFile() {
		return "conf/xmetdb.pref";
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new XmetdbHTMLBeauty(Resources.notify);
	}
	
	
}
