<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<#include "/protocols_head.ftl" >
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
		<th>XMETDBID</th>
		<th>Substrate (dataset)</th>
		<th>Substrate</th>
		<th>Product (dataset)</th>
		<th>Product</th>
		<th>Product amount</th>
		<th>Experiment</th>
		<th>Enzyme</th>
		<th>Last updated</th>
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