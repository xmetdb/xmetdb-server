<#include "/s_html.ftl">
<head>
  <#include "/s_head.ftl">
  <script type='text/javascript' src='${xmet_root}/scripts/jendpoints.js'></script>
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
					this.src = "${xmet_root}/images/zoom_in.png";
					this.alt = "Zoom in";
					this.title='Click to show XMETDB observations';
					oTable.fnClose(nTr);
				} else {
				    this.alt = "Zoom out";
					this.src = "${xmet_root}/images/zoom_out.png";
					this.title='Click to close XMETDB observations list';
					var id = 'v'+getID();
					oTable.fnOpen(nTr, fnEndpointXMETDBList(oTable,nTr,id),	'details');
											       
				}
		});
} );
</script>
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns ui-widget-content ui-corner-all" style="padding:0;" >
		
		<table id='endpoints'  cellpadding='0' border='0' width='100%' cellspacing='0' style="margin:0;padding:0;" >
		<thead>
		<th></th>
		<th>Code</th>
		<th>Name</th>
		<th>XMETDB observations</th>
		</thead>
		<tbody></tbody>
		</table>
		
		</div>
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>