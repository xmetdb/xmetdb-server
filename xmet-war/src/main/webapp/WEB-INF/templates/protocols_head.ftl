<script type="text/javascript">
$(document).ready(function() {
<#if xmetdb_curator?? && xmetdb_curator>
	defineObservationsTable("#protocols","${xmet_request_json}","${xmet_root}",true);
<#else>
	defineObservationsTable("#protocols","${xmet_request_json}","${xmet_root}",false);
</#if>	
} );

</script>