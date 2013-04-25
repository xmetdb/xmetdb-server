package org.xmetdb.rest.protocol;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.modbcum.p.ProcessorException;
import net.idea.modbcum.p.QueryExecutor;
import net.idea.modbcum.p.UpdateExecutor;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.opentox.cli.task.FibonacciSequence;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.groups.db.CreateGroup;
import net.idea.restnet.i.task.TaskResult;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;
import net.toxbank.client.policy.AccessRights;
import net.toxbank.client.resource.User;

import org.apache.commons.fileupload.FileItem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
import org.xmetdb.rest.protocol.attachments.db.AddAttachment;
import org.xmetdb.rest.protocol.db.CreateProtocol;
import org.xmetdb.rest.protocol.db.CreateProtocolVersion;
import org.xmetdb.rest.protocol.db.DeleteProtocol;
import org.xmetdb.rest.protocol.db.PublishProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.db.ReadProtocolByID;
import org.xmetdb.rest.protocol.db.UpdateProtocol;
import org.xmetdb.rest.protocol.resource.db.ProtocolQueryURIReporter;
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
	
	protected String queryService;
	protected UsernamePasswordCredentials creds;
	
	
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
					File dir,String queryService, UsernamePasswordCredentials creds ) throws Exception {
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
		this.queryService = queryService;
		this.creds = creds; 
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
					//TODO get the real version generated in order to return corect URI
					CreateProtocolVersion q = new CreateProtocolVersion(DBProtocol.generateIdentifier(),protocol);
					exec.process(q);
				} else {
					protocol.setIdentifier(identifier);
					CreateProtocol q = new CreateProtocol(protocol);
					exec.process(q);
					protocol.setID(q.getObject().getID());
					protocol.setVersion(q.getObject().getVersion());
					protocol.setIdentifier(String.format("XMETDB%d", protocol.getID()));
				}

				String uri = reporter.getURI(protocol);
				
				/*
				if ((protocol.getAuthors()!=null) && protocol.getAuthors().size()>0) {
					AddAuthors k = new AddAuthors(protocol);
					exec.process(k);
				}
				*/
				if ((protocol.getAttachments()!=null) && protocol.getAttachments().size()>0)  {
					for (DBAttachment attachment: protocol.getAttachments()) {
						AddAttachment k = new AddAttachment(protocol,attachment);
						exec.process(k);
						attachment.setID(k.getObject().getID());
					}
				
				}
				
				connection.commit();
				
				//if commit succeeds, start import, but don't wait for it to complete
				if ((protocol.getAttachments()!=null) && protocol.getAttachments().size()>0)  {
					try {
						RemoteImport rimport = new RemoteImport(queryService, creds);
						for (DBAttachment attachment: protocol.getAttachments()) {
							//String attachmentURL = String.format("riap://component/protocol/XMETDB%d/attachment/A%d/dataset",
								//	protocol.getID(),attachment.getID());
							//postImportJob(attachmentURL,getToken());
							rimport.remoteImport(attachment);
							
						}
					} finally {
					}
				}
				//will redirect to the SOM editing page
				TaskResult result = new TaskResult(uri + "/som",true);
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
	
	protected String postImportJob(String url, String token)  {
		ClientResource cr = null;
		Representation repr = null;
		try {
			Form form = new Form();
			form.add("import", "true");
			
			cr = new ClientResource(url);
			Form headers = (Form) cr.getRequest().getAttributes().get("org.restlet.http.headers");
			if (headers == null) {
			    headers = new Form();
			    cr.getRequest().getAttributes().put("org.restlet.http.headers", headers);
			}
			headers.add("Cookie", "xmetdb="+token);
			repr = cr.post(form.getWebRepresentation(),MediaType.TEXT_URI_LIST);
			return repr.getText();
		} catch (ResourceException x) {
			x.printStackTrace();
			if (Status.CLIENT_ERROR_NOT_FOUND.equals(x.getStatus())) {

				return x.getStatus().toString();
			}
			else throw x;
		} catch (Exception x) {
			x.printStackTrace();

		} finally {
			try { repr.release(); } catch (Exception x) {}
			try {cr.release();} catch (Exception x) {}
		}
		return null;
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

				if ((protocol.getAttachments()!=null) && protocol.getAttachments().size()>0) 
					for (DBAttachment attachment: protocol.getAttachments()) {
						AddAttachment k = new AddAttachment(protocol,attachment);
						exec.process(k);
					}
				
				connection.commit();
				
				if ((protocol.getAttachments()!=null) && protocol.getAttachments().size()>0)  {
					try {
						for (DBAttachment attachment: protocol.getAttachments()) {
							String attachmentURL = String.format("riap://component/protocol/XMETDB%d/attachment/A%d/dataset",
									protocol.getID(),attachment.getID());
							if (!attachment.isImported())
								postImportJob(attachmentURL,getToken());
						}
					} finally {
					}
				}				
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


class RemoteImport {
	protected String[] algorithms = new String[] {"/algorithm/fingerprints","/algorithm/struckeys","/algorithm/smartsprop","/algorithm/inchi"};
	protected String queryService;
	UsernamePasswordCredentials creds = null;
	
	public RemoteImport(String queryService,UsernamePasswordCredentials creds) {
		super();
		this.queryService = queryService;
		this.creds = creds;
	}
	protected RemoteTask remoteImport(DBAttachment attachment) throws Exception {
		Reference uri = new Reference(queryService);
		
		HttpClient client = createHTTPClient(uri.getHostDomain(),uri.getHostPort());
		RemoteTask task = new RemoteTask(client, 
					new URL(String.format("%s/dataset",queryService)), 
					"text/uri-list", createPOSTEntity(attachment), HttpPost.METHOD_NAME);
	
		try {
			task = wait(task, System.currentTimeMillis());
			String dataset_uri = "dataset_uri";
			URL dataset = task.getResult();
			if (task.isCompletedOK()) {
				if (!"text/uri-list".equals(attachment.getFormat())) { //was a file
					//now post the dataset uri to get the /R datasets (query table)
					attachment.setFormat("text/uri-list");
					attachment.setDescription(dataset.toExternalForm());
					task = new RemoteTask(client, 
							new URL(String.format("%s/dataset",queryService)), 
							"text/uri-list", createPOSTEntity(attachment), HttpPost.METHOD_NAME);
					task = wait(task, System.currentTimeMillis());
				}
				
				Form form = new Form();
				form.add(dataset_uri, dataset.toURI().toString());
				for (String algorithm: algorithms) { //just launch tasks and don't wait
					List<NameValuePair> formparams = new ArrayList<NameValuePair>();
					formparams.add(new BasicNameValuePair(dataset_uri, dataset.toURI().toString()));
					HttpEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
					HttpClient newclient = createHTTPClient(uri.getHostDomain(),uri.getHostPort());
					try {
						new RemoteTask(newclient, 
									new URL(String.format("%s%s",queryService,algorithm)), 
									"text/uri-list", entity, HttpPost.METHOD_NAME);

					} catch (Exception x) { } finally { 
						try {newclient.getConnectionManager().shutdown();} catch (Exception x) {}
					}
				}
			}
			
		} catch (Exception x)  {
			task.setError(new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY,String.format("Error importing chemical structures dataset to %s",uri),x));
		} finally {
			try {client.getConnectionManager().shutdown();} catch (Exception x) {}
		}
		return task;
	}
	
	protected HttpEntity createPOSTEntity(DBAttachment attachment) throws Exception {
		Charset utf8 = Charset.forName("UTF-8");

		if ("text/uri-list".equals(attachment.getFormat())) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("title", attachment.getTitle()));
            formparams.add(new BasicNameValuePair("dataset_uri", attachment.getDescription()));
            formparams.add(new BasicNameValuePair("folder", attachment_type.data_training.equals(attachment.getType())?"substrate":"product"));
			return new UrlEncodedFormEntity(formparams, "UTF-8");
		} else {
			if (attachment.getResourceURL()==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Attachment resource URL is null! ");
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,utf8);
			entity.addPart("title", new StringBody(attachment.getTitle(),utf8));
			entity.addPart("seeAlso", new StringBody(attachment.getDescription(),utf8));
			entity.addPart("license", new StringBody("XMETDB",utf8));
			entity.addPart("file", new FileBody(new File(attachment.getResourceURL().toURI())));
			return entity;
		}
//match, seeAlso, license
	}
	
	protected HttpClient createHTTPClient(String hostName,int port) {
		DefaultHttpClient  cli = new DefaultHttpClient();
		List<String> authpref = new ArrayList<String>();
		authpref.add(AuthPolicy.BASIC);
		cli.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
		cli.getCredentialsProvider().setCredentials(
		        new AuthScope(hostName,port), 
		        creds);		
		((DefaultHttpClient)cli).addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context)
					throws HttpException, IOException {
				//if (ssoToken != null)
					//request.addHeader("subjectid",ssoToken.getToken());
			}
		});
		return cli;
	}
	
	protected long pollInterval = 1500;
	protected long pollTimeout = 10000L*60L*5L; //50 min
	
	protected RemoteTask wait(RemoteTask task, long now) throws Exception {
		if (task.getError()!=null) throw task.getError();
		if (task.getResult()==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,String.format("%s returns empty contend instead of URI"));
		String result = task.getResult().toString();
		FibonacciSequence sequence = new FibonacciSequence();
		while (!task.poll()) {
			if (task.getError()!=null) throw task.getError();
			Thread.sleep(sequence.sleepInterval(pollInterval,true,1000 * 60 * 5)); 				
			Thread.yield();
			if ((System.currentTimeMillis()-now) > pollTimeout) 
				throw new ResourceException(Status.SERVER_ERROR_GATEWAY_TIMEOUT,
						String.format("%s %s ms > %s ms",result==null?task.getUrl():result,System.currentTimeMillis()-now,pollTimeout));
		}
		
		if (task.getError()!=null) 
			if(task.getError() instanceof ResourceException)
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY,
						String.format("%s %d %s",result==null?task.getUrl():result,
						((ResourceException)task.getError()).getStatus().getCode(),
						task.getError().getMessage()),
						task.getError());
			else
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY,
						String.format("%s %s",result==null?task.getUrl():result,task.getError().getMessage()),
						task.getError());
		
	
		return task;
	}	 	
}
