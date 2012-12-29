package org.xmetdb.rest.protocol.db;

import java.sql.ResultSet;
import java.sql.Timestamp;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.modbcum.q.query.AbstractQuery;

import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.DBProtocol;

public abstract class ReadProtocolAbstract<T> extends AbstractQuery<T, DBProtocol, StringCondition, DBProtocol>  implements IQueryRetrieval<DBProtocol> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228939989116141217L;
	protected Boolean showUnpublished = true;
	protected Boolean onlyUnpublished = false;

	public Boolean getOnlyUnpublished() {
		return onlyUnpublished;
	}
	public void setOnlyUnpublished(Boolean onlyUnpublished) {
		this.onlyUnpublished = onlyUnpublished;
	}
	public Boolean getShowUnpublished() {
		return showUnpublished;
	}
	public void setShowUnpublished(Boolean showUnpublished) {
		this.showUnpublished = showUnpublished;
	}

	protected static String sql_withkeywords =  //for text search
		"select idprotocol,version,protocol.title,qmrf_number,abstract as anabstract,iduser,summarySearchable," +
		"idproject," +
		"idorganisation,user.username,user.firstname,user.lastname," +
		"reference,updated,status,`created`,published_status,atom_uncertainty,product_amount\n" +
		"from protocol join user using(iduser)\n" +
		"left join keywords using(idprotocol,version) %s %s";


	protected static String sql_nokeywords = 
		"select idprotocol,protocol.version,protocol.title,qmrf_number,abstract as anabstract,iduser,summarySearchable," +
		"idproject," +
		"idorganisation,user.username,user.firstname,user.lastname," +
		"reference,updated,status,`created`,published_status,atom_uncertainty,product_amount\n" +
		"from protocol join user using(iduser)\n" +
		" %s %s order by idprotocol desc,version desc";		
	
	
	public ReadProtocolAbstract(String identifier) {
		this(identifier==null?null:new DBProtocol(identifier));
		setFieldname(null);
	}	
	public ReadProtocolAbstract() {
		this((DBProtocol)null);
	}
	public ReadProtocolAbstract(DBProtocol protocol) {
		super();
		setValue(protocol);
		setFieldname(null);
	}	
		
	public double calculateMetric(DBProtocol object) {
		return 1;
	}

	public boolean isPrescreen() {
		return false;
	}


	public DBProtocol getObject(ResultSet rs) throws AmbitException {
		
		DBProtocol p = null;
		try {
			p =  new DBProtocol();
			for (ReadProtocol.fields field:ReadProtocol.sqlFields) try {
				field.setParam(p,rs);
				
			} catch (Exception x) {
				x.printStackTrace();
			}
			try {
				Timestamp ts = rs.getTimestamp(ReadProtocol.fields.updated.name());
				p.setTimeModified(ts.getTime());
			} catch (Exception x) {}
			try {
				Timestamp ts = rs.getTimestamp(ReadProtocol.fields.created.name());
				p.setSubmissionDate(ts.getTime());
			} catch (Exception x) {
				x.printStackTrace();
				
			}
			try {
				String qmrf_number = rs.getString(DBProtocol.QMRFNUMBER);
				p.setIdentifier(qmrf_number);
			} catch (Exception x) {
				throw new AmbitException("Error when reading XMETDB number",x);
				
			}				
			return p;
		} catch (AmbitException x) {
			throw x;
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		} finally {
			//if (p!=null) p.setIdentifier(generateIdentifier(p));
		}
	}
	@Override
	public String toString() {
		return getValue()==null?"All observations":String.format("Observation id=XMETDB%s",getValue().getID());
	}

	public static String generateIdentifier(DBProtocol protocol) throws ResourceException {
		return protocol.getIdentifier();
	}

}
