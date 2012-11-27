<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<#include "/observation_head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content ui-widget ' style='margin-top: 25px; margin-left: 0px;  margin-right: 10px; padding: 0 .1em;'>
	
	<div class='ui-widget-header ui-corner-top'><p>&nbsp;Observation ID: <span id="xmet_id"></span></p></div>
	
	<#include "/observation_ro.ftl" >
	  	
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>

	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>