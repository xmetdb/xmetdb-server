<script type="text/javascript">
$(document).ready(function() {
	var url = "${xmet_structuresearch}";
	console.log(url);
  		//"http://localhost:8080/ambit2/query/similarity?search=c1ccccc1Cl&threshold=0.5&media=application%2Fx-javascript";
  	defineTable(url);
    	

} );

function defineTable(url) {

	
	$('#structures').dataTable( {
		"bProcessing": true,
		"bServerSide": false,
		"aoColumns": [
				{ "mDataProp": "compound.URI" , "asSorting": [ "asc", "desc" ],
				  "fnRender" : function(o,val) {
						var cmpURI = val;
						if (val.indexOf("/conformer")>=0) {
								cmpURI = val.substring(0,val.indexOf("/conformer"));
						}								
						//if ((opentox["model_uri"]==null) || (opentox["model_uri"] == undefined)) {
								cmpURI = cmpURI + "?media=image/png";
						//} else {
						//		cmpURI = opentox["model_uri"] + "?dataset_uri=" + cmpURI + "&media=image/png";
						//}
						return '<a href="'+val+'" title="'+cmpURI+'"><img src="'+cmpURI+'&w=150&h=150"></a>';
				  }
				},
				{ "mDataProp": "compound.name" , "asSorting": [ "asc", "desc" ]}
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": url,
		"sAjaxDataProp" : "dataEntry",
		"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
		      oSettings.jqXHR = $.ajax( {
		        "type": "GET",
		        "url": sSource ,
		        "data": aoData,
		        "dataType": "jsonp", 
		        "contentType" : "application/x-javascript",
		        "success": fnCallback,
		        "cache": false
		      } );
		}     
	} );
	
}
</script>