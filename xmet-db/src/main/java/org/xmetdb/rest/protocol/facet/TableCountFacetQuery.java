package org.xmetdb.rest.protocol.facet;

import java.sql.ResultSet;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.modbcum.q.facet.AbstractFacetQuery;

import org.xmetdb.rest.protocol.facet.TableCountFacetQuery.RESOURCES;

public class TableCountFacetQuery extends AbstractFacetQuery<RESOURCES,String,StringCondition,IFacet<String>> { 
	enum RESOURCES {
		enzymes {
			@Override
			public String getSQL() {
				return "select 'Enzymes' as name,count(idtemplate) c from template";
			}
		},

		observations {
			@Override
			public String getSQL() {
				return "select 'Observations' as name,count(idprotocol) c from protocol where published_status='published'";
			}
		},
		observationsbystatus {
			@Override
			public String getSQL() {
				return 
				"SELECT concat(ifnull(published_status, 'ALL'),' ',if(curated is null,' [All]',if(curated=1,'[Curated]','[Not curated]'))), count(idprotocol) from protocol\n"+
				"group by published_status,curated with rollup";
			}
		},			
		structuresbygroup {
			@Override
			public String getSQL() {
				return 
				"SELECT concat(title,'s'),count(distinct(idchemical)) FROM `ambit2-xmetdb`.sessions join `ambit2-xmetdb`.query using(idsessions) join `ambit2-xmetdb`.query_results using(idquery)\n"+ 
				"where title='substrate' or title='product'	group by idsessions";
			}			
		},
		substratesandproducts {
			@Override
			public String getSQL() {
				return 
				"SELECT 'Chemical structures',count(distinct(idchemical)) FROM `ambit2-xmetdb`.sessions join `ambit2-xmetdb`.query using(idsessions) join `ambit2-xmetdb`.query_results using(idquery) where title=\"substrate\" or title=\"product\"";
			}			
		},
		structures {
			@Override
			public String getSQL() {
				return 
				"SELECT 'Chemical structures',count(idchemical) FROM `ambit2-xmetdb`.chemicals";
			}			
		};		
		public abstract String getSQL();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8340773122431657623L;
	protected StatisticsFacet record;
	
	/**
	 * 
	 */
	public TableCountFacetQuery(String url) {
		this(url,RESOURCES.enzymes);
	}
	public TableCountFacetQuery(String url,RESOURCES resources) {
		super(url);
		setFieldname(resources);
		record = new StatisticsFacet();
	}

	@Override
	public double calculateMetric(IFacet<String> object) {
		return 1;
	}

	@Override
	public boolean isPrescreen() {
		return false;
	}

	@Override
	public List<QueryParam> getParameters() throws AmbitException {
		return null;
	}

	@Override
	public String getSQL() throws AmbitException {
		return getFieldname().getSQL();
	}

	@Override
	public StatisticsFacet getObject(ResultSet rs) throws AmbitException {
		if (record == null) record = new StatisticsFacet();
		try {
			record.setValue(rs.getString(1));
			record.setCount(rs.getInt(2));
			return record;
		} catch (Exception x) {
			record.setValue(null);
			try { record.setCount(rs.getInt(2));} catch (Exception xx) {}
			return record;
		}
	}

	@Override
	public String toString() {
		return getFieldname().name();
	}
}