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
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ],
				   "fnRender": function ( o, val ) {
	          				return "<span title='"+ o.aData["description"] +"'>" + val + "</span>";
	        		}
				},
				{ "mDataProp": "enzyme.code" , "asSorting": [ "asc", "desc" ], "bSearchable" : true	},
				{ "mDataProp": "updated", "asSorting": [ "asc", "desc" ] },
				{ "mDataProp": "owner.username" , "asSorting": [ "asc", "desc" ] }
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		/*
		"sDom": '<"H"fr>tC<"F"ip>', //ColVis 
		"oColVis": {
			"buttonText": "&nbsp;",
			"bRestore": true,
			"sAlign": "left"
		},
		"fnDrawCallback": function (o) {
			var nColVis = $('div.ColVis', o.nTableWrapper)[0];
			nColVis.style.width = o.oScroll.iBarWidth+"px";
			nColVis.style.top = ($('div.dataTables_scroll', o.nTableWrapper).position().top)+"px";
			nColVis.style.height = ($('div.dataTables_scrollHead table', o.nTableWrapper).height())+"px";
		},		
		*/
		"oLanguage": {
	            "sProcessing": "<img src='images/progress.gif' border='0'>"
	    },
		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
				//retrieve the first compound URI from substrates dataset URI
				 if ((aData.Substrate.dataset.structure === undefined) || (aData.Product.dataset.structure==null)) {
				      $.ajax({
				          dataType: "jsonp",
				          url: aData.Substrate.dataset.uri + "?max=1&media=application%2Fx-javascript",
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
				//retrieve the first compound URI from products dataset URI
				 if ((aData.Product.dataset.structure === undefined) || (aData.Product.dataset.structure==null)) {
				      $.ajax({
				          dataType: "jsonp",
				          url: aData.Product.dataset.uri + "?max=1&media=application%2Fx-javascript",
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
				 //retrieve the enzyme from /protocol/id/endpoint URI
				 if ((aData.enzyme.code === undefined) || (aData.enzyme.code ==null)) {
				      $.ajax({
				          dataType: "json",
				          url: aData.uri + "/endpoint?max=1&media=application%2Fjson",
				          success: function(data, status, xhr) {
				        	  aData.enzyme.code = data[0].code;
				        	  aData.enzyme.name = data[0].name;
				        	  $('td:eq(4)', nRow).html(renderEnzyme(data[0].code,data[0].name));	
				          },
				          error: function(xhr, status, err) { },
				          complete: function(xhr, status) { }
				       });
				 } else {
					 $('td:eq(4)', nRow).html(renderEnzyme(aData.enzyme.code,aData.enzyme.name));	
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
		return '<a href="'+val+'" title="'+cmpURI+'"><img border="0" src="'+cmpURI+'&w=150&h=150"></a><div>Test</div>';
}

function renderEnzyme(code,name) {
	return "<span title='"+ name +"'>" + code + "</span>";
}
</script>