<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript'>


$().ready(function() {
	// validate the comment form when it is submitted
	$("#registerForm").validate({
		rules : {
			'username': {
				required : true,
				minlength: 3,
				maxlength: 16
			},		
			'firstname': {
				required : true
			},
			'lastname': {
				required : true
			},
			'email': {
				required : true,
				email: true
			},
			'homepage': {
				url: true
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
			'username'  : {
				required: "Please enter user name"
			},
			'firstname' : "Please provide your first name",
			'lastname'  : {
				required: "Please provide your last name"
			},
			'email'     : {
				required: "Please provide e-mail",
				email: "Please provide valid e-mail"
			},
			'homepage'  : {
				url: "Please provide valid web address"
			},
			'pwd1'      : {
				required: "Please provide a password",
				minlength: "Your password must be at least 6 characters long"
			},
			'pwd2'      : {
				required: "Please confirm the password",
				minlength: "Your password must be at least 6 characters long",
				equalTo: "Please enter the same password as above"
			}			
		}
	});
});
	
</script>
<style type="text/css">
label { width: 10em; float: left; }
label.error { float: none; color: red; padding-left: .5em; vertical-align: top; }
p { clear: both; }
.submit { margin-left: 12em; }
em { font-weight: bold; padding-right: 1em; vertical-align: top; }
</style>

</head>
<body>
	<div id='wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<div class='ui-widget ' style='padding: 0 .7em;'>
		<div class='ui-widget-header ui-corner-top'><p>Registration form</p></div>
	    <div class='ui-widget-content ui-corner-bottom'>					
			<form action="${xmet_root}/register" id="registerForm"  method="POST" >		
			
			<p><label for="username">User name<em>*</em></label><input type="text" size='40' name='username' id='username' value=''/></p>
			<p><label for="title">Title</label><input type="text" size='40' name='title' id='title' value=''/></p>
			<p><label for="firstname">First name<em>*</em></label><input type="text" size='40' name='firstname' id='firstname' value=''/></p>
			<p><label for="lastname">Last name<em>*</em></label><input type="text"  size='40' name='lastname' id='lastname' value=''/></p>
			<p><label for="affiliation">Affiliation</label><input type="text"  size='40' name='affiliation' id='affiliation' value=''/></p>
			<p><label for="email">e-mail<em>*</em></label><input type="text"size='40' name='email' id='email' value=''/></p>
			
			<p><label for="homepage">WWW</label><input type="text"  size='40' name='homepage' id='homepage' value=''/></p>
			<p><label for="keywords">Keywords</label><input type="text"  size='40' name='keywords' id='keywords' value=''/></p>
			<p><label for="reviewer">Available as a reviewer</label><input type="checkbox" name='reviewer' id='reviewer' value=''/></p>
					
			<p><label for='pwd1'>Password<em>*</em></label><input type='password' size='40' id='pwd1' name='pwd1' value=''/></p>
			<p><label for='pwd2'>Confirm password<em>*</em></label><input type='password' size='40' id='pwd2' name='pwd2' value=''/></p>
			<p><input id='register' name='register' type='submit' class='submit' value='Register'></p>

			</form>		
		</div>
		</div>
		
	</div> <#-- content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>