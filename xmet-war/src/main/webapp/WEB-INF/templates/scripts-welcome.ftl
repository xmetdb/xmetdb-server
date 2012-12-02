<script type="text/javascript">
	$(document).ready( function () {
		$('a.selectable').mouseover(function () { $(this).addClass('ui-state-active');    } );
		$('a.selectable').mouseout(function  () { $(this).removeClass('ui-state-active'); } );

		try {
			$('span#valueDatasets').load(  '${queryService}/admin/stats/dataset'   );
			$('span#valueStructures').load( '${queryService}/admin/stats/structures');
		} catch (e) {
		}

	});

</script>