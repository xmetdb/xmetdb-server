package org.xmetdb.rest.db.exceptions;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 * Invalid XMETDB number exception
 * @author nina
 *
 */
public class InvalidXmetdbNumberException extends ResourceException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7218438568085514503L;

	public InvalidXmetdbNumberException(String xmetdbnumber) {
		super(Status.CLIENT_ERROR_BAD_REQUEST.getCode(),
			String.format("Invalid XMETDB number %s",xmetdbnumber),
			String.format("The XMETDB number <i>%s</i> is not valid. The XMETDB numbers should have format XMETDB-??????",xmetdbnumber),
			null
			);
	}
}
