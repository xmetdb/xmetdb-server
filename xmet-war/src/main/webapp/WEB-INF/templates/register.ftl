<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
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
				required: " Please enter user name"
			},
			'firstname' : " Please provide your first name",
			'lastname'  : {
				required: " Please provide your last name"
			},
			'email'     : {
				required: " Please provide valid e-mail",
				email: " Please provide valid e-mail"
			},
			'homepage'  : {
				url: " Please provide valid web address"
			},
			'pwd1'      : {
				required: " Please provide a password",
				minlength: " Your password must be at least 6 characters long"
			},
			'pwd2'      : {
				required: " Please confirm the password",
				minlength: " Your password must be at least 6 characters long",
				equalTo: " Please enter the same password as above"
			}			
		}
	});
});
	
</script>
</head>

<body>
<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns" style="padding:0;" >	
		
		<div class='ui-widget-header ui-corner-top'>&nbsp;XMETDB Registration</div>
	    <div class='ui-widget-content ui-corner-bottom'>	
				
			<div style="margin:5px;padding:5px;">	
			<form action="${xmet_root}/register" id="registerForm"  method="POST" >		
			
			<div class='row remove-bottom'>
				<label class='three columns alpha' for="username">User name <em>*</em></label>
				<input class='three columns alpha half-bottom' type="text" size='40' name='username' id='username' value=''/>
				<div class='ten columns omega'></div>
			</div>
			<div class='row remove-bottom'>
				<label class='three columns alpha' for="title">Title</label>
				<input class='three columns alpha half-bottom'  type="text" size='40' name='title' id='title' value=''/>
				<div class='ten columns omega'></div>
			</div>
			<div class='row remove-bottom'>
				<label class='three columns alpha' for="firstname">First name <em>*</em></label>
				<input class='eight columns alpha half-bottom' type="text" size='40' name='firstname' id='firstname' value=''/>
				<div class='ten columns omega'></div>
			</div>
			<div class='row remove-bottom'>
				<label class='three columns alpha' for="lastname">Last name <em>*</em></label>
				<input class='eight columns alpha half-bottom' type="text"  size='40' name='lastname' id='lastname' value=''/>
				<div class='ten columns omega'></div>
			</div>				
			<div class='row remove-bottom'>
				<label class='three columns alpha'  for="affiliation">Affiliation</label>
				<input class='eight columns alpha half-bottom' type="text"  size='40' name='affiliation' id='affiliation' value=''/>
				<div class='ten columns omega'></div>
			</div>				
			<div class='row remove-bottom'>
				<label class='three columns alpha'  for="email">e-mail <em>*</em></label>
				<input class='eight columns alpha half-bottom' type="text"size='40' name='email' id='email' value=''/>
				<div class='ten columns omega'></div>
			</div>				
			<div class='row remove-bottom'>
				<label class='three columns alpha'  for="homepage">WWW</label>
				<input class='eight columns alpha half-bottom' type="text"  size='40' name='homepage' id='homepage' value=''/>
				<div class='five columns omega'></div>
			</div>				
			<div class='row remove-bottom'>
				<label class='three columns alpha'  for="keywords">Keywords</label>
				<input class='eight columns alpha half-bottom'  type="text"  size='40' name='keywords' id='keywords' value=''/>
				<div class='five columns omega'></div>
			</div>				
			<div class='row remove-bottom'>
				<label class='three columns alpha'  for="reviewer">Available as a curator</label>
				<input class='eight columns alpha half-bottom' type="checkbox" name='reviewer' id='reviewer' value=''/>
				<div class='five columns omega'></div>
			</div>				
			<div class='row remove-bottom'>
				<label class='three columns alpha'  for='pwd1'>Password <em>*</em></label>
				<input class='six columns alpha half-bottom'  type='password' size='40' id='pwd1' name='pwd1' value=''/>
				<div class='five columns omega'></div>
			</div>				
			<div class='row remove-bottom'>
				<label class='three columns alpha'  for='pwd2'>Confirm password <em>*</em></label>
				<input class='six columns alpha half-bottom' type='password' size='40' id='pwd2' name='pwd2' value=''/>
				<div class='five columns omega'></div>
			</div>				
			<div class='row remove-bottom'>
				<label class='three columns alpha'>&nbsp;</label>
				<input class='three columns alpha' id='register' name='register' type='submit' class='submit' value='Register'>
				<div class='ten columns omega'></div>
			</form>		
			</div>
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