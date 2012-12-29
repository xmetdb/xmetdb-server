package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.db.ReadProtocol.fields;
import org.xmetdb.rest.structure.resource.Structure;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

public class ReadProtocolByStructure extends ReadProtocolAbstract<Structure> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2670988238522573149L;
	public static final ReadProtocol.fields[] sqlFields = new ReadProtocol.fields[] {
		fields.idprotocol,
		fields.version,
		fields.title,
		fields.anabstract,
		fields.iduser,
		fields.summarySearchable,
		fields.idproject,
		fields.idorganisation,
		fields.reference,
		fields.status,
		fields.published_status,
		fields.atom_uncertainty,
		fields.product_amount
		
		//ReadProtocol.fields.accesslevel
	};	
	protected static String sql = 
		"select protocol.idprotocol,protocol.version,protocol.title,protocol.qmrf_number,abstract as anabstract,iduser,\n"+
		"summarySearchable,idproject,idorganisation,reference,protocol.updated,status,\n"+
		"protocol.`created`,published_status,atom_uncertainty,product_amount\n"+
		"from protocol,attachments a, `ambit2-xmetdb`.query q\n"+
		"join `ambit2-xmetdb`.query_results using(idquery)\n"+
		"where idchemical=?\n"+
		"and published_status='published' and a.name=q.name and a.idprotocol=protocol.idprotocol and a.version=protocol.version\n";

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if ((getFieldname()!=null) && (getFieldname().getIdchemical()>0)) 
			params.add(new QueryParam<Integer>(Integer.class, getFieldname().getIdchemical()));
		else throw new AmbitException("No structure!");
		return params;
	}

	public String getSQL() throws AmbitException {
		return sql;

	}

}
