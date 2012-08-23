package org.xmetdb.rest.protocol;

import java.io.File;
import java.io.FileFilter;

import org.xmetdb.rest.FilesIterator;

import net.toxbank.client.resource.Protocol;


public class ProtocolsIterator extends FilesIterator<Protocol> {
	protected String key;
	public ProtocolsIterator(File directory, final String key) {
		super(directory, new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if (key==null)
					return pathname.isDirectory();
				else return key.toString().equals(pathname.getName()) && pathname.isDirectory();
			}
		});
		this.key = key;
	}

	@Override
	protected Protocol convert(File directory) {
		Protocol protocol = new Protocol();
		protocol.setIdentifier(directory.getName());
		if (key!=null) {
			
		}
		return protocol;
	}

}
