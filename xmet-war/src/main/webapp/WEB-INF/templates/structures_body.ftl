<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
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
		<th>Structure</th>
		<th>Name</th>
		</thead>
		<tbody></tbody>
		</table>
		</div>
		   		</p>
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>