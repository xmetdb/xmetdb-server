package org.xmetdb.rest.db.exceptions;

import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class MethodNotAllowedException extends ResourceException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 233210091955805120L;

	public MethodNotAllowedException(Reference uri, Method method) {
		super(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED.getCode(),
				String.format("Method %s not allowed",method),
				String.format("Method %s not allowed to %s", method.getName(),uri),
				null
				);
	}
}
