<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<#include "/protocols_head.ftl" >
<script type="text/javascript">
jQuery(document).ready(function()
{
	jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}" title="XMetDB search">Search</a></li>');
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/protocol" title="XMetDB observations">Observations</a></li>');
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_request}" title="XMetDB search results">${xmet_breadcrumb}</a></li>');
    jQuery("#breadCrumb").jBreadCrumb();
    loadHelp("${xmet_root}","observation_list");
    $('#download').html(getDownloadLinksObservation("${xmet_root}","${xmet_request}"));
})
</script>
</head>
<body>
<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">

	<!-- Page Content
		================================================== -->
		<div class="twelve columns ui-widget-content ui-corner-all" >
		
		<table id='protocols'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<tr>
		<th>XMetDB ID</th>
		<th>Substrate<a href='#' class='chelp substrate'></a></th>
		<th>Product<a href='#' class='chelp product'></a></th>
		<th>Product amount<a href='#' class='chelp productamount'></a></th>
		<th>Experiment<a href='#' class='chelp experiment'></a></th>
		<th>Enzyme<a href='#' class='chelp enzyme'></a></th>
		<th>Last updated</th>
		<th>Curated<a href='#' class='chelp hcurator'></a></th>
		</tr>
		</thead>
		<tbody></tbody>
		</table>
		
		</div>
		   
		   <!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
	</div> <#-- container -->
	
<#include "/scripts-welcome.ftl">
</body>
</html>