package org.xmetdb.rest.structure.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;
import net.idea.restnet.db.aalocal.DBRole;
import net.idea.restnet.db.aalocal.user.IDBConfig;
import net.idea.restnet.db.aalocal.user.IUser;

/**
 * Copy SOM from one observation to another
 * @author nina
 *
 */
public class CopyStructureSOM extends AbstractUpdate<PropertyValueBuffer,PropertyValueBuffer> {
	private static final String[] sql = { 
		"insert into `ambit2-xmetdb`.property_values (\n"+
		"select null,?,idstructure,idchemical,user_name,status,text,idvalue_string,value_num,idtype\n"+ 
		"from `ambit2-xmetdb`.property_values v where v.idproperty=?)\n"+
		"on duplicate key update status=values(status),text=values(text),idvalue_string=values(idvalue_string),value_num=values(value_num),idtype=values(idtype)"
	};
				
	public CopyStructureSOM(PropertyValueBuffer source,PropertyValueBuffer target) {
		super();
		setGroup(source);
		setObject(target);
	}
	
	@Override
	public String[] getSQL() throws AmbitException {
		return sql;
	}

	@Override
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params = new ArrayList<QueryParam>();
		switch (index) {
		case 0: {
			params.add(new QueryParam<Integer>(Integer.class, (Integer)getObject().get("idproperty")));
			params.add(new QueryParam<Integer>(Integer.class, (Integer)getGroup().get("idproperty")));
			break;
		}
		}
		return params;
	}

	@Override
	public void setID(int index, int id) {

	}

}