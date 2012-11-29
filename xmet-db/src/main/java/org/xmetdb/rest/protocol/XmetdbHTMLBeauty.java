package org.xmetdb.rest.protocol;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.idea.restnet.c.AbstractResource;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.toxbank.client.resource.Query;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.security.Role;
import org.restlet.security.User;
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
import org.xmetdb.rest.protocol.db.ReadProtocol;
import org.xmetdb.rest.protocol.resource.db.ProtocolDBResource.SearchMode;
import org.xmetdb.rest.user.alerts.db.DBAlert;
import org.xmetdb.xmet.client.XMETDBRoles;
import org.xmetdb.xmet.client.Resources;

public class XmetdbHTMLBeauty extends HTMLBeauty {
	
	private final static String qmrfEditorDownloadLink = "http://ambit.uni-plovdiv.bg/downloads/qmrf/QMRFEditor-v2.0.0-setup.exe";
	private final static String qmrfEditorEmail = "admin@xmetdb.org";
	private final static String XMETDB_title = "Xenobiotics metabolism database";
	private final static String[] css = new String[] {
		"<link href=\"%s/style/ambit.css\" rel=\"stylesheet\" type=\"text/css\">\n",
		"<!--[if IE 7]><link rel='stylesheet' type='text/css' media='all' href='%s/style/ambit-msie7.css'><![endif]-->",
		"<link href=\"%s/style/jquery-ui-1.8.18.custom.css\" rel=\"stylesheet\" type=\"text/css\">\n",
		"<link href=\"%s/style/jquery.dataTables.css\" rel=\"stylesheet\" type=\"text/css\">\n",
		"<link href=\"%s/images/favicon.ico\" rel=\"shortcut icon\" type=\"image/ico\">\n"
	};

	private final static String[] js = new String[] {
		"<script type='text/javascript' src='%s/jquery/jquery-1.7.2.min.js'></script>\n",
		"<script type='text/javascript' src='%s/jquery/jquery-ui-1.8.18.custom.min.js'></script>\n",
		"<script type='text/javascript' charset='utf8' src='%s/jquery/jquery.dataTables-1.9.0.min.js'></script>\n",
		"<script type='text/javascript' src='%s/scripts/jopentox.js'></script>\n",
		//"<script type='text/javascript' src='%s/scripts/jendpoints.js'></script>\n",
		"<script type='text/javascript' src='%s/jme/jme.js'></script>\n"
	};
	
	//meta
	private final static String[] metaTag = new String[] {
		"<meta name=\"robots\" content=\"index,follow\"><META NAME=\"GOOGLEBOT\" CONTENT=\"index,FOLLOW\">\n",
		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n",
		"<meta http-equiv='content-type' content='text/html; charset=iso-8859-1' />\n"
	};
	
	// Google +1 buttons
	private final static String googlePlusInit =
		"<script type='text/javascript'>" +
		"(function() {" +
		"var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;" +
		"po.src = 'https://apis.google.com/js/plusone.js';" +
		"var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);" +
		"})();" +
		"</script>";

	// Facebook JavaScript SDK
	private final static String facebookInit =
			"<div id=\"fb-root\"></div>\n" +
			"<script>(function(d, s, id) {\n" +
			"var js, fjs = d.getElementsByTagName(s)[0];\n" +
			"if (d.getElementById(id)) return;\n" +
			"js = d.createElement(s); js.id = id;\n" +
			"js.src = \"//connect.facebook.net/en_GB/all.js#xfbml=1\";\n" +
			"fjs.parentNode.insertBefore(js, fjs);\n" +
			"}(document, 'script', 'facebook-jssdk'));</script>\n";
	
	// Twitter JS
	private final static String twitterInit =
			"<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id))" +
			"{js=d.createElement(s);js.id=id;js.src=\"//platform.twitter.com/widgets.js\";" +
			"fjs.parentNode.insertBefore(js,fjs);}}(document,\"script\",\"twitter-wjs\");</script>\n";

	// LinkedIn
	private final static String linkedInInit = "<script src='https://platform.linkedin.com/in.js' type='text/javascript'></script>\n";
	
	// ВКонтакте (VKontakte)
	// Disabled for the time being, as there are issues with it.
	//final static String vKontakteInit = "<script type=\"text/javascript\" src=\"https://vk.com/js/api/share.js?11\" charset=\"windows-1251\"></script>";
	
	// table row expander script
	private final static String toggleDivScript =
			"<script type='text/javascript'>function toggleDiv(divId) {\n" +
			"$('#'+divId).toggle();\n" +
			"if ($('#'+divId+'_toggler').hasClass('togglerPlus')) {\n" +
			"$('#'+divId+'_toggler').removeClass('togglerPlus');\n" +
			"$('#'+divId+'_toggler').addClass('togglerMinus');\n" +
			"} else if ($('#'+divId+'_toggler').hasClass('togglerMinus')) {\n" +
			"$('#'+divId+'_toggler').removeClass('togglerMinus');\n" +
			"$('#'+divId+'_toggler').addClass('togglerPlus');\n" +
			"}\n" +
			"}</script>\n";	

	private final static String simpleToggleDivScript =
			"<script type='text/javascript'>function toggleDivSimple(divId) {$('#'+divId).toggle();}</script>\n";
	
    // show the footer when the mouse is near
	private final static String showFooterScript =
    	"<script type='text/javascript'>\n" +
    		"$(document).ready( function () {\n" +
    			"$('div#footer-in').mouseenter( function () {\n" +
    					"$('div#footer').stop().animate({bottom: '15px'}, 'fast');\n" +
    			"});\n" +
    			"$('div#footer-out').mouseleave( function () {\n" +
    					"$('div#footer').stop().animate({bottom: '-17px'}, 'slow');\n" +
    			"});\n" +
    		"});\n" +
    	"</script>\n";
	
	// initialize the toTop link and show it when the contents are scrolled down enough
	private final static String showToTopLink =
		"<script type='text/javascript'>\n" +
			"$(document).ready( function () {\n" +
				"$('#toTop').click( function () {\n" +
					"$('html, body').animate({scrollTop: '0'}, 1000);" +
				"});" +
				"$(window).scroll( function () {\n" +
					"var h = $('#header').height();\n" +
					"var p = $(window).scrollTop();\n" +
					"if ( p > (h + 100) ) {" +
						"$('#toTop').stop().animate({left: '-5px'}, 'fast');" +
					"} else if ( p < (h + 50) ) {" +
						"$('#toTop').stop().animate({left: '-30px'}, 'slow');" +
					"}" +
				"});\n" +
			"});\n" +
		"</script>\n";
	
	// the JRC IHCP logo
	private final static String logoTopLeft =
			"<a href='http://xmetdb.org/'>\n" +
			"<img class='logo_top-left' src='%s/images/logo_xmetdb.png' alt='XMETDB logo'>\n" +
			"</a>\n";
	
	// menu mouseover effect
	private final static String menuMouseOverScript =
		"<script>\n" +
		"$('a.selectable').mouseover(function () { $(this).addClass('hovered');    } );\n" +
		"$('a.selectable').mouseout(function  () { $(this).removeClass('hovered'); } );\n" +
		"</script>\n";

	// top links
	private final static String topLinks =
		"<ul class='topLinks'>\n" +
			"<li class='topLinks'>\n" +
				"<a class='topLinks help' href='%s/help'>Help</a>\n" +
			"</li>\n" +
			"<li class='topLinks'>|</li>\n" +
			"<li class='topLinks'>\n" +
				"<a class='topLinks help' href='%s/register'>Register</a>\n" +
			"</li>\n" +
			"%s" +
		"</ul>\n";
	
	// home menu option
	private final static String homeMenuOption =
		"<li>" +
			"<a class='selectable' title='Go to the welcome page' href='%s'>" +
				"<img class='logo_home_menu' src='%s/images/logo_menu.png'>" +
					"Home" +
			"</a>" +
		"</li>";
		
	// log in/out link
	private final static String logInOutLinkTemplate =
		"<li class='topLinks'>|</li>\n" +
		"<li class='topLinks'>\n" +
		"<a class='topLinks login' title='%s' href='%s%s' %s>%s</a>\n" +
		"%s" + // this is a placeholder for the logout form
		"</li>\n";

	// footer
	private final static String footerText =
		"<div id='footer-out'>\n" +
		"<div id='footer-in'>\n" +
		"<div id='footer'>\n" +
		"Developed by <a class='footerLink' href='http://www.ideaconsult.net/'>" +
		"IDEAconsult Ltd.</a> (2012) on behalf of " +
		"<a class='footerLink' href='http://xmetdb.org/'>XMETDB</a>" +
		"</div>\n" +
		"</div>\n" +
		"</div>\n";

	// toTop
	private final static String toTopLink =
		"<div id='toTop'>\n" +
		"&Delta;<br>&Delta;<br>&Delta;\n" +
		"</div>\n";
		
	private final static String _trStart = "<tr>\n";
	private final static String _trEnd = "</tr>\n";
	private final static String _tdStart = "<td>";
	private final static String _tdEnd = "</td>";
	private final static String _tableStart = "<table>\n";
	private final static String _tableEnd = "</table>\n";
	private final static String _divStart = "\n<div>\n";
	private final static String _divEnd = "\n</div>\n";
	
	private boolean loadTabs;

	public enum update_mode {
		publish {
			@Override
			public String toString() {
				return "Publish ";
			}
		},		
		delete {
			@Override
			public String toString() {
				return "Delete ";
			}
		},			
		update {
			@Override
			public String toString() {
				return "Update ";
			}
		},
		attachments {
			@Override
			public String toString() {
				return "Add attachment(s)";
			}
		},
		newdocument {
			@Override
			public String toString() {
				return "Upload new ";
			}	
		},
		newversion {
			@Override
			public String toString() {
				return "New version ";
			}
		}		
	}

	protected String condition;
	protected SearchMode option;

	protected void setLoadTabs(boolean loadTabs) {
		this.loadTabs = loadTabs;
	}
	public boolean isLoadTabs() {
		return loadTabs;
	}
	
	public String getSearchQuery() {
		return searchQuery;
	}
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public XmetdbHTMLBeauty(String searchURI) {
		this(searchURI,!Resources.protocol.equals(searchURI));
	}
	public XmetdbHTMLBeauty(String searchURI,boolean loadTabs) {
		super(searchURI==null?Resources.protocol:searchURI);
		setSearchTitle("Observations");
		setLoadTabs(loadTabs);
	};

		@Override
		protected String getHomeURI() {
			return "http://xmetdb.org";
		}
		@Override
		protected String getLogoURI(String root) {
			return null;//String.format("%s/images/ambit-logo.png",root==null?"":root);
		}
		@Override
		public String getTitle() {
			return XMETDB_title;
		}
		public void writeTopLinks(Writer w,String title,Request request,String meta,ResourceDoc doc, Reference baseReference) throws IOException {
		}
	
		@Override
		public String getLoginLink() {
			return "Login";
		}
		@Override
		public void writeTopHeader(Writer w,String title,Request request,String meta,ResourceDoc doc) throws IOException {
			Reference baseReference = request==null?null:request.getRootRef();
			
			// Determine if the request is made by Microsoft Internet Explorer 7, as many elements on the page break on it.
			if (request!=null) {
				setMsie7(request.getClientInfo().getAgent().toLowerCase().indexOf("msie 7.")>=0?true:false);
			} else {
				setMsie7(false);
			}
			
			w.write(
					"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
					);
			
			w.write(String.format("<html %s %s %s>",
					"xmlns=\"http://www.w3.org/1999/xhtml\"",
					"xmlns:dc=\"http://purl.org/dc/elements/1.1/\"",
					"xmlns:ot=\"http://opentox.org/api/1.1/\"")
					);
			
			// HEAD starts here.
			w.write(String.format("<head> <meta property=\"dc:creator\" content=\"%s\"/> <meta property=\"dc:title\" content=\"%s\"/>",
					request.getResourceRef(),
					title
					));
			
			Reference ref = request.getResourceRef().clone();
			ref.addQueryParameter("media", Reference.encode("application/rdf+xml"));
			w.write(String.format("<link rel=\"meta\" type=\"application/rdf+xml\" title=\"%s\" href=\"%s\"/>\n",
					title,
					ref
					)); 
			w.write(String.format("<link rel=\"meta\" type=\"text/n3\" title=\"%s\" href=\"%s\"/>\n",
					title,
					ref
					)); 
			
			w.write(String.format("<link rel=\"primarytopic\" type=\"application/rdf+xml\" href=\"%s\"/>",
					ref
					)); 		
			w.write(String.format("<link rel=\"primarytopic\" type=\"text/n3\" href=\"%s\"/>",
					ref
					)); 			
			w.write(String.format("<title>%s</title>\n",title));
			
			// Initialize Google +1, Twitter, Linked In and VKontakte buttons; Facebook is initialized at the beginning of BODY
			// But don't load if Microsoft IE 7 is detected, because this produces JS parsing errors.
			if (!isMsie7()) {
				w.write(googlePlusInit);
				w.write(twitterInit);
				w.write(linkedInInit);
			}
			// Disabled for the time being, as there are issues with it.
			//w.write(vKontakteInit);

			w.write(meta);

			//meta		
			for (String tag : metaTag ) w.write(String.format(tag,baseReference));
			//css			
			for (String style : css ) w.write(String.format(style,baseReference));
			//js
			for (String script : js ) w.write(String.format(script,baseReference));



			w.write("<script>$(function() {$( \".accordion\" ).accordion({autoHeight: false,navigation: true});});</script>");
			w.write("<script>$(function() {$( \"#selectable\" ).selectable();});</script>");
			w.write("<script type='text/javascript'>function hideDiv(divId) {\n$('#'+divId).hide();}</script>\n");
			final String dtableOptions = "'bJQueryUI': true, "+
					//"'sPaginationType': 'full_numbers',"+
					"'bPaginate'      : true,"+
					"\"sDom\": 'T<\"clear\"><\"fg-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix\"lfr>t<\"fg-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix\"ip>'";

			w.write(String.format("<script>$(function() {$( \".datatable\" ).dataTable({%s });});</script>",dtableOptions));
			
			w.write("<script>$(function() {$(\"#submit\").button();});</script>");
			
			// The next line is commented, because we MUST NOT initialise any div-tabs before they get populated.
			// But this is only true for documents, as the rest of the resources are loaded the usual way.
			if (isLoadTabs()) 
				w.write("<script>$(function() {$( \".tabs\" ).tabs({cache: true});});</script>");

			// table row expander script
			w.write(toggleDivScript);
			
			// Show the footer when the mouse gets near it.
			w.write(showFooterScript);
			
			// Show the to top link when the contents are scrolled down enough.
			w.write(showToTopLink);
			
			// HEAD ends here.
			w.write("</head>\n");
			
			// HTML body begins here.
			w.write("<body>\n");
			
			// Initialize Facebook button; Google +1, Twitter, Linked In and VKontakte are initialized in the HEAD
			// But don't load if Microsoft IE 7 is detected, because this produces JS parsing errors.
			if (!isMsie7()) w.write(facebookInit);
			
			w.write("<div id='wrap'>\n");
			
			w.write("<div id='header'>\n");
			
			String logInOutLink = "";
			
			if (!getSearchURI().equals(Resources.login)) {
				if (request.getClientInfo().getUser()==null) {
					// Log in
					logInOutLink = String.format(logInOutLinkTemplate,
							// Log in hint
							"Log in here to submit new observations (only required for editors)",
							// Log in URL
							baseReference.toString(), Resources.login,
							// onClick event (used when logging out, hence empty here)
							"",
							// Log in text
							"Log in",
							// The log out form (empty here as we're not yet logged in)
							"");
				} else {
					// Log out
					logInOutLink = String.format(logInOutLinkTemplate,
							// Log out hint
							String.format("You are currently logged in as \"%s\". Click here to log out.", request.getClientInfo().getUser()),
							// Log out URL (empty anchor, because we use the form below with JS)
							"#", "",
							// onClick event used to submit the form below
							"onClick='document.forms[\"logoutForm\"].submit(); return false;'",
							// Log out text
							String.format("Log out [<b>%s</b>]", request.getClientInfo().getUser()),
							// The log out form
							getLogout(baseReference, request.getClientInfo().getUser()));
				}
			}
			
			w.write(String.format(
					topLinks,
					baseReference.toString(),
					baseReference.toString(),					
					logInOutLink
			));
			/*
			TODO logout immediately 
			if (request.getClientInfo().getUser()!=null) 
				w.write(getLogout(baseReference));
						*/
			w.write(String.format(logoTopLeft, baseReference));
			
			w.write("</div>\n"); // header
			
			// left section
			w.write("<div id='inner-wrap'>\n" +
					"<div id='left'>\n"
			);
			
			//menu
			String[][] menu = {
					{ Resources.protocol, "Observations", "10", "Search all published XMETDB documents" },
					{ Resources.chemical, "Structures search", "10", "Search chemical structures by identifiers, similarity or substructure" },
					{ Resources.endpoint, "Enzymes", null, "XMETDB documents by enzymes" },
			};

			w.write(
					"<div id='menu'>\n" +
					"<ul id='navmenu'>\n"
			);
						
			// Home
			w.write(String.format(homeMenuOption, baseReference.toString(), baseReference.toString()));
			
			// Documents, Structures, Enzymes
			for (String[] menuItem: menu) {
				w.write(printMenuItem(menuItem[0], menuItem[1], baseReference.toString(), menuItem[2], menuItem[3]));
			}
			
			// If user is logged in, show My profile.
			if (request.getClientInfo().getUser() != null) {
				w.write(printMenuItem(
						Resources.myaccount,
						"My Profile",
						baseReference.toString(),
						null,
						String.format("%s's profile and documents.", request.getClientInfo().getUser())
				));
			}
		
			
			List<String> myProfile= new ArrayList<String>();
			String unpublishedDoc = null;
			
			for (Role role: request.getClientInfo().getRoles()) try {
				XMETDBRoles qmrfrole = XMETDBRoles.valueOf(role.getName());
				if (qmrfrole.getURI()!=null) {
					String myself = printMenuItem(qmrfrole.getURI(), qmrfrole.toString(), baseReference.toString(),null,qmrfrole.getHint());
					if (myProfile.indexOf(myself)<0) myProfile.add(myself);
				}	
				switch (qmrfrole) {
					case xmetdb_manager: {
						w.write(printMenuItem(Resources.user, "Users", baseReference.toString(),null,"All registered users."));
						w.write(printMenuItem(Resources.organisation, "Organisations", baseReference.toString(),null,"All registered user affiliations."));
						unpublishedDoc = printMenuItem(Resources.unpublished, "Unpublished Documents", baseReference.toString(),"10","All unpublished QMRF documents.");
						break;
					}
					case xmetdb_admin: {
						break;
					}
					case xmetdb_editor: {
						unpublishedDoc = printMenuItem(Resources.unpublished, "Unpublished Documents", baseReference.toString(),"10","All unpublished QMRF documents.");
						break;
					}
				}
			} catch (Exception x) {/* unknown role */}
				
			if (myProfile!=null) for (String myself: myProfile) w.write(myself);
			
			if (unpublishedDoc!=null) w.write(unpublishedDoc);
			
			//Saved search menu
			w.write(getSavedSearchMenu(request));
			

			w.write("</ul>\n</div>\n"); // div id='menu'
		
			// Apply style for the hovered buttons sans (!) the currently selected one.
			// There are better ways to do it, but this should be okay for now.
			w.write(menuMouseOverScript);
				
			//followed by the search form
			
			/*
			left = "";
			middle = "";
			right = String.format("<a href='%s/%s'>%s</a>",
					baseReference.toString(),
					getLoginLink(),
					request.getClientInfo().getUser()==null?"Login":"My account");
			writeDiv3(w, left, middle, right);
			*/

		} // writeTopHeader()
		protected static final String freq_hint = "Optional, if no alert frequency is selected, retrieve your saved search from the profile page.";
		protected static final String alert_hint = "Save your search and configure the frequency of e-mail update alerts";
		protected String getSavedSearchMenu(Request request) {
			if ((Resources.protocol.equals(getSearchURI())) &&
				(request.getClientInfo().getUser()!=null) && (request.getClientInfo().getUser().getIdentifier()!=null) && 
				(request.getResourceRef().getQuery()!=null)) {
				StringBuilder w = new StringBuilder();
				w.append(String.format("<li><a class='%s' title='%s, %s' href='#' onClick=\"toggleDiv('saveSearch'); return false;\">%s</a></li>\n",
						"selectable",
						request.getClientInfo().getUser(),
						alert_hint,
						"Save this search"
						));
				w.append("<div class=\"ui-widget-content ui-corner-all\" style='display: none;width:180px;' id='saveSearch'>");
				w.append("<p>");
				w.append(String.format("<form action='%s%s%s' method='POST'>",request.getRootRef(),Resources.myaccount,Resources.alert));
				w.append(alert_hint);
				w.append(String.format("<input type='hidden' name='%s' value='%s'/>",DBAlert._fields.name.name(),Resources.protocol));
				w.append(String.format("<input type='hidden' name='%s' value='%s'/>",DBAlert._fields.query.name(),request.getResourceRef().getQuery()));
				w.append(String.format("<input type='hidden' name='%s' value='%s'/>",DBAlert._fields.qformat.name(),Query.QueryType.FREETEXT.name()));
				w.append(String.format("<input type='hidden' name='%s' value='%s'/>","username",request.getClientInfo().getUser()));
				w.append(String.format("<br><label for='%s' title='%s'>Frequency of e-mail alert</label><select name='%s'>",DBAlert._fields.rfrequency.name(),freq_hint,DBAlert._fields.rfrequency.name()));
				w.append("<option value=\"monthly\">Monthly</option>");
				w.append("<option value=\"weekly\">Weekly</option>");
				w.append("<option value=\"daily\">Daily</option>");				
				w.append(String.format("<option value=\"\" title='%s'>Never</option>",freq_hint));
				w.append("</select>");
				w.append(String.format("<input type='submit' title='%s. %s' value='Save'/>",alert_hint,freq_hint));
				w.append("</form>");
				w.append("</p>");
				w.append("</div>");
				return w.toString();
			}
			return "";
			
		}
		protected String printMenuItem(String relativeURI,String title,String baseReference,String pagesize) {
			return this.printMenuItem(relativeURI, title, baseReference, pagesize,"");
		}
		protected String printMenuItem(String relativeURI,String title,String baseReference,String pagesize,String hint) {
			return String.format("<li><a class='%s' title='%s' href='%s%s%s%s'>%s</a></li>\n",
					getSearchURI().equals(relativeURI)?"selected":"selectable",
					hint==null?title:hint,
					baseReference,relativeURI,
					pagesize==null?"":"?pagesize=",
					pagesize==null?"":pagesize,
					title);
		}
		
		@Override
		public void writeHTMLFooter(Writer output,String title,Request request) throws IOException {
			//div ui-widget
			output.write(_divEnd); 
			//div id=content
			output.write(_divEnd); 
			//div inner-wrap
			output.write(_divEnd);
			// Push the footer downwards, so that we don't accidentally step on it.
			output.write("\n<div class='pusher'></div>");
			//div id=wrap
			output.write(_divEnd); 
			//footer
			
			output.write(footerText);
			
			// to top link (invisible in the beginning, scripted to show up on scroll down)
			output.write(toTopLink);
			
			output.write(jsGoogleAnalytics()==null?"":jsGoogleAnalytics());
			output.write("\n</body>");
			output.write("</html>");

		}
		@Override
		public void writeSearchForm(Writer w,String title,Request request ,String meta,Method method,Form params) throws IOException {

			Reference baseReference = request.getRootRef();
			try {
				w.write(searchMenu(baseReference,getParams(params,request)));
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				w.write(_divEnd);
			}
		}	
	
		protected String searchMenu(Reference baseReference,Form form)  {
			String pageSize = "10";
			String structure = null;
			try {
				if ((form != null) && (form.size()>0)) {
					searchQuery = form.getFirstValue(AbstractResource.search_param)==null?"":form.getFirstValue(AbstractResource.search_param);
					pageSize = form.getFirstValue("pagesize")==null?"10":form.getFirstValue("pagesize");
					structure = form.getFirstValue("structure");
				}
			} catch (Exception x) {
				searchQuery = "";
				pageSize = "10";
			}
			option = SearchMode.text;
			try {
				option = SearchMode.valueOf(form.getFirstValue("option").toLowerCase());
			} catch (Exception x) {
				option = SearchMode.text;
			}
			condition = "";
			try {
				condition = form.getFirstValue("condition").toLowerCase();
			} catch (Exception x) {
				condition = "";
			}
			String hint = "";
			String imgURI = (structure==null) || !structure.startsWith("http")?"":
				String.format("<img border='0' title='Showing XMETDB documents for this chemical' width='150' height='150' src='%s?media=%s&w=150&h=150'><br>Showing QMRF documents\n",
						structure,Reference.encode("image/png"));
			
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("<div class='search ui-widget'>\n");
				stringBuilder.append("<p title='%s'>%s</p>\n");
				stringBuilder.append("<form method='GET' action='%s%s?pagesize=10'>\n");
				stringBuilder.append("<table width='200px'>\n");
				stringBuilder.append("<tr><td colspan='2'><input type='text' name='search' size='20' value='%s' tabindex='0' title='Enter search query'></td></tr>\n");
				stringBuilder.append("<tr><td colspan='2'><input %s tabindex='1' type='radio' value='title' name='option' title='Title' size='20'>Title</td></tr>\n");
				stringBuilder.append("<tr><td colspan='2'><input %s tabindex='1' type='radio' value='text' name='option' title='Free text search' size='20'>Free text</td></tr>\n");
				stringBuilder.append("<tr><td><input %s type='radio' tabindex='2' name='option' value='endpoint' title='Search by endpoint'>Endpoint</td>\n");
				stringBuilder.append("<tr><td colspan='2'><input %s tabindex='3' type='radio' value='author' name='option' title='Search by author' size='20'>Author</td></tr>\n");
				stringBuilder.append("<tr><td><input %s type='radio' tabindex='4' name='option' value='qmrfnumber' title='Search by QMRF number'>QMRF number</td>\n");
				stringBuilder.append("<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n");
				stringBuilder.append("<input type='hidden' name='structure' value='%s'>\n");
				stringBuilder.append("<tr><td colspan='2' align='center'><input type='submit' id='submit' tabindex='4' value='Search'/></td></tr>\n");
				stringBuilder.append("</table>\n");
				stringBuilder.append("</form> \n");
				stringBuilder.append("&nbsp;\n");
				stringBuilder.append("<div class='structureright'>%s</div>");
				stringBuilder.append(_divEnd);
				
				return String.format(
						stringBuilder.toString(),
						hint,
						getSearchTitle(),
						baseReference,
						getSearchURI(),
						searchQuery==null?"":searchQuery,
						SearchMode.title.equals(option)?"checked":"",
						SearchMode.text.equals(option)?"checked":"",
						SearchMode.endpoint.equals(option)?"checked":"",
						SearchMode.author.equals(option)?"checked":"",
						SearchMode.qmrfnumber.equals(option)?"checked":"",
						pageSize,
						structure==null?"":structure,
						imgURI
			   );
		}
		
		@Override
		public void writeHTMLHeader(Writer w,String title,Request request,String meta,ResourceDoc doc) throws IOException {

			writeTopHeader(w, title, request, meta,doc);
			writeSearchForm(w, title, request, meta,null);
			
			w.write("<div id='content'>\n");
		}
		
		// Paging
		public String getPaging(int page, int start, int last, long pageSize) {

			// Having a constant number of pages display on top is convenient for the users and provides more consistent
			// overall look. But this would require the function to define different input parameters. In order to not
			// break it, implement a workaround, by calculating how many pages the caller (likely) intended to be shown.
			int total = last - start;

			// Normalization
			start = start<0?0:start; // don't go beyond first page
			last = start + total;

			String search = searchQuery==null?"":Reference.encode(searchQuery);
			String cond = condition==null?"":Reference.encode(condition);
			String url = "<li><a class='%s' href='?page=%d&pagesize=%d&search=%s&option=%s&condition=%s'>%s</a></li>";

			StringBuilder b = new StringBuilder(); 
			b.append("<div><ul id='hnavlist'>");

			// Disable this for the time being as it seems to not fit well into the overall look.
			//b.append(String.format("<li id='pagerPages'>Pages</li>"));

			// Display "first" and "previous" for the first page as inactive.
			if (page > 0) {
				b.append(String.format(url, "pselectable", 0, pageSize, search, option==null?"":option.name(), cond, "&lt;&lt;"));
				b.append(String.format(url, "pselectable", page-1, pageSize, search, option==null?"":option.name(), cond, "&lt;"));
			} else {
				b.append(String.format("<li class='inactive'>&lt;&lt;</li>"));
				b.append(String.format("<li class='inactive'>&lt;</li>"));
			}

			// Display links to pages. Pages are counted from zero! Hence why we display "i+1".
			for (int i=start; i<= last; i++)
				b.append(String.format(url, i==page?"current":"pselectable", i, pageSize, search, option==null?"":option.name(), cond, i+1)); 
			b.append(String.format(url, "pselectable", page+1, pageSize, search, option==null?"":option.name(), cond, "&gt;"));
			b.append("</ul></div><br>");

			// Apply style for the hovered buttons sans (!) the currently selected one.
			// There are better ways to do it, but this should be okay for now.
			b.append("<script>\n");
			b.append("$('a.pselectable').mouseover(function () { $(this).addClass('phovered');    } );\n");
			b.append("$('a.pselectable').mouseout(function  () { $(this).removeClass('phovered'); } );\n");
			b.append("</script>\n");

			return b.toString();
		}

		/*
		public String getPaging(int page,int start, int last, long pageSize) {
			String url = "<li><a href='?page=%d&pagesize=%d'>%s</a></li>";
		    StringBuilder b = new StringBuilder(); 
		    b.append("<div><ul id='hnavlist'>");
		    b.append(String.format(url,page,pageSize,"Pages:"));
		    b.append(String.format(url,0,pageSize,"<<"));
		    b.append(String.format(url,page==0?page:page-1,pageSize,"Prev"));
		    for (int i=start; i<= last; i++)
		    	b.append(String.format(url,i,pageSize,i+1)); //zero numbered pages
		    b.append(String.format(url,page+1,pageSize,"Next"));
		   // b.append(String.format("<li><label name='pageSize' value='%d' size='4' title='Page size'></li>",pageSize));
		    b.append("</ul></div><br>");
		    return b.toString();
		}
		*/
		
		public String getSearchURI() {
			return searchURI;
		}

		/*
		public void setSearchURI(String searchURI) {
			this.searchURI = searchURI;
		}
		*/
		
		public String getSearchTitle() {
			return searchTitle;
		}

		public void setSearchTitle(String searchTitle) {
			this.searchTitle = searchTitle;
		}
		
		public String getPaging(int start, int last) {
			return getPaging(page, start, last, pageSize);
		}
		
		public String printUploadForm(String uri, DBProtocol protocol, update_mode mode, String baseReference) throws Exception {
			return printUploadForm("",uri, protocol,mode,baseReference);
		}
		public String printUploadForm(String action, String uri, DBProtocol protocol,update_mode mode, String baseReference) throws Exception {

				StringBuilder content = new StringBuilder();
				String form = null;
				String header = null;
				String hint = "<p>Upload a QMRF XML file, complying to <a href='http://qmrf.sf.net/qmrf.dtd' target='help'>QMRF DTD</a> schema. The QMRF Editor can be downloaded from <a href='http://qmrf.sf.net' target='help'>http://qmrf.sf.net</a>. </p> ";
				String submit = "Upload";
				switch (mode) {
				case attachments: {
					header = String.format("%s to <a href='%s' >%s</a>",mode.toString(),protocol.getResourceURL(),protocol.getVisibleIdentifier());
					form = String.format("<form method='%s' action=\"%s\" ENCTYPE=\"multipart/form-data\">","POST",action);
					hint = "<p>All files are optional, you could select any combination of attachment type(s) to upload.</p>";
					break;
				}
				case update : {
					header = String.format("%s QMRF XML file of <a href='%s' >%s</a>",mode.toString(),protocol.getResourceURL(),protocol.getVisibleIdentifier());
					form = String.format("<form method='%s' action=\"%s?method=PUT\" ENCTYPE=\"multipart/form-data\">","POST",protocol.getResourceURL());
					submit = "Update";
					break;
				}
				case newversion : {
					header = String.format("%s QMRF XML file of <a href='%s' >%s</a>",mode.toString(),protocol.getResourceURL(),protocol.getVisibleIdentifier());
					form = String.format("<form method='POST' action=\"%s%s\" ENCTYPE=\"multipart/form-data\">",protocol.getResourceURL(),Resources.versions);
					submit = "Create new version";
					break;
				}				
				case publish : {
					header = String.format("%s QMRF document <a href='%s' title='%s'>%s</a>",mode.toString(),protocol.getResourceURL(),protocol.getIdentifier(),protocol.getVisibleIdentifier());
					form = String.format("<form method='%s' action=\"%s?method=PUT\" ENCTYPE=\"multipart/form-data\">","POST",protocol.getResourceURL());
					hint = "<p>Please verify the endpoint. The endpoint should be assigned in order to publish the document.</p>";
					submit = "Publish";
					break;
				}	
				case delete : {
					header = String.format("%s QMRF document <a href='%s'  title='%s'>%s</a>",mode.toString(),protocol.getResourceURL(),protocol.getIdentifier(),protocol.getVisibleIdentifier());
					form = String.format("<form method='%s' action=\"%s?method=DELETE\" ENCTYPE=\"multipart/form-data\">","POST",protocol.getResourceURL());
					hint = "<p style='color:red'>Do you really want to delete this document?</p>";
					submit = "Confirm document removal";
					break;
				}					
				case newdocument : {
					
					form = String.format("<form method='%s' action=\"%s\" ENCTYPE=\"multipart/form-data\">","POST",action);
					header = String.format("%s %s %s",mode.toString(),"QMRF document",uri.toString().contains("versions")?" version":"");
					break;
				}
				}
				
				//content.append(printWidgetHeader(header));
				//content.append(printWidgetContentHeader(""));
				content.append("<div class=\"ui-widget-content\">");
				
				content.append(form);
				content.append("<table width='100%%'>");
				content.append(_trStart);
				
				try {
					content.append(String.format("<p><input type='hidden' name='%s' title='%s' value='%s' size=\"30\"></p>",
							ReadProtocol.fields.user_uri.name(),"Owner",protocol==null?"":protocol.getOwner()==null?"":protocol.getOwner().getResourceURL()));
					content.append(String.format(
							"<p><input type='hidden' name='%s' title='%s' value='%s' size=\"30\"></p>",
							ReadProtocol.fields.organisation_uri.name(),"Organisation",protocol==null?"":protocol.getOrganisation()==null?"":protocol.getOrganisation().getResourceURL()));
					content.append(String.format("<p><input type='hidden' name='%s' title='%s' value='%s' size=\"30\"></p>",
							ReadProtocol.fields.project_uri.name(),"Project",protocol==null?"":protocol.getProject()==null?"":protocol.getProject().getResourceURL()));
					} catch (Exception x) {x.printStackTrace(); /*ok, no defaults if anything goes wrong */ }	

				content.append(hint);
				
				switch (mode) {
				case delete : {
					content.append("<input type='hidden' name='published_status' value='deleted'>");
					break;
				}
				case publish : {
					
					String protocolURI = String.format("%s%s/%s%s?media=application/json", baseReference,Resources.protocol,protocol.getIdentifier(),Resources.endpoint);

					content.append("<th width='15%%'>Endpoint</th>");
					content.append(_tdStart);
					content.append(String.format("<input type='text' readonly size='15' id='%s' name='%s'>",ReadProtocol.fields.endpointParentCode.name(),ReadProtocol.fields.endpointParentCode.name()));
					content.append(String.format("<input type='text' readonly size='75' id='%s' name='%s'><br>",ReadProtocol.fields.endpointParentName.name(),ReadProtocol.fields.endpointParentName.name()));
					content.append(String.format("<input type='text' size='75' title = 'Start writing here to get list of enzymes. The rest of the fields will be automatically filled in.' id='%s' name='%s'>","endpoint","endpoint"));

					
					content.append(String.format(		
					"<script>\n"+
					"$(function() {\n"+
				
						"$( \"#endpoint\" ).each(function() {\n" +
						"var autoCompleteElement = this;\n"+
						"var formElementName = $(this).attr('name');\n"+
						"var hiddenElementID  = formElementName + '_autocomplete_hidden';\n"+
						"$(this).attr('name', formElementName + 'Name');\n"+
						"$(this).before(\"<input type='text' size='15' readonly name=\" + formElementName + \" id=\" + hiddenElementID + \" />\");\n"+
						"$(this).autocomplete({source:'%s/catalog?media=application/json', \n"+
						"	select: function(event, ui) {\n"+
						"		var selectedObj = ui.item;\n"+
						"		$(autoCompleteElement).val(selectedObj.name);\n"+
						"		$('#'+hiddenElementID).val(selectedObj.code);\n"+
						"   	$('#endpointParentCode').val(selectedObj.parentCode);\n"+
						"   	$('#endpointParentName').val(selectedObj.parentName);\n"+			
						"		return false;\n"+
						"	}\n"+
						"});\n"+
						"});\n"+
						//load the current value
						"$.getJSON(\"%s\",\n"+
				        "function(data){\n"+
				        "  $.each(data, function(i,item){\n"+
				        "   	$('#endpoint_autocomplete_hidden').val(item.code);\n"+
				        "   	$('#endpoint').val(item.name);\n"+
				        "   	$('#endpointParentCode').val(item.parentCode);\n"+
				        "   	$('#endpointParentName').val(item.parentName);\n"+
				        "  });\n"+
				        "});\n" +							
					"});\n"+
					"</script>\n",baseReference,protocolURI));
					
					content.append(_tdEnd);
					content.append(_trEnd); 
					content.append(_trStart);						
					content.append("<th width='15%%'>XMETDB identifier</th>");
					content.append("<th>");
					content.append(String.format("Q%s-%s-%s",new SimpleDateFormat("yy").format(new Date()),
								"endpoint","number of QMRF documents published in the specified year"));
					/*
					content.append(String.format("<strong>Q</strong>&nbsp;<input type='text' size='2' title='Year in YY format' value='%s'>",
								new SimpleDateFormat("yy").format(new Date())));
					content.append("&nbsp;-&nbsp;endpoint");
					content.append(String.format("&nbsp;-&nbsp;<input type='text' title='Sequential number of QMRF documents published in the specified year'>"));
					*/
					content.append("<br><input type='hidden' value='published' name='published_status'>");					
					content.append("</th>"); 

					break;
				}				
				case attachments: {
					for (attachment_type atype: attachment_type.values()) {
						if (atype.ordinal() % 2 ==0) {
							content.append(_trEnd);
							content.append(_trStart);
						}
						String title= String.format("Attachments: %s(s) - %s, %s files max", atype.toString(),atype.getDescription(),atype.maxFiles());
						content.append(_tdStart);
						content.append(printWidget(title,
								String.format("<p><input type=\"file\"  class='multi' maxlength='%d' accept='%s' name=\"%s\" title='%s' size=\"30\"></p>",
										atype.maxFiles(),
										atype.acceptFormats(),
										atype.name(),
										title),"box"
								));
						content.append(_tdEnd);
					}
					break;
					
				} 
				case newdocument: {
					
					content.append(_tdStart);
					content.append(String.format("<p class='%s'><input type=\"file\" class='multi max-1' accept='xml' name=\"%s\" title='%s' size=\"30\"></p>",
							"box",
							ReadProtocol.fields.filename.name(),
							"QMRF XML"
					));					
				
					content.append(_tdEnd);			
					break;
				}
				case update: {
				
					content.append(_tdStart);
					content.append(String.format("<p class='%s'><input type=\"file\" class='multi max-1' accept='xml' name=\"%s\" title='%s' size=\"30\"></p>",
								"box",
								ReadProtocol.fields.filename.name(),
								"QMRF XML"
						));
					content.append("<p style='color:red'>Warning: Uploading will replace the current QMRF document content!</p>");
					content.append(_tdEnd);		
					break;
				}
				case newversion: {
					
					content.append(_tdStart);
					content.append(String.format("<p class='%s'><input type=\"file\" class='multi max-1' accept='xml' name=\"%s\" title='%s' size=\"30\"></p>",
								"box",
								ReadProtocol.fields.filename.name(),
								"QMRF XML"
						));
					content.append("<p style='color:red'>New version will be created and assigned identifier in the format XMETDB{id}v{newversion}.<br>The old version will be assigned <i>archived</i> status and will not be visible in published observations!</p>");
					content.append(_tdEnd);		
					break;
				}				
				}
				content.append(_trEnd);
				//content.append(_trStart);
					
				//content.append(_trEnd);
				content.append(_trStart);
				content.append(String.format("<td colspan='2' align='center'><input type='submit' id='submit' enabled='false' value='%s'></td>",submit));
//				content.append("<input type='submit' enabled='false' value='Submit'>");

				content.append(_trEnd);
				content.append(_tableEnd);
				
				
				content.append("</form>");


				content.append(printWidgetContentFooter());
				//content.append(printWidgetFooter());
				
				
				switch (mode) {
				case newdocument: break;
				default : {
					content.append(String.format("<span id='%s'></span><script>$('#%s').load('%s%s/%s?headless=true');</script>", 
							protocol.getIdentifier(),protocol.getIdentifier(),baseReference,Resources.protocol ,protocol.getIdentifier()));
				}
				}
				
				return	content.toString();

		}	
		
		protected String getLogout(Reference baseReference, User user) {
			String redirect = Reference.encode(String.format("%s/", baseReference));
			return String.format("<form id='logoutForm' action='%s/protected/signout?targetUri=%s' method='POST'></form>", baseReference, redirect);
		}	
}
