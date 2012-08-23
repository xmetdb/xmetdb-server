package org.xmetdb.rest.protocol.resource.db;

import net.idea.restnet.c.ChemicalMediaType;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.xmet.client.Resources;

public class ProtocolDocumentResource extends SingleProtocolResource {
	
	public ProtocolDocumentResource() {
		super(Resources.document);
	}
	
	@Override
	protected void customizeVariants(MediaType[] mimeTypes) {
		super.customizeVariants(mimeTypes);
		getVariants().add(new Variant(MediaType.APPLICATION_RTF));
		getVariants().add(new Variant(MediaType.APPLICATION_MSOFFICE_DOCX));
		getVariants().add(new Variant(MediaType.APPLICATION_WORD));
		getVariants().add(new Variant(MediaType.APPLICATION_OPENOFFICE_ODT));
		getVariants().add(new Variant(MediaType.APPLICATION_TEX));
		getVariants().add(new Variant(MediaType.APPLICATION_TAR));
		getVariants().add(new Variant(MediaType.APPLICATION_ZIP));
		getVariants().add(new Variant(MediaType.APPLICATION_ALL));
		getVariants().add(new Variant(ChemicalMediaType.CHEMICAL_MDLSDF));
		getVariants().add(new Variant(ChemicalMediaType.CHEMICAL_MDLMOL));
		getVariants().add(new Variant(ChemicalMediaType.CHEMICAL_SMILES));
		getVariants().add(new Variant(ChemicalMediaType.WEKA_ARFF));
		getVariants().add(new Variant(ChemicalMediaType.CHEMICAL_CML));
		getVariants().add(new Variant(MediaType.TEXT_CSV));
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}
	
	protected QueryHTMLReporter createHTMLReporter() throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
	}
	
	protected FileReporter createFileReporter() throws ResourceException {
		return new FileReporter();
	}
	
	
	
	@Override
	protected String getExtension(MediaType mediaType) {
		String ext = super.getExtension(mediaType);
		if (ext == null) {
			if (MediaType.APPLICATION_RTF.equals(mediaType))
				return ".rtf";
			if (MediaType.APPLICATION_WORD.equals(mediaType))
				return ".doc";
			if (MediaType.APPLICATION_EXCEL.equals(mediaType))
				return ".xls";			
			else if (MediaType.APPLICATION_GNU_TAR.equals(mediaType))
				return ".tar";
			else if (MediaType.APPLICATION_ZIP.equals(mediaType))
				return ".zip";
			else if (MediaType.APPLICATION_MSOFFICE_DOCX.equals(mediaType))
				return ".docx";	
			else if (MediaType.APPLICATION_MSOFFICE_PPTX.equals(mediaType))
				return ".pptx";
			else if (MediaType.APPLICATION_MSOFFICE_XLSX.equals(mediaType))
				return ".xslx";
			else if (MediaType.APPLICATION_OPENOFFICE_ODT.equals(mediaType))
				return ".odt";
			else if (MediaType.APPLICATION_LATEX.equals(mediaType))
				return ".tex";
			
			else return "";
		}
		return ext;
	}
}
