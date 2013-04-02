package org.xmetdb.rest.endpoints.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;

import org.xmetdb.rest.endpoints.Enzyme;


/**
 * Delete an enzyme
 * @author nina
 *
 */
public class DeleteEnzyme extends AbstractObjectUpdate<Enzyme> {
	private static final String[] sql_byid = {"DELETE FROM template where idtemplate=?"};
	public DeleteEnzyme(Enzyme ref) {
		super(ref);
	}
	public DeleteEnzyme() {
		this(null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getObject()==null || getObject().getID()<=0) throw new AmbitException("No enzyme id!");
		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<Integer>(Integer.class, getObject().getID()));
		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		if (getObject()==null || getObject().getID()<=0) throw new AmbitException("No enzyme id!");
		return sql_byid;
	}
	public void setID(int index, int id) {
			
	}
}