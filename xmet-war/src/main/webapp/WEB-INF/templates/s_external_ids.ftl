<#include "/s_html.ftl">
<head>
  <#include "/s_head.ftl">
  <script type='text/javascript' src='${xmet_root}/scripts/xmetdb.js'></script>
  <script type='text/javascript' src='${xmet_root}/jquery/jquery.jeditable.js'></script>
  <script type='text/javascript' src='${xmet_root}/jquery/jquery.dataTables.editable.js'></script>
  <script type='text/javascript' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
  <script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
  	var oTable = defineLinksTable("${xmet_request_json}","${xmet_root}");

} );
</script>

<script type="text/javascript">
jQuery(document).ready(function()
{
	jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/protocol" title="XMetDB observations">Observations</a></li>');
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_request}" title="This observation"><span id="xmet_id"></span></a></li>');
    jQuery("#breadCrumb").jBreadCrumb();
    loadHelp("${xmet_root}","observation_list");
})
</script>
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu_enzyme.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns ui-widget-content ui-corner-all add-bottom" style="padding:0;" >
		<div class='row remove-bottom'>
		</div>
		<table id='extids' class='row' cellpadding='0' border='0' width='100%' cellspacing='0' style="margin:0;padding:0;" >
		<thead>
		<tr>
		<th>Type</th>
		<th>URI</th>
		</tr>
		</thead>
		<tbody></tbody>
		</table>
		
		</div>
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
	<form id="formAddNewLink" action="#" title="Add new link">
		<input type="hidden" name="id" value="" rel="0">
	    <label for="type">Link type</label><input type="text" name="type" size="64" id="type" class="required" rel="1" />
	    <label for="id">Link URI</label><input type="text" size="64" name="id" id="id" class="required" rel="2" />
	</form>				
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
		

</body>
</html>