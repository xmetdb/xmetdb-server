<#include "/s_html.ftl">
<head>
  <#include "/s_head.ftl">
<script type='text/javascript' src='${xmet_root}/scripts/myprofile.js'></script>
<script type='text/javascript' src='${xmet_root}/jquery/jquery.validate.min.js'></script>

<script type='text/javascript'>
$(document).ready(function() {
	var oTable = defineAlertsTable("${xmet_root}","${xmet_request_json}");
});
</script>


<script type="text/javascript">
       jQuery(document).ready(function()
       {
           jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/myaccount/alert" title="XMetDB Home">Alerts</a></li>');
           jQuery("#breadCrumb").jBreadCrumb();
           loadHelp("${xmet_root}","alert");
       })
</script>
  
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns ui-widget-content ui-corner-all" style="padding:0;" >
		
		<table id='alerts'  cellpadding='0' border='0' width='100%' cellspacing='0' style="margin:0;padding:0;" >
		<thead>
		<tr>
		<th>Created</th>
		<th>Alert</th>
		<th>Recurrence</th>
		<th>Sent at</th>
		<th>Delete</th>
		</tr>
		</thead>
		<tbody></tbody>
		</table>
		
		</div>
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>