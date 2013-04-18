<#include "/s_html.ftl">
<head>
  <#include "/s_head.ftl">
<script type="text/javascript">
jQuery(document).ready(function()
{
	jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/admin" title="XMetDB admin">Admin</a></li>');
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/login" title="XMetDB Database">Database</a></li>');
    jQuery("#breadCrumb").jBreadCrumb();
    loadHelp("${xmet_root}","admin_db");
    loadDBInfo("${xmet_root}");
});
</script>  
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns add-bottom" style="padding:0;" >
		
	    <div class="row remove-bottom ui-widget-header ui-corner-top">Database info</div>
    	<div class="half-bottom ui-widget-content ui-corner-bottom">
		<div style='margin:5px;padding:5px;'>
		
		<div class="row add-bottom" >		
			<label class='three columns alpha'>Current database</label>	
			<div class='ten columns alpha' id="dbinfo">&nbsp;</div>
			<div class='three columns alpha'><a href='#' class='chelp database'></a></div>
		</div>
				
		<div class="row add-bottom"  id="dbcreate" style="display:none;">		
			<label class='three columns alpha'>How to create the database:</label>	
			<div class='ten columns alpha'>
	
	    Use the following <a href="http://curl.haxx.se/">cURL</a> command 
	<pre>
		curl -X POST \ 
		-d "dbname=xmetdb" \ 
		-d "user=mysqladminuser" \ 
		-d "pass=mysqladminpass" \
        ${xmet_root}/admin/database 
	</pre>
	 Database tables will be created, only if the database exists,
    is empty and the database name is the same as configured server side.
    
			</div>
			<div class='three columns alpha'><a href='#' class='chelp create'></a></div>
		</div>
						
		</div>
		</div>				
		</div>
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>