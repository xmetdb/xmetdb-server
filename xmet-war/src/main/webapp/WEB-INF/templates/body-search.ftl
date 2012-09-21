<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<div class='w_left'>
		<ul id='navmenu'>
		<li><a class='selectable enter_qmrf' title='Click here to search the Xenobiotics Metabolism Database' href='./protocol'>
			<img class='w_logo_inventory' src='./images/logo_menu.png'>
			Search
		</a></li>
		<li><a class='selectable' title='Click here to submit an observation' href='./submit}'>Submit</a></li>
		<li><a class='selectable' title='About XMETDB' href='${xmet_about}'>About</a></li>
		<li><a class='selectable' title='Click here to read the XMETDB guide' href='${xmet_guide}'>Guide</a></li>
		</ul>
	
		<div id='stats' class="w_stats">
		<table class='w_table_stats'>
			<tr class='w_tr'>
				<th class='w_th' colspan='2'>Statistics</th>
			</tr>
			<tr class='w_tr'>
				<td class='w_td_param'>
					Datasets
				</td>
				<td class='w_td_value'>
					<span id='valueDatasets'></span>
				</td>
			</tr>
			<tr class='w_tr'>
				<td class='w_td_param'>
					Chemical structures
				</td>
				<td class='w_td_value'>
                	<span id='valueStructures'></span>
				</td>
			</tr>
		</table>
		</div>

	</div> <#-- w_menu -->
	<div class='w_content'>
		<p class='w_p_header'>Search XMETDB</p>
		<p class='w_p'>

		<div >
		   <form method='GET' name='form' action='./protocol'>
		   <input type='hidden' name='page' value=''>
		   <input type='hidden' name='type' value='smiles'>
		   <table width='20%'>
		   <tr><td colspan='2' align='center'><input type='button' class='draw' tabindex='0' value='Draw (sub)structure' title='Launches structure diagram editor' onClick='startEditor("");'></td></tr>
		   <tr><td colspan='2' align='center'><input type='text' name='search' size='20' value='%s' tabindex='1' title='%s'></td></tr>
		   <tr><td colspan='2'><input %s type='radio' value='auto' name='option' title='Exact structure or search by identifier' size='20'>Auto</td></tr>
		   <tr><td><input %s type='radio' name='option' value='similarity' title='Enter SMILES or draw structure'>Similarity</td>
		   <td align='left'>
		   <select title ='Tanimoto similarity threshold' name='threshold'><option value='0.9' checked>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5'>0.5</option></select>
		   </td></tr>
		   <tr><td colspan='2'><input %s type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' size='20'>Substructure</td></tr>
		   <tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>
		   <tr><td colspan='2' align='center'><input tabindex='2' id='submit' type='submit' value='Search'/></td></tr>		   
		   </div>
		   </table>
		   </form> 
			&nbsp;
		   <div id='querypic' class='structureright'>%s</div>
	   </div>
		   		</p>
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>