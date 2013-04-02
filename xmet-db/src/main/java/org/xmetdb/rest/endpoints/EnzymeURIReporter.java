package org.xmetdb.rest.endpoints;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.Reference;
import org.xmetdb.xmet.client.Resources;

/**
 * Generates uri of {@link PropertyResource}
 * @author nina
 *
 */
public class EnzymeURIReporter<D extends Enzyme> extends QueryURIReporter<D, IQueryRetrieval<D>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 711954935147788056L;

	public EnzymeURIReporter(Reference baseRef,ResourceDoc doc) {
		super(baseRef,doc);
	}
	public EnzymeURIReporter(Request ref,ResourceDoc doc) {
		super(ref,doc);
	}
	public EnzymeURIReporter() {
		this((Request)null,null);
	}
	@Override
	public String getURI(String ref, Enzyme record) {
		
		if (record==null) return null;
		
		return String.format("%s%s/%s",
			ref,
			Resources.enzyme,
			Reference.encode(record.getCode()),
			getDelimiter()
		);
		

	}

}
