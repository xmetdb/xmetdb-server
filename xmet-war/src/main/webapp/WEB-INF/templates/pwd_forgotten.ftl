<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<script type='text/javascript' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' src='${xmet_root}/scripts/myprofile.js'></script>
<script type='text/javascript' src='${xmet_root}/scripts/jopentox.js'></script>
<script type='text/javascript'>

$().ready(function() {
	// validate the reset form when it is submitted
	$("#pwdresetForm").validate({
		rules : {
			'username': {
				required : true,
				minlength: 3,
				maxlength: 16
			},		
			'email': {
				required : true,
				email: true
			}
		},
		messages : {
			'username'  : {
				required: " Please enter user name"
			},
			'email'     : {
				required: " Please provide valid e-mail",
				email: " Please provide valid e-mail"
			}
		}
	});
});
	
</script>
<script type="text/javascript">
jQuery(document).ready(function()
{
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/register" title="XMetDB password reset">Request password reset</a></li>');
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
		
		<div class='ui-widget-header ui-corner-top'>&nbsp;XMetDB password reset</div>
	    <div class='ui-widget-content ui-corner-bottom'>	
				
			<div style="margin:5px;padding:5px;">	
			<form action="${xmet_root}/forgotten" id="pwdresetForm"  method="POST" >		
			
			<div class='row remove-bottom'>
				<label class='three columns alpha' for="username">User name <em>*</em></label>
				<input class='three columns alpha half-bottom' type="text" size='40' name='username' id='username' value=''/>
				<div class='ten columns omega'></div>
			</div>
		
			<div class='row remove-bottom'>
				<label class='three columns alpha'  for="email">e-mail <em>*</em></label>
				<input class='eight columns alpha half-bottom' type="text"size='40' name='email' id='email' value=''/>
				<div class='ten columns omega'></div>
			</div>				
		
			<div class='row remove-bottom'>
				<label class='three columns alpha'>&nbsp;</label>
				<input class='three columns alpha' id='register' name='register' type='submit' class='submit' value='Reset'>
				<div class='ten columns omega'><a href='#' class='chelp reset'></a></div>
			</div>
			</form>	
		</div>			
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