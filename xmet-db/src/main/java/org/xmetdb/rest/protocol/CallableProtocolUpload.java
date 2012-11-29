package org.xmetdb.rest.protocol;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.modbcum.p.ProcessorException;
import net.idea.modbcum.p.QueryExecutor;
import net.idea.modbcum.p.UpdateExecutor;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.i.task.TaskResult;
import net.toxbank.client.policy.AccessRights;
import net.toxbank.client.resource.User;

import org.apache.commons.fileupload.FileItem;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.groups.DBOrganisation;
import org.xmetdb.rest.groups.DBProject;
import org.xmetdb.rest.groups.db.CreateGroup;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.db.AddAttachment;
import org.xmetdb.rest.protocol.db.CreateProtocol;
import org.xmetdb.rest.protocol.db.CreateProtocolVersion;
import org.xmetdb.rest.protocol.db.DeleteProtocol;
import org.xmetdb.rest.protocol.db.PublishProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocolByID;
import org.xmetdb.rest.protocol.db.UpdateFreeTextIndex;
import org.xmetdb.rest.protocol.db.UpdateKeywords;
import org.xmetdb.rest.protocol.db.UpdateProtocol;
import org.xmetdb.rest.protocol.resource.db.ProtocolQueryURIReporter;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.rest.user.db.ReadUser;
import org.xmetdb.xmet.client.Resources;


public class CallableProtocolUpload extends CallableProtectedTask<String> {
	public enum UpdateMode {
		create {
			@Override
			public String getDescription() {
				return "New observation";
			}
		},
		update {
			@Override
			public String getDescription() {
				return "Update";
			}
		},
		dataTemplateOnly {
			@Override
			public String getDescription() {
				return "Upload attachment(s)";
			}			
		},
		createversion {
			@Override
			public String getDescription() {
				return "Observation new version";
			}				
		};		
		public String getDescription() {return name();}
		}
	protected List<FileItem> input;
	protected ProtocolQueryURIReporter reporter;
	protected Connection connection;
	protected UpdateExecutor exec;
	protected String baseReference;
	protected DBUser user;
	protected File dir;
	protected DBProtocol protocol;
	protected Method method;
	protected UpdateMode updateMode = UpdateMode.create;
	
	public UpdateMode getUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(UpdateMode updateMode) {
		this.updateMode = updateMode;
	}

	public boolean isSetDataTemplateOnly() {
		return UpdateMode.dataTemplateOnly.equals(updateMode);
	}

	public void setSetDataTemplateOnly(boolean setDataTemplateOnly) {
		this.updateMode = UpdateMode.dataTemplateOnly;
	}

	/**
	 * 
	 * @param protocol  NULL if a new protocol, otherwise the protocol which version to be created
	 * @param user
	 * @param input
	 * @param connection
	 * @param r
	 * @param token
	 * @param baseReference
	 * @param dir
	 */
	public CallableProtocolUpload(Method method,DBProtocol protocol,DBUser user,List<FileItem> input,
					Connection connection,
					ProtocolQueryURIReporter r,
					String token,
					String baseReference,
					File dir) throws Exception {
		super(token);
		this.method = method;
		this.protocol = protocol;
		this.connection = connection;
		this.input = input;
		this.reporter = r;
		this.baseReference = baseReference;
		this.user = user;
		this.dir = dir;
		try {
			if (user!=null) {
				retrieveAccountNames(user,connection);
				if (user.getID()<=0) user = null; //throw new Exception("Invalid user "+user.getUserName());
			}
		} catch (Exception x) {
			user = null;
		}
	}
	@Override
	public TaskResult doCall() throws Exception {
		if (Method.POST.equals(method)) return create();
		else if (Method.PUT.equals(method)) return update();
		else if (Method.DELETE.equals(method)) return delete();
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED,method.toString());
	}	
	
	
	@Override
	public String toString() {
		if (Method.DELETE.equals(method)) return "Remove document";
		if (Method.PUT.equals(method)) return "Update document";
		else return updateMode.getDescription();
	}
	
	public TaskResult delete() throws ResourceException {
		try {
			connection.setAutoCommit(false);
			//protocol.setOwner(user);
			exec = new UpdateExecutor<IQueryUpdate>();
			exec.setConnection(connection);
			if (isSetDataTemplateOnly()) {
				//DeleteProtocol k = new DeleteProtocol(protocol);
				//exec.process(k);				
			} else {
				DeleteProtocol k = new DeleteProtocol(protocol);
				exec.process(k);
				connection.commit();
				
			}
			return new TaskResult(String.format("%s%s", baseReference,Resources.protocol),false);
		} catch (ResourceException x) {
			try {connection.rollback();} catch (Exception xx) {}
			throw x;
		} catch (Exception x) {
			try {connection.rollback();} catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		} finally {
			try {exec.close();} catch (Exception x) {}
			try {connection.setAutoCommit(true);} catch (Exception x) {}
			try {connection.close();} catch (Exception x) {}
		}
	}

	public TaskResult create() throws ResourceException {
		boolean existing = protocol!=null&&protocol.getIdentifier()!=null;
		String identifier = existing?protocol.getIdentifier():DBProtocol.generateIdentifier();
		AccessRights policy = new AccessRights(null);
		try {
			if (existing && UpdateMode.dataTemplateOnly.equals(updateMode) && (protocol.getID()<=0))
				retrieveProtocol(protocol,connection);
		} catch (Exception x) {
			
		}
		try {
			protocol = ProtocolFactory.getProtocol(protocol,input, 10000000,dir,policy,updateMode);
			if (user!=null) protocol.setOwner(user);
			else {
				user = (DBUser)protocol.getOwner();
				if ((user!=null) && (user.getID()<=0)&&(user.getResourceURL()!=null)) user.setID(user.parseURI(baseReference));
					//retrieveAccountNames(user, connection);
			}
			if (user==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Observation owner not specified!");
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
		}
		//now write
		switch (updateMode) {
		case dataTemplateOnly:  {
			if ((protocol.getAttachments()!=null) && protocol.getAttachments().size()>0) 
				try {
					
						connection.setAutoCommit(false);
						//protocol.setOwner(user);
						exec = new UpdateExecutor<IQueryUpdate>();
						exec.setConnection(connection);
						AddAttachment k = new AddAttachment(protocol,null);
						for (DBAttachment attachment : protocol.getAttachments()) {
							//.getResourceURL().toString().startsWith("file:")
							k.setObject(attachment);
							exec.process(k);
						}
						connection.commit();
						String uri = String.format("%s%s",reporter.getURI(protocol),org.xmetdb.xmet.client.Resources.attachment);
						return new TaskResult(uri,false);
					
				} catch (ProcessorException x) {
					try {connection.rollback();} catch (Exception xx) {}
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
				} catch (ResourceException x) {
					try {connection.rollback();} catch (Exception xx) {}
					throw x;
				} catch (Exception x) {
					try {connection.rollback();} catch (Exception xx) {}
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
				} finally {
					try {exec.close();} catch (Exception x) {}
					try {connection.setAutoCommit(true);} catch (Exception x) {}
					try {connection.close();} catch (Exception x) {}
				}
			else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"No attachments found!");
//			break;
		}
		default: {
			try {
				connection.setAutoCommit(false);
				//protocol.setOwner(user);
				exec = new UpdateExecutor<IQueryUpdate>();
				exec.setConnection(connection);
				/*
				CreateUser quser = new CreateUser(null);
				//user
				DBUser user = protocol.getOwner() instanceof DBUser?
							(DBUser)protocol.getOwner():
							new DBUser(protocol.getOwner());
			    protocol.setOwner(user);
			    if (user.getID()<=0) user.setID(user.parseURI(baseReference));
				if (user.getID()<=0) {
					quser.setObject(user);
					exec.process(quser);
				}	
				
				for (User u: protocol.getAuthors()) { 
					DBUser author =u instanceof DBUser?(DBUser)u:new DBUser(u);
	 			    if (author.getID()<=0) author.setID(author.parseURI(baseReference));
						if (author.getID()<=0) {
							quser.setObject(author);
							exec.process(quser);
						}	
				}
				*/
				//project
				DBProject p;
				if (protocol.getProject()==null) 
					protocol.setProject(new DBProject(1));  //default xmetdb pproject
				else {
					p = protocol.getProject() instanceof DBProject?
								(DBProject)protocol.getProject():
								new DBProject(protocol.getProject());
				    protocol.setProject(p);
				    if (p.getID()<=0) p.setID(p.parseURI(baseReference));
					if (p.getID()<=0) {
						CreateGroup q1 = new CreateGroup(p);
						exec.process(q1);
					}
				}
				//organisation
				DBOrganisation o;
				if (protocol.getOrganisation()==null)
					protocol.setOrganisation(new DBOrganisation(1)); //default xmetdb org
				else {	
					o = protocol.getOrganisation() instanceof DBOrganisation?
							(DBOrganisation)protocol.getOrganisation():
							new DBOrganisation(protocol.getOrganisation());
					protocol.setOrganisation(o);
				    if (o.getID()<=0) o.setID(o.parseURI(baseReference));
					if (o.getID()<=0) {
						CreateGroup q2 = new CreateGroup(o);
						exec.process(q2);
					}
				}
				
				if (existing) {
					protocol.setIdentifier(identifier); //in case the web form messed up the identifier
					CreateProtocolVersion q = new CreateProtocolVersion(DBProtocol.generateIdentifier(),protocol);
					exec.process(q);
				} else {
					protocol.setIdentifier(identifier);
					CreateProtocol q = new CreateProtocol(protocol);
					exec.process(q);
				}
				
				try {
					UpdateFreeTextIndex q = new UpdateFreeTextIndex(protocol);
					exec.process(q);
				} catch (Exception x) {
					x.printStackTrace();
					//free text index failed, but ignore so far
				}
				String uri = reporter.getURI(protocol);
				
				if ((protocol.getKeywords()!=null) && (protocol.getKeywords().size()>0)) {
					UpdateKeywords k = new UpdateKeywords(protocol);
					exec.process(k);
				}
				/*
				if ((protocol.getAuthors()!=null) && protocol.getAuthors().size()>0) {
					AddAuthors k = new AddAuthors(protocol);
					exec.process(k);
				}
				*/
				if ((protocol.getAttachments()!=null) && protocol.getAttachments().size()>0) 
					for (DBAttachment attachment: protocol.getAttachments()) {
						AddAttachment k = new AddAttachment(protocol,attachment);
						exec.process(k);
					}
				
				connection.commit();
				TaskResult result = new TaskResult(uri,true);
				/*
				try {
					//adding the owner and the authors
					addDefaultProtocolRights(policy,protocol.getOwner(),true,true,true,true);
					//for (User u: protocol.getAuthors())	addDefaultProtocolRights(policy,u,true,true,true,true);
					if ((policy.getRules()!=null) && (policy.getRules().size()>0)) {
						retrieveAccountNames(policy,connection);
						policy.setResource(new URL(uri));
						result.setPolicy(generatePolicy(protocol,policy));
					} else result.setPolicy(null);
				} 
				catch (Exception x) { result.setPolicy(null);}
				*/
				return result;
			} catch (ProcessorException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
			} catch (ResourceException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw x;
			} catch (Exception x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
			} finally {
				try {exec.close();} catch (Exception x) {}
				try {connection.setAutoCommit(true);} catch (Exception x) {}
				try {connection.close();} catch (Exception x) {}
			}
		}
		} //switch

	}

	public TaskResult update() throws ResourceException {
		if ((protocol==null)||(!protocol.isValidIdentifier())||(protocol.getID()<=0)||(protocol.getVersion()<=0)) 
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Can't update: Not an existing protocol!");
	
		AccessRights policy = new AccessRights(null);
		try {
			//get only fields from the web form
		
			DBProtocol newProtocol = ProtocolFactory.getProtocol(null,input, 10000000,dir,policy,updateMode);
			newProtocol.setID(protocol.getID());
			newProtocol.setVersion(protocol.getVersion());
			if (newProtocol.getIdentifier()==null)
				newProtocol.setIdentifier(protocol.getIdentifier());
			
			//all non-null fields will be updated
			if (newProtocol.getProject() != null) {
				DBProject p = (DBProject) newProtocol.getProject();
				p.setID(p.parseURI(baseReference));
			}
			if (newProtocol.getOrganisation() != null) {
				DBOrganisation p = (DBOrganisation) newProtocol.getOrganisation();
				p.setID(p.parseURI(baseReference));
			}		
			if (newProtocol.getOwner() != null) {
				DBUser p = (DBUser) newProtocol.getOwner();
				p.setID(p.parseURI(baseReference));
			}					
			/*
			if (newProtocol.getAuthors()!=null)
				for (User u: newProtocol.getAuthors()) { 
					DBUser author =u instanceof DBUser?(DBUser)u:new DBUser(u);
	 			    if (author.getID()<=0) author.setID(author.parseURI(baseReference));
				}
			*/
			protocol = newProtocol;
		} catch (Exception x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
		}
			String uri = null; 
			try {
				connection.setAutoCommit(false);
				//protocol.setOwner(user);
				exec = new UpdateExecutor<IQueryUpdate>();
				exec.setConnection(connection);

				UpdateProtocol q = new UpdateProtocol(protocol);
				exec.process(q);
				if ((protocol.isPublished()!=null) && protocol.isPublished() && (protocol.getEndpoint()!=null)) {
					PublishProtocol pq = new PublishProtocol(protocol);
					exec.process(pq);
					retrieveProtocolIdentifier(protocol,connection);
				}
				uri = reporter.getURI(protocol);
				if ((protocol.getKeywords()!=null) && (protocol.getKeywords().size()>0)) {
					UpdateKeywords k = new UpdateKeywords(protocol);
					exec.process(k);
				}
				try {
					UpdateFreeTextIndex x = new UpdateFreeTextIndex(protocol);
					exec.process(x);
				} catch (Exception x) {
					x.printStackTrace(); //free text index failed, but ignore so far
				}
				if ((protocol.getAttachments()!=null) && protocol.getAttachments().size()>0) 
					for (DBAttachment attachment: protocol.getAttachments()) {
						AddAttachment k = new AddAttachment(protocol,attachment);
						exec.process(k);
					}
				
				connection.commit();
				TaskResult result = new TaskResult(uri,false);

			
				return result;
			} catch (ProcessorException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
			} catch (ResourceException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw x;				
			} catch (Exception x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
			} finally {
				try {exec.close();} catch (Exception x) {}
				try {connection.setAutoCommit(true);} catch (Exception x) {}
				try {connection.close();} catch (Exception x) {}
			}

	}
	
	protected void addDefaultProtocolRights(AccessRights accessRights, User owner, Boolean get, Boolean post, Boolean put, Boolean delete ) throws Exception {
		/*
		boolean added = false;
		for (PolicyRule rule: accessRights.getRules()) { 
			if (rule instanceof UserPolicyRule)  
				if (rule.hasSubject(owner)) added = true;
		}
		if (!added)
		*/
		//it may be added alreayd, but with different rights. We enforce the owner always having full rights
		accessRights.addUserRule(owner, get,post,put,delete);
	}
	
	protected void retrieveAccountNames(DBUser user,Connection connection) throws Exception {
		if ((user==null) || (user.getUserName()==null)) throw new Exception("No owner defined");
		QueryExecutor qexec = new QueryExecutor();
		try {
			qexec.setCloseConnection(false);
			qexec.setConnection(connection);
			ReadUser getUser = new ReadUser();
			getUser.setValue(user);
			getUser.setCondition(EQCondition.getInstance());
			qexec.setConnection(connection);
			ResultSet rs = null;
			try { 
					rs = qexec.process(getUser); 
					while (rs.next()) { 
						DBUser result = getUser.getObject(rs);
						user.setID(result.getID());
					}	
			} catch (Exception x) { if (rs!=null) rs.close(); }
			qexec.setConnection(null);
		}
		finally { try {qexec.close(); } catch (Exception x) {}}			

	}
	
	protected void retrieveProtocol(DBProtocol protocol,Connection connection) throws Exception {
		QueryExecutor qexec = new QueryExecutor();
		try {
			qexec.setCloseConnection(false);
			qexec.setConnection(connection);
			ReadProtocol getProtocol = new ReadProtocol(protocol);
			qexec.setConnection(connection);
			ResultSet rs = null;
			try { 
					rs = qexec.process(getProtocol); 
					while (rs.next()) { 
						DBProtocol result = getProtocol.getObject(rs);
						protocol.setID(result.getID());
						protocol.setVersion(result.getVersion());
					}	
			} catch (Exception x) { if (rs!=null) rs.close(); }
			qexec.setConnection(null);
		}
		finally { try {qexec.close(); } catch (Exception x) {}}			

	}
	protected void retrieveProtocolIdentifier(DBProtocol protocol,Connection connection) throws Exception {
		QueryExecutor qexec = new QueryExecutor();
		try {
			qexec.setCloseConnection(false);
			qexec.setConnection(connection);
			ReadProtocolByID getProtocol = new ReadProtocolByID();
			getProtocol.setValue(protocol);
			qexec.setConnection(connection);
			ResultSet rs = null;
			try { 
					rs = qexec.process(getProtocol); 
					while (rs.next()) { 
						DBProtocol result = getProtocol.getObject(rs);
						protocol.setIdentifier(result.getIdentifier());
					}	
			} catch (Exception x) { if (rs!=null) rs.close(); }
			qexec.setConnection(null);
		}
		finally { try {qexec.close(); } catch (Exception x) {}}			

	}	
}

