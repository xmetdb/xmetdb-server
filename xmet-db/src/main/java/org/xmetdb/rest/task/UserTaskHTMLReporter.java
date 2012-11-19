package org.xmetdb.rest.task;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.SimpleTaskResource;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.TaskHTMLReporter;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.i.task.Task;
import net.idea.restnet.i.task.TaskResult;
import net.idea.restnet.i.task.Task.TaskStatus;

import org.restlet.Request;

public class UserTaskHTMLReporter<USERID> extends TaskHTMLReporter<USERID> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8566674498695951801L;
	protected String title = "User profile updated";
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public UserTaskHTMLReporter(ITaskStorage<USERID> storage,Request ref,ResourceDoc doc) {
		this(storage,ref,doc,null);
	}
	public UserTaskHTMLReporter(ITaskStorage<USERID> storage,Request ref,ResourceDoc doc,HTMLBeauty htmlbeauty) {
		super(storage,ref,doc,htmlbeauty);
	}

	public void header(Writer output, Iterator<UUID> query) {
		
		try {
			if (!headless) {
				if (htmlBeauty==null) htmlBeauty = new HTMLBeauty();
				htmlBeauty.writeHTMLHeader(output, htmlBeauty.getTitle(), getRequest(),
					//"<meta http-equiv=\"refresh\" content=\"10\">",
					"",
					getDocumentation()
					);//,"<meta http-equiv=\"refresh\" content=\"10\">");
			}
			printNavigation();
		} catch (Exception x) {
			
		}
	}
	
	@Override
	public void processItem(UUID name, Writer output) {
		Task<TaskResult,USERID> item = storage.findTask(name);
		String t = "";
		String status = "Unknown";
		try {
			t = item.getUri()==null?"":item.getUri().toString();
			status = item.getStatus().toString();
		} catch (Exception x) {
			x.printStackTrace();
			status = "Error";
			t = "";
		} finally {

			try {
				String uri = String.format("%s%s/%s",baseReference,SimpleTaskResource.resource,item.getUuid());
				output.write(
				String.format(		
				"<div class=\"ui-widget \" style=\"margin-top: 20px; padding: 0 .7em;\">\n"+
				"<div class=\"ui-widget-header ui-corner-top\"><p><a href='%s'>User profile update</a> %s &nbsp;</p></div>\n"+
				"<div class=\"ui-widget-content ui-corner-bottom %s\">\n"+
				"%s\n"+
				"<p>Name:&nbsp;<strong>%s</strong></p>" +
				"<p>Status:&nbsp;<a href='%s' id='result'>%s</a>&nbsp;<img src='%s/images/%s' id='status'>"+
				"%s\n"+
				"</p>\n"+				
				"</div></div>\n",

				uri,
				new Date(item.getStarted()),
				item.isDone()?(item.getError()==null?"":"ui-state-highlight"):"",
				item.isDone()?"":
					String.format("<script>checkTask('%s','result', 'status', '%s/images/tick.png', '%s/images/cross.png','%s');</script>\n",
								uri,baseReference,baseReference,title ),
				item.getName(),
				item.isDone()?item.getUri().getUri():"", //href
				TaskStatus.Completed.equals(item.getStatus())?title:
				item.getStatus() , //status
				baseReference,item.isDone()?item.getError()!=null?"cross.png":"tick.png":"progress.gif", //image
				getErrorReport(item.getError())
				));


			} catch (Exception x) {
				x.printStackTrace();
			}
		}
	};
	
	protected String getErrorReport(Throwable x) {
		if (x==null) return "";
		if (x.getCause()!=null) x = x.getCause();

		StringWriter details = new StringWriter();
			x.printStackTrace(new PrintWriter(details) {
				@Override
				public void print(String s) {
					super.print(String.format("%s<br>", s));
					
				}
			});
		String detailsDiv = details==null?"":
			String.format("<a href=\"#\" style=\"padding: 5px 10px;\" onClick=\"toggleDiv('%s'); return false;\">Details: %s</a><br><div style='display: none;' id='details'><p>%s</p></div>\n",
					"details",
					x.getMessage(),
					details);			
		return detailsDiv;
	}
	
	@Override
	protected void printNavigation() throws Exception {
	
	}
	
}
