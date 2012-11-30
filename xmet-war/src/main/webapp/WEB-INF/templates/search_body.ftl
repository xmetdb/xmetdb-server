<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<#include "/search_head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content ui-widget ' style='margin-top: 25px; margin-left: 10px;  margin-right: 10px; padding: 0 .1em;'>
	<div class='ui-widget-header ui-corner-top' style='margin-top: 0px'><p>Search XMETDB</p></div>
	<div class='ui-widget-content ui-corner-bottom' style='margin-top: 0px;' />
			<p class='w_p' style='margin-top: 0px; ' >						
	<form method="GET" action="/xmetdb/chemical" name="form" id="search_form">		
	<table id="xmet_obs" style='margin-top: 0px;' >
	<tr>
	<th align="right" valign="top">Structure:</th>
	<td colspan="2">
	   <input type='hidden' name='type' value='smiles'>
	   <input type='button' class='draw' tabindex='0' value='Draw (sub)structure' title='Launches structure diagram editor' onClick='startEditor("${xmet_root}","form");'><br>
	   <input type='text' name='search' size='40' value='c1ccccc1Cl' tabindex='1' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'><br>
	   <input class="ui-widget" type='checkbox' id="search_substrates" checked>Substrate&nbsp;
	   <input class="ui-widget" type='checkbox' id="search_products">Product
	</td>
	<td align="left" valign="top">
	   <input type='radio' value='auto' name='option'  title='Exact structure or search by identifier' size='20' tabindex='2'>Auto<br>
	   <input type='radio' name='option' value='similarity' checked title='Enter SMILES or draw structure' tabindex='3'>Similarity&nbsp;
	   <select title ='Tanimoto similarity threshold' name='threshold' tabindex='4'><option value='0.9'>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5' selected >0.5</option></select>
	   <br>
	   <input type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' tabindex='5' size='20'>Substructure
	
	</td>
	</tr>
	<tr>
	<td colspan="4"><hr></td>
	</tr>
	<tr>
	<th align="right" valign="top">Experiment:</th>
	<td colspan="3">
		<input class="ui-widget" type='checkbox' id="search_exp_ms"  tabindex='6'>MS (Microsomes)&nbsp;
		<input class="ui-widget" type='checkbox' id="search_exp_hep" tabindex='7'>HEP (Hepatocytes)&nbsp;
		<input class="ui-widget" type='checkbox' id="search_exp_enz" tabindex='8'>ENZ (Enzyme)
	</td>
	</tr>	
	<tr>
	<th align="right" valign="top">Enzyme:</th>
	<td>
		<select id="search_enzyme" multiple tabindex='9'>
		</select>
	</td>
	<th align="right" valign="top">	
		Allele
	</th>
	<td valign="top">
		<select id="search_allele" multiple tabindex='10'>
		</select>
	</td>
	</tr>	
	<td colspan="4"><hr></td>
	<tr>
	<th align="right" valign="top">XMETID:</th>
	<td colspan="3">
		<input class="ui-widget" type='text' id="search_xmetid" tabindex='11'>
	</td>
	</tr>
			
	<tr>
	<th align="right" valign="top">Max number of hits</th>
	<td >
		<input type='text' size='3' name='pagesize' value='100' tabindex='12'>
	</td>
	<td colspan="2">
			<input type="submit" class="search" value="Search" tabindex='13'>	
	</td>
	</tr>	
	
	</table>
	</form>
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