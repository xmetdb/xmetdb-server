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

<div class="container columns">
		<div class="sixteen columns" id="header">
			<#include "/s_banner.ftl">
		</div>
		<div class="three columns">
			<a href='http://xmetdb.org/'>
				<img class='scale-with-grid' border='0' src='/xmetdb/images/logo.png' alt='XMETDB logo'>
			</a>
		</div>
		<div class="thirteen columns">
			<h1 class="remove-bottom">
			Xenobiotics Metabolism Database			
			</h1>
			<h5>Version 1.0</h5>
		</div>
		<div class="sixteen columns" >
			<div id="header_bottom">&nbsp;</div>
		</div>
		
		<!-- Menu
		================================================== -->
		<div class="two columns" style="margin-right:0;">
		 	<#include "/s_menu.ftl">
			 <br>
			 <#include "/s_stats.ftl">
		</div>
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns" >
		
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
		
		<!-- Help on the right
		================================================== -->
		<div class="two columns">
			<p>Help links</p>
		</div>
		
		<!-- Footer
		================================================== -->
		
		<div class="sixteen columns add-bottom"></div>
		
		<#include "/s_footer.ftl">
		
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
</body>
</html>