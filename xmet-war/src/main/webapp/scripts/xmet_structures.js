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
					"mDataProp" : "facets",
					"bUseRendered" : false,
					sWidth : "32px",
					"fnRender" : function(o,val) {
						var sOut = "";
						 $.each(val,function(index) {
							 $.each(val[index],function(i) {
								 sOut += "<span title='" + i + "'>(" + val[index][i] + ")</span>";
							 });

						 });
						 sOut += "<span class='ui-icon ui-icon-folder-collapsed zoomstruc' style='float: left; margin: .1em;' title='Click to show XMetDB observations'></span> ";
						 return sOut;
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
				{     "mDataProp": "compound.URI" , 
					  "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 6 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"smiles");
						  	else return val;						  
					  },
					  "bVisible" : false
				},		
				{	  "mDataProp": "compound.URI" , 
					  "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 7 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"inchi");
						  	else return val;						  
					  },
					  "bVisible" : false
				},	
				{ 	  "mDataProp": "compound.URI", 
					  "asSorting": [ "asc", "desc" ],
					  "sClass" : "inchikey",	
					  "aTargets": [ 8 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"inchikey");
						  	else return val;						  
					  },
					  "bVisible" : false
				}							
			],
		"sDom" : '<"help remove-bottom"i><"help"p>Trt<"help"lf>',
		"bJQueryUI" : true,
		"bPaginate" : true,
		"sPaginationType": "full_numbers",
		"sPaginate" : ".dataTables_paginate _paging",
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
				"sSearch": "Filter:",
	            "sProcessing": "<img src='/xmetdb/images/progress.gif' border='0'>",
	            "sLoadingRecords": "No structures found.",
	            "sLengthMenu": 'Display <select>' +
                '<option value="10">10</option>' +
                '<option value="20">20</option>' +
                '<option value="50">50</option>' +
                '<option value="100">100</option>' +
                '<option value="-1">all</option>' +
                '</select> structures.'	            
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

		

	/* QMRF list per structure */
function fnStructureXMETObservationsList(oTable, nTr, id) {
	var obj = oTable.fnGetData(nTr);
	var details = id + '_xmetdb';
	var sOut = '<div class="ui-widget-content" id="' + id + '">';
	sOut = sOut + "<div id='" + details + "' >Please wait while XMetDB observations list is loading...</div>";
	sOut += "</div>";	

	var uri = encodeURIComponent(obj["compound"]["URI"]);
	var xmet_query = "/xmetdb/protocol?structure=" + uri + "&media=application%2Fjson";
	details = '#' + details;
      $.ajax({
          dataType: "json",
          url: xmet_query,
          success: function(data, status, xhr) {
        	  var dataSize = data.observations.length;
        	  $(details).empty();
        	  $(details).append('<table id="' + id + '_table"  cellpadding="1" border="1" width="100%" cellspacing="1"><thead>\n');
        	  $(details).append('<th class="contentTable" >XMETID</th>');
        	  $(details).append('<th class="contentTable">Experiment&nbsp;</th>');
        	  $(details).append('<th class="contentTable">Product amount&nbsp;</th>');
        	  $(details).append('<th class="contentTable">Enzyme&nbsp;</th>');
        	  $(details).append('<th class="contentTable">Atom uncertainty&nbsp;</th>');
        	  $(details).append('<th class="contentTable">Last updated&nbsp;</th>');
        	  $(details).append('</thead>');
        	  $(details).append('<tbody>');
        	  for (i = 0; i < dataSize; i++) {
        		  $(details).append('<tr>');
        		  $(details).append('<td><a href="'+data.observations[i].uri+'" target=_blank>'+data.observations[i].identifier+'</a></td>');
        		  $(details).append('<td>'+data.observations[i].title+ ' (' + data.observations[i].description + ')</td>');
        		  $(details).append('<td>'+data.observations[i].product_amount + '</td>');
        		  $(details).append('<td>&nbsp;</td>');
        		  $(details).append('<td>'+data.observations[i].atom_uncertainty + '</td>');
        		  $(details).append('<td>'+data.observations[i].updated + '</td>');
        		  $(details).append('</tr>\n');
        	  }
        	  $(details).append('</tbody>');
        	  $(details).append('</table>\n');
        	  $('#'+id + '_table').dataTable();
          },
          error: function(xhr, status, err) {
        	  if (status=404)
        		  $(details).html('No observations found');
        	  else
        		  $(details).html('Error retrieving XMetDB observations [' + status + '] ' +err);
          },
          complete: function(xhr, status) {
          }
       });

	return sOut;
}


