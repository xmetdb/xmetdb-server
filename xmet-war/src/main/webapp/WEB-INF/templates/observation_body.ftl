<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<#include "/observation_head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content ui-widget ' style='margin-top: 25px; padding: 0 .1em;'>
	<div class='ui-widget-header ui-corner-top'><p>&nbsp;Observation ID: <span id="xmet_id"></span></p></div>
	<div class='ui-widget-content ui-corner-bottom' style='margin-top: 0px;' />
			<p class='w_p'>						
	<table id="xmet_obs">
	<tr>
	<th align="right" height="150px">Substrate:</th><td colspan="2"><span id="xmet_substrate"></span></td>
	<!-- Export & edit will be links -->
	<td align="right" valign="top" title="Not working yet!!!"><u>Export</u><p><u>Edit</u></td>
	</tr>
	<tr>
	<th align="right">Atom uncertainty:</th><td colspan="3">Certain/Uncertain (TODO)</td>
	</tr>		
	<tr>
	<th align="right" height="150px">Product:</th><td colspan="3"><span id="xmet_product"></span></td>
	</tr>
	<tr>
	<th align="right">Product amount:</th><td colspan="3">Major/Minor/Unknown (TODO)</td>
	</tr>	
	<tr>
	<th align="right">Experiment:</th><td colspan="3"><span id="xmet_experiment"></span></td>
	</tr>	
	<tr>
	<th align="right">Enzyme:</th><td><span id="xmet_enzyme"></span></td>
	<th align="right">Allele:</th><td><span id="xmet_allele">TODO</span></td>
	</tr>	
	<tr>
	<th align="right">Reference</th><td colspan="3"><span id="xmet_reference"></span></td>
	</tr>	
	</table>
		</p>
	</div>
		   	
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>

	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>