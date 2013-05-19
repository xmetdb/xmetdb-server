<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.autogrow.js'></script>
<script  type="text/javascript" src="${xmet_root}/jquery/jquery.imagemapster.min.js"></script>
<script  type="text/javascript" src="${xmet_root}/scripts/jopentox-ui-atoms.js"></script>
<script type='text/javascript' src='${xmet_root}/scripts/jcompound.js'></script>
<script type="text/javascript">

$(document).ready(function() {
	<#assign isAdmin=0>
	<#if xmetdb_admin?? && xmetdb_admin>
		<#assign isAdmin=1>
	<#else>
		<#assign isAdmin=0>
	</#if>
	<#if username??>
		<#assign currentuser=username>
	<#else>
		<#assign currentuser="">
	</#if>
	
	loadObservation("${xmet_root}","${xmet_request_json}","${queryService}","${currentuser}",${isAdmin});
			
} );

</script>				       