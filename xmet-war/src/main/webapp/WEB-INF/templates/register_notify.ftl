<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
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
			Please follow the instructions in the confirmation e-mail, in order to complete the registration procedure.
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