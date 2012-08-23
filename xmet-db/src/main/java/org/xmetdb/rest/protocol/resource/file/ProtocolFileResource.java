package org.xmetdb.rest.protocol.resource.file;

import java.io.File;
import java.util.Iterator;

import net.idea.modbcum.i.reporter.Reporter;
import net.toxbank.client.resource.Protocol;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.protocol.ProtocolHTMLReporter;
import org.xmetdb.rest.protocol.ProtocolURIReporter;
import org.xmetdb.rest.protocol.ProtocolsIterator;
import org.xmetdb.xmet.client.Resources;

public class ProtocolFileResource extends FileResource<Protocol> {
	
	public ProtocolFileResource() {
		super(Resources.protocol);
		try {
			directoryPrefix =  System.getProperty("java.io.tmpdir");

		} catch (Exception x) {
			x.printStackTrace();
		}
		File file = new File(String.format("%s%s", directoryPrefix,prefix));
		if (!file.exists()) file.mkdir();
	}
	@Override
	protected Reporter createURIReporter() {
		return
		new ProtocolURIReporter(getRequest(),getDocumentation());
	}
	@Override
	protected Reporter createHTMLReporter(boolean headles) {
		return new ProtocolHTMLReporter(getRequest(),getDocumentation());
	}
	@Override
	protected Iterator<Protocol> createQuery(Context context, Request request,
			Response response) throws ResourceException {
	
		
		final Object key = request.getAttributes().get(resourceKey);
		//System.out.println(key);
		File file = new File(String.format("%s%s", directoryPrefix,prefix));

		return new ProtocolsIterator(file,key==null?null:key.toString());
		
	}

	
}
