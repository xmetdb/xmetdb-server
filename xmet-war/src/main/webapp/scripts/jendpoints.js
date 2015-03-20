function defineEndpointsTable(url,root) {

	var facet = getEndpointsFacet(); 
	jQuery.fn.dataTableExt.oSort['facet-asc']  = function(a,b) {
		var x = facet[a];
		var y = facet[b];
		return ((x < y) ? -1 : ((x > y) ?  1 : 0));
	};

	jQuery.fn.dataTableExt.oSort['facet-desc'] = function(a,b) {
		var x = facet[a];
		var y = facet[b];
		return ((x < y) ?  1 : ((x > y) ? -1 : 0));
	};	
	var eTable = $('#endpoints').dataTable( {
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": false,
		"aoColumnDefs": [
				{ //0
					"aTargets": [ 0 ],	
					"sClass" : "center",
					"bSortable" : true,
					"bSearchable" : true,
					"bUseRendered" : false,
					"asSorting": [ "desc","asc" ],
					"mDataProp" : "name",
					"sType": "facet" ,
					"sClass": "readonly",
					sWidth : "32px",
					"fnRender" : function(o,val) {
						 var count = facet[val];	
						 if (count == null) return "";
						 else {
							 return  "<span class='ui-icon ui-icon-folder-collapsed zoomxmet' style='float: left; margin: .1em;' title='Click to show XMetDB observations'></span>"+
							 		"<label>("+count + ")</label>";
						 }
					}
				},			
				{ "mDataProp": "code" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 1 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  sWidth : "10%"
				},
				{ "mDataProp": "name" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 2 ],
				  "bSearchable" : true,
				  "bSortable" : true,
				  "bUseRendered" : false,
				  "fnRender" : function(o,val) {
					  return "<a title='Click to display the available observations for this enzyme' href='/xmetdb/protocol?xmet_enzyme=" + encodeURIComponent(o.aData["code"]) + "'>"+o.aData["name"]+"</a>";
				  }
				},
				{ "mDataProp": "uniprot" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 3 ],
					  "bSearchable" : true,
					  "sWidth" : "10%",
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "fnRender" : function(o,val) {
						  return "<a href='http://www.uniprot.org/uniprot/"+ val + "' target='uniprot'>" + val + "</a>";
					  }
				},
				{ "mDataProp": "alleles" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 4 ],
					  "bSearchable" : true,
					  "sWidth" : "6em",
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "fnRender" : function(o,val) {
						  if (!(val instanceof Array)) {
						  	val = val.split("\n");
						  }
						  var sOut = "<select style='width:5em;' id='alleles_"+o.aData["id"]+"'>";
						  sOut += '<option value="" selected></option>';
						  $.each(val, function(i) {
							    if (""!=val[i].trim())
							    	sOut += '<option value="' + val[i] + '">' + val[i] + '</option>';
							});
						  sOut += "</select>";
						  return sOut;
					  }
				}				
			],
	    "order": [[0, "desc" ]],
		"sDom" : '<"help remove-bottom"i><"help"p>Trt<"help"lf>',
		"sSearch": "Filter:",
		"bJQueryUI" : true,
		"bPaginate" : true,
		"sPaginationType": "full_numbers",
		"sPaginate" : ".dataTables_paginate _paging",
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": url,
		"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
		      oSettings.jqXHR = $.ajax( {
		        "type": "GET",
		        "url": sSource ,
		        "data": aoData,
		        "dataType": "json", 
		        "contentType" : "application/json",
		        "success": function(json) {
		        	var a = { "aaData" : json };
		        	fnCallback(a);
		        },
		        "cache": false,
		        "error" : function( xhr, textStatus, error ) {
		        	oSettings.oApi._fnProcessingDisplay( oSettings, false );
		        }
		      } );
		},
		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
	          $(nRow).attr("id",aData["id"]);
	          return nRow;
	    },		
		"oLanguage": {
	            "sProcessing": "<img src='/xmetdb/images/progress.gif' border='0'>",
	            "sLoadingRecords": "No enzymes found.",
	            "sLengthMenu": 'Display <select>' +
                '<option value="10">10</option>' +
                '<option value="20">20</option>' +
                '<option value="50">50</option>' +
                '<option value="100">100</option>' +
                '<option value="-1">all</option>' +
                '</select> enzymes.'	            
	    }
    
	} )
	.makeEditable({
		"aoColumns": [
                null,
                {
                    type:'text'	,
                    loadtext: 'loading...',
                    indicator: 'Saving enzyme code ...',
                    tooltip: 'Click to edit enzyme code',
                    loadtext: 'loading...',
                    onblur: 'cancel',
                    submit: 'Save changes',
                    fnOnCellUpdated: function(sStatus, sValue, settings){
						//alert("Enzyme code is updated with value " + sValue);
					}
                },
                {
                    type:'text'	,
                    loadtext: 'loading...',
                    indicator: 'Saving enzyme name ...',
                    tooltip: 'Click to edit enzyme name',
                    loadtext: 'loading...',
                    onblur: 'cancel',
                    data: function (a,b) {
                    	return $(a).text();
                    },
                    submit: 'Save changes'                  	  
                },
                {
                    type:'text'	,
                    loadtext: 'loading...',
                    indicator: 'Saving UNIPROT code ...',
                    tooltip: 'Click to edit UNIPROT code',
                    loadtext: 'loading...',
                    data: function (a,b) {
                    	return $(a).text();
                    },
                    onblur: 'cancel',
                    submit: 'Save changes'                  	  
                },
                {
                      type:'textarea'	,
                      loadtext: 'loading...',
                      indicator: 'Saving Alleles...',
                      tooltip: 'Click to edit alleles',
                      loadtext: 'loading...',
                      data: function (a,b) {
                    	var sOut = "";
                    	$(a).children().each(function(index,option){
                    		if (sOut!="") sOut +="\n"; 
                    		sOut += $(option).text();
                    	});
                    	return sOut;
                      },
                      onblur: 'cancel',
                      submit: 'Save changes',
                      //loadurl: root + "/catalog?max=1", //?
                     // loadtype: 'GET'                    	  
                 }
        ],		
        fnOnDeleting: function(tr, id)  {       
                this["sDeleteURL"] = root + "/catalog/"+id +"?method=DELETE";
                return true;
        },
        fnOnDeleted: function(status) {       
        		eTable.fnDraw(true);
        },        
        sAddURL: root+"/catalog",
        sUpdateURL: root+"/catalog?method=PUT",
        sAddNewRowFormId: "formAddNewEnzyme",
        sAddNewRowButtonId: "btnAddNewEnzyme",
        sDeleteURL: root + "/catalog?method=DELETE",
        oDeleteRowButtonOptions: {
            label: "Remove",
            icons: { primary: 'ui-icon-trash' }
        },
		oAddNewRowButtonOptions: {	label: "Add...",
			icons: {primary:'ui-icon-plus'} 
		},
		sAddDeleteToolbarSelector: ".dataTables_length"		
	});
	return eTable;
}

/* XMetDB list per structure */
function fnEndpointXMETDBList(oTable, nTr, id) {
	var obj = oTable.fnGetData(nTr);
	var sOut = '<div class="ui-widget-content ui-corner-all" id="' + id + '">';
	sOut = sOut + "<div id='" + id + "_xmetdb' >Please wait while XMetDB observations list is loading...</div>";
	sOut += "</div>";	
	
	var details = '#' + id + '_xmetdb';

	var code = encodeURIComponent(obj["code"]);
	var qmrf_query = "/xmetdb/protocol?xmet_enzyme=" + code + "&media=application%2Fjson";
      $.ajax({
          dataType: "json",
          url: qmrf_query,
          success: function(data, status, xhr) {
        	  var dataSize = data.observations.length;
        	  $(details).empty();
        	  $(details).append('<table id="' + id + '_table"  cellpadding="1" border="1" width="100%" cellspacing="1"><thead>\n');
        	  $(details).append('<th class="contentTable" >XMetDB ID</th>');
        	  $(details).append('<th class="contentTable">Experiment&nbsp;</th>');
        	  $(details).append('<th class="contentTable">Product amount&nbsp;</th>');
        	  $(details).append('<th class="contentTable">Atom uncertainty&nbsp;</th>');
        	  $(details).append('<th class="contentTable">Last updated&nbsp;</th>');
        	  $(details).append('</thead>');
        	  $(details).append('<tbody>');
        	  for (i = 0; i < dataSize; i++) {
        		  $(details).append('<tr>');
        		  $(details).append('<td><a href="'+data.observations[i].uri+'" target=_blank>'+data.observations[i].identifier+'</a></td>');
        		  $(details).append('<td>'+data.observations[i].title+ ' (' + data.observations[i].description + ')</td>');
        		  $(details).append('<td>'+data.observations[i].product_amount + '</td>');
        		  $(details).append('<td>'+data.observations[i].atom_uncertainty + '</td>');
        		  $(details).append('<td>'+data.observations[i].updated + '</td>');
        		  $(details).append('</tr>\n');
        	  }
        	  $(details).append('</tbody>');
        	  $(details).append('</table>\n');
        	  //$('#'+id + '_table').dataTable();
          },
          error: function(xhr, status, err) {
          },
          complete: function(xhr, status) {
          }
       });
       
	return sOut;
}
function getID() {
	   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}	

function getEndpointsFacet() {
	var facet = {};	
    $.ajax({
        dataType: "json",
        async: false,
        url: "/xmetdb/endpoint?media=application/json",
        success: function(data, status, xhr) {
        	$.each(data["facet"],function(index, entry) {
        		facet[entry["value"]] = entry["count"]; 
        	});
        },
        error: function(xhr, status, err) {
        },
        complete: function(xhr, status) {
        }
     });
    return facet;
}

