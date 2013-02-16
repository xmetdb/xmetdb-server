var _xmet = {
help : {
	title:"Help",
	content:"XMETDB Help"
}		
}
function cmp2image(val) {
		var cmpURI = val;
		if (val.indexOf("/conformer")>=0) {
			cmpURI = val.substring(0,val.indexOf("/conformer"));
		}								
		//if ((opentox["model_uri"]==null) || (opentox["model_uri"] == undefined)) {
			cmpURI = cmpURI + "?media=image/png";
		//} else {
		//		cmpURI = opentox["model_uri"] + "?dataset_uri=" + cmpURI + "&media=image/png";
		//}
		//return '<a href="'+val+'" title="'+cmpURI+'"><img border="0" src="'+cmpURI+'&w=150&h=150"></a>';
		var id= cmpURI.replace(/:/g,"").replace(/\//g,"").replace(/\./g,"");
		return '<img border="0" alt="'+val+'" src="'+cmpURI+'&w=150&h=150" usemap="#m'+id+'" id="i'+id+'">\n<map id="m'+id+'" name="m'+id+'"></map>';
}

function cmpatoms2image(uri, model_uri) {
	if (uri.indexOf("/conformer")>=0) {
		cmpURI = uri.substring(0,uri.indexOf("/conformer"));
	}								
	if ((model_uri==null) || (model_uri == undefined)) {
		cmpURI = cmpURI + "?media=image/png";
	} else {
		cmpURI = model_uri + "?dataset_uri=" + cmpURI + "&media=image/png";
	}
	//return '<a href="'+val+'" title="'+cmpURI+'"><img border="0" src="'+cmpURI+'&w=150&h=150"></a>';
	var id= uri.replace(/:/g,"").replace(/\//g,"").replace(/\./g,"");
	return '<img border="0" alt="'+uri+'" src="'+cmpURI+'&w=150&h=150" usemap="#m'+id+'" id="i'+id+'">\n<map id="m'+id+'" name="m'+id+'"></map>';
}

function renderEnzyme(code,name) {
	return "<span title='"+ name +"'>" + (code==null?"":code) + "</span>";
}

function loadEnzymesList(root,selectTag,allelesTag) {
	  //clear the list	
	$(selectTag).html("");
	  //get all enzymes
    $.ajax({
        dataType: "json",
        url: root + "/catalog?media=application%2Fjson",
        success: function(data, status, xhr) {
        	  data.sort(function sortfunction(a,b){
        		  if(a.code == b.code){
	          			return 0;
	          	  }
         		  return (a.code < b.code) ? -1 : 1;
			  });
          $("<option value=''></option>").appendTo(selectTag);
          var alleles = {};
      	  data.forEach(function enz(element, index, array) {
      		  $("<option value='" + element.code + "'>" + element.code + "&nbsp;" +element.name + "</option>").appendTo(selectTag);
      		  alleles[element.code] = element.alleles;
      	  });
      	  $(selectTag).change(function() {
        		var x = $(this).val();
                $(allelesTag).html("");
                $("<option value='' selected></option>").appendTo(allelesTag);
                if (alleles[x]!=undefined)
	                alleles[x].forEach(function enz(element, index, array) {
	              		  $("<option value='" + element + "'>" + element + "</option>").appendTo(allelesTag);
	              	});
      	  });
        },
        error: function(xhr, status, err) { },
        complete: function(xhr, status) { }
     });
}	


/**
 * Fills in two hidden text fields with selected compound URIs
 * @param prefix
 */
function useSelected(prefix) {
	$( '#structureSearchResults li.ui-selected img').each(function (index,entry) {
		var results = '#xmet_'+prefix+'_img';
		$('input[name=xmet_'+prefix+'_uppload]').val();
		var results_uri = 'input[name=xmet_'+prefix+'_uri]';
		var results_mol = 'input[name=xmet_'+prefix+'_mol]';
		if (entry.alt.indexOf('http')==0) {
			$(results_uri).val(entry.alt);
			$(results).empty();
			$(results).append('<li class="ui-state-default" >'+cmp2image(entry.alt)+'</li>');
			$(results_mol).val('');
		} else {
			$(results_uri).val(entry.alt);
			$(results).empty();
			$(results).append('<li class="ui-state-default" ><img border="0" src="'+entry.src+'"></li>');
			$(results_mol).val('');
		}
		$("#xmet_"+prefix+"_type").val("uri");
	});
}

function clearStructure(prefix) {
	$("#xmet_"+prefix+"_img").empty();
	$("#xmet_"+prefix+"_type").val("file");
	/*
	<ul class='structresults' id="xmet_product_img" style='height:150px;'></ul>
	<input type="hidden" id="xmet_product_uri" name="xmet_product_uri" value="">
	<input type="hidden" id="xmet_product_mol" name="xmet_product_mol" value="">
	*/
}

function useDrawn(queryService,prefix) {
	var molFile = document.getElementById("iframeSketcher").contentWindow.getMolecule();
	if ((molFile!==undefined) && (molFile != null)) {
		var results = '#xmet_'+prefix+'_img';
		$('input[name=xmet_'+prefix+'_uppload]').val();
		var results_mol = 'input[name=xmet_'+prefix+'_mol]';
		$(results_mol).val(molFile);
		var results_uri = 'input[name=xmet_'+prefix+'_uri]';
		$(results_uri).val();
		$("#xmet_"+prefix+"_type").val("mol");
		$(results).empty();
		runSearchURI(queryService + '/query/compound/search/all?media=application/x-javascript&type=mol&max=1&b64search=' 
								  + encodeURIComponent($.base64.encode(molFile)),
				results,
				function(json) {
					var found = 0;
					$(results_mol).val(molFile);
					$(results).empty();
       				json.dataEntry.forEach(function img(element, index, array) {
       					$(results_uri).val(element.compound.URI);
       					$(results).append('<li class="ui-state-default" >'+cmp2image(element.compound.URI)+'</li>');
       					found++;
       				});
       				if (found==0) {
       					$(results).append('<li class="ui-state-default" ><img border="1" src="'+
       			    			queryService + '/depict/cdk/kekule?type=mol&b64search=' + encodeURIComponent($.base64.encode(molFile)) +
       			    			'&w=150&h=150" alt="'+ molFile +'"></li>');
       				} else {
       					$("#xmet_"+prefix+"_type").val("uri");
       				}

		});
		
	} else {
		alert("Sorry, can't use an empty molecule as a "+prefix + ". Please use the structure diagram editor on the left to draw a chemical structure.");
	}
}

function getQueryURL(queryService,option) {
	switch(option)
	{
	case 'similarity': 
	  url = queryService	+ "/query/similarity?" + $.param(params,false);
	  break;
	case 'smarts':
		  url = queryService	+ "/query/smarts?" + $.param(params,false);
	  break;
	default: //auto
	  url = queryService + "/query/compound/search/all?"+ $.param(params,false);
	}
	
	if (purl.param('search').length>60) $('#qvalue').text(purl.param('search').substring(0,60)+" ...");
	else  $('#qvalue').text(purl.param('search'));
	$('#qvalue').attr('title',purl.param('search'));
	
}

function runSearch(queryService,form,results) {
	runSearchURI(queryService + '?media=application/x-javascript&'+ form.serialize(),
				results,
				function(json) {
       				json.dataEntry.forEach(function img(element, index, array) {
       				$(results).append('<li class="ui-state-default" >'+cmp2image(element.compound.URI)+'</li>');
       				});
	});	 
}

function runSearchURI(sSource,results,callback, errorcallback) {
	$(results).empty(); 
	$.ajax( {
	        "type": "GET",
	        "url": sSource ,
	        "dataType": "jsonp", 
	        "crossDomain": true,  //bloody IE
	        "contentType" : "application/x-javascript",
	        "success": function(json) {
	        	callback(json);
	        },
	        "cache": false,
	        "statusCode" : {
	            400: function() {
	              alert("Not found");
	            },
	            404: function() {
		              alert("Not found");
		        }
	        },
	        "error" : function( xhr, textStatus, error ) {
	        	oSettings.oApi._fnProcessingDisplay( oSettings, false );
	        	if (errorcallback!=undefined) errorcalback();
	        }
	      } );
}

function toggleDrawUI(prefix, idButton, msg) {
 	$( '#drawUI').toggle( 'blind', {}, function(x) {
 		if ($('#drawUI').css('display') == 'none') {
 			$( idButton).text('Show '+msg);
 		} else {
 			$( idButton).text('Hide '+msg);
 			
 			var results_mol = 'input[name=xmet_'+prefix+'_mol]';
 			try {
 				var molFile = $(results_mol).val();
 				if ((molFile===undefined) || (molFile== null) || ("" == molFile)) {
 					//document.JME.readMolFile("");
 				} else {
 					//doesn't work. how to set molecule to the sketcher? 
 					//document.getElementById("iframeSketcher").contentWindow.???
 				}
 			} catch (err) {
 				//document.JME.readMolFile("");
 			}
 		} 
 	});
}      

/**
 * Load structures from remote Ambit dataset uri via JSONP
 */
function loadStructures(datasetURI, results, modelURI) {
	if ((datasetURI===undefined) || (datasetURI==null)) {
		  $(results).empty();
	} else {
	      $.ajax({
	          dataType: "jsonp",
	          "crossDomain": true,  //bloody IE
	          url: datasetURI + "?media=application%2Fx-javascript",
	          success: function(data, status, xhr) {
	        	  var dataSize = data.dataEntry.length;
	        	  $(results).empty();
	        	  for (i = 0; i < dataSize; i++) {
	        		  $(results).append('<li class="ui-state-default" >'+cmpatoms2image(data.dataEntry[i].compound.URI,modelURI)+'</li>');
	        		  var id= data.dataEntry[i].compound.URI.replace(/:/g,"").replace(/\//g,"").replace(/\./g,"");
	        		  createImageMap(data.dataEntry[i].compound.URI, '150','150', '#i'+id, '#m'+id);
	        	  };
	          },
	          error: function(xhr, status, err) { 
	          },
	          complete: function(xhr, status) { }
	       });
	}
}

/*
 * Loads single observation via JSON and fills in the relevant HTML tags
*/
function loadObservation(observation_uri) {
	//fixed uri just for tetsing, otherwise will be read from the observation
	var model_uri = null;//"http://ambit.uni-plovdiv.bg:8080/xmetdata/model/1";
	
	var observation;
    $.ajax({
	          dataType: "json",
	          contentType: 'application/json; charset=utf-8',
	          url: observation_uri,
	        	  //"/xmetdb/protocol/XMETDB2?media=application%2Fjson",
	          success: function(data, status, xhr) {
	        	  
	        	  observation = data.observations[0];
	        	  $('#xmet_id').replaceWith("<a href='"+ observation["uri"] + "'>" + observation["identifier"] + "</a>");
	        	  $('#xmet_experiment').replaceWith(observation["description"] + " (" + observation["title"] + ")");
	        	  //$('span#xmet_substrate').replaceWith(observation.Substrate.dataset.uri);
	        	  //$('span#xmet_product').replaceWith(observation.Product.dataset.uri);
	        	  $('#xmet_reference').replaceWith(observation["reference"]);
	        	  try {
	        		  $('#xmet_doi').hide();
	        		  var doi = observation["reference"].toLowerCase();
	        		  if (doi.indexOf("doi:")==0) {
	        			  $('#xmet_doi').attr("href","http://dx.doi.org/" + doi.replace("doi:",""));
	        			  $('#xmet_doi').show();
	        		  }	  
	        	  } catch (err) {}
	        	  
	        	  $('#xmet_comments').text(observation["comments"]===undefined?"":observation["comments"]);
	        	  
	        	  $('#xmet_atom_uncertainty').replaceWith(observation["atom_uncertainty"]);
	        	  $('#xmet_product_amount').replaceWith(observation["product_amount"]);
	        	  $('#xmet_editor').attr("href","/xmetdb/editor/"+observation["identifier"]);
	        	  $('#xmet_export_json').attr("href",observation["uri"] + "?media=application%2Fjson");
	        	  
	        	  if ((observation.Substrate!=undefined)  && (observation.Substrate!=null) && (observation.Substrate.dataset.uri!=null)) {
	        		  $('#xmet_export_substrate').attr("href",observation.Substrate.dataset.uri + "?media=chemical%2Fx-mdl-sdfile");
	        	  	  $('#xmet_export_substrate').show();
	        	  	loadStructures(observation.Substrate.dataset.uri,"#xmet_substrate",model_uri);
	        	  } else {
	        		  $('#xmet_export_substrate').hide();
	        	  }
	        	  if ((observation.Product!=undefined) && (observation.Product!=null) && (observation.Product.dataset.uri!=null)) {
	        		  $('#xmet_export_product').attr("href",observation.Product.dataset.uri + "?media=chemical%2Fx-mdl-sdfile");
	        		  $('#xmet_export_product').show();
	        		  loadStructures(observation.Product.dataset.uri,"#xmet_product",model_uri);
	          	  } else {
	          		 $('#xmet_export_product').hide();
	          	  }
	        	  loadEnzyme(observation);
	        	  
	          },
	          error: function(xhr, status, err) { 
	        	  xmetdblog(status + " " + xhr.responseText);
	          },
	          complete: function(xhr, status) { 
	        	  xmetdblog(status);
	          }
	       });
}


function editObservation(root,observation_uri) {
	var model_uri = null;
	var observation;
    $.ajax({
	          dataType: "json",
	          contentType: 'application/json; charset=utf-8',
	          url: observation_uri,
	        	  //"/xmetdb/protocol/XMETDB2?media=application%2Fjson",
	          success: function(data, status, xhr) {
	        	  
	        	  observation = data.observations[0];
	        	  $('#breadCrumb_xmet_id').html("<a href='"+ observation["uri"] + "' title='Click to view the observation'>" + observation["identifier"] + "</a>");
	        	  $('#breadCrumb_xmet_id_modify').html("<a href='"+ root + "/editor/"+ observation["identifier"] + "' title='Click to edit the observation'>Modify</a>");
	        	  $('#xmet_id').replaceWith("Modify Observation ID: <a href='"+ observation["uri"] + "' title='Click to view the observation'>" + observation["identifier"] + "</a>");
	        	  $('#xmet_atom_uncertainty option[value='+observation["atom_uncertainty"]+']').attr('selected', 'selected');
	        	  $('#xmet_product_amount option[value='+observation["product_amount"]+']').attr('selected', 'selected');
	        	  $('#xmet_experiment option[value='+observation["title"]+']').attr('selected', 'selected');
	        	  loadEnzyme(observation,function(obs) {
	        		  var allele = obs.enzyme.allele==null?"":obs.enzyme.allele.length>0?obs.enzyme.allele[0]:"";
	        		  $('#xmet_enzyme option[value='+obs.enzyme.code+']').attr('selected', 'selected');
	        		  //$('#xmet_enzyme option[value='+obs.enzyme.code+']').change();
	        		  $('#xmet_allele option[value='+allele+']').attr('selected', 'selected');
	        	  });
	        	  $('#xmet_reference').attr("value",observation["reference"]);
	        	  $('#xmet_comments').text(observation["comments"]===undefined?"":observation["comments"]);
	        	  $('#xmet_substrate_type').val('uri');
	        	  $('#xmet_product_type').val('uri');
	        	  $('#xmet_substrate_upload').val('');
	        	  $('#xmet_product_upload').val('');
	        	  if ((observation.Substrate!=undefined) && (observation.Substrate!=null)  && (observation.Substrate.dataset.uri!=null)) {
	        		  if ( (observation.Substrate.dataset.structure === undefined) || (observation.Substrate.dataset.structure==null)) 
	        			  loadStructures(observation.Substrate.dataset.uri,"#xmet_substrate_img",model_uri);
	        	  }
	        	  if ((observation.Product!=undefined) && (observation.Product!=null) && (observation.Product.dataset.uri!=null)) {
		        	  if ((observation.Product.dataset.structure === undefined) || (observation.Product.dataset.structure==null)) 
		        		  loadStructures(observation.Product.dataset.uri,"#xmet_product_img",model_uri);
	        	  }


	          },
	          error: function(xhr, status, err) { 
	        	  xmetdblog(status + " " + xhr.responseText);
	          },
	          complete: function(xhr, status) { 
	        	  xmetdblog(status);
	          }
	       });
}
/*
* Loads enzyme for a single observation via JSON
*/
function loadEnzyme(observation, renderEnzyme) {
	 if (renderEnzyme === undefined) renderEnzyme = displayEnzyme;
	 if ((observation.enzyme.code === undefined) || (observation.enzyme.code ==null)) {
	      $.ajax({
	          dataType: "json",
	          contentType: 'application/json; charset=utf-8',
	          url: observation.uri + "/endpoint?max=1&media=application%2Fjson",
	          success: function(data, status, xhr) {
	        	  observation.enzyme.code = data[0].code;
	        	  observation.enzyme.name = data[0].name;
	        	  observation.enzyme['allele'] = data[0].alleles==null?"":data[0].alleles.length>0?data[0].alleles[0]:"";
	        	  renderEnzyme(observation);
	          },
	          error: function(xhr, status, err) {
	        	  xmetdblog(status+err);
	          },
	          complete: function(xhr, status) { }
	       });
	 } else {
		 renderEnzyme(observation);
	 }	
}

function displayEnzyme(observation) {
	  $('#xmet_enzyme').replaceWith(observation.enzyme.code + "&nbsp;" +  observation.enzyme.name );
	  $('#xmet_allele').replaceWith(observation.enzyme['allele']);
}

/**
* Defines dataTable for a list of observations, retrieved via JSON
*/
function defineObservationsTable(tableSelector,observations_uri,root) {
	$(tableSelector).dataTable( {
		"sAjaxDataProp" : "observations",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": true,
		"sAjaxSource": observations_uri,
		"aoColumns": [
				{ "mDataProp": "identifier" , "asSorting": [ "asc", "desc" ],
 			      "fnRender": function ( o, val ) {
          				return "<a href='"+o.aData["uri"] + "'>" + o.aData["identifier"] + "</a>";
        			}
				},
				{ "mDataProp": "Substrate.dataset.structure" , "asSorting": [ "asc", "desc" ], "sDefaultContent" : "N/A" },
				{ "mDataProp": "Product.dataset.structure" , "asSorting": [ "asc", "desc" ],"sDefaultContent" : "N/A"},
				{ "mDataProp": "product_amount" , "asSorting": [ "asc", "desc" ], "bVisible": true},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ],
				   "fnRender": function ( o, val ) {
	          				return "<span title='"+ o.aData["description"] +"'>" + val + "</span>";
	        		}
				},
				{ "mDataProp": "enzyme.code" , 
				  "asSorting": [ "asc", "desc" ], 
				  "bSearchable" : true		  
				},
				{ "mDataProp": "updated", "asSorting": [ "asc", "desc" ] }
			],
		"sDom" : '<"help remove-bottom"i><"help"p>Trt<"help"lf>',	
		"bJQueryUI" : true,
		"bPaginate" : true,
		"sPaginationType": "full_numbers",
		"sPaginate" : ".dataTables_paginate _paging",
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
			"sSearch": "Filter:",
            "sProcessing": "<img src='"+root+"/images/progress.gif' border='0'>",
            "sLoadingRecords": "No records found.",
            "sLengthMenu": 'Display <select>' +
            '<option value="10">10</option>' +
            '<option value="20">20</option>' +
            '<option value="50">50</option>' +
            '<option value="100">100</option>' +
            '<option value="-1">all</option>' +
            '</select> observations.'            
		},	    
		"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
		    $.ajax( {
		        "dataType": 'json',
		        "type": "GET",
		        "url": sSource,
		        "data": aoData,
		        "contentType" : "application/json",
		        "success": fnCallback,
		        "timeout": 15000,  
		        "error" : function( xhr, textStatus, error ) {
		        	oSettings.oApi._fnProcessingDisplay( oSettings, false );
		        }		        
		    } );
		},
		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
				//retrieve the first compound URI from substrates dataset URI
				 if ((aData.Substrate != undefined) && (aData.Substrate!=null) && (aData.Substrate.dataset!=undefined) && (aData.Substrate.dataset!=null)) {
					 if ((aData.Substrate.dataset.structure === undefined) || (aData.Substrate.dataset.structure==null)) {
						 if (aData.Substrate.dataset.uri==null) return;	 
					      $.ajax({
					          dataType: "jsonp",
					          url: aData.Substrate.dataset.uri + "?max=1&media=application%2Fx-javascript",
					          success: function(data, status, xhr) {
					        	  if (data.dataEntry.length>0) {
						        	  aData.Substrate.dataset.structure = data.dataEntry[0].compound.URI;
						        	  $('td:eq(1)', nRow).html(
						        			  cmp2image( aData.Substrate.dataset.structure)
						        			  + "<br>" +
						        			  searchSimilar(aData.Substrate.dataset.structure)
						        			  );
					          	  } else {
					          		$('td:eq(1)', nRow).html("N/A");
					          	  }
					          },
					          error: function(xhr, status, err) { },
					          complete: function(xhr, status) { }
					       });
					 } else {
						 $('td:eq(1)', nRow).html(
								 	cmp2image(aData.Substrate.dataset.structure) + "<br>" +
								 	searchSimilar(aData.Substrate.dataset.structure)
								 	);
					 }	
				 }
				//retrieve the first compound URI from products dataset URI
				 if ((aData.Product != undefined) && (aData.Product!=null) && (aData.Product.dataset!=undefined) && (aData.Product.dataset!=null)) {
					 if ((aData.Product.dataset.structure === undefined) || (aData.Product.dataset.structure==null)) {
						 if (aData.Product.dataset.uri==null) return;
					      $.ajax({
					          dataType: "jsonp",
					          url: aData.Product.dataset.uri + "?max=1&media=application%2Fx-javascript",
					          success: function(data, status, xhr) {
					        	  if (data.dataEntry.length>0) {
					        		  aData.Product.dataset.structure = data.dataEntry[0].compound.URI;
					        		  $('td:eq(2)', nRow).html(cmp2image( aData.Product.dataset.structure)
						        			  + "<br>" +
						        			  searchSimilar(aData.Product.dataset.structure)
					        				  );
					        	  } else {
					        		  $('td:eq(2)', nRow).html("N/A");
					        	  }
					          },
					          error: function(xhr, status, err) { 
					        	  
					          },
					          complete: function(xhr, status) { 
					        	  
					          }
					       });
					 } else {
						 $('td:eq(2)', nRow).html(cmp2image(aData.Product.dataset.structure)
			        			  + "<br>" +
			        			  searchSimilar(aData.Product.dataset.structure)
								 );
					 }
				 }
				 //retrieve the enzyme from /protocol/id/endpoint URI
				 if ((aData.enzyme.code === undefined) || (aData.enzyme.code ==null)) {
				      $.ajax({
				          dataType: "json",
				          url: aData.uri + "/endpoint?max=1&media=application%2Fjson",
				          success: function(data, status, xhr) {
				        	  aData.enzyme.code = data[0].code;
				        	  aData.enzyme.name = data[0].name;
				        	  $('td:eq(5)', nRow).html(renderEnzyme(data[0].code,data[0].name));	
				          },
				          error: function(xhr, status, err) { 
				        	  $('td:eq(5)', nRow).html("");	
				          },
				          complete: function(xhr, status) { }
				       });
				 } else {
					 $('td:eq(5)', nRow).html(renderEnzyme(aData.enzyme.code,aData.enzyme.name));	
				 }				 
        }			
	} );
}
function xmetdblog(msg) {
	//try { console.log(msg); } catch (e) { alert(msg); }
}

function searchSimilar(uri) {
	var query = "/xmetdb/protocol?structure=" + encodeURIComponent(uri);
	return "<a href='"+query+"'><span class='ui-icon ui-icon-search' title='Click to search for observations involving the same chemical'></span></a>"
}

function searchFormValidation(formName) {
	$(formName).validate({
		rules : {
			'search': {
				required : function() {
					var molFile = document.getElementById("iframeSketcher").contentWindow.getMolecule();
					console.log(molFile);
					return (molFile == null); //if no structures drawn, the search field should not be empty
				}
			},		
			'option': {
				required : true
			},
			'threshold': {
			},
			'pagesize': {
				required : true,
				number	 : true,
				range: [1, 1000]
			},
			search_exp_ms: {
				
			},
			search_exp_hep: {
				
			},
			search_exp_enz: {
				
			},
			search_enzyme: {
				
			},
			search_allele: {
				
			},
			search_xmetid: {
				
			}
		},
		messages : {
			'search'  : {
				required: "Please enter a search query <br>(CAS, Chemical Name, SMILES, InChI or SMARTS in case of <i>Substructure</i> search), <br>or use the structure diagram editor on the left."
			},
			'option' : "Please select an option",
			'threshold'  : {
				required: "Please select the Tanimoto similarity threshold."
			},
			'pagesize'  : {
				required: "Please provide number of hits."
			},	
			search_exp_ms : {
				
			},
			search_exp_hep : {
				
			},
			search_exp_enz : {
				
			},
			search_enzyme : {
				
			},
			search_allele : {
				
			},
			search_xmetid : {
				
			}
		},
		submitHandler: function(form) {
			var molFile = document.getElementById("iframeSketcher").contentWindow.getMolecule();
			if (molFile != null) {
				form.type.value = "b64mol"; 
				form.search.value=$.base64.encode(molFile);
			}
			form.submit();
		}
	});
}

/**
 * Validates new observation form
 * @param formName
 */
function submitFormValidation(formName) {
	$(formName).validate({
		rules : {
			'xmet_substrate_uri': {
			},		
			'xmet_substrate_mol': {
			},
			'xmet_substrate_upload': {
			},
			'xmet_product_uri': {
			},		
			'xmet_product_mol': {
			},
			'xmet_product_upload': {
			},
			'xmet_atom_uncertainty': {
				required: true
			},
			'xmet_product_amount': {
				required: true
			},
			'xmet_experiment': {
				required: true
			},
			'xmet_enzyme': {
				
			},
			'xmet_allele': {
				
			},
			'xmet_reference': {
				
			},
			'xmet_comments': {
				
			}			
		},
		messages : {
			'xmet_substrate_uri'  : {
				required: "Ambit URI"
			},
			'xmet_substrate_mol' : "MOL file content",
			'xmet_substrate_upload'  : {
				required: "Substrate file upload"
			},
			'xmet_product_uri'  : {
				required: "Ambit URI"
			},
			'xmet_product_mol' : "MOL file content",
			'xmet_product_upload'  : {
				required: "Product file upload"
			},
			'xmet_atom_uncertainty' : {
				required: "How certain is the SOM atom mapping?"
			},
			'xmet_product_amount' : {
				required: "Is the product major or minor metabolite?"
			},
			'xmet_experiment' : {
				required: "Type of the experiment"
			},
			'xmet_enzyme' : {
				required: "Specify the enzyme"
			},
			'xmet_allele' : {
				required: "Specify the allele"
			},
			'xmet_reference' : {
				required: "Reference"
			}
		}
	});
}

function escape (key, val) {
    if (typeof(val)!="string") return val;
    return val      
        .replace(/[\\]/g, '\\\\')
        .replace(/[\/]/g, '\\/')
        .replace(/[\b]/g, '\\b')
        .replace(/[\f]/g, '\\f')
        .replace(/[\n]/g, '\\n')
        .replace(/[\r]/g, '\\r')
        .replace(/[\t]/g, '\\t')
        .replace(/[\"]/g, '\\"')
        .replace(/\\'/g, "\\'"); 
}
/**
 * Parses HTML help file. Context help entries should be arranged as for http://jqueryui.com/tabs/
 * @param root
 * @param topic
 */
function loadHelp(root,topic) {
	var helpURI =  root + "/help/" + (topic===undefined?"":topic) + "?media=text/html";
	$.get(helpURI,function(data) {
		$('#pagehelp').append(data);	
		
		$('#keys ul li').map(function(el, value) {
			var key = $(value.innerHTML).attr('href');
		    var content = $(key).html();
		    var title = $(value).text();
		    key = key.replace('#','');
		    $('a.chelp.'+key)
			 .attr('title','Help: '+key)
			 .html('<span id="info-link" class="ui-icon ui-icon-help" style="display: inline-block;"></span>')
			 .click(function() {
				 $('#keytitle').text(title);
				 $('#keycontent').html(content);
		     });		    
		});
	});
}
function loadHelpJSON(root,topic) {
	var helpURI =  root + "/help/" + (topic===undefined?"":topic) + "?media=application/json";
	$.getJSON(helpURI,function(data) {
		_xmet.help = data;
		$('#helptitle').text(data['title']);
		$('#helpcontent').html(data['content']);
		$('#keytitle').text("");
		$('#keycontent').text("");
		
		if (data.keys != undefined)
			$.each(data.keys,function(key,value) {
				 $('a.chelp.'+key)
				 .attr('title','Help: '+key)
				 .html('<span id="info-link" class="ui-icon ui-icon-info" style="display: inline-block;"></span>')
				 .click(function() {
					 showContextHelp(key,value);

			     });
			});
	
	});
}

