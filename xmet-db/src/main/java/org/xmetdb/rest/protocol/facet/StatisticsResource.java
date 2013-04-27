package org.xmetdb.rest.protocol.facet;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.restnet.db.convertors.OutputWriterConvertor;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.facet.TableCountFacetQuery.RESOURCES;

public class StatisticsResource extends XMETFacetResource {
	
	public StatisticsResource() {
		super();
	}
	@Override
	public String getTemplateName() {
		return "facet_body.ftl";
	}
	
	@Override
	protected IQueryRetrieval<IFacet<String>> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		RESOURCES term = RESOURCES.enzymes;
		if (form.getFirstValue("term")!=null)
		try {
			term = RESOURCES.valueOf(form.getFirstValue("term").toString());
		} catch (Exception x) {	x.printStackTrace();}
		return new TableCountFacetQuery(getResourceRef(getRequest()).toString(),term);
	}
	@Override
	public IProcessor<IQueryRetrieval<IFacet<String>>, Representation> createConvertor(
			Variant variant) throws AmbitException, ResourceException {

			if (variant.getMediaType().equals(MediaType.TEXT_PLAIN)) {
				return new OutputWriterConvertor(
						new FacetTXTReporter(getRequest()),
						MediaType.TEXT_PLAIN);
			} else return super.createConvertor(variant);
	}
	
}