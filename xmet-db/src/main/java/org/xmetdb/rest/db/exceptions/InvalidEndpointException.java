package org.xmetdb.rest.db.exceptions;

import net.idea.modbcum.i.exceptions.AmbitException;

public class InvalidEndpointException extends AmbitException {
	private final static String msg = "Invalid endpoint";
	/**
	 * 
	 */
	private static final long serialVersionUID = 9054812229642318416L;
	public InvalidEndpointException() {
		super(msg);
	}

	public InvalidEndpointException(String arg0) {
		super(arg0);
	}

	public InvalidEndpointException(Throwable arg0) {
		this(msg,arg0);
	}

	public InvalidEndpointException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}