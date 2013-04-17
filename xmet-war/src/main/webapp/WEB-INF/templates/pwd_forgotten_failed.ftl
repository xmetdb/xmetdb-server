<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<script type="text/javascript">
jQuery(document).ready(function()
{
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/forgotten" title="XMetDB password reset">Password reset</a></li>');
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/forgotten/notify" title="XMetDB password reset">Failed</a></li>');
    jQuery("#breadCrumb").jBreadCrumb();
     loadHelp("${xmet_root}","pwd_forgotten");
})
</script>
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns add-bottom" style="padding:0;" >	
		
		<div class='ui-widget-header ui-corner-top'>&nbsp;XMetDB password reset failed</div>
	    <div class='ui-widget-content ui-corner-bottom'>	
	    	<div class='row'></div>
			<div  class='row' style="text-align:center;">
			<span class="ui-icon ui-icon-info" style="display:inline-block"></span>
			The user name or email you provided do not match the XMETDB Database records.
			</div>	
			<div class='row'></div>
		</div>
	

		<!-- twelve -->
		</div>
		
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>