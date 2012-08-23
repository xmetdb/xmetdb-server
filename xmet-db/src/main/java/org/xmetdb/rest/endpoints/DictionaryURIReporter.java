package org.xmetdb.rest.endpoints;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.Reference;

import ambit2.base.data.Dictionary;

/**
 * Generates uri of {@link PropertyResource}
 * @author nina
 *
 */
public class DictionaryURIReporter<D extends Dictionary> extends QueryURIReporter<D, IQueryRetrieval<D>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 711954935147788056L;

	public DictionaryURIReporter(Reference baseRef,ResourceDoc doc) {
		super(baseRef,doc);
	}
	public DictionaryURIReporter(Request ref,ResourceDoc doc) {
		super(ref,doc);
	}
	public DictionaryURIReporter() {
		this((Request)null,null);
	}
	@Override
	public String getURI(String ref, Dictionary record) {
		
		if (record==null) return null;
		
		return String.format("%s%s/%s/%s%s",
			ref,
			EndpointsResource.resource,
			record.getReference()==null?"All":
			Reference.encode(record.getTitle().replace("/", "_")),
			Reference.encode(record.getName().replace("/", "_")),
			getDelimiter()
		);
		

	}

}
