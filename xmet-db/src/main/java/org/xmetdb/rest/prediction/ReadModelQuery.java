package org.xmetdb.rest.prediction;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol.fields;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.modbcum.q.query.AbstractQuery;
import net.idea.opentox.cli.algorithm.Algorithm;
import net.idea.opentox.cli.dataset.Dataset;

public class ReadModelQuery extends AbstractQuery<DBProtocol, Algorithm, EQCondition, DBModel>  implements IQueryRetrieval<DBModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3389302153227851643L;
	protected String modelRoot;
	
	public String getModelRoot() {
		return modelRoot;
	}
	public void setModelRoot(String modelRoot) {
		this.modelRoot = modelRoot;
	}
	protected static String sql = 
		"SELECT idmodel,id_srcdataset,algorithm,dataset,models.name FROM protocol\n"+
		"join attachments a using(idprotocol,version)\n"+
		"left join `ambit2-qmrf`.src_dataset a1 using(name)\n"+
		"join `ambit2-qmrf`.models on\n"+
		"concat(\"%s\",id_srcdataset)=models.dataset\n"+
		"where idprotocol=? and version=?\n"+
		"and algorithm=?\n"+
		"and type='data_training'";

	/**
	 * 
	 * @param protocol
	 */
	public ReadModelQuery(DBProtocol protocol) {
		super();
		setFieldname(protocol);
	}
	/*
	public ReadModelQuery(Integer id, Integer version,Integer year, String dir) {
		super();
		setFieldname(id==null?null:new DBProtocol(id,version,year));
	}
	*/
	public ReadModelQuery() {
		this((DBProtocol)null);
	}

	@Override
	public double calculateMetric(DBModel object) {
		return 1;
	}

	public boolean isPrescreen() {
		return false;
	}

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null) {
			params.add(fields.identifier.getParam(getFieldname()));
		} 
		if (getValue()!=null)
			params.add(new QueryParam<String>(String.class, getValue().getResourceIdentifier().toExternalForm()));
		if (params.size()==0) throw new AmbitException("No protocol or attachment id");
		return params;
	}

	public String getSQL() throws AmbitException {
		return String.format(sql,modelRoot);			
	}

	public DBModel getObject(ResultSet rs) throws AmbitException {
		String url = "";
		try {
				DBModel model = new DBModel();
				model.setID(rs.getInt("idmodel"));
				model.setResourceIdentifier(new URL(String.format("%s/%d", modelRoot,model.getID())));
				model.setAlgorithm(getValue());
				Dataset dataset = new Dataset();
				dataset.setResourceIdentifier(new URL(rs.getString("dataset")));
				model.setTrainingDataset(dataset);
				return model;

		} catch (Exception x) {
			throw new AmbitException(String.format("Error reading %s", url),x);
		}
	}
	@Override
	public String toString() {
		return getFieldname()==null?"All attachments":String.format("Attachments for QMRF-%s",getFieldname().getID());
	}
}
