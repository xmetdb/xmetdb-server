<script type="text/javascript">
$(document).ready(function() {
	$('#protocols').dataTable( {
		"sAjaxDataProp" : "observations",
		"bProcessing": true,
		"bServerSide": false,
		"sAjaxSource": "/xmetdb/protocol?media=application/json",
		"aoColumns": [
				{ "mDataProp": "identifier" , "asSorting": [ "asc", "desc" ],
 			      "fnRender": function ( o, val ) {
          				return "<a href='"+o.aData["uri"] + "'>" + o.aData["identifier"] + "</a>";
        			}
				},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ]},
				{ "mDataProp": "enzyme" , "asSorting": [ "asc", "desc" ]},
				{ "mDataProp": "updated", "asSorting": [ "asc", "desc" ] },
				{ "mDataProp": "owner.username" , "asSorting": [ "asc", "desc" ] }
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true
			
	} );
} );
</script>