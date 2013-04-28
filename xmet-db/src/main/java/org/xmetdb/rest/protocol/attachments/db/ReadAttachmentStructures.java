package org.xmetdb.rest.protocol.attachments.db;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.modbcum.q.query.AbstractQuery;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.db.ReadProtocol.fields;

import ambit2.base.data.StructureRecord;
import ambit2.base.interfaces.IStructureRecord;
import ambit2.base.interfaces.IStructureRecord.STRUC_TYPE;

public class ReadAttachmentStructures extends AbstractQuery<DBProtocol, DBAttachment, EQCondition, DBAttachment>  implements IQueryRetrieval<DBAttachment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -580539411377445277L;
	/**
	 * 
	 */
	protected boolean retrieveStructure = false;
	public boolean isRetrieveStructure() {
		return retrieveStructure;
	}

	public void setRetrieveStructure(boolean retrieveStructure) {
		this.retrieveStructure = retrieveStructure;
	}
	protected static String sql = 
	"SELECT name as identifier,title as atype,idchemical,idstructure,inchi,inchikey,smiles,formula\n"+  
	"from `ambit2-xmetdb`.sessions join `ambit2-xmetdb`.query using(idsessions) join `ambit2-xmetdb`.query_results using(idquery)\n" +
	"join `ambit2-xmetdb`.chemicals using(idchemical) \n"+
	"where name=? and title in ('substrate','product')"; 

	/*
	protected static String sql_structure = 
		"SELECT name as identifier,title as atype,idchemical,idstructure,uncompress(structure) struc\n"+  
		"from `ambit2-xmetdb`.sessions join `ambit2-xmetdb`.query using(idsessions) join `ambit2-xmetdb`.query_results using(idquery)\n" +
		"join `ambit2-xmetdb`.structure using(idchemical,idstructure) \n"+
		"where name=? and title in ('substrate','product')"; 
	*/
	
	protected static String sql_structure = 
		"SELECT query.name as identifier,title as atype,idchemical,idstructure,format,type_structure,uncompress(structure) struc,value\n"+ 
		"from `ambit2-xmetdb`.sessions join `ambit2-xmetdb`.query using(idsessions) join `ambit2-xmetdb`.query_results using(idquery)\n"+
		"join `ambit2-xmetdb`.structure using(idchemical,idstructure) \n"+
		"left join `ambit2-xmetdb`.property_values using(idchemical,idstructure)\n"+
		"left join `ambit2-xmetdb`.properties using(idproperty)\n"+
		"left join `ambit2-xmetdb`.property_string using(idvalue_string)\n"+
		"where query.name=? and properties.name=? and title in ('substrate','product')";	
	/**
	 * 
	 * @param protocol
	 */
	public ReadAttachmentStructures(DBProtocol protocol) {
		super();
		setFieldname(protocol);
	}
		
	public double calculateMetric(DBAttachment object) {
		return 1;
	}

	public boolean isPrescreen() {
		return false;
	}

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null) {
			params.add(fields.identifier.getParam(getFieldname()));
			if (retrieveStructure)
				params.add(fields.identifier.getParam(getFieldname()));
		} 
		if (params.size()==0) throw new AmbitException("No observation");
		return params;
	}

	public String getSQL() throws AmbitException {
		return retrieveStructure?sql_structure:sql;			
	}

	public DBAttachment getObject(ResultSet rs) throws AmbitException {
		URL url = null;
		try {
				String type = rs.getString("atype");
				DBAttachment attachment = new DBAttachment();
				attachment.setType(DBAttachment.attachment_type.valueOf(type));
				//protocol
				DBProtocol protocol = new DBProtocol();
				try {
					protocol.setIdentifier(rs.getString("identifier"));
					attachment.setProtocol(protocol);
				} catch (Exception x) {
					x.printStackTrace();
				}
				//structure
				IStructureRecord record = new StructureRecord();
				try {record.setIdchemical(rs.getInt("idchemical"));} catch (Exception x) {}
				try {record.setIdstructure(rs.getInt("idstructure"));} catch (Exception x) {}
				if (retrieveStructure) {
					try {record.setFormat(rs.getString("format"));} catch (Exception x) {}
					try {record.setType(STRUC_TYPE.valueOf(rs.getString("type_structure")));} catch (Exception x) {}
					try {record.setContent(rs.getString("struc"));} catch (Exception x) {}
					try {attachment.setSom(rs.getString("value"));} catch (Exception x) {attachment.setSom(null);}
				} else {
					attachment.setSom(null);
					try {record.setInchi(rs.getString("inchi"));} catch (Exception x) {}
					try {record.setInchiKey(rs.getString("inchikey"));} catch (Exception x) {}
					try {record.setSmiles(rs.getString("smiles"));} catch (Exception x) {}
					try {record.setFormula(rs.getString("formula"));} catch (Exception x) {}
				}
				attachment.setStructure(record);
				return attachment;

		} catch (Exception x) {
			throw new AmbitException(String.format("Error reading %s", url),x);
		}
	}
	@Override
	public String toString() {
		return getFieldname()==null?"All attachments":String.format("Attachments for %s",getFieldname().getIdentifier());
	}
}