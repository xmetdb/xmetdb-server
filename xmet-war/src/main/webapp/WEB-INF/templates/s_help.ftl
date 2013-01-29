<div class="two columns" style='padding:0;margin:0;'>
	<div class='row half-bottom' style='padding:0;margin:0;' id='pagehelp'>		
	</div>
	<div class='row remove-bottom help' style='padding:0;margin:0;font-weight:bold;' id='keytitle'>		
	</div>
	<div class='row half-bottom help' style='padding:0;margin:0;' id='keycontent'>		
	</div>
	<div class='row remove-bottom' style='padding:0;margin:0;'>		
	<!-- save search widget only in case of query -->			
	<#if xmet_query??><#if username??>
		<#if s["/protocol"]??>
			<#include "/alerts_menu.ftl" >
		<#else><#if s["/chemical"]??>
			<#include "/alerts_menu.ftl" >
				</#if>
			</#if>
	</#if></#if>
	<!-- end saved search -->
	</div>				
</div>
		