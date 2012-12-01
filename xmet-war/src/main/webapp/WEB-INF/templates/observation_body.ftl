<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<#include "/observation_head.ftl" >
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
    <div class="eleven columns ui-widget-header ui-corner-top" style='margin: 0px'>&nbsp;Observation ID: <span id="xmet_id"></span></div>

	<div class="eleven columns ui-widget-content ui-corner-bottom" style="margin:0;" >
	<div   style="margin:5px;padding:5px;" class="remove-bottom"> 	
		
	<#include "/observation_ro.ftl" >
	  	
	</div>
	</div>
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

<#include "/scripts-welcome.ftl">
</body>
</html>