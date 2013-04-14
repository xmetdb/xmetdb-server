package org.xmetdb.rest.user.resource;

public class PwdForgottenFailedResource extends XMETRegistrationNotifyResource {

	public PwdForgottenFailedResource() {
		super();
	}

	@Override
	public String getTemplateName() {
		return "pwd_forgotten_failed.ftl";
	}

	
}
