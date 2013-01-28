package org.xmetdb.rest.endpoints.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;

import org.xmetdb.rest.endpoints.Enzyme;

public class CreateEndpoint extends AbstractObjectUpdate<Enzyme>{
	private static final String[] sql = {"insert into template (name,code,uri,allele) values (?,?,?,?)"};
	public CreateEndpoint(Enzyme enzyme) {
		setObject(enzyme);
	}
	@Override
	public String[] getSQL() throws AmbitException {
		return sql;
	}

	@Override
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params1 = new ArrayList<QueryParam>();
		params1.add(new QueryParam<String>(String.class, getObject().getName()));
		params1.add(new QueryParam<String>(String.class,  getObject().getCode()));
		params1.add(new QueryParam<String>(String.class,  getObject().getUri().toString()));
		StringBuilder b = new StringBuilder();
		String d = "";
		for (String a : getObject().getAlleles()) {b.append(d);b.append(a); d = ",";}
		params1.add(new QueryParam<String>(String.class,  b.toString()));

		return params1;
	}

	@Override
	public void setID(int index, int id) {
		getObject().setId(id);
	}

	@Override
	public boolean returnKeys(int index) {
		return true;
	}
}