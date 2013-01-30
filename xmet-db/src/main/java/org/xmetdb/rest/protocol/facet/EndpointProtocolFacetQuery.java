package org.xmetdb.rest.protocol.facet;

import java.sql.ResultSet;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.modbcum.q.facet.AbstractFacetQuery;


/**
 * Lists number of protocols, given endpoint and (optionally) a compound
 * @author nina
 * 
 */
public class EndpointProtocolFacetQuery extends AbstractFacetQuery<String,String,StringCondition,IFacet<String>> { 
									
	/**
	 * 
	 */
	private static final long serialVersionUID = -8340773122431657623L;
	protected EndpointProtocolFacet record;
	protected static String sql_protocol = 
		"select t.code,t.name,t.uri,t.allele,count(distinct(idprotocol)) as c from protocol\n"+
		"left join protocol_endpoints using(idprotocol,version)\n"+
		"left join template t using(idtemplate)\n"+
		"where published_status = 'published'\n"+
		"group by t.idtemplate\n"+
		"order by t.code\n";
	
	/**
	 * 
	 */

	public EndpointProtocolFacetQuery(String url) {
		super(url);
		setCondition(StringCondition.getInstance(StringCondition.C_STARTS_WITH));
		record = new EndpointProtocolFacet(url);
		record.setProperty1(null);
		record.setProperty2(null);
	
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
		return sql_protocol;
	}

	@Override
	public EndpointProtocolFacet getObject(ResultSet rs) throws AmbitException {
		if (record == null) {
			record = new EndpointProtocolFacet(null);
		}
		try {
			record.setProperty1(rs.getString("uri"));
			record.setProperty2(rs.getString("code"));
			record.setValue(rs.getString("name"));
			record.setCount(rs.getInt("c"));
			return record;
		} catch (Exception x) {
			record.setProperty1(null);
			record.setProperty2(null);
			record.setValue(null);
			try { record.setCount(rs.getInt(4));} catch (Exception xx) {}
			return record;
		}
	}

	@Override
	public String toString() {
		return "Protocols by endpoint";
	}
}