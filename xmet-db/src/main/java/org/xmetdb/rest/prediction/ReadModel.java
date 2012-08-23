package org.xmetdb.rest.prediction;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.modbcum.q.query.AbstractQuery;
import net.idea.opentox.cli.algorithm.Algorithm;
import net.idea.opentox.cli.dataset.Dataset;

public class ReadModel  extends AbstractQuery<DBProtocol, DBModel, EQCondition, DBModel>  implements IQueryRetrieval<DBModel> {

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
		"SELECT idmodel,algorithm,dataset,models.name FROM `ambit2-qmrf`.models\n"+
		"where idmodel=?\n";

	/**
	 * 
	 * @param protocol
	 */
	public ReadModel(DBProtocol protocol) {
		super();
		setFieldname(protocol);
	}
	/*
	public ReadModel(Integer id, Integer version,Integer year, String dir) {
		super();
		setFieldname(id==null?null:new DBProtocol(id,version,year));
	}
	*/
	public ReadModel() {
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
		if (getValue()!=null) 
			params.add(new QueryParam<Integer>(Integer.class, getValue().getID()));
		else throw new AmbitException("No model ID");
		return params;
	}

	public String getSQL() throws AmbitException {
		return sql;			
	}

	public DBModel getObject(ResultSet rs) throws AmbitException {
		String url = "";
		try {
				DBModel model = new DBModel();
				model.setID(rs.getInt("idmodel"));
				model.setResourceIdentifier(new URL(String.format("%s/%d", modelRoot,model.getID())));
				Algorithm algorithm = new Algorithm();
				algorithm.setResourceIdentifier(new URL(rs.getString("algorithm")));
				model.setAlgorithm(algorithm);
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
		return "Model";
	}
}
