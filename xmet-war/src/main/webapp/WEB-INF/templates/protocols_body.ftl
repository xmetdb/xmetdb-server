<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<#include "/protocols_head.ftl" >
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
		<p class='w_p'>
		<div>
		<table id='protocols'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<th>XMETDBID</th>
		<th>Experiment</th>
		<th>Enzyme</th>
		<th>Updated</th>
		<th>Owner</th>
		</thead>
		<tbody></tbody>
		</table>
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