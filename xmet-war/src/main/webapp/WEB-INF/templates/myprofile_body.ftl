<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<#include "/users_head.ftl" >


</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="eleven columns" style="margin:0;padding:0;" >
						
		<div class='tabs'>
		<ul>
		<li><a href='#tabs-id'>User Profile</a></li>
		<li><a href='' id='protocoluri'  title='XMEDTB observations'>XMETDB observations</a><span></span></li>
		<li><a href='' id='alerturi' title='XMETDB alerts alerts'>Saved searches</a><span></span></li>
		</ul>
		<!-- user details tab -->
			<div id='tabs-id'>
			<span class='summary'>
			<form action="/xmetdb/myaccount/?method=put" id="form_myaccount" method="POST" >			
			<#if myprofile>
				<#assign ro=''>
			<#else>
				<#assign ro='readonly'>
			</#if>

			<h2><a href=''><span id='useruri'></span></a></h2>
			<p class="remove-bottom"><label for="username">User name</label><b><span id='username'></span></b></p>
			<p class="remove-bottom"><label for="title">Title</label><input type="text" ${ro} size='40' name='title' id='title' value=''/></p>
			<p class="remove-bottom"><label for="firstname">First name</label><input type="text" ${ro} size='40' name='firstname' id='firstname' value=''/></p>
			<p class="remove-bottom"><label for="lastname">Last name</label><input type="text" ${ro} size='40' name='lastname' id='lastname' value=''/></p>
			<p class="remove-bottom"><label for="organisation">Affiliation</label>
			<table id='organisations'>
				<thead style="display:none;">
				<th></th>
				</thead>
				<tbody></tbody>
			</table>
			</p>

			<p class="remove-bottom"><label for="email">e-mail</label><input type="text" ${ro} size='40' name='email' id='email' value=''/></p>
			
			<p class="remove-bottom"><label for="homepage">WWW</label><input type="text" ${ro} size='40' name='homepage' id='homepage' value=''/></p>
			<p class="remove-bottom"><label for="keywords">Keywords</label><input type="text" ${ro} size='40' name='keywords' id='keywords' value=''/></p>
			<p class="remove-bottom"><label for="reviewer">Available as a reviewer</label><input type="checkbox" ${ro} name='reviewer' id='reviewer' value=''/></p>
			<#if myprofile>
				<p><label for="update">&nbsp;</label><input id='update' name='update' type='submit' value='Update'></p>
			</#if>
			
			<#if myprofile>
				<hr>
				<p class="remove-bottom"><a href="/xmetdb/myaccount/reset">Change password</a></p>			
			</#if>
			</form>
			</span></div>
		<!-- protocols -->
			<div id='XMETDB observations'></div>
		<!-- alerts -->
			<div id='QMRF_alerts'></div>
		<!-- wrapping up -->
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