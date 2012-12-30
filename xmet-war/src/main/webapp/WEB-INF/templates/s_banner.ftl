<div class="sixteen columns" id="header">
					
	<ul class='topLinks'>
	<li class='topLinks'>
		<a class='topLinks' href='http://xmetdb.org/'>Help</a>
	</li>
	<li class='topLinks'>|</li>
	<li class='topLinks'>
		<#if username??>
			<a class='topLinks login' title='You are currently logged in as "${username}". Click here to log out.' href='#' onClick='document.forms["logoutForm"].submit(); return false;'>Log out</a>
			&nbsp;<a class='topLinks' title='You are currently logged in as "${username}".Click here to edit your profile' href='${xmet_root}/myaccount'>[<b>${username}</b>]</a>			   
			<form id='logoutForm' action='${xmet_root}/protected/signout?targetUri=.' method='POST'></form>
		<#else>
			<a class='topLinks login' title='Log in here to submit new observations (not required for searching)' href='${xmet_root}/login'>Log in</a>
			<li class='topLinks'>|</li>
			<li class='topLinks'>
			<a class='topLinks register' title='Register' href='${xmet_root}/register'>Register</a>
			</li>				
		</#if>			
	</li>
	</ul>
		</div>
		<div class="three columns">
			<a href='http://xmetdb.org/'>
				<img class='scale-with-grid' border='0' src='${xmet_root}/images/logo.png' alt='XMETDB logo'>
			</a>
			<h5>Version 1.0</h5>
		</div>
		<div class="thirteen columns">
			<h1 class="remove-bottom">
			Xenobiotics Metabolism Database			
			</h1>
			<div id="breadCrumb" class="row breadCrumb module remove-bottom">
                    <ul>
                        <li>
                            <a href="${xmet_root}" title="XMETDB Home">Home</a>
                        </li>
                    </ul>
			</div>
		</div>
		<div class="sixteen columns" >
			<div id="header_bottom" class="remove-bottom">&nbsp;</div>
		</div>
