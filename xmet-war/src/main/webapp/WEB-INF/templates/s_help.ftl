<div class="two columns" style='padding:0;margin:0;'>
	<div class='row remove-bottom'>Help</div>
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
		