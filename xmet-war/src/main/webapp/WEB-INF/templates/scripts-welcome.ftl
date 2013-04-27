<script type="text/javascript">
	$(document).ready( function () {
		$('a.selectable').mouseover(function () { $(this).addClass('ui-state-active');    } );
		$('a.selectable').mouseout(function  () { $(this).removeClass('ui-state-active'); } );

		try {
			$('span#valueStructures').load( '${queryService}/admin/stats/structures');
		} catch (e) {}
		try {
			$('span#valueEnzymes').load( '${xmet_root}/stats?term=enzymes&media=text/plain');
		} catch (e) {}
		try {
			$('span#valueObservations').load( '${xmet_root}/stats?term=observations&media=text/plain');
		} catch (e) {}
	});

</script>