<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<#include "/observation_head.ftl" >


<script type="text/javascript">
       jQuery(document).ready(function()
       {
           jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/protocol" title="XMETDB observations">Observations</a></li>');
           jQuery("#breadCrumb ul").append('<li><a href="${xmet_request}" title="This observation"><span id="xmet_id"></span></a></li>');
           jQuery("#breadCrumb").jBreadCrumb();
       })
</script>
       
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
    <div class="twelve columns ui-widget-header ui-corner-top">&nbsp;Observation ID: <span id="xmet_id"></span></div>

	<div class="twelve columns ui-widget-content ui-corner-bottom" >
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