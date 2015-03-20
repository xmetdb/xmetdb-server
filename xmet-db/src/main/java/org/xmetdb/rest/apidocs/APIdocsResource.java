package org.xmetdb.rest.apidocs;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CookieSetting;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.CatalogFTLResource;

public class APIdocsResource extends CatalogFTLResource {
	public static final String key1 = "key1";
	public static final String key2 = "key2";
	

	private enum apidoc {
		protocol,
		catalog,
		myaccount,  //myaccount/protocol
		editor,
		chemical //chemical?option=similarity&threshold=0.5&
	}
	public APIdocsResource() {
		super();
		setHtmlbyTemplate(true);
	}
	@Override
	public boolean isHtmlbyTemplate() {
		return true;
	}
	@Override
	protected Object createQuery(Context arg0, Request arg1, Response arg2)
			throws ResourceException {
		return null;
	}
	
	@Override
	public String getTemplateName() {
		Object k1 = getRequest().getAttributes().get(key1);
		Object k2 = getRequest().getAttributes().get(key2);
		apidoc page1 = null;
		apidoc page2 = null;
		try {
			page1 = apidoc.valueOf(k1.toString());
		} catch (Exception x) {
			return "apidocs/api.ftl";
		}		
		try {
			page2 = apidoc.valueOf(k2.toString());
		} catch (Exception x) {
		}
		return page1==null?"apidocs/api.ftl":
			   page2==null?String.format("apidocs/%s.ftl", page1.name()):
			   String.format("apidocs/%s_%s.ftl", page1.name(),page2.name());
	}
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
        getVariants().clear();
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        
	}
	
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		//setFrameOptions("SAMEORIGIN");
		CookieSetting cS = new CookieSetting(0, "xmetdb", getToken());
		cS.setPath("/");
        this.getResponse().getCookieSettings().add(cS);
        return getHTMLByTemplate(variant);
	}
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
	
			throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}	
	@Override
	protected Representation delete(Variant variant) throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation put(Representation representation, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
}