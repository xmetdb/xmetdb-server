<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<#include "/users_head.ftl" >

<#if xmetdb_admin?? && xmetdb_admin>
    <script type='text/javascript' src='${xmet_root}/jquery/jquery.jeditable.js'></script>
    <script type='text/javascript' src='${xmet_root}/jquery/jquery.dataTables.editable.js'></script>
</#if>	

<script type='text/javascript'>
$(document).ready(function() {
	var oTable = defineUsersTable("${xmet_root}","${xmet_request_json}","#users");	
	<#if xmetdb_admin?? && xmetdb_admin>
		makeEditableUsersTable("${xmet_root}",oTable);
	</#if>	
});
</script>

<script type="text/javascript">
jQuery(document).ready(function()
{
	jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/admin" title="XMetDB admin">Admin</a></li>');
	jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/user" title="XMetDB users">Users</a></li>');
    jQuery("#breadCrumb").jBreadCrumb();
    loadHelp("${xmet_root}","users");
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
		
		<table id='users' cellpadding='0' border='0' width='100%' cellspacing='0' style="margin:0;padding:0;" >
		<thead>
		<th>Username</th>	
		<th>Name</th>
		<th>e-mail</th>
		<th>Affiliation</th>
		<th>Keywords</th>
		<th>Available as a Curator<a href='#' class='chelp hcurator'></a></th>
		<th>Curator Role</th>
		<th>Admin Role</th>
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