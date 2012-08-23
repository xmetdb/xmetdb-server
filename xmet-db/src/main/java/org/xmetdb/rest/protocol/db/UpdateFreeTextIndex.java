package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import org.xmetdb.rest.protocol.DBProtocol;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;

public class UpdateFreeTextIndex extends AbstractObjectUpdate<DBProtocol>{

	public static final String update_sql = 
		"insert into keywords select idprotocol,version," +
		"trim(" +
		"replace(" +
		"replace(" +
		"replace(" +
		"replace(" +		
		"replace(" +
		"replace(" +
		"replace(" +
		"replace(" +
		"replace(\n" +
		"extractvalue(abstract,'//@text()')," +
		"'&lt;html&gt;',''),\n" +
		"'&lt;head&gt;',''),\n" +
		"'&lt;/head&gt;',''),\n" +
		"'&lt;body&gt;',''),\n" +
		"'&lt;/body&gt;',''),\n" +
		"'&lt;/html&gt;',''),\n" +
		"'&lt;p style=\"margin-top: 0\"&gt',''),\n" +
		"'&lt;/p&gt;',''),\n" +
		"'&#13;',''))\n" 	+
		"from protocol %s on duplicate key update keywords=values(keywords) ";
		
	public static final String where = "where qmrf_number=?";

	public UpdateFreeTextIndex(DBProtocol ref) {
		super(ref);
	}
	public UpdateFreeTextIndex() {
		this(null);
	}			
	public List<QueryParam> getParameters(int index) throws AmbitException {

		List<QueryParam> params = new ArrayList<QueryParam>();
		
		if ((getObject()!=null) && (getObject().isValidIdentifier())) 
			params.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
		
		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		String where = null;
		if ((getObject()!=null) && (getObject().isValidIdentifier())) 
			 where = UpdateFreeTextIndex.where;
	
		return  new String[] {String.format(update_sql,where==null?"":where)};
	}
	public void setID(int index, int id) {
			
	}
}