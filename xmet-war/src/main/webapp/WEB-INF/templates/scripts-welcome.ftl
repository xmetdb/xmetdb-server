<script type="text/javascript">
	$(document).ready( function () {
		$('a.selectable').mouseover(function () { $(this).addClass('hovered');    } );
		$('a.selectable').mouseout(function  () { $(this).removeClass('hovered'); } );

		try {
			$('span#valueDatasets').load(  '${queryService}/admin/stats/dataset'   );
			$('span#valueStructures').load( '${queryService}/admin/stats/structures');
		} catch (e) {
		}
		
<#-- XXX Remove these two lines when the above issue is fixed. 
		$('span#valueDatasets').replaceWith('47');
		$('span#valueStructures').replaceWith('12049');
	-->
	});

</script>