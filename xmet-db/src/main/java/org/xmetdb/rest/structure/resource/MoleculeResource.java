package org.xmetdb.rest.structure.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.idea.modbcum.i.reporter.Reporter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.attachments.DBAttachment;

/**
 * One molecule resource
 * @author nina
 *
 */
public class MoleculeResource extends StructureResource { 
	protected static String chemicalKey = "idchemical";
	protected static String structureKey = "idstructure";

	protected String tabID="Molecule";
	
	@Override
	protected Iterator<Structure> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		parseParameters(context,request,response);
		StructureHTMLBeauty parameters = ((StructureHTMLBeauty)getHTMLBeauty());
		
		StringBuilder url = new StringBuilder();
		url.append(queryService);
		
		Form form = request.getResourceRef().getQueryAsForm();
		List<DBAttachment> datasets = parameters.getDatasets();

		Object idchemical = request.getAttributes().get(chemicalKey);
		if (idchemical==null) return Collections.EMPTY_LIST.iterator();
		try { 
			int id = Integer.parseInt(idchemical.toString().trim());
			url.append(String.format("/%s/%d",OpenTox.URI.compound.name(),id));
			singleItem = true;
		} catch (Exception x) { return Collections.EMPTY_LIST.iterator(); }

		if (getRequest().getResourceRef().getLastSegment().equals("structure")) {
			url.append("/");
			url.append(OpenTox.URI.conformer.name());
			singleItem = false;
		} else {
			Object idstructure = request.getAttributes().get(structureKey);
			try { 
				int id = Integer.parseInt(idstructure.toString().trim());
				if (id>0)
					url.append(String.format("/%s/%d",OpenTox.URI.conformer.name(),id));
				singleItem = true;
			} catch (Exception x) { }
		}
		String query = String.format("%s/query/compound/url/all?search=%s",queryService,Reference.encode(url.toString()));
		if (datasets!=null)  {
			for (DBAttachment attachment: datasets) 
				query = String.format("%s?feature_uris[]=%s%s/%d%s",url,
						queryService,OpenTox.URI.dataset.getURI(),attachment.getIdquerydatabase(),OpenTox.URI.feature.getURI());
		}
		
		List<Structure> records = new ArrayList<Structure>();
		try {
			PropertiesIterator i = new PropertiesIterator(query);
			Reference queryURI = new Reference(queryService);
			try {
				while (i.hasNext()) {
					Structure struc = i.next();
					try {
						Object[] ids = struc.parseURI(queryURI);
						if (ids[0]!=null) struc.setIdchemical((Integer) ids[0]);
						if (ids[1]!=null) struc.setIdstructure((Integer) ids[1]);
					} catch (Exception x) {}					
					records.add(struc);
				}
			} catch (Throwable x) {
				throw createException(Status.SERVER_ERROR_BAD_GATEWAY, "Molecule", SearchMode.auto, url.toString(), x);					
			} finally {
				i.close();
			}
			return records.iterator();
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw createException(Status.CLIENT_ERROR_BAD_REQUEST, "Molecule", SearchMode.auto, url.toString(), x);			
		}
		
		
	}
	
	@Override
	protected Reporter createHTMLReporter(boolean headles) {
		return null;
	}
	


}
