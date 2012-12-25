<#include "/s_html.ftl">
<head>
<#include "/s_head.ftl">
<script type='text/javascript' src='${xmet_root}/scripts/jopentox.js'></script>

<#if taskid??>
	<script type='text/javascript'>
		var task = readTask("${xmet_root}","${xmet_request_json}");
	</script>
<#else>
	<script type='text/javascript'>
	
	$(document).ready(function() {
	  	var oTable = defineTaskTable("${xmet_root}","${xmet_request_json}");
	  	console.log(oTable);
	});
	</script>
</#if>
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<#if taskid??>
		<div class="twelve columns" style="padding:0;" >			
			<div class="ui-widget-header ui-corner-top"><a href='${xmet_root}/task/${taskid}'>Job</a> started <span id=task_started></span> &nbsp;</div>
			<div class="ui-widget-content ui-corner-bottom">
			<span id=task_status>
			<script>
				checkTask('${xmet_root}/task/${taskid}','result', 'status', '${xmet_root}/images/tick.png', '${xmet_root}/images/cross.png');
			</script>
			</span>
			<p>Name:&nbsp;<strong id=task_name></strong></p>
			<p>Status:&nbsp;<a href='#' id='result'></a>&nbsp;<img src='${xmet_root}/images/progress.gif' id='status'>
			<span id=task_errorreport></span>
			</p>
			</div>
		<#else>
		<div class="twelve columns ui-widget-content ui-corner-all" style="padding:0;" >
			<table id='task'  cellpadding='0' border='0' width='100%' cellspacing='0' style="margin:0;padding:0;" >
			<thead>
			<th>Job</th>
			<th>Status</th>
			<th>Start time</th>
			<th>Elapsed time</th>
			</thead>
			<tbody></tbody>
			</table>
		
		</#if>
		
		</div>
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>