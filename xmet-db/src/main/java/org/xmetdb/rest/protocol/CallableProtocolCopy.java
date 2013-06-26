package org.xmetdb.rest.protocol;

import java.sql.Connection;
import java.sql.ResultSet;

import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.modbcum.p.QueryExecutor;
import net.idea.modbcum.p.UpdateExecutor;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.restnet.db.update.CallableDBUpdateTask;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.db.CreateProtocolCopy;
import org.xmetdb.rest.protocol.db.CreateStructuresCopy;
import org.xmetdb.rest.structure.db.CopyStructureSOM;
import org.xmetdb.rest.structure.db.PropertyValueBuffer;
import org.xmetdb.rest.structure.db.ReadStructureSOM;
import org.xmetdb.xmet.client.Resources;

public class CallableProtocolCopy extends CallableDBUpdateTask<DBProtocol,Form,String> {
	protected DBUser owner;
	public CallableProtocolCopy(Method method, Form input,String baseReference,DBUser user,
			Connection connection, String token) {
		super(method, input, baseReference, connection, token);
		this.owner = user;
	}


	@Override
	protected DBProtocol getTarget(Form input) throws Exception {
		String identifier = input.getFirstValue("source_uri");
		String prefix = String.format("%s%s/",baseReference, Resources.protocol);
		if ((identifier!=null) && identifier.startsWith(prefix)) {
			DBProtocol p = new DBProtocol();
			p.setIdentifier(identifier.substring(prefix.length()));
			return p;
		} else 
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Invalid value of 'observation_uri' form parameter.");
	}

	@Override
	protected IQueryUpdate<? extends Object, DBProtocol> createUpdate(DBProtocol target) throws Exception {
		CreateProtocolCopy q = new CreateProtocolCopy(owner,target);
		return q;
	}
	
	@Override
	protected Object executeQuery(IQueryUpdate<? extends Object, DBProtocol> q)
			throws Exception {
		String oldID = q.getObject().getIdentifier();

		PropertyValueBuffer pvSource = readSOM(q.getObject());
		Object result = super.executeQuery(q);
		//now copy the structures as well
		CreateStructuresCopy strucs = new CreateStructuresCopy(oldID, q.getObject());
		exec.process(strucs);
		PropertyValueBuffer pvTarget = new PropertyValueBuffer();
		pvTarget.put("idproperty",strucs.getNewpropertyid());
		CopyStructureSOM copySOM = new CopyStructureSOM(pvSource, pvTarget);
		exec.process(copySOM);
		return result;
	}

	private PropertyValueBuffer readSOM(DBProtocol protocol) throws Exception {
		PropertyValueBuffer pv = null;
		QueryExecutor qexec = new QueryExecutor();
		try {
			qexec.setCloseConnection(false);
			qexec.setConnection(connection);
			ReadStructureSOM som = new ReadStructureSOM(protocol, null);
			ResultSet rs = null;
			try { 
					rs = qexec.process(som); 
					while (rs.next()) { 
						pv = som.getObject(rs);
					}	
			} catch (Exception x) { if (rs!=null) rs.close(); }
			qexec.setConnection(null);
		}
		finally { try {qexec.close(); } catch (Exception x) {}}	
		return pv;
	}
	@Override
	protected String getURI(DBProtocol target) throws Exception {
		return String.format("%s%s/%s",baseReference, Resources.protocol,target.getIdentifier());
	}

}
