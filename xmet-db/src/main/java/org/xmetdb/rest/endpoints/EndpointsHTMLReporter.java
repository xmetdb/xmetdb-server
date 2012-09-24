package org.xmetdb.rest.endpoints;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.DisplayMode;
import net.idea.restnet.db.QueryURIReporter;
import net.toxbank.client.Resources;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.Reference;
import org.xmetdb.rest.XmetdbHTMLReporter;
import org.xmetdb.rest.endpoints.db.QueryOntology;
import org.xmetdb.rest.protocol.resource.db.ProtocolDBResource.SearchMode;

import ambit2.base.data.Dictionary;
import ambit2.base.data.LiteratureEntry;
import ambit2.base.data.Property;

/**
 * Reporter for {@link Dictionary} or {@link Property}
 * @author nina
 *
 */
@Deprecated
public class EndpointsHTMLReporter<D extends Dictionary> extends XmetdbHTMLReporter<D, IQueryRetrieval<D>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4281274169475316720L;
	protected int count = 0;
	public enum search_features {

		feature_name {
			@Override
			public void setProperty(Property p, Object arg1) {
				p.setName(arg1.toString());
			}
		},
		feature_sameas {
			@Override
			public void setProperty(Property p, Object arg1) {
				p.setLabel(arg1.toString());
			}
		},
		feature_hassource {
			@Override
			public void setProperty(Property p, Object arg1) {
				p.setReference(new LiteratureEntry(arg1.toString(),""));

				
			}
		},
		feature_type {
			@Override
			public void setProperty(Property p, Object arg1) {
				p.setClazz(arg1.toString().equals("STRING")?String.class:Number.class);
				
			}
		},
		feature_id {
			@Override
			public void setProperty(Property p, Object arg1) {
				try {
					p.setId(Integer.parseInt(arg1.toString()));
				} catch (Exception x) {
					
				}
			}
		}

		;	
		public abstract void setProperty(Property p, Object value);
	}
	
	public EndpointsHTMLReporter(Request reference,DisplayMode _dmode,HTMLBeauty htmlBeauty) {
		super(reference,DisplayMode.table.equals(_dmode),null,htmlBeauty);

	}	
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new DictionaryURIReporter(request,doc);
	}
	
	public String toURI(D record) {
		count++;
		//if (count==1) return ""; 
		
		String w = uriReporter.getURI(record);
		
		return String.format(
				"<img src=\"%s/images/%s\">%s<a href=\"%s\">%s</a>&nbsp;<br>",

				uriReporter.getBaseReference().toString(),
				"qmrf/chapter3.png",
				"",						
				w,
				record.getTemplate()
						);

		
	}	
	
	public String up(D record) {
		//if (count==1) return ""; 
		
		return String.format("%s%s",
				uriReporter.getBaseReference(),
				EndpointsResource.resource
			);

		
	}	
	@Override
	public Object processItem(D record) throws AmbitException {


		try {
			output.write("<tr>");
			output.write("<td>");
			if (((EndpointTest)record).getCode()!=null)
				output.write(((EndpointTest)record).getCode());
			output.write("</td>");
			/*
			output.write("<td>");
			if ((record!=null)&&record.getReference()!=null)
				output.write(String.format("<a href='%s'>%s</a>",up(record),"Up"));
			output.write("</td>");
			*/
			output.write(String.format("<td>%s</td>",toURI(record)));
			output.write("<td>");
			output.write(String.format("<a href='%s%s?option=%s&search=%s'>%s</a>", 
				getUriReporter().getBaseReference(),
				Resources.protocol,
				SearchMode.endpointcode,
				Reference.encode(((EndpointTest)record).getCode()),
				"XMETDB documents"
			));
			
			output.write("</td>");
			output.write("</tr>");
		} catch (Exception x) {
			StringWriter w = new StringWriter();
			x.printStackTrace(new PrintWriter(w));
			Context.getCurrentLogger().severe(w.toString());
		}
		return null;
	}
	
	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		try {
			output.write("<div style='float:left;width:100%;align:center' >");
			output.write("<table class='datatable'  cellpadding='0' border='0' width='100%' cellspacing='0'>\n");
			output.write("<thead>\n");	
			output.write(String.format("<th>%s</th>","Code"));
			//output.write(String.format("<th>%s</th>","Category"));
			output.write(String.format("<th>%s</th>","Name"));
			output.write(String.format("<th>%s</th>","XMETDB documents"));
			output.write("</thead>\n");
			output.write("<tbody>\n");
		} catch (Exception x) {
			
		}	
		
	}
	@Override
	protected void printTable(Writer output, String uri, D item) {

		
	}
	@Override
	protected void printForm(Writer output, String uri, D item,
			boolean editable) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void printPageNavigator(IQueryRetrieval<D> query)
			throws Exception {
		try {
			String url = "<li><a class='%s' style='width: 25em;' href='%s%s/%s/%s' title='%s'>%s</a></li>";
			output.write("<div><ul id='hnavlist' >");
			output.write(String.format(url, 
					query instanceof QueryOntology?"pselectable":"current",
					uriReporter.getBaseReference(),
					EndpointsResource.resource,
					"All",
					"",
					"",
					"All enzymes"));
			if (query instanceof QueryOntology) {
					EndpointTest et = ((QueryOntology<EndpointTest>)query).getValue();
					/*
					if (et.getReference()!=null)
					output.write(String.format(url, 
							"pselectable",
							uriReporter.getBaseReference(),
							EndpointsResource.resource,
							"All",
							Reference.encode(et.getTitle()),
							et.getTitle(),
							et.getTitle())
							);
							*/
					
					output.write(String.format(url, 
							"current",
							uriReporter.getBaseReference(),
							EndpointsResource.resource,
							"All",
							Reference.encode(et.getName()),
							et.getName(),
							et.getName())
							);
			}
			output.write("</ul></div><br>");
			output.flush();
		} catch (Exception x) {
			
		}
	}
	@Override
	public void header(Writer w, IQueryRetrieval<D> query) {
		super.header(w, query);
	}
	@Override
	public void footer(Writer output, IQueryRetrieval<D> query) {
		try {


			output.write("</tbody></table>");
			/*
			output.write("<div class=\"ui-widget-content\">	<label for=\"tags\">Tags: </label><input size='40' id=\"tags\"></div>");
			output.write(
			String.format(		
			"<script>\n"+
			"$(function() {\n"+
				"$( \"#tags\" ).autocomplete({\n"+
					"source: '%s/catalog?media=application/json',minLength:3\n"+
				"});\n"+
			"});\n"+
			"</script>\n",uriReporter.getBaseReference()));
			
			output.write("</div>");
			*/
			if (!headless) {
				if (htmlBeauty == null) htmlBeauty = new HTMLBeauty();
				htmlBeauty.writeHTMLFooter(output, "", uriReporter.getRequest());			
			}
			output.flush();
		} catch (Exception x) {
			
		}
	}
	
}
