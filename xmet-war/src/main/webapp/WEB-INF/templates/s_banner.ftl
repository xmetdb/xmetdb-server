<div class="sixteen columns" id="header">
					
	<ul class='topLinks'>
	<li class='topLinks'>
		<a class='topLinks ccby' href='http://creativecommons.org/licenses/by/2.0/' target='license' title='CC-BY'>License</a>
	</li>
	<li class='topLinks'>|</li>
	<#if username??>
		<li class='topLinks'>
			<a class='topLinks login' title='You are currently logged in as "${username}". Click here to log out.' href='#' onClick='document.forms["logoutForm"].submit(); return false;'>Log out</a>
			&nbsp;<a class='topLinks' title='You are currently logged in as "${username}".Click here to edit your profile' href='${xmet_root}/myaccount'>[<b>${username}</b>]</a>
			<#if xmetdb_curator?? && xmetdb_curator>
				&nbsp;|<a class='topLinks curator' href='${xmet_root}/curator' title='Click to view uncurated observations'>Curator</a>
			</#if>
			<#if xmetdb_admin?? && xmetdb_admin>
				&nbsp;|<a class='topLinks admin' href='${xmet_root}/admin' title='Go to admin tasks'>Admin</a>
			</#if>						   
			
			<form id='logoutForm' action='${xmet_root}/protected/signout?targetUri=.' method='POST'></form>
		</li>	
	<#else>
		<li class='topLinks'>
			<a class='topLinks login' title='Log in here to submit new observations (not required for searching)' href='${xmet_root}/login'>Log in</a>
		</li>	
		<li class='topLinks'>|</li>
		<li class='topLinks'>
		<a class='topLinks register' title='Register' href='${xmet_root}/register'>Register</a>
		</li>				
	</#if>			
	</ul>
		</div>
		<div class="three columns">
			<a href='http://xmetdb.org/'>
				<img class='scale-with-grid' border='0' src='${xmet_root}/images/logo.png' alt='XMETDB logo'>
			</a>
			<#if xmet_version_short??>
				<div class='h5' title='${xmet_version_long}'>Version ${xmet_version_short}</div>
			<#else>
				<div class='h5'>Version 1.0</div>	
			</#if>
		</div>
		<div class="thirteen columns">
			<h1 class="remove-bottom">
			Xenobiotics Metabolism Database			
			</h1>
			<div id="breadCrumb" class="row breadCrumb module remove-bottom">
                    <ul>
                        <li>
                            <a href="${xmet_root}" title="XMetDB Home">Home</a>
                        </li>
                    </ul>
			</div>
		</div>
		<div class="sixteen columns" >
			<div id="header_bottom" class="remove-bottom">&nbsp;</div>
		</div>
