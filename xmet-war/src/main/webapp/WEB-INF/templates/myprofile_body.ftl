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
		<div class="twelve columns remove-bottom" >
		
		<#if myprofile>
			<#assign ro=''>
		<#else>
			<#assign ro='readonly'>
		</#if>
		<form action="${xmet_root}/myaccount/?method=put" id="form_myaccount" method="POST" >		

    	<div class="row remove-bottom ui-widget-header ui-corner-top"> <a href='#' id='useruri'></a></div>
    	<div class="half-bottom ui-widget-content ui-corner-bottom" >
    			
		<div class='row' style="margin:5px;padding:5px;"> 	
		
			<label class='three columns alpha' for="username">User name</label>
			<span class="seven columns alpha remove-bottom" id='username'></span>
			<div class="six columns omega remove-bottom">
			<#if myprofile>
					<a href="${xmet_root}/myaccount/reset">Change password</a>&nbsp;
					<a id="protocoluri" href="#">My Observations</a>&nbsp;
					<a id="alerturi" href="#">My Alerts</a>
			<#else>	
					<a id="protocoluri" href="#">Observations</a>&nbsp;
					<a id="alerturi" href="#">Alerts</a>
			</#if>
			</div>
		</div>
		
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label for="title" class='three columns alpha'>Title</label>
			<input class="three columns alpha remove-bottom" type="text" ${ro} size='40' name='title' id='title' value=''/>
			<div class="ten columns omega">&nbsp;</div>
		</div>
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label class='three columns alpha' for="firstname">First name</label>
			<input class="eight columns alpha remove-bottom" type="text" ${ro} size='40' name='firstname' id='firstname' value=''/>
			<div class="five columns omega">&nbsp;</div>
		</div>
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label class='three columns alpha' for="lastname">Last name</label>
			<input class="eight columns alpha remove-bottom" type="text" ${ro} size='40' name='lastname' id='lastname' value=''/>
			<div class="five columns omega">&nbsp;</div>
		</div>
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label class='three columns alpha' for="organisation">Affiliation</label>
			<div class="eight columns alpha remove-bottom" >
				<table id='organisations'>
				<thead style="display:none;">
				<th></th>
				</thead>
				<tbody></tbody>
				</table>
			</div>
			<div class="five columns omega">&nbsp;</div>
		</div>
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label class='three columns alpha' for="email">e-mail</label>
			<input class="eight columns alpha remove-bottom" type="text" ${ro} size='40' name='email' id='email' value=''/>
			<div class="five columns omega">&nbsp;</div>
		</div>
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label class='three columns alpha' for="homepage">WWW</label>
			<input class="eight columns alpha remove-bottom" type="text" ${ro} size='40' name='homepage' id='homepage' value=''/>
			<div class="five columns omega">&nbsp;</div>
		</div>		
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label class='three columns alpha' for="keywords">Keywords</label>
			<input class="eight columns alpha remove-bottom" type="text" ${ro} size='40' name='keywords' id='keywords' value=''/>
			<div class="five columns omega">&nbsp;</div>
		</div>		
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label class='three columns alpha' for="reviewer">Available as a reviewer</label>
			<input class="one columns alpha remove-bottom" type="checkbox" ${ro} name='reviewer' id='reviewer' value=''/>
			<div class="seven columns alpha">&nbsp;
			</div>
			<div class="five columns omega">&nbsp;</div>
		</div>		
		<div class='row' style="margin:5px;padding:5px;"> 	
			<label class='three columns alpha' for="update">&nbsp;</label>
			<#if myprofile>
				<input class="submit three columns alpha" id='update' name='update' type='submit' value='Update'>
			</#if>
		</div>	

		</form>
		</span>
		</div>
		<!-- twelve columns  -->

		</div> 
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>