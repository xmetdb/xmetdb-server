<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<script type='text/javascript' src='${xmet_root}/jme/jme.js'></script>
<#include "/structures_head.ftl" >


<script type="text/javascript">
jQuery(document).ready(function()
{
	
	jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}" title="XMetDB search">Search</a></li>');
    jQuery("#breadCrumb ul").append('<li>Chemical structures</li>');
    <#switch query.option>
    <#case "similarity">
    	jQuery("#breadCrumb ul").append('<li><a href="${this_url}" title="XMetDB similarity search">Similarity search results</a></li>');
    	<#break>
    <#case "substructure">
		jQuery("#breadCrumb ul").append('<li><a href="${this_url}" title="XMetDB substructure search">Substructure search results</a></li>');
		<#break>    
    <#case "auto">
		jQuery("#breadCrumb ul").append('<li><a href="${this_url}" title="XMetDB chemical identifier search">Chemical identifier search results</a></li>');
		<#break>
    <#default>
		jQuery("#breadCrumb ul").append('<li><a href="${this_url}" title="XMetDB structure search">Structure search results</a></li>');
		<#break>
    </#switch>
    jQuery("#breadCrumb").jBreadCrumb();
    loadHelp("${xmet_root}","structure");
})
</script>
</head>
<body>
<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">

		<!-- Page Content
		================================================== -->
		<div class="twelve columns ui-widget-content ui-corner-all add-bottom" style="padding:0;" >
		
		<table class='compoundtable' id='structures'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<tr>
		<th></th>
		<th>Structure</th>
		<th>Name</th>
		<th>CAS</th>
		<th>XMetDB observations</th>
		<th>Similarity</th>
		<th>SMILES</th>
		<th>InChI</th>
		<th>InChI Key</th>
		</tr>		
		</thead>
		<tbody></tbody>
		</table>

		
		<!-- Download links for the entire list -->
		<div style='float:right; width:100%; align:center; margin:20px 0 0 0;'>
		<p>Download as&nbsp;
		<a href="${xmet_request_csv}" id="downloadcsv"><img id="downloadimg" src="${xmet_root}/images/excel.png" alt="text/csv" title="Download as MS Excel" border="0"/></a>
		&nbsp;
		<a href="${xmet_request_sdf}" id="downloadsdf"><img id="downloadimg" src="${xmet_root}/images/sdf.jpg" alt="chemical/x-mdl-sdfile" title="Download as SDF" border="0"/></a>						
		</p></div>
		<!-- End download links -->
		
	</div>
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

<#include "/scripts-welcome.ftl">
</body>
</html>