<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='/xmetdb/scripts/jendpoints.js'></script>
<script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	
  	var oTable = defineEndpointsTable("${xmet_request_json}");

    <!-- Details panel -->	
	$('#endpoints tbody td .zoomqmrf img').live(
			'click',
			function() {
				var nTr = $(this).parents('tr')[0];
				if (oTable.fnIsOpen(nTr)) {
					this.src = "/xmetdb/images/zoom_in.png";
					this.alt = "Zoom in";
					this.title='Click to show XMETDB observations';
					oTable.fnClose(nTr);
				} else {
				    this.alt = "Zoom out";
					this.src = "/xmetdb/images/zoom_out.png";
					this.title='Click to close XMETDB observations list';
					var id = 'v'+getID();
					oTable.fnOpen(nTr, fnEndpointXMETDBList(oTable,nTr,id),	'details');
											       
				}
		});
} );
</script>
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<div><ul id='hnavlist' >
		<li><a class='pselectable' style='width: 25em;' href='/xmetdb/catalog' title='All enzymes'>All enzymes</a></li>
		</div>
		<p class='w_p'>
		<div>
		<table id='endpoints'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<th></th>
		<th>Code</th>
		<th>Name</th>
		<th>XMETDB observations</th>
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