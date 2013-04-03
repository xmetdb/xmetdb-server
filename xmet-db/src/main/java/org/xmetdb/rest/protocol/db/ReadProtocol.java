package org.xmetdb.rest.protocol.db;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.user.DBUser;
import net.toxbank.client.resource.Document;
import net.toxbank.client.resource.Organisation;
import net.toxbank.client.resource.Project;
import net.toxbank.client.resource.Protocol;
import net.toxbank.client.resource.Protocol.STATUS;
import net.toxbank.client.resource.Template;

import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.xmet.client.AtomUncertainty;
import org.xmetdb.xmet.client.ProductAmount;
import org.xmetdb.xmet.client.PublishedStatus;
import org.xmetdb.xmet.client.Resources;

/**
 * Retrieve references (by id or all)
 * @author nina
 *
 */
public class ReadProtocol  extends ReadProtocolAbstract<DBUser>  implements IQueryRetrieval<DBProtocol> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228939989116141217L;
	

	public static final ReadProtocol.fields[] displayFields = new ReadProtocol.fields[] {
			fields.idprotocol,
			fields.identifier,
			fields.version,
			fields.published_status,
			fields.created,
			fields.updated,
			fields.reference,
			fields.title,
			fields.anabstract,
			fields.author_uri,
			fields.status,
			fields.curated,
			//ReadProtocol.fields.status
			fields.idproject,
			fields.idorganisation,
			fields.user_uri,
			fields.data_training,
			fields.data_validation,
			fields.document //attachment of type document

			//ReadProtocol.fields.accesslevel
		};	
	public enum fields {
		idprotocol {
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				protocol.setID(rs.getInt(name()));
			}
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<Integer>(Integer.class, (Integer)getValue(protocol));
			}	
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.getID()>0?protocol.getID():null;
			}
			@Override
			public String toString() {
				return "URI";
			}
		},
		version {
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				protocol.setVersion(rs.getInt(name()));
			}
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<Integer>(Integer.class, (Integer)getValue(protocol));
			}	
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?0:protocol.getVersion()<=0?1:protocol.getVersion();
			}
			@Override
			public String toString() {
				return "Version";
			}
		},		
		identifier {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<String>(String.class, (String)getValue(protocol));
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				protocol.setIdentifier(rs.getString(name()));
			}
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.getIdentifier();
			}
			
			public String getCondition() {
				return " protocol.qmrf_number = ? ";
			}			
		},
		
		title {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				String title = getValue(protocol).toString();
				return new QueryParam<String>(String.class, title.length()>32?title.substring(0,31).trim():title.trim());
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				protocol.setTitle(rs.getString(name()));
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.getTitle();
			}
			
			public String getCondition() {
				return String.format(" protocol.%s regexp ? ",name());
			}
		},
		anabstract {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				String title = getValue(protocol).toString();
				return new QueryParam<String>(String.class, title.length()>255?title.substring(0,254).trim():title.trim());
			}	
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				protocol.setAbstract(rs.getString(name()));
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.getAbstract();
			}
			public String getHTMLField(DBProtocol protocol) {
				Object value = getValue(protocol);
				return String.format("<textarea name='%s' cols='40' rows='5' title='%s'>%s</textarea>\n",
						name(),
						getDescription(),
						value==null?"":value.toString());
			}			
			@Override
			public String toString() {
				return "Abstract";
			}
		},
		iduser {

			@Override
			public QueryParam getParam(DBProtocol protocol) {
				Object project = getValue(protocol);
				return new QueryParam<Integer>(Integer.class, project==null?null:((DBUser) protocol.getOwner()).getID());
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
					DBUser user = new DBUser();
					user.setID(rs.getInt(name()));
					protocol.setOwner(user);
				} catch (Exception x) {
					throw new SQLException(x);
				}
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:protocol.getOwner()==null?null:((DBUser) protocol.getOwner()).getID();
			}			
			@Override
			public String toString() {
				return "Owner";
			}
		},		
		user_uri {

			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:
						protocol.getOwner()==null?null:protocol.getOwner()==null?null:protocol.getOwner().getResourceURL().toString();
			}		

			@Override
			public String toString() {
				return "User (owner) URI";
			}
			@Override
			public String getExampleValue(String uri) {
				return String.format("%s%s/U1",uri,Resources.user);
			}
			@Override
			public String getHelp(String uri) {
				return String.format("<a href='%s%s'>Users list</a>",uri,Resources.user);
			}
		
		},			
		curated {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				Object value = getValue(protocol);
				return new QueryParam<Boolean>(Boolean.class,value==null?null:(Boolean) value);
			}		
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				protocol.setSearchable(rs.getBoolean(name()));
			}
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.isSearchable();
			}
			@Override
			public Class getClassType(DBProtocol protocol) {
				return Boolean.class;
			}
			public String getHTMLField(DBProtocol protocol) {
				Object value = getValue(protocol);
				return String.format("<input name='%s' type='checkbox' title='%s' value='%s'>\n",
						name(),
						getDescription(),
						value==null?"":value.toString());
			}			
			@Override
			public String toString() {
				return "Is summary searchable";
			}
		},
		idproject {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				DBProject project = (DBProject) getValue(protocol);
				return new QueryParam<Integer>(Integer.class, project==null?null:project.getID());
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
					Project p = protocol.getProject();
					if (p==null) { 
						DBProject dbp = new DBProject(); 
						protocol.setProject(dbp);
						dbp.setID(rs.getInt(name()));
					} else if (p instanceof DBProject) {
						((DBProject)p).setID(rs.getInt(name()));
					}
				} catch (Exception x) {
					throw new SQLException(x);
				}
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:protocol.getProject();
			}		
			@Override
			public String toString() {
				return "Project";
			}
		},
		project_uri {

			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:
						protocol.getProject()==null?null:protocol.getProject().getResourceURL()==null?null:protocol.getProject().getResourceURL().toString();
			}		

			@Override
			public String toString() {
				return "Project URI";
			}
			@Override
			public String getExampleValue(String uri) {
				return String.format("%s%s/G1",uri,Resources.project);
			}
			@Override
			public String getHelp(String uri) {
				return String.format("<a href='%s%s'>Projects list</a>",uri,Resources.project);
			}
		},		
		project {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				/*
				DBProject project = (DBProject) getValue(protocol);
				return new QueryParam<Integer>(Integer.class, project==null?null:project.getID());
				*/
				return null;
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
					Project p = protocol.getProject();
					if (p==null) { 
						DBProject dbp = new DBProject(); 
						protocol.setProject(dbp);
						dbp.setTitle(rs.getString(name()));
						dbp.setGroupName(rs.getString("pgroupname"));
					} else if (p instanceof DBProject) {
						((DBProject)p).setTitle(rs.getString(name()));
						((DBProject)p).setGroupName(rs.getString("pgroupname"));
					}
				} catch (Exception x) {
					throw new SQLException(x);
				}
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:protocol.getProject();
			}			
		},
		idorganisation {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				DBOrganisation project = (DBOrganisation) getValue(protocol);
				return new QueryParam<Integer>(Integer.class, project==null?null:project.getID());
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
					Organisation p = protocol.getOrganisation();
					if (p==null) { 
						DBOrganisation dbp = new DBOrganisation(); 
						protocol.setOrganisation(dbp);
						dbp.setID(rs.getInt(name()));
					} else if (p instanceof DBOrganisation) {
						((DBOrganisation)p).setID(rs.getInt(name()));
					}
				} catch (Exception x) {
					throw new SQLException(x);
				}
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:protocol.getOrganisation();
			}	
			@Override
			public String toString() {
				return "Organisation";
			}
			@Override
			public String getExampleValue(String uri) {
				return String.format("%s%s/G1",uri,Resources.organisation);
			}
		},	
		organisation {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return null;
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
					Organisation p = protocol.getOrganisation();
					if (p==null) { 
						DBOrganisation dbp = new DBOrganisation(); 
						protocol.setOrganisation(dbp);
						dbp.setTitle(rs.getString(name()));
						dbp.setGroupName(rs.getString("ogroupname"));
					} else if (p instanceof DBOrganisation) {
						((DBOrganisation)p).setTitle(rs.getString(name()));
						((DBOrganisation)p).setGroupName(rs.getString("ogroupname"));
					}
				} catch (Exception x) {
					throw new SQLException(x);
				}
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:protocol.getOrganisation();
			}			
		},		
		organisation_uri {

			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:
						protocol.getOrganisation()==null?null:
							protocol.getOrganisation().getResourceURL()==null?null:
							protocol.getOrganisation().getResourceURL().toString();
			}		

			@Override
			public String toString() {
				return "Organisation URI";
			}
			@Override
			public String getExampleValue(String uri) {
				return String.format("%s%s/G1",uri,Resources.organisation);
			}
			@Override
			public String getHelp(String uri) {
				return String.format("<a href='%s%s' >Organisations list</a>",uri,Resources.organisation);
			}
		},			
		author_uri {

			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:
						protocol.getResourceURL()==null?"":
						String.format("%s%s",protocol.getResourceURL(),Resources.authors);
			}		

			@Override
			public String toString() {
				return "Author URI";
			}
			@Override
			public String getExampleValue(String uri) {
				return String.format("%s%s/U1",uri,Resources.user);
			}

			@Override
			public String getHelp(String uri) {
				return String.format("<a href='%s%s'>Authors list</a>",uri,Resources.user);
			}
		
		},		
		reference {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				String title = getValue(protocol).toString();
				return new QueryParam<String>(String.class, title.length()>255?title.substring(0,254).trim():title.trim());
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				Template t = new Template();
				t.setTitle(rs.getString(name()));
				protocol.setDataTemplate(t);
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.getDataTemplate()==null?null:protocol.getDataTemplate().getTitle();
			}
			
			public String getCondition() {
				return String.format(" protocol.%s regexp ? ",name());
			}
		},		
		
		filename {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<String>(String.class, getValue(protocol).toString());
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
				protocol.setDocument(new Document(new URL(rs.getString(name()))));
				} catch (Exception x) { protocol.setDocument(null);}
			}
			@Override
			public Object getValue(DBProtocol protocol) {
				return  protocol==null?null:protocol.getDocument()==null?null:protocol.getDocument().getResourceURL();
			}				
			public String getHTMLField(DBProtocol protocol) {
				Object value = getValue(protocol);
				return String.format("<input name='%s' type='file' title='%s' size='40' value='%s'>\n",
						name(),
						getDescription(),
						value==null?"":value.toString());
			}		
			@Override
			public String toString() {
				return "Document";
			}
		},	
		data_training {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return null;
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				
			}
			@Override
			public Object getValue(DBProtocol protocol) {
				return null;
			}				
			public String getHTMLField(DBProtocol protocol) {
				return null;
			}		
			@Override
			public String toString() {
				return "Attachment (document)";
			}
		},
		data_validation {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return null;
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				
			}
			@Override
			public Object getValue(DBProtocol protocol) {
				return null;
			}				
			public String getHTMLField(DBProtocol protocol) {
				return null;
			}		
			@Override
			public String toString() {
				return "Attachment (document)";
			}
		},
		document {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return null;
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				
			}
			@Override
			public Object getValue(DBProtocol protocol) {
				return null;
			}				
			public String getHTMLField(DBProtocol protocol) {
				return null;
			}		
			@Override
			public String toString() {
				return "Attachment (document)";
			}
		},
		status {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<String>(String.class,protocol.getStatus().name());
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
				protocol.setStatus(Protocol.STATUS.valueOf(rs.getString(name())));
				} catch (Exception x) {protocol.setStatus(STATUS.RESEARCH);}
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.getStatus();
			}
			
			public String getCondition() {
				return String.format(" %s = ? ",name());
			}
		},
		created {
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol.getSubmissionDate();
			}
			@Override
			public String toString() {
				return "Submission date";
			}
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<Timestamp>(Timestamp.class,new Timestamp(protocol.getSubmissionDate()));
			}
			@Override
			public String getCondition() {
				return String.format(" %s >= ? ",name());
			}
		},			
		updated {
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol.getTimeModified();
			}
			@Override
			public String toString() {
				return "Date updated";
			}
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<Timestamp>(Timestamp.class,new Timestamp(protocol.getTimeModified()));
			}
			@Override
			public String getCondition() {
				return String.format(" %s >= ? ",name());
			}
		},		
		endpoint {
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol.getEndpoint()==null?null:protocol.getEndpoint().getCode();
			}
			@Override
			public String toString() {
				return "Endpoint code";
			}
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return protocol.getEndpoint()==null?null:
					   new QueryParam<String>(String.class,protocol.getEndpoint().getCode());
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs)
					throws SQLException {
				if (protocol.getEndpoint()==null) protocol.setEndpoint(new Enzyme());
				protocol.getEndpoint().setCode(rs.getString("code"));
			}
			@Override
			public String getCondition() {
				return " code = ? ";
			}
		},	
		endpointName {
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol.getEndpoint()==null?null:protocol.getEndpoint().getName();
			}
			@Override
			public String toString() {
				return "Endpoint name";
			}
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return protocol.getEndpoint()==null?null:
					   new QueryParam<String>(String.class,protocol.getEndpoint().getName());
			}
			@Override
			public String getCondition() {
				return " name = ? ";
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs)
					throws SQLException {
				if (protocol.getEndpoint()==null) protocol.setEndpoint(new Enzyme());
				protocol.getEndpoint().setCode(rs.getString("name"));
			}			
		},		
		endpointParentCode {
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol.getEndpoint()==null?null:protocol.getEndpoint().getParentCode();
			}
			@Override
			public String toString() {
				return "Endpoint name";
			}
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return protocol.getEndpoint()==null?null:
					   new QueryParam<String>(String.class,protocol.getEndpoint().getParentCode());
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs)
					throws SQLException {
				if (protocol.getEndpoint()==null) protocol.setEndpoint(new Enzyme());
				protocol.getEndpoint().setCode(rs.getString("parentcode"));
			}
			@Override
			public String getCondition() {
				return " category = ? ";
			}
		},				
		published_status {
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null||protocol.isPublished()==null?null:protocol.isPublished();
			}
			@Override
			public String toString() {
				return "Published status";
			}
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<String>(String.class,protocol==null || (protocol.getPublishedStatus().name()==null) ?
							PublishedStatus.draft.name():protocol.getPublishedStatus().name());
			}
			@Override
			public String getCondition() {
				return String.format(" %s = ? ",name());
			}
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
					protocol.setPublishedStatus(PublishedStatus.valueOf(rs.getString(name())));
				} catch (Exception x) {
					x.printStackTrace();
					protocol.setPublished(false);
				}
			}
			@Override
			public Class getClassType(DBProtocol protocol) {
				return Boolean.class;
			}
			public String getHTMLField(DBProtocol protocol) {
				return String.format("<input name='%s' type='checkbox' title='%s' %s value='%s'>\n",
						name(),
						getDescription(),
						(protocol.isPublished()!=null)&&protocol.isPublished()?"checked":"",
						"on");
			}			
		},		
		atom_uncertainty {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<String>(String.class,protocol.getAtomUncertainty().name());
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
					protocol.setAtomUncertainty(AtomUncertainty.valueOf(rs.getString(name())));
				} catch (Exception x) {protocol.setAtomUncertainty(AtomUncertainty.Uncertain);}
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.getAtomUncertainty();
			}
			
			public String getCondition() {
				return String.format(" %s = ? ",name());
			}
		},		
		product_amount {
			@Override
			public QueryParam getParam(DBProtocol protocol) {
				return new QueryParam<String>(String.class,protocol.getProductAmount().name());
			}			
			@Override
			public void setParam(DBProtocol protocol, ResultSet rs) throws SQLException {
				try {
					protocol.setProductAmount(ProductAmount.valueOf(rs.getString(name())));
				} catch (Exception x) {
					protocol.setProductAmount(ProductAmount.Unknown);
				}
			}		
			@Override
			public Object getValue(DBProtocol protocol) {
				return protocol==null?null:protocol.getProductAmount();
			}
			
			public String getCondition() {
				return String.format(" %s = ? ",name());
			}
		},			
		allowReadByUser {
			@Override
			public Object getValue(DBProtocol protocol) {
				return "";
			}
			@Override
			public String toString() {
				return "Allow read by a registered user";
			}

		},
		allowReadByGroup {
			@Override
			public Object getValue(DBProtocol protocol) {
				return "";
			}
			@Override
			public String toString() {
				return "Allow read by members of organisaiton or project";
			}
		};
				
		public String getCondition() {
			return String.format(" %s = ? ",name());
		}
	
		public Object getValue(DBProtocol protocol) {
			return protocol.getResourceURL().toString();
		}
		public Class getClassType(DBProtocol protocol) {
			return String.class;
		}
		/**
		 * SQL
		 * @param protocol
		 * @param rs
		 * @throws SQLException
		 */
		public void setParam(DBProtocol protocol,ResultSet rs) throws SQLException {}
		public QueryParam getParam(DBProtocol protocol) {
			return null;
		}	
		/**
		 * HTML
		 * @param protocol
		 * @return
		 */
		public String getHTMLField(DBProtocol protocol) {
			Object value = getValue(protocol);
			return String.format("<input name='%s' type='text' size='60' title='%s' value='%s'>\n",
					name(),getDescription(),value==null?"":value.toString());
		}
		/**
		 * Hints
		 * @return
		 */
		public String getDescription() { return toString();}
		public String getHelp(String uri) {return null;}
		public String getExampleValue(String uri) {return null;}
		
		@Override
		public String toString() {
			String name= name();
			return  String.format("%s%s",
					name.substring(0,1).toUpperCase(),
					name.substring(1).toLowerCase());
		}
	}


	public static final ReadProtocol.fields[] sqlFields = new ReadProtocol.fields[] {
		fields.idprotocol,
		fields.version,
		fields.title,
		fields.anabstract,
		fields.iduser,
		fields.curated,
		fields.idproject,
		fields.iduser,
		fields.idorganisation,
		fields.reference,
		fields.status,
		fields.published_status,
		fields.atom_uncertainty,
		fields.product_amount
		
		//ReadProtocol.fields.accesslevel
	};	
	/*
	public ReadProtocol(Integer id) {
		this(id,null,null);
	}
	public ReadProtocol(Integer id, Integer version, Integer year) {
		super();
		setValue(id==null?null:new DBProtocol(id,version,year));
		setFieldname(null);
	}
	public ReadProtocol() {
		this(null,null,null);
	}
	*/
	public ReadProtocol(String identifier) {
		this(identifier==null?null:new DBProtocol(identifier));
	}
	public ReadProtocol(DBProtocol protocol) {
		super();
		setValue(protocol);
		setFieldname(null);
	}		
	public ReadProtocol() {
		this((DBProtocol)null);
	}

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getValue()!=null) {
			if (getValue().isValidIdentifier()) {
				params.add(fields.identifier.getParam(getValue()));		
			} else if (getValue().getTitle()!=null) {
				params.add(fields.title.getParam(getValue()));
			} else if (getValue().getTimeModified()!=null) {
				params.add(fields.updated.getParam(getValue()));
			}
		}
		if ((getFieldname()!=null) && (getFieldname().getID()>0)) 
			params.add(new QueryParam<Integer>(Integer.class, getFieldname().getID()));

		return params;
	}

	public String getSQL() throws AmbitException {
		
		String publishedOnly = getShowUnpublished()?"":" and published_status = 'published'";
		if (onlyUnpublished) publishedOnly = " and published != 'published'";
		String byUser = null;
		if ((getFieldname()!=null) && (getFieldname().getID()>0)) byUser = fields.iduser.getCondition();
		
		if (getValue()!=null) {
			if (getValue().getIdentifier()!=null) {
					return String.format(sql_withkeywords,"where",
							String.format("%s %s %s",
									fields.identifier.getCondition(),
									byUser==null?"":" and ",
									byUser==null?"":byUser,
									publishedOnly));

			} else 
				if (getValue().getTitle()!=null)
					return String.format(sql_withkeywords,"where",
										String.format("%s %s %s %s",
												fields.title.getCondition(),
												byUser==null?"":" and ",
												byUser==null?"":byUser,
												publishedOnly));
				else if (getValue().getTimeModified()!=null)
					return String.format(sql_withkeywords,"where",
									String.format("%s %s %s %s",
											fields.updated.getCondition(),
											byUser==null?"":" and ",
											byUser==null?"":byUser,
											publishedOnly));			
		} 
		String sql = onlyUnpublished?
				String.format(sql_withkeywords,
						"where",byUser==null?"published_status!='published'":String.format("%s %s",byUser,publishedOnly))
				:getShowUnpublished()?
				String.format(sql_withkeywords,
						"where",byUser==null?"":byUser):
				String.format(sql_withkeywords,
						"where",byUser==null?"published_status='published'":String.format("%s %s",byUser,publishedOnly)); //published only
		return sql;
	}

	public DBProtocol getObject(ResultSet rs) throws AmbitException {
		DBProtocol p = super.getObject(rs);
		DBUser user = (DBUser) p.getOwner();
		try {
			user.setUserName(rs.getString("username"));
			user.setFirstname(rs.getString("firstname"));
			user.setLastname(rs.getString("lastname"));
		} catch (Exception x) {x.printStackTrace();}			

		return p;
	}
	@Override
	public String toString() {
		return getValue()==null?"All protocols":String.format("Protocol id=P%s",getValue().getID());
	}


}

class Helper {
	protected static Pattern HTML_tags = Pattern.compile("(<html>|</html>|<head>|</head>|<title>|</title>|<body>|</body>|<b>|</b>|<i>|</i>|\t)");
	protected static Pattern ptag = Pattern.compile("(<p>|<p style=\"margin-top: 0\">|</p>)");
	protected static Pattern CRLF = Pattern.compile("(\n|\r)");
	
	public static String replaceTags(String text) {
	
		if (text == null) return text;
	
		Matcher m = HTML_tags.matcher(text);
		String  newText = "";
		if (m.find()) {
			newText = m.replaceAll("");
		}
		else{
			newText = text;
		}
		m = CRLF.matcher(newText);
		if (m.find()) {
			newText = m.replaceAll("");
		}
		m = ptag.matcher(newText);
		if (m.find()) {
			newText = m.replaceAll("\n");
		}
	
		return newText.trim();
	
	}
}