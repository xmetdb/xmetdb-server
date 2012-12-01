<#include "/s_html.ftl">
<head>
  <#include "/s_head.ftl">
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

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
		
		<!-- Menu
		================================================== -->
		<div class="three columns" style="padding:0;" >
		 	<#include "/s_menu.ftl">
			 <br>
			 <#include "/s_stats.ftl">
		</div>
		
		<!-- Page Content
		================================================== -->
		<div class="eleven columns" style="margin:0;padding:0;" >
		
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
		
		<!-- Help on the right
		================================================== -->
		<div class="two columns">
			<p>Help links</p>
		</div>
		
		<!-- Footer
		================================================== -->
		
		<div class="sixteen  columns add-bottom"></div>
		
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>