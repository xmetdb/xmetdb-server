<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >

<script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	
	<#if xmet_reg_confirmed??>
	    $.ajax({
	        dataType: "json",
	        async: false,
	        url: "${xmet_root}/register/confirm?code=${xmet_reg_confirmed}&media=application%2Fjson",
	        success: function(data, status, xhr) {
	           	$.each(data["confirmation"],function(index, entry) {
	        		if ('confirmed' == entry["status"]) { 
	        			$("#failure").hide();
	        			$("#success").show();
	        		} else {
	        			$("#failure").hide();
	        			$("#success").show();	        			
	        		}
	        	});
	        },
	        statusCode: {
	            404: function() {
	        		$("#failure").show();
	        		$("#success").hide();
	            }
	        },
	        error: function(xhr, status, err) {
	       		$("#failure").show();
        		$("#success").hide();
        		$("#progress").hide();
	        },
	        complete: function(xhr, status) {
	        	$("#progress").hide();
	        }
	     });		
	</#if>
});
</script>
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns" style="padding:0;" >	

		<div class='ui-widget-header ui-corner-top' >&nbsp;XMETDB Registration</div>
	    <div class='ui-widget-content ui-corner-bottom'>	
	    	<div class='row'></div>
			<#if xmet_reg_confirmed??>
			<div class='row' id='success' style='display:none;text-align:center;'>
			&nbsp;<span class="ui-icon ui-icon-info" style="display:inline-block"></span>
			Your registration is now confirmed and you can <a href="${xmet_root}/login">log in</a> into XMETDB.
			</div>

			<span id='failure' class='row' style='display:none'>
				<strong>Invalid confirmation code!</strong>
			</span>
			
			<img id='progress' src="${xmet_root}/images/progress.gif" alt="Please wait..." title="Please wait...">
			
			<#else>
			<div  class='row' style="text-align:center;">
			<span class="ui-icon ui-icon-info" style="display:inline-block"></span>
			Please follow the instructions in the confirmation mail, in order to complete the registration procedure.
			</div>
			</#if>
			<div class='row'></div>
		</div>
		
		<!-- twelve -->
		</div>
		
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>