<#include "/html.ftl" >
<head>

<#include "/head.ftl" >
<script type='text/javascript' src='/xmet/jquery/jquery.validate.min.js'></script>
<script type='text/javascript'>


$().ready(function() {
	// validate the comment form when it is submitted
	$("#pwdForm").validate({
		rules : {
			'pwdold': {
				required : true
			},
			'pwd1': {
				required : true,
				minlength: 6
			},
			'pwd2': {
				required : true,
				minlength: 6,
				equalTo: "#pwd1"
			}
			
		},
		messages : {
			'pwdold' : "Please provide your current password",
			'pwd1'   : {
				required: "Please provide a new password",
				minlength: "Your password must be at least 6 characters long"
			},
			'pwd2'   : {
				required: "Please provide a new password",
				minlength: "Your password must be at least 6 characters long",
				equalTo: "Please enter the same password as above"
			}			
		}
	});
});
	
</script>
<style type="text/css">
#pwdForm label.error {
	margin-left: 10px;
	width: auto;
	display: inline;
}
</style>

</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<div class='ui-widget ' style='margin-top: 20px; padding: 0 .7em;'>
		<div class='ui-widget-header ui-corner-top'><p>Password change</p></div>
	    <div class='ui-widget-content ui-corner-bottom'>					
			<form action="/qmrf/myaccount/reset?method=PUT" id="pwdForm"  method="POST" >		
	    	<table width='80%%'>
			<tbody>
			<tr><th colwidth='25%'><label for='pwdold'>Current password</label></th><td align='left'><input type='password' size='40' id='pwdold' name='pwdold' value='' required/></td></tr>
			<tr><th><label for='pwd1'>New password</label></th><td align='left'><input type='password' size='40' id='pwd1' name='pwd1' value=''/></td></tr>
			<tr><th><label for='pwd2'>Confirm new password</label></th><td align='left'><input type='password' size='40' id='pwd2' name='pwd2' value=''/></td></tr>
			<tr><th></th><td align='left' ><input id='updatepwd' name='updatepwd' type='submit' class='submit' value='Submit'></td></tr>
			</tbody>
			</table>
			</form>		
		</div>
		
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>