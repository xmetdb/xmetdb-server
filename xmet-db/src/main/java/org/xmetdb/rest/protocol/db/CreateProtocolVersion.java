package org.xmetdb.rest.protocol.db;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;

import net.idea.modbcum.i.IStoredProcStatement;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;


public class CreateProtocolVersion  extends AbstractUpdate<String,DBProtocol> implements IStoredProcStatement {
	protected static final ReadProtocol.fields[] f = new ReadProtocol.fields[] {
			ReadProtocol.fields.idprotocol,
			ReadProtocol.fields.version,
			ReadProtocol.fields.title,
			ReadProtocol.fields.anabstract,
		//	ReadProtocol.fields.filename
	};
	protected String[] create_sql = {"{CALL createProtocolVersion(?,?,?,?,?)}"};

	public CreateProtocolVersion(String newIdentifier,DBProtocol ref) {
		super(ref);
		setGroup(newIdentifier);
	}
	public CreateProtocolVersion() {
		this(DBProtocol.generateIdentifier(),null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getGroup()==null) throw new AmbitException("The new XMetDB number is not specified!");
		if (!getObject().isValidIdentifier()) throw new AmbitException("No protocol ID");
		
		
		List<QueryParam> params1 = new ArrayList<QueryParam>();

		params1.add(ReadProtocol.fields.identifier.getParam(getObject()));
		params1.add(new QueryParam<String>(String.class,getGroup()));
		params1.add(ReadProtocol.fields.title.getParam(getObject()));
		params1.add(ReadProtocol.fields.anabstract.getParam(getObject()));
		
		params1.add(new QueryParam<Integer>(Integer.class, -1));
		
		return params1;
		
	}

	public String[] getSQL() throws AmbitException {
		return create_sql;
	}
	public void setID(int index, int id) {
		getObject().setID(id);
	}
	@Override
	public boolean returnKeys(int index) {
		return false;
	}
	
	@Override
	public boolean isStoredProcedure() {
		return true;
	}
	
	/**
	 * Allows retrieving stored procedure output parameters; 
	 * Does nothing by default
	 */
	@Override
	public void getStoredProcedureOutVars(CallableStatement statement) throws SQLException {
		int version = statement.getInt(f.length+1);
		getObject().setVersion(version);
	}
}
