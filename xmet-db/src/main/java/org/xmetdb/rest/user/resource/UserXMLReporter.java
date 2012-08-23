package org.xmetdb.rest.user.resource;

import java.io.IOException;
import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.DbAmbitException;
import net.idea.modbcum.r.QueryReporter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.Reference;
import org.xmetdb.rest.user.DBUser;

/**
 *   <authors_catalog>
    	
 * @author nina
 *
 * @param <Q>
 */
public class UserXMLReporter <Q extends IQueryRetrieval<DBUser>>  extends QueryReporter<DBUser,Q,Writer>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4566136103208284105L;

	protected Request request;
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	protected Reference baseReference;
	public Reference getBaseReference() {
		return baseReference;
	}
	protected UserXMLReporter(Reference baseRef) {
		this.baseReference = baseRef;
	}
	public UserXMLReporter(Request request) {
		this(request==null?null:request.getRootRef());
		setRequest(request);
	}	
	protected UserXMLReporter() {
	}	
	@Override
	public Object processItem(DBUser item) throws Exception {
		try {
			output.write(String.format("<author id='id%d' name='%s %s %s' affiliation='%s' contact='%s' url='%s' email='%s' number='%d'/>\n",
					item.getID(),
					item.getTitle(),item.getFirstname(),item.getLastname(),
					"","","",
					item.getEmail()==null?"":item.getEmail(),
					item.getID()
					));
			output.flush();
		} catch (IOException x) {
			Context.getCurrentLogger().severe(x.getMessage());
		}
		return null;
	}	

	public void footer(Writer output, Q query) {
		try {
			output.write("</authors_catalog>\n");
		} catch (Exception x) {
			
		}		
	};
	public void header(Writer output, Q query) {
		try {
			output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			output.write("<authors_catalog>\n");
		} catch (Exception x) {
			
		}
	};
	
	public void open() throws DbAmbitException {
		
	}
	@Override
	public void close() throws Exception {
		setRequest(null);
		super.close();
	}
	@Override
	public String getFileExtension() {
		return "xml";
	}
}