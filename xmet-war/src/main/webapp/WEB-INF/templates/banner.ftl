<div id='header'>

			<ul class='topLinks'>
			<!--
			<li class='topLinks'>
			<a class='topLinks' href='http://ambit.uni-plovdiv.bg/downloads/qmrf/QMRFEditor-v2.0.0-setup.exe'>Download QMRF Editor</a>
			</li>
			<li class='topLinks'>|</li>
			<li class='topLinks'>
			<a class='topLinks email' href='mailto:JRC-IHCP-COMPUTOX@ec.europa.eu'>Submit QMRF by e-mail</a>
			</li>
			<li class='topLinks'>|</li>
			<li class='topLinks'>
			<a class='topLinks' href='http://qmrf.sf.net/'>Help</a>
			</li>
			<li class='topLinks'>|</li>
						-->
			<li class='topLinks'>

			<#if username??>
				<a class='topLinks login' title='You are currently logged in as "${username}". Click here to log out.' href='#' onClick='document.forms["logoutForm"].submit(); return false;'>Log out [<b>${username}</b>]</a>			   
				<form id='logoutForm' action='./protected/signout?targetUri=.' method='POST'></form>
			<#else>
				<a class='topLinks login' title='Log in here to submit new documents (only required for editors)' href='./login'>Log in</a>
			</#if>			
			</li>
			</ul>

			<a href='http://ihcp.jrc.ec.europa.eu/'>
			<img class='logo_top-left' src='./images/logo_xmetdb.png' alt='XMETDB logo'>
			</a>
</div>