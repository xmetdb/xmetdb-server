package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;


public class UpdateKeywords extends AbstractObjectUpdate<DBProtocol>{

	public static final String[] update_sql = {
		"update protocol set abstract=UpdateXML(abstract, '//keywords', concat('<keywords>',?,'</keywords>')) where qmrf_number=?"		
		};

	public UpdateKeywords(DBProtocol ref) {
		super(ref);
	}
	public UpdateKeywords() {
		this(null);
	}			
	public List<QueryParam> getParameters(int index) throws AmbitException {
		
		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<String>(String.class, 
						ReadProtocol.fields.xmlkeywords.getValue(getObject()).toString()));
		params.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		return update_sql;
	}
	public void setID(int index, int id) {
			
	}
}