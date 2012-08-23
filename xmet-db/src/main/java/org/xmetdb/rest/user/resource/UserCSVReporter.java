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

public class UserCSVReporter<Q extends IQueryRetrieval<DBUser>>  extends QueryReporter<DBUser,Q,Writer>  {
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
	protected UserCSVReporter(Reference baseRef) {
		this.baseReference = baseRef;
	}
	public UserCSVReporter(Request request) {
		this(request==null?null:request.getRootRef());
		setRequest(request);
	}	
	protected UserCSVReporter() {
	}	
	@Override
	public Object processItem(DBUser item) throws Exception {
		try {
			output.write(String.format("%s,\"%s\",\"%s\",%s,%s,\"%s\",%s\n",
					item.getTitle(),item.getFirstname(),item.getLastname(),item.getUserName()==null?"":item.getUserName(),
					item.getEmail()==null?"":item.getEmail(),item.getKeywords(),item.isReviewer()?"Yes":""
					));
			output.flush();
		} catch (IOException x) {
			Context.getCurrentLogger().severe(x.getMessage());
		}
		return null;
	}	

	public void footer(Writer output, Q query) {};
	public void header(Writer output, Q query) {
		try {
			output.write("Title,First name,Last name,user name,email,Keywords,Reviewer\n");
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
		return "csv";
	}
}