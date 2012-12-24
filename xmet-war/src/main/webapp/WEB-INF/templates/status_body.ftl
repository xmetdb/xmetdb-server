<#include "/s_html.ftl">
<head>
  <#include "/s_head.ftl">
<script type='text/javascript'>  
  function toggleDiv(divId) {
		$('#'+divId).toggle();
  }		
</script>  
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns" style="padding:0;" >
		
		<div class="ui-widget-header ui-corner-top">
		<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;' title='' id='status_name'>${status_name}</span>
		<span id='error_name'>${status_error_name}</span>
		</div>
		
		<div class='ui-widget-content ui-corner-bottom '>
			<div class='row' style='text-align:center;margin:10px;'>
				<span id='error_description'>${status_error_description}</span>
			</div>
			<div class='row help' style='text-align:right;margin:10px;'>
					<a href='#' style='background-color: #fff; padding: 5px 10px;' onClick="toggleDiv('details'); return false;">Details</a>
			</div>
			<div class='row help' style='display: none;text-align:right;margin:10px;' id='details'>
				<#if status_details??>
				<span id='details_description'>[${status_code}]&nbsp;<a href='${status_uri}' target=_blank>${status_details}</a></span>
				<#else>
				<span id='details_description'>[${status_code}]&nbsp;<a href='${status_uri}' target=_blank>${status_name}</a></span>
				</#if>
			</div>
		</div>
		
		
		</div>
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>