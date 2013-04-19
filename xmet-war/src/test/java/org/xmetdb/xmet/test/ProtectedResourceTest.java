package org.xmetdb.xmet.test;

import java.io.InputStream;
import java.util.Properties;

import net.idea.restnet.aa.opensso.OpenSSOServicesConfig;
import net.idea.restnet.c.task.ClientResourceWrapper;
import net.idea.restnet.i.aa.IAuthToken;

import org.opentox.aa.opensso.OpenSSOToken;


public abstract class ProtectedResourceTest extends ResourceTest implements IAuthToken  {
	Properties properties = new Properties();
	protected String getCreator() {
			return "test";
	}

	protected boolean isAAEnabled() {
   		InputStream in = null;
		try {
			properties = new Properties();
			in = this.getClass().getClassLoader().getResourceAsStream("config/xmetdb.properties");
			properties.load(in);
			return Boolean.parseBoolean(properties.get("xmet.protected").toString());	
		} catch (Exception x) {
			try {in.close(); } catch (Exception xx) {}	
		}
		return false;
	}	
	@Override
	public void setUp() throws Exception {
		setUpAA();
		super.setUp();
	}
	
	public void setUpAA() throws Exception {
		if (isAAEnabled()) {
			//todo send a cookie
		}
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		try {
			ClientResourceWrapper.setTokenFactory(null);
			if (ssoToken!= null) ssoToken.logout();
		} catch (Exception x) {
		}
	}
	@Override
	public String getToken() {
		return ssoToken==null?null:ssoToken.getToken();
	}
	
	
}
