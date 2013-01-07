package org.xmetdb.rest.protocol.attachments;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.opentox.cli.task.FibonacciSequence;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.restnet.db.update.CallableDBUpdateTask;

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
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
import org.xmetdb.rest.protocol.attachments.db.UpdateAttachment;

/**
 * Imports structure files into the remote ambit service via OpenTox API
 * @author nina
 *
 */
public class CallableAttachmentImporter extends  CallableDBUpdateTask<DBAttachment,Form,String> {
	protected AttachmentURIReporter reporter;
	protected DBAttachment attachment;
	protected String queryService;
	protected String[] algorithms = new String[] {
			"/algorithm/fingerprints","/algorithm/struckeys","/algorithm/smartsprop","/algorithm/inchi"};
	UsernamePasswordCredentials creds = null;
	public CallableAttachmentImporter(Method method, Reference baseReference,
			AttachmentURIReporter reporter,
			DBAttachment attachment,
			Form input,
			String queryService,
			Connection connection, UsernamePasswordCredentials credentials) {
		super(method, input, connection, null);
		this.reporter = reporter;
		this.attachment = attachment;
		this.queryService= queryService;
		this.creds = credentials;
	}

	@Override
	protected DBAttachment getTarget(Form input) throws Exception {
		try {
			RemoteTask task = remoteImport(attachment);
			if (task.isCompletedOK()) {
				attachment.setImported(true);
				return attachment;
			} else throw task.getError();
		} catch (Exception x) {
			throw x;
		}
	}

	@Override
	protected IQueryUpdate<Object, DBAttachment> createUpdate(
			DBAttachment target) throws Exception {
		return new UpdateAttachment(null,target);
	}

	@Override
	protected String getURI(DBAttachment target) throws Exception {
		return String.format("%s/dataset",reporter.getURI(target));
	}
	
	@Override
	protected boolean isNewResource() {
		return false;
	}

	protected RemoteTask remoteImport(DBAttachment target) throws Exception {
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
	@Override
	public String toString() {
		if (Method.POST.equals(method)) {
			return String.format("Import dataset");
		} else if (Method.PUT.equals(method)) {
			return String.format("Update dataset");
		} else if (Method.DELETE.equals(method)) {
			return String.format("Delete dataset");
		}
		return "Read dataset";
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
}
