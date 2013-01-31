package org.xmetdb.rest.endpoints.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;

import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.endpoints.Enzyme.EnzymeFields;

public class UpdateEnzyme extends AbstractObjectUpdate<Enzyme>{
	private static String sql =  "update template set %s=? where idtemplate=?";
	@Override
	public String[] getSQL() throws AmbitException {
		for (Enzyme.EnzymeFields field : EnzymeFields.values()) 
			switch (field) {
			case id: {
				break;
			}
			default: {
				if (field.getValue(getObject()) !=null) { 
					return new String[] {String.format(sql,field.name())};
				} else break;
			}
		
		}

		throw new AmbitException();
	}

	@Override
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params = new ArrayList<QueryParam>();
		for (Enzyme.EnzymeFields field : EnzymeFields.values()) {
			switch (field) {
			case id: {
				break;
			}
			default: {
				String value = field.getStringValue(getObject(),"");
				if (value!=null) {
					params.add(new QueryParam<String>(String.class, value));
					params.add(new QueryParam<Integer>(Integer.class, getObject().getId()));
					return params;
				}
			}
			}
		}
		return null;
	}

	@Override
	public boolean returnKeys(int index) {
		return false;
	}
	@Override
	public void setID(int index, int id) {
	}

}
