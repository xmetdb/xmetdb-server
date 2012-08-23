package org.xmetdb.rest.protocol.resource.db;

import java.io.File;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.r.QueryReporter;
import net.idea.restnet.db.convertors.AbstractObjectConvertor;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.xmetdb.rest.protocol.attachments.DBAttachment;

public class DownloadDocumentConvertor extends   AbstractObjectConvertor<DBAttachment, IQueryRetrieval<DBAttachment>,FileRepresentation> {

	public DownloadDocumentConvertor(
			QueryReporter<DBAttachment, IQueryRetrieval<DBAttachment>, FileRepresentation> reporter,MediaType media,String fileNamePrefix) {
		super(reporter,media,fileNamePrefix);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1979008352251532084L;

	protected File getFile(DBAttachment item) throws Exception  {
		return new File(item.getResourceURL().toURI());
	}

	public Representation process(DBAttachment item) throws AmbitException {
		try {
			if (item==null) throw new AmbitException("No attachment!");

			File file = getFile(item);
			if (!file.exists()) throw new AmbitException("No file!");
			return new FileRepresentation(file, MediaType.APPLICATION_PDF);
			
		} catch (AmbitException x) {
			throw x;
		} catch (Exception x) {
			throw new AmbitException(x);
		}

	}
	
	@Override
	protected FileRepresentation createOutput(IQueryRetrieval<DBAttachment> query)
			throws AmbitException {
		return null;
	}
	@Override
	public Representation process(FileRepresentation doc) throws AmbitException {
		return doc;
	}

}
