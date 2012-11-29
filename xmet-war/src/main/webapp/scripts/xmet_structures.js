function defineStructuresTable(url, query_service, similarity) {

	
	var oTable = $('#structures').dataTable( {
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": false,
		"aoColumnDefs": [
				{ //0
					"aTargets": [ 0 ],	
					"sClass" : "center",
					"bSortable" : false,
					"bSearchable" : false,
					"mDataProp" : null,
					sWidth : "32px",
					"fnRender" : function(o,val) {
							return "<span class='zoomstruc'><img border='0' src='/xmetdb/images/zoom_in.png' alt='zoom in' title='Click to show compound details'></span>";
					}
				},			
				{ "mDataProp": "compound.URI" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 1 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "sHeight" : "155px",
				  "sWidth" : "155px",
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
								/*
"<a href=\"%s%s/%d?headless=true&details=false&media=text/html\" title=\"Molecule\">Molecule</a>", 
								 */
						return '<img class="ui-widget-content" title="'+val+'" border="0" src="'+cmpURI+'&w=150&h=150">';
				  }
				},
				{ "mDataProp": "compound.name" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 2 ],
				  "bSearchable" : true,
				  "bSortable" : true,
				  "bUseRendered" : false,
				  "sClass" : "names",	
				  "fnRender" : function(o,val) {
					  	if ((val === undefined) || (val == ""))
					  		return formatValues(o.aData,"names");
					  	else return val;
				  },
				  "bVisible" : true
				},
				{ "mDataProp": "compound.cas" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 3 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "sWidth" : "10%",
					  "sClass" : "cas",	
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"cas");
						  	else return val;						  
					  },
					  "bVisible" : true
				},				
				{ "mDataProp": "compound.URI" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 4 ],
					  "bSearchable" : false,
					  "bSortable" : false,
					  "bUseRendered" : false,
					  "sWidth" : "5%",
					  "fnRender" : function(o,val) {
							var uri = encodeURIComponent(val);
							var qmrf_query = "/xmetdb/protocol?structure=" + uri + "&media=text%2Fcsv";
							return '<a href="'+qmrf_query+'" title="Download the list as CSV"><img class="draw" border="0" src="/xmetdb/images/excel.png"></a>';
					  }
				},
				{ "mDataProp": "compound.metric" , "asSorting": [ "asc", "desc" ],
						  "aTargets": [ 5 ],
						  "sTitle" : "Similarity",
						  "sClass" : "similarity",
						  "bSearchable" : true,
						  "bSortable" : true,
						  "sWidth" : "5%",
						  "bVisible"  : similarity
				},
				{ "mDataProp": null , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 6 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : true,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"smiles");
						  	else return val;						  
					  },
					  "bVisible" : false
				},		
				{ "mDataProp": null , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 7 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : true,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"inchi");
						  	else return val;						  
					  },
					  "bVisible" : false
				},	
				{ "mDataProp": null , "asSorting": [ "asc", "desc" ],
					  "sClass" : "inchikey",	
					  "aTargets": [ 8 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : true,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"inchikey");
						  	else return val;						  
					  },
					  "bVisible" : false
				}							
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
		        "crossDomain": true, 
		        "contentType" : "application/x-javascript",
		        "success": function(json) {
		        	identifiers(json);
		        	fnCallback(json);
		        },
		        "cache": false,
		        "error" : function( xhr, textStatus, error ) {
		        	oSettings.oApi._fnProcessingDisplay( oSettings, false );
		        }
		      } );
		},
		"oLanguage": {
	            "sProcessing": "<img src='/xmetdb/images/progress.gif' border='0'>",
	            "sLoadingRecords": "No records found."
	    },
		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
			//retrieve identifiers
			id_uri = query_service + "/query/compound/url/all?search=" + encodeURIComponent(aData.compound.URI) + "?max=1&media=application%2Fx-javascript";
			$.ajax({
			         dataType: "jsonp",
			         "crossDomain": true, 
			         url: id_uri,
			         success: function(data, status, xhr) {
			        	identifiers(data);
			        	$.each(data.dataEntry,function(index, entry) {
				        	aData.compound.name = formatValues(entry,"names");
				        	$('td:eq(2)', nRow).html(aData.compound.name);
				        	aData.compound.cas = formatValues(entry,"cas");
				        	$('td:eq(3)', nRow).html(aData.compound.cas);
				        	aData.compound['smiles'] = formatValues(entry,"smiles");
				        	$('td:eq(6)', nRow).html(aData.compound['smiles']);
				        	aData.compound['inchi'] = formatValues(entry,"inchi");
				        	$('td:eq(7)', nRow).html(aData.compound['inchi']);
				        	aData.compound['inchikey'] = formatValues(entry,"inchikey");
				        	$('td:eq(8)', nRow).html(aData.compound['inchikey']);
			        	});

			         },
			         error: function(xhr, status, err) { },
			         complete: function(xhr, status) { }
			});
			
		}
	} );
	return oTable;
}


function getID() {
	   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}			

	/* QMRF list per structure */
function fnStructureXMETObservationsList(oTable, nTr, id) {
	var obj = oTable.fnGetData(nTr);
	var sOut = '<div class="ui-widget-content ui-corner-all" id="' + id + '">';
	sOut = sOut + "<div id='" + id + "_xmetdb' >Please wait while XMETDB observations list is loading...</div>";
	sOut += "</div>";	
	//TODO rewrite it to use JSON to retrieve observations per structure
	/*
	var uri = encodeURIComponent(obj["compound"]["URI"]);
	var qmrf_query = "/xmetdb/protocol?structure=" + uri + "&headless=true&details=false&media=text%2Fhtml&"+ new Date().getTime();

      $.ajax({
          dataType: "html",
          url: qmrf_query,
          success: function(data, status, xhr) {
        	  $('div#' + id + '_qmrf').html(data);
          },
          error: function(xhr, status, err) {
          },
          complete: function(xhr, status) {
          }
       });
      */ 
	return sOut;
}


function formatValues(dataEntry,tag) {
	var sOut = "";
	$.each(dataEntry.lookup[tag], function(index, value) { 
	  if (dataEntry.values[value] != undefined) {
		  $.each(dataEntry.values[value].split("|"), function (index, v) {
			  if (v.indexOf(".mol")==-1) {
				sOut += v;
			  	sOut += "<br>";
		  	  }
		  });
		  //sOut += dataEntry.values[value];
	  }
	});
	return sOut;
}
