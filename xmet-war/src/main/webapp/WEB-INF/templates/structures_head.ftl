<script type='text/javascript' src='${xmet_root}/scripts/jcompound.js'></script>
<script type='text/javascript' src='${xmet_root}/scripts/xmet_structures.js'></script>
<script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	
	var url = "${xmet_request_jsonp}";

  	var oTable = defineStructuresTable(url,'${queryService}','${query.option!""}' == 'similarity');
    <!-- Details panel -->	
	$('#structures tbody td .zoomstruc').live(
			'click',
			function() {
				var nTr = $(this).parents('tr')[0];
				if (oTable.fnIsOpen(nTr)) {
					$(this).removeClass("ui-icon-folder-open");
					$(this).addClass("ui-icon-folder-collapsed");
					this.title='Click to show XMETDB observations';
					oTable.fnClose(nTr);
				} else {
					$(this).removeClass("ui-icon-folder-collapsed");
					$(this).addClass("ui-icon-folder-open");
					this.title='Click to close XMETDB observations list';
					var id = 'v'+getID();
					oTable.fnOpen(nTr, fnStructureXMETObservationsList(oTable,nTr,id),	'details');
											       
				}
		});
	
} );
</script>
