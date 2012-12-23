<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<script type='text/javascript' src='${xmet_root}/jme/jme.js'></script>
<#include "/structures_head.ftl" >
</head>
<body>
<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">

		<!-- Page Content
		================================================== -->
		<div class="twelve columns" style="padding:0;" >
		
		<table class='compoundtable' id='structures'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<th></th>
		<th>Structure</th>
		<th>Name</th>
		<th>CAS</th>
		<th>XMETDB observations</th>
		<th>Similarity</th>
		<th>SMILES</th>
		<th>InChI</th>
		<th>InChI Key</th>		
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