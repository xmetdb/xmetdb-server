<#include "/html.ftl" >
<head>
<#include "/head.ftl" >

<script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	
	<#if xmet_reg_confirmed??>
	    $.ajax({
	        dataType: "json",
	        async: false,
	        url: "/xmetdb/register/confirm?code=${xmet_reg_confirmed}&media=application%2Fjson",
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
	<div id='wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<div class='ui-widget ' style='margin-top: 20px; padding: 0 .7em;'>
		<div class='ui-widget-header ui-corner-top'><p>Registration</p></div>
	    <div class='ui-widget-content ui-corner-bottom'>	
	    	<p>			
	    	<br>
			<#if xmet_reg_confirmed??>
			<span id='success' style='display:none'>
			Your registration is now confirmed and you can <a href="/xmet/login">log in</a> into XMETDB.<br>
			</span>

			<span id='failure' style='display:none'>
				<strong>Invalid confirmation code!</strong>
			</span>
			
			<img id='progress' src="/xmet/images/progress.gif" alt="Please wait..." title="Please wait...">
			
			<#else>
			Please follow the instructions in the confirmation mail, in order to complete the registration procedure.
			</#if>
			<br>
			</p>
		</div>
		</div>
		
	</div> <#-- content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>