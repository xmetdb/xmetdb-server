<div class="two columns" style="padding:0;margin-right:0px;" >
<#if searchURI??>
<#assign s = {searchURI:"selected"}>
<#else>
	<#assign s = {}>
</#if>
<ul id='navmenu'>
	<li><a class='selectable ui-widget-header' title='Click here to search the Xenobiotics Metabolism Database' href='/xmetdb'>
		Search
	</a></li>
	<li ><a class='selectable ui-widget-header' title='All observations' href='${xmet_root}/protocols'>Observations</a></li>
	<li><a class='selectable ui-widget-header' title='Click here to submit an observation' href='${xmet_root}/editor'>Submit</a></li>
	<li><a class='selectable ui-widget-header' title='' href='${xmet_root}/catalog'>Enzymes</a></li>
	<#if username??>
				<li><a class='ui-widget-header selectable ${s["/myaccount"]!""}' href='${xmet_root}/myaccount' title="${username}'s profile and documents.">My profile</a></li>
	</#if>
	<li><a class='selectable ui-widget-header' title='About XMetDB' href='${xmet_about}'>About</a></li>	
	<li><a class='selectable ui-widget-header' title='Click here to read the XMEXMetDBTDB guide' href='${xmet_guide}'>Guide</a></li>
</ul>
<br>
<#include "/s_stats.ftl">
</div>

		

