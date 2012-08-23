package org.xmetdb.rest.protocol.attachments;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.c.task.TaskCreator;
import net.idea.restnet.c.task.TaskCreatorForm;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.i.task.ICallableTask;
import net.idea.restnet.i.task.Task;

import org.apache.commons.fileupload.FileItem;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.FileResource;
import org.xmetdb.rest.protocol.resource.db.DownloadDocumentConvertor;
import org.xmetdb.xmet.client.Resources;

public class AttachmentDatasetResource extends ProtocolAttachmentResource {

	Object key ;
	Object aKey ;
	
	@Override
	protected IQueryRetrieval<DBAttachment> createUpdateQuery(Method method,
			Context context, Request request, Response response)
			throws ResourceException {
		key = request.getAttributes().get(FileResource.resourceKey);
		aKey = request.getAttributes().get(ProtocolAttachmentResource.resourceKey);	
		if (key==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,String.format("No protocol ID"));
		if (Method.POST.equals(method)) {
			if ((key==null)||(aKey==null) )  //post allowed only on /protocol/id/attachment/a1/dataset
				return null;
			else {
				return super.createQuery(context, request, response);
			}
		} //POST only so far
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);		
	}
	

	@Override
	protected boolean isAllowedMediaType(MediaType mediaType)
			throws ResourceException {
		return MediaType.APPLICATION_WWW_FORM.equals(mediaType);
	}
	@Override
	protected String getObjectURI(Form queryForm) throws ResourceException {
		return null;
	}
	
	@Override
	protected TaskCreator getTaskCreator(Form form, final Method method, boolean async, final Reference reference) throws Exception {
		return new TaskCreatorForm<Object,DBAttachment>(form,async) {
			@Override
			protected ICallableTask getCallable(Form form,
					DBAttachment item) throws ResourceException {
				return createCallable(method,form,item);
			}
			@Override
			protected Task<Reference, Object> createTask(
					ICallableTask callable,
					DBAttachment item) throws ResourceException {
					return addTask(callable, item,reference);
				}
		};
	}
	
	@Override
	protected TaskCreator getTaskCreator(List<FileItem> fileItems,
			Method method, boolean async) throws Exception {
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Not a web form!");
	}
	
	@Override
	protected TaskCreator getTaskCreator(File file, MediaType mediaType,
			Method method, boolean async) throws Exception {
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Not a web form!");
	}
	
	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			Form form, DBAttachment item) throws ResourceException {
		Connection conn = null;
		try {
			AttachmentURIReporter r = new AttachmentURIReporter(getRequest(),String.format("%s/%s",Resources.protocol,key.toString()));
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			String ambituser = ((TaskApplication)getApplication()).getProperty(Resources.AMBIT_LOCAL_USER);
			String ambitpass = ((TaskApplication)getApplication()).getProperty(Resources.AMBIT_LOCAL_PWD);
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(ambituser,ambitpass);
			return new CallableAttachmentImporter(method,getRequest().getRootRef(), r,item, form,getQueryService(), conn,creds);
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	};

	
	@Override
	public IProcessor<IQueryRetrieval<DBAttachment>, Representation> createConvertor(
			Variant variant) throws AmbitException, ResourceException {
		String filenamePrefix = getRequest().getResourceRef().getPath();
		if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) 
			return new StringConvertor(	//TODO return ambit2 URIs
					new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(getRequest(),
							protocol==null?"":String.format("%s/%s",Resources.protocol ,protocol.getIdentifier()))
					,MediaType.TEXT_URI_LIST,filenamePrefix);
		if (variant.getMediaType().equals(MediaType.TEXT_HTML)) 
			return new StringConvertor(createHTMLReporter(headless),MediaType.TEXT_HTML,filenamePrefix);	
			else	
				return new DownloadDocumentConvertor(createFileReporter(),null,filenamePrefix);
	}
	
	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException {
		AttachmentHTMLReporter rep = new AttachmentHTMLReporter(protocol,getRequest(),true,null,getHTMLBeauty());
		rep.setHeadless(headless);
		return rep;
	}
}
