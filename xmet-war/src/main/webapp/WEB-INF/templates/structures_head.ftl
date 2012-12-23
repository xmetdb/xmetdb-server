<script type='text/javascript' src='${xmet_root}/scripts/jcompound.js'></script>
<script type='text/javascript' src='${xmet_root}/scripts/xmet_structures.js'></script>
<script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	
	var url = "${xmet_request_jsonp}";

  	var oTable = defineStructuresTable(url,'${queryService}','${query.option!""}' == 'similarity');
    <!-- Details panel -->	
	$('#structures tbody td .zoomstruc img').live(
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
					oTable.fnOpen(nTr, fnStructureXMETObservationsList(oTable,nTr,id),	'details');
											       
				}
		});
	
} );
</script>
