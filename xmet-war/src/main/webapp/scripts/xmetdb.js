function cmp2image(val) {
		if (val.indexOf("/conformer")>=0) {
			cmpURI = val.substring(0,val.indexOf("/conformer"));
		}								
		//if ((opentox["model_uri"]==null) || (opentox["model_uri"] == undefined)) {
			cmpURI = cmpURI + "?media=image/png";
		//} else {
		//		cmpURI = opentox["model_uri"] + "?dataset_uri=" + cmpURI + "&media=image/png";
		//}
		//return '<a href="'+val+'" title="'+cmpURI+'"><img border="0" src="'+cmpURI+'&w=150&h=150"></a>';
		return '<img border="0" alt="'+val+'" src="'+cmpURI+'&w=150&h=150">';
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
	return '<img border="0" alt="'+uri+'" src="'+cmpURI+'&w=150&h=150">';
}

function renderEnzyme(code,name) {
	return "<span title='"+ name +"'>" + code + "</span>";
}

function loadEnzymesList(selectTag) {
	  //clear the list	
	  $(selectTag).html("");
	  //get all enzymes
    $.ajax({
        dataType: "json",
        url: "/xmetdb/catalog?media=application%2Fjson",
        success: function(data, status, xhr) {
        	  data.sort(function sortfunction(a,b){
        		  if(a.code == b.code){
	          			return 0;
	          	  }
         		  return (a.code < b.code) ? -1 : 1;
			  });
      	  data.forEach(function enz(element, index, array) {
      		  $("<option value='" + element.code + "'>" + element.code + "&nbsp;" +element.name + "</option>").appendTo(selectTag);
      	  });
        },
        error: function(xhr, status, err) { },
        complete: function(xhr, status) { }
     });
}	
/**
 * Structure search UI  
 * @param searchSelector
 * @param tableid
 */
//addSearchUI('#substratesearch','substrateSearchUI','${xmet_root}','substrateSearchForm','substrateResults');
function addSearchUI(prefix, xmet_root) {
	var searchSelector = '#searchUI';
	var formName = prefix + 'SearchForm';
	var resultsName = 'structureSearchResults';
	var searchUI = 
	"<div class='row ui-widget-header ui-corner-top remove-bottom'>Structure search</div>" +
	"<div class='ui-widget-content ui-corner-bottom remove-bottom'>"+
	"<div style='margin:5px;padding:5px;' class='row remove-bottom'>"+ 	
	"<form method='GET' action='#' name='" + formName+ "'>"+
	"	<div class='ten columns alpha '>"+
	" 	  <input type='hidden' name='type' value='smiles'>"+
	" 	  <a href='#' title='Launches structure diagram editor' onClick='startEditor(\""+ xmet_root +"\",\""+formName+"\");'>Draw (sub)structure</a><br>"+
	" 	  <input type='text' name='search' class='remove-bottom' value='c1ccccc1Cl' tabindex='1' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'><br/>"+
	" 	  <input type='radio' value='auto' name='option'  title='Exact structure or search by identifier' size='20'>Auto"+
	"  	 <input type='radio' name='option' value='similarity' checked title='Enter SMILES or draw structure'>Similarity&nbsp;"+
	"  	 <input type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' size='20'>Substructure"+
	" 	  <select title ='Tanimoto similarity threshold' name='threshold' class='half-bottom'><option value='0.9'>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5' selected >0.5</option><option value='0.4' selected >0.4</option><option value='0.3' selected >0.3</option><option value='0.2' selected >0.2</option></select>"+
	"		<input type='submit' value='Search' class='remove-bottom'>" +
	"	</div>"+
	"	<div class='six columns omega'><br/>"+	
	"		<a href='#' class='button' onClick='useSelected(\"substrate\");return false;'>Use selected structures as a substrate</a><br/>" +
	"		<a href='#' class='button' onClick='useSelected(\"product\");return false;'>Use selected structures as products</a>&nbsp;" +
	"	</div>" +
	"</div>" +
	"<div style='margin:5px;padding:5px;' class='row remove-bottom'>"+ 	
	"	<ul id='" + resultsName+ "' class='structresults '></ul>"+
	"</div></form>"+
	"</div>";
	$( searchSelector ).append(searchUI);
	$( '#'+resultsName ).selectable();
	
}		


/**
 * Fills in two hidden text fields with selected compound URIs
 * @param prefix
 */
function useSelected(prefix) {
	$( '#structureSearchResults li.ui-selected img').each(function (index,entry) {
		var results = '#xmet_'+prefix+'_img';
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
	});
}

function useDrawn(queryService,prefix) {
	var smiles = document.JME.smiles();
	var jme = document.JME.molFile();
	if (smiles == "") {
		alert("Nothing to submit");
	} else {
		var results = '#xmet_'+prefix+'_img';
		var results_uri = 'input[name=xmet_'+prefix+'_uri]';
		var results_mol = 'input[name=xmet_'+prefix+'_mol]';
		$(results_uri).val(smiles);
		$(results_mol).val(jme);
		$(results).empty();
		$(results).append('<li class="ui-state-default" ><img border="0" src="'+
    			queryService + '/depict/cdk/kekule?search=' + encodeURIComponent(smiles) +
    			'&w=150&h=150" alt="'+ smiles +'"></li>');
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
	var sSource = queryService + '/query/similarity?media=application/x-javascript&'+ form.serialize();
	var search = "";
	jQuery.each(form.serializeArray(),function(index,entry){
		if ('search' == entry.name) search = entry.value;
	});
	$(results).empty(); 
	$.ajax( {
	        "type": "GET",
	        "url": sSource ,
	        "dataType": "jsonp", 
	        "crossDomain": true,  //bloody IE
	        "contentType" : "application/x-javascript",
	        "success": function(json) {
	        	$(results).append('<li class="ui-state-default" ><img src="'+
	        			queryService + '/depict/cdk/kekule?search=' + encodeURIComponent(search) +
	        			'&w=150&h=150" alt="'+ search +'"></li>');
   	        	json.dataEntry.forEach(function img(element, index, array) {
	        		$(results).append('<li class="ui-state-default" >'+cmp2image(element.compound.URI)+'</li>');
	        	  });
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
	        }
	      } );
}
/**
 * Toggles search UI visibility
 * @param id
 * @param idButton
 */
function toggleSearchUI(id, idButton, msg) {
 	$( id ).toggle( 'blind', {}, function(x) {
 		$( idButton).text($(id).is(":hidden")?'Show '+msg:'Hide '+msg);
 	});
}      

function toggleDrawUI(prefix, idButton, msg) {
 	$( '#drawUI').toggle( 'blind', {}, function(x) {
 		if ($('#drawUI').is(":hidden")) {
 			$( idButton).text('Show '+msg);
 		} else {
 			$( idButton).text('Hide '+msg);
 			var results_mol = 'input[name=xmet_'+prefix+'_mol]';
 			try {
 				var mol = $(results_mol).val();
 				if ((mol===undefined) || (mol== null) || ("" == mol)) {
 					document.JME.reset();
 				} else
 					document.JME.readMolFile(mol);
 			} catch (err) {
 				document.JME.reset();
 			}
 		} 
 	});
}      

/**
 * Load structures from remote Ambit dataset uri via JSONP
 */
function loadStructures(datasetURI, results, modelURI) {
		 
	      $.ajax({
	          dataType: "jsonp",
	          "crossDomain": true,  //bloody IE
	          url: datasetURI + "?media=application%2Fx-javascript",
	          success: function(data, status, xhr) {
	        	  var dataSize = data.dataEntry.length;
	        	  $(results).empty();
	        	  for (i = 0; i < dataSize; i++) {
	        		  $(results).append('<li class="ui-state-default" >'+cmpatoms2image(data.dataEntry[i].compound.URI,modelURI)+'</li>');
	        	  };
	          },
	          error: function(xhr, status, err) { 
	          },
	          complete: function(xhr, status) { }
	       });
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
	        	  $('#xmet_reference').replaceWith("TODO");
	        	  
	        	  $('#xmet_atom_uncertainty').replaceWith(observation["atom_uncertainty"]);
	        	  $('#xmet_product_amount').replaceWith(observation["product_amount"]);
	        	  $('#xmet_editor').attr("href","/xmetdb/editor/"+observation["identifier"]);
	        	  $('#xmet_export_json').attr("href",observation["uri"] + "?media=application%2Fjson");
	        	  
	        	  $('#xmet_export_substrate').attr("href",observation.Substrate.dataset.uri + "?media=chemical%2Fx-mdl-sdfile");
	        	  $('#xmet_export_product').attr("href",observation.Product.dataset.uri + "?media=chemical%2Fx-mdl-sdfile");
	        	  
	        	  loadEnzyme(observation);
	        	  if ((observation.Substrate.dataset.structure === undefined) || (observation.Substrate.dataset.structure==null)) 
	        		  loadStructures(observation.Substrate.dataset.uri,"#xmet_substrate",model_uri);
	        				  //");
	        	  
	        	  if ((observation.Product.dataset.structure === undefined) || (observation.Product.dataset.structure==null)) 
	        		  loadStructures(observation.Product.dataset.uri,"#xmet_product",model_uri);
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
}

/**
* Defines dataTable for a list of observations, retrieved via JSON
*/
function defineObservationsTable(tableSelector,observations_uri) {
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
				{ "mDataProp": "Substrate.dataset.uri" , "asSorting": [ "asc", "desc" ], "bVisible": false },
				{ "mDataProp": "Substrate.dataset.structure" , "asSorting": [ "asc", "desc" ], "sDefaultContent" : "TODO" },
				{ "mDataProp": "Product.dataset.uri" , "asSorting": [ "asc", "desc" ], "bVisible": false },
				{ "mDataProp": "Product.dataset.structure" , "asSorting": [ "asc", "desc" ],"sDefaultContent" : "TODO"},
				{ "mDataProp": "product_amount" , "asSorting": [ "asc", "desc" ], "bVisible": true},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ],
				   "fnRender": function ( o, val ) {
	          				return "<span title='"+ o.aData["description"] +"'>" + val + "</span>";
	        		}
				},
				{ "mDataProp": "enzyme.code" , "asSorting": [ "asc", "desc" ], "bSearchable" : true	},
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
            "sProcessing": "<img src='/xmetdb/images/progress.gif' border='0'>",
            "sLoadingRecords": "No records found."
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
				 if ((aData.Substrate.dataset.structure === undefined) || (aData.Product.dataset.structure==null)) {
				      $.ajax({
				          dataType: "jsonp",
				          url: aData.Substrate.dataset.uri + "?max=1&media=application%2Fx-javascript",
				          success: function(data, status, xhr) {
				        	  if (data.dataEntry.length>0) {
					        	  aData.Substrate.dataset.structure = data.dataEntry[0].compound.URI;
					        	  $('td:eq(1)', nRow).html(cmp2image( aData.Substrate.dataset.structure));
				          	  } else {
				          		$('td:eq(1)', nRow).html("N/A");
				          	  }
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
				        	  if (data.dataEntry.length>0) {
				        		  aData.Product.dataset.structure = data.dataEntry[0].compound.URI;
				        		  $('td:eq(2)', nRow).html(cmp2image( aData.Product.dataset.structure));
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
				        	  $('td:eq(5)', nRow).html(renderEnzyme(data[0].code,data[0].name));	
				          },
				          error: function(xhr, status, err) { },
				          complete: function(xhr, status) { }
				       });
				 } else {
					 $('td:eq(5)', nRow).html(renderEnzyme(aData.enzyme.code,aData.enzyme.name));	
				 }				 
        }			
	} );
}
function xmetdblog(msg) {
	try { console.log(msg); } catch (e) { alert(msg); }
}


function searchFormValidation(formName) {
	$(formName).validate({
		rules : {
			'search': {
				required : true
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