package org.xmetdb.rest.structure.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.modbcum.q.query.AbstractQuery;

import org.xmetdb.rest.protocol.DBProtocol;

/**
 * Retrieve references (by id or all)
 * @author nina
 *
 */
public class ReadStructureSOM  extends AbstractQuery<DBProtocol, PropertyValueBuffer, StringCondition, PropertyValueBuffer>  implements IQueryRetrieval<PropertyValueBuffer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3777962034208414659L;
	private static final String sql = 
		"select id,idproperty,idstructure,v.idchemical,user_name,status,v.text,idvalue_string,value_num,idtype,value from `ambit2-xmetdb`.properties p\n"+
		"left join `ambit2-xmetdb`.property_values v using(idproperty)\n"+
		"left join `ambit2-xmetdb`.query_results using(idstructure)\n"+
		"left join `ambit2-xmetdb`.query using(idquery)\n"+
		"left join `ambit2-xmetdb`.property_string using(idvalue_string)\n"+
		"where p.name = ? and query.name = ?\n";

	public ReadStructureSOM(String identifier) {
		this(identifier==null?null:new DBProtocol(identifier),null);
	}
	public ReadStructureSOM(DBProtocol protocol,PropertyValueBuffer pv ) {
		super();
		setValue(pv);
		setFieldname(protocol);
	}		


	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if ((getFieldname()!=null) && getFieldname().isValidIdentifier()) {
				params.add(new QueryParam<String>(String.class,getFieldname().getIdentifier()));
				params.add(new QueryParam<String>(String.class,getFieldname().getIdentifier()));
				return params;
		}
		throw new AmbitException("Invalid identifier");

	}

	public String getSQL() throws AmbitException {
		return sql;
	}

	
	public PropertyValueBuffer getObject(ResultSet rs) throws AmbitException {
		PropertyValueBuffer pv = getValue();
		if (getValue()==null) pv = new PropertyValueBuffer();
		else pv.clear();
		//"select id,idproperty,idstructure,v.idchemical,user_name,status,v.text,idvalue_string,value_num,idtype,value from properties\n"+
		try {
			pv.put("idchemical",rs.getInt("idchemical"));
			pv.put("idstructure",rs.getInt("idstructure"));
			pv.put("idproperty",rs.getInt("idproperty"));
			pv.put("user_name",rs.getString("user_name"));
			pv.put("status",rs.getString("status"));
			if (rs.getString("text")!=null)
				pv.put("text",rs.getString("text"));
			pv.put("idvalue_string",rs.getInt("idvalue_string"));
			pv.put("value_num",rs.getDouble("value_num"));
			pv.put("idtype",rs.getString("idtype"));
			if (rs.getString("value")!=null)
				pv.put("value",rs.getString("value"));
			return pv;
		} catch (SQLException x) {
			return null;
		}
	}
	@Override
	public String toString() {
		return getValue()==null?"SOM":String.format("Observation",getFieldname().getIdentifier());
	}
	@Override
	public boolean isPrescreen() {
		return false;
	}
	@Override
	public double calculateMetric(PropertyValueBuffer object) {
		return 1;
	}


}
