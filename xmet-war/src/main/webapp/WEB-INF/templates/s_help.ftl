<div class="two columns" style='padding:0;margin:0;'>
	<div class='row remove-bottom'><span id="info-link" class="ui-icon ui-icon-info" style="display: inline-block;"></span><a href='#' >Help</a></div>
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
		