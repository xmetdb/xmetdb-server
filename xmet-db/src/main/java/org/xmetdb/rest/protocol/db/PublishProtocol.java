package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.db.exceptions.InvalidEndpointException;
import org.xmetdb.rest.db.exceptions.InvalidProtocolException;
import org.xmetdb.rest.endpoints.EndpointTest;
import org.xmetdb.rest.protocol.DBProtocol;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

/**
 * Expects:
 * published : Boolean
 * Endpoint code:
 * Endpoint group
 * Endpoint name
 * idprotocol
 * version 
 * @author nina
 *
 */
public class PublishProtocol extends AbstractUpdate<EndpointTest,DBProtocol>{
	
	public static final String[] publish_sql = new String[] { 
		"update protocol set \n" +
		"qmrf_number=(select concat(\"Q\",substr(year(now()),3,2),'-',?,'-',lpad(n.nperyear,4,'0')) from\n" +
		"(select count(idprotocol)+1 as nperyear from protocol where published_status='published' and year(updated)=year(now())) as n),\n"+
		"abstract=\n" +
		"updatexml(updatexml(updatexml(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@group',?),\n"+
		"'/QMRF/Catalogs/endpoints_catalog/endpoint/@subgroup','subgroup=\"\"'),\n"+
		"'/QMRF/Catalogs/endpoints_catalog/endpoint/@name',?)\n" +
		"where idprotocol=? and version=? and published_status!='published'",
		
		"update protocol set published_status=?,abstract=updatexml(abstract,'//QMRF_number',concat('<QMRF_number chapter=\"10.1\"  name=\"QMRF number\">',qmrf_number,'</QMRF_number>')) where idprotocol=? and version=? and published_status!='published'",
		
		"delete from protocol_endpoints where idprotocol=? and version=?",
		
		"insert into protocol_endpoints select idprotocol,version,idtemplate\n"+
		"from protocol join template where name = ? and code = ? and idprotocol=? and version=?"
		
	};
	public PublishProtocol(EndpointTest endpoint,DBProtocol ref) {
		super(ref);
		setGroup(endpoint);
	}
		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params1 = new ArrayList<QueryParam>();
		if (getObject()==null || (getObject().getID()<=0) || (getObject().getVersion()<=0)) 
			throw new InvalidProtocolException();
	
		if (getGroup()==null 
				|| (getGroup().getCode()==null) 
				|| (getGroup().getName()==null) 
				|| (getGroup().getParentCode()==null) 
				|| (getGroup().getParentTemplate()==null)) throw new InvalidEndpointException();
		
		if (index==0) {
			//code
			String code = getGroup().getCode().replace("QMRF","").replace("OECD","").replace("EC","").replace(" ","");
			String pcode = getGroup().getParentCode().replace("QMRF","").replace("OECD","").replace("EC","").replace(" ","");
			params1.add(new QueryParam<String>(String.class, code.replace(".","")));
			params1.add(new QueryParam<String>(String.class, String.format("group=\"%s%s\"",pcode,getGroup().getParentTemplate())));
			params1.add(new QueryParam<String>(String.class, String.format("name=\"%s%s\"",code,getGroup().getName())));

		} else if (index == 1) {
			params1.add(ReadProtocol.fields.published_status.getParam(getObject()));			
		} else if (index == 2) {
			//none
		} else if (index == 3) {
			params1.add(new QueryParam<String>(String.class,getGroup().getName()));
			params1.add(new QueryParam<String>(String.class,getGroup().getCode()));
		}
		
		params1.add(ReadProtocol.fields.idprotocol.getParam(getObject()));
		params1.add(ReadProtocol.fields.version.getParam(getObject()));
		
		return params1;
		
	}
	public String[] getSQL() throws AmbitException {
		
		return publish_sql;
	}
	public void setID(int index, int id) {
			
	}
}