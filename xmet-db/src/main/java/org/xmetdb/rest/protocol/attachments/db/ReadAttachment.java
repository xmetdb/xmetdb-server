package org.xmetdb.rest.protocol.attachments.db;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.modbcum.q.query.AbstractQuery;
import net.idea.restnet.c.ChemicalMediaType;

import org.restlet.data.MediaType;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol.fields;
import org.xmetdb.xmet.client.PublishedStatus;


public class ReadAttachment extends AbstractQuery<DBProtocol, DBAttachment, EQCondition, DBAttachment>  implements IQueryRetrieval<DBAttachment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228939989116141217L;
	protected String dir = null;
	private enum _fields {
		idattachment,
		type,
		name,
		format,
		description,
		imported,
		idquery
	}
	/*
	protected static String sql = 
		"SELECT idprotocol,version,qmrf_number,protocol.created,idattachment,type,a.name,`format`,description,a1.name is not null as imported,idquery,published_status,title FROM protocol\n" +
		"join attachments a using(idprotocol,version)\n" +
		"left join `ambit2-xmetdb`.query a1 using(name) where %s ";
	*/
	protected static String sql = 
	"SELECT idprotocol,version,qmrf_number,protocol.created,idattachment,type,a.name,`format`,description,a1.name is not null as imported,idquery,published_status,protocol.title FROM protocol\n"+
	"join attachments a using(idprotocol,version)\n"+
	"left join `ambit2-xmetdb`.query a1 on a1.name=protocol.qmrf_number\n"+ 
	"join `ambit2-xmetdb`.sessions s using(idsessions)\n"+
	"where s.title=a.`type` and %s\n";
	
	protected static String where_protocol = "protocol.qmrf_number=?";
	protected static String where_attachment = "idattachment=?";
	protected static String where_datasetname = "a.name=?";
	protected static String where_datasetid = "a1.idquery=?";
	
	/**
	 * 
	 * @param protocol
	 */
	public ReadAttachment(DBProtocol protocol,String dir) {
		super();
		setFieldname(protocol);
		this.dir = dir;
	}
	/*
	public ReadAttachment(Integer id, Integer version,Integer year, String dir) {
		super();
		setFieldname(id==null?null:new DBProtocol(id,version,year));
		this.dir = dir;
	}
	*/
	public ReadAttachment(String dir) {
		this((DBProtocol)null,dir);
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
		} 
		if ((getValue()!=null)&&(getValue().getID()>0))
			params.add(new QueryParam<Integer>(Integer.class, getValue().getID()));
		if (params.size()==0) throw new AmbitException("No protocol or attachment id");
		return params;
	}

	public String getSQL() throws AmbitException {
		StringBuilder b = new StringBuilder();
		int c = 0;
		if (getFieldname()!=null) { b.append(where_protocol); c++;}
		if ((getValue()!=null)&&(getValue().getID()>0)) {
			if (c>0) b.append(" and ");
			b.append(where_attachment); 
		}
		return String.format(sql,b);			
	}

	public DBAttachment getObject(ResultSet rs) throws AmbitException {
		URL url = null;
		try {
				String format = rs.getString(_fields.format.name());
				String name = rs.getString(_fields.name.name());
				String type = rs.getString(_fields.type.name());
				DBAttachment attachment = null;
				MediaType media = MediaType.APPLICATION_ALL;
				if ("text/uri-list".equals(format)) { 
					url = null; media = MediaType.TEXT_URI_LIST;
					attachment = new DBAttachment();
				} else {
					url = DBAttachment.getPath(dir,type,name,format);
					attachment = new DBAttachment(url);
					if ("pdf".equals(format)) media = MediaType.APPLICATION_PDF;
					else if ("sdf".equals(format)) media = ChemicalMediaType.CHEMICAL_MDLSDF;
					else if ("mol".equals(format)) media =  ChemicalMediaType.CHEMICAL_MDLMOL;
					else if ("csv".equals(format)) media =  MediaType.TEXT_CSV;
					else if ("txt".equals(format)) media =   MediaType.TEXT_PLAIN;
					else if ("smi".equals(format)) media =  ChemicalMediaType.CHEMICAL_SMILES;
					else media =  MediaType.APPLICATION_ALL;
				}
				attachment.setFormat(format);
				attachment.setMediaType(media.toString());
				attachment.setID(rs.getInt(_fields.idattachment.name()));
				attachment.setTitle(name);
				attachment.setType(DBAttachment.attachment_type.valueOf(type));
				attachment.setDescription(rs.getString(_fields.description.name()));
				attachment.setImported(rs.getBoolean(_fields.imported.name()));
				try {
					if (attachment.isImported()) attachment.setIdquerydatabase(rs.getInt(_fields.idquery.name()));
				} catch (Exception x) { attachment.setIdquerydatabase(-1);}
				//protocol
				DBProtocol protocol = new DBProtocol();
				try {
					protocol.setID(rs.getInt(ReadProtocol.fields.idprotocol.name()));
					protocol.setVersion(rs.getInt(ReadProtocol.fields.version.name()));
					try {
						protocol.setPublishedStatus(PublishedStatus.valueOf(rs.getString(ReadProtocol.fields.published_status.name())));
					} catch (Exception x) {
						x.printStackTrace();
						protocol.setPublished(false);	
					}
					protocol.setTitle(rs.getString(ReadProtocol.fields.title.name()));
					protocol.setIdentifier(rs.getString("qmrf_number"));
					Timestamp ts = rs.getTimestamp(ReadProtocol.fields.created.name());
					protocol.setSubmissionDate(ts.getTime());
					attachment.setProtocol(protocol);
				} catch (Exception x) {
					x.printStackTrace();
					
				}
				
				
				return attachment;

		} catch (Exception x) {
			throw new AmbitException(String.format("Error reading %s", url),x);
		}
	}
	@Override
	public String toString() {
		return getFieldname()==null?"All attachments":String.format("Attachments for QMRF-%s",getFieldname().getID());
	}
}