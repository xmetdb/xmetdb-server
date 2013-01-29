package org.xmetdb.rest.endpoints.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;

import org.xmetdb.rest.endpoints.Enzyme;

public class CreateEndpoint extends AbstractObjectUpdate<Enzyme>{
	private static final String[] sql = {
		"insert into template (name,code,uri,allele) values (?,?,?,?)",
		"insert ignore into dictionary\n"+
		"SELECT t2.idtemplate,'is_part_of',t1.idtemplate FROM template t1\n"+
		"join template t2 where t1.code is null and t2.code=?"
	};
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
		switch (index) {
		case 0:{
			params1.add(new QueryParam<String>(String.class, getObject().getName()));
			params1.add(new QueryParam<String>(String.class,  getObject().getCode()));
			params1.add(new QueryParam<String>(String.class,  getObject().getUri().toString()));
			StringBuilder b = new StringBuilder();
			String d = "";
			for (String a : getObject().getAlleles()) {b.append(d);b.append(a); d = ",";}
			params1.add(new QueryParam<String>(String.class,  b.toString()));
			return params1;
		}
		case 1:{
			params1.add(new QueryParam<String>(String.class,  getObject().getCode()));
			return params1;
		}
		}
		return null;
	}

	@Override
	public void setID(int index, int id) {
		if (index==0)
			getObject().setId(id);
	}

	@Override
	public boolean returnKeys(int index) {
		return true;
	}
}