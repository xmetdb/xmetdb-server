<div class="sixteen columns" id="header">
					
	<ul class='topLinks'>
	<li class='topLinks'>
		<a class='topLinks' href='http://xmetdb.org/'>Help</a>
	</li>
	<li class='topLinks'>|</li>
	<li class='topLinks'>
		<#if username??>
			<a class='topLinks login' title='You are currently logged in as "${username}". Click here to log out.' href='#' onClick='document.forms["logoutForm"].submit(); return false;'>Log out [<b>${username}</b>]</a>			   
			<form id='logoutForm' action='/xmetdb/protected/signout?targetUri=.' method='POST'></form>
		<#else>
			<a class='topLinks login' title='Log in here to submit new observations (not required for searching)' href='/xmetdb/login'>Log in</a>
			<li class='topLinks'>|</li>
			<li class='topLinks'>
			<a class='topLinks register' title='Register' href='/xmetdb/register'>Register</a>
			</li>				
		</#if>			
	</li>
	</ul>
		</div>
		<div class="three columns">
			<a href='http://xmetdb.org/'>
				<img class='scale-with-grid' border='0' src='/xmetdb/images/logo.png' alt='XMETDB logo'>
			</a>
		</div>
		<div class="thirteen columns">
			<h1 class="remove-bottom">
			Xenobiotics Metabolism Database			
			</h1>
			<h5>Version 1.0</h5>
		</div>
		<div class="sixteen columns" >
			<div id="header_bottom" class="remove-bottom">&nbsp;</div>
		</div>

