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
select code,name,count(*) from protocol
left join protocol_endpoints using(idprotocol)
join template using(idtemplate)
group by idtemplate
order by template.code
 */
public class EndpointProtocolFacetQueryXML extends AbstractFacetQuery<String,String,StringCondition,IFacet<String>> { 
									
	/**
	 * 
	 */
	private static final long serialVersionUID = -8340773122431657623L;
	protected EndpointProtocolFacet record;
	protected static String sql_protocol = 
		"SELECT\n"+
		"extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@group') g,\n"+
		"extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@subgroup') sg,\n"+
		"extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@name') n,\n"+
		"count(idprotocol)\n"+
		"FROM protocol where published_status='published'\n"+
		"group by extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@name')\n"+
		"order by g,n\n";
	
	//"comments regexp \"^http://www.opentox.org/echaEndpoints.owl\" \n"+
	/**
	 * 
	 */

	public EndpointProtocolFacetQueryXML(String url) {
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
			record.setProperty1(rs.getString(1));
			record.setProperty2(rs.getString(2));
			record.setValue(rs.getString(3));
			record.setCount(rs.getInt(4));
			return record;
		} catch (Exception x) {
			record.setValue(x.getMessage());
			record.setCount(-1);
			return record;
		}
	}

	@Override
	public String toString() {
		return "Protocols by endpoint";
	}
}