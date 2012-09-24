<script type="text/javascript">
$(document).ready(function() {
	$('#protocols').dataTable( {
		"sAjaxDataProp" : "observations",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": true,
		"sAjaxSource": "/xmetdb/protocol?media=application/json",
		"aoColumns": [
				{ "mDataProp": "identifier" , "asSorting": [ "asc", "desc" ],
 			      "fnRender": function ( o, val ) {
          				return "<a href='"+o.aData["uri"] + "'>" + o.aData["identifier"] + "</a>";
        			}
				},
				{ "mDataProp": "Substrate.dataset.uri" , "asSorting": [ "asc", "desc" ], "bVisible": false },
				{ "mDataProp": "Substrate.dataset.structure" , "asSorting": [ "asc", "desc" ], "sDefaultContent" : "TODO" },
				{ "mDataProp": "Product.dataset.uri" , "asSorting": [ "asc", "desc" ], "bVisible": false },
				{ "mDataProp": "Product.dataset.structure" , "asSorting": [ "asc", "desc" ],"sDefaultContent" : "TODO"},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ]},
				{ "mDataProp": "enzyme" , "asSorting": [ "asc", "desc" ], "sDefaultContent" : "TODO"},
				{ "mDataProp": "updated", "asSorting": [ "asc", "desc" ] },
				{ "mDataProp": "owner.username" , "asSorting": [ "asc", "desc" ] }
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"oLanguage": {
	            "sProcessing": "<img src='images/progress.gif'>"
	    },
		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
				//substrates
				 if ((aData.Substrate.dataset.structure === undefined) || (aData.Product.dataset.structure==null)) {
				      $.ajax({
				          dataType: "jsonp",
				          url: aData.Substrate.dataset.uri + "?media=application%2Fx-javascript",
				          success: function(data, status, xhr) {
				        	  aData.Substrate.dataset.structure = data.dataEntry[0].compound.URI;
				        	  $('td:eq(1)', nRow).html(cmp2image( aData.Substrate.dataset.structure));	
				          },
				          error: function(xhr, status, err) { },
				          complete: function(xhr, status) { }
				       });
				 } else {
					 $('td:eq(1)', nRow).html(cmp2image(aData.Substrate.dataset.structure));
				 }			
				//products
				 if ((aData.Product.dataset.structure === undefined) || (aData.Product.dataset.structure==null)) {
				      $.ajax({
				          dataType: "jsonp",
				          url: aData.Product.dataset.uri + "?media=application%2Fx-javascript",
				          success: function(data, status, xhr) {
				        	  aData.Product.dataset.structure = data.dataEntry[0].compound.URI;
				        	  $('td:eq(2)', nRow).html(cmp2image( aData.Product.dataset.structure));	
				          },
				          error: function(xhr, status, err) { },
				          complete: function(xhr, status) { }
				       });
				 } else {
					 $('td:eq(2)', nRow).html(cmp2image(aData.Product.dataset.structure));
				 }
        }			
	} );
} );

function cmp2image(val) {
		if (val.indexOf("/conformer")>=0) {
			cmpURI = val.substring(0,val.indexOf("/conformer"));
		}								
		//if ((opentox["model_uri"]==null) || (opentox["model_uri"] == undefined)) {
			cmpURI = cmpURI + "?media=image/png";
		//} else {
		//		cmpURI = opentox["model_uri"] + "?dataset_uri=" + cmpURI + "&media=image/png";
		//}
		return '<a href="'+val+'" title="'+cmpURI+'"><img src="'+cmpURI+'&w=150&h=150"></a><div>Test</div>';
}
</script>