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
