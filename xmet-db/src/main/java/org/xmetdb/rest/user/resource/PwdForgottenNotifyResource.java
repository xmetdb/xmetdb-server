package org.xmetdb.rest.user.resource;


public class PwdForgottenNotifyResource  extends XMETRegistrationNotifyResource {
	
	public PwdForgottenNotifyResource() {
		super();
	}

	@Override
	public String getTemplateName() {
		return "pwd_forgotten_notify.ftl";
	}

	
	
}