<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='/xmetdb/jme/jme.js'></script>
<#include "/structures_head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<p class='w_p'>
		<div>
		<table class='compoundtable' id='structures'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<th></th>
		<th>Structure</th>
		<th>Name</th>
		<th>CAS</th>
		<th>QMRF documents</th>
		<th>Similarity</th>
		<th>SMILES</th>
		<th>InChI</th>
		<th>InChI Key</th>		
		</thead>
		<tbody></tbody>
		</table>
		</div>
		   		</p>
		   		
		<!-- Download links for the entire list -->
		<div style='float:right; width:100%; align:center; margin:20px 0 0 0;'>
		<p>Download as&nbsp;
		<a href="${xmet_request_csv}" id="downloadcsv"><img id="downloadimg" src="/xmetdb/images/excel.png" alt="text/csv" title="Download as MS Excel" border="0"/></a>
		&nbsp;
		<a href="${xmet_request_sdf}" id="downloadsdf"><img id="downloadimg" src="/xmetdb/images/sdf.jpg" alt="chemical/x-mdl-sdfile" title="Download as SDF" border="0"/></a>						
		</p></div>
		<!-- End download links -->
	   		
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>