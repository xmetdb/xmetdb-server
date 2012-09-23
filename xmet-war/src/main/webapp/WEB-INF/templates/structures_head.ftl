<script type="text/javascript">
$(document).ready(function() {
	
	var opentox;
	
  	var url =  "http://localhost:8080/ambit2/dataset/8";
  	jQuery.ajax({ 
        type: "GET",
        async: false,
        url: url + "?media=application%2Fx-javascript" ,
        dataType: "jsonp", 
        contentType : "application/x-javascript",
        success: function(data) { 
            defineTable(data);
        }
    });

    	

} );

function defineTable(dataset) {
	console.log(dataset);
	
	$('#structures').dataTable( {
		"aaData" : dataset.dataEntry,
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
		"bSearchable": true
			
	} );
	
}
</script>