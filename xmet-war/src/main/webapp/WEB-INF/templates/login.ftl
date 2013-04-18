<#include "/s_html.ftl">
<head>
  <#include "/s_head.ftl">
<script type="text/javascript">
jQuery(document).ready(function()
{
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/login" title="XMetDB log in">Log in</a></li>');
    jQuery("#breadCrumb").jBreadCrumb();
    loadHelp("${xmet_root}","login");
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
		
	    <div class="row remove-bottom ui-widget-header ui-corner-top">&nbsp;Sign In</div>
    	<div class="half-bottom ui-widget-content ui-corner-bottom">
		<div style='margin:5px;padding:5px;'>
		<form method='post' action='${xmet_root}/protected/signin?targetUri=${xmet_root}/login'>
		
		<div class="row remove-bottom">		
		<label class='three columns alpha'>User name</label> 
		<input class='five columns omega' type='text' size='40' name='login' value=''>
		<div class='eight columns omega'></div>
		</div>
		
		<div class="row remove-bottom">		
		<label class='three columns alpha'>Password</label> 
		<input class='five columns omega' type='password' size='40' name='password' value=''>
		<div class='eight columns omega'><a href="${xmet_root}/forgotten" title='Click to request one time password reset'>Forgotten password?</a></div>
		</div>

		<div class="row remove-bottom">		
		<label class='five columns alpha'>&nbsp;</label>		
		<input class='three columns omega'  type="submit" value="Log in"><a href='#' class='chelp loginhelp'></a>
		<input type='hidden' size='40' name='targetURI' value='${xmet_root}/login'>
		</div>
		</form>
		
		</div>
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