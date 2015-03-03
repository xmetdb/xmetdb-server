var _xmet = {
help : {
	title:"Help",
	content:"XMETDB Help"
},
atoms : {
	"#xmet_substrate_atoms": [],
	"#xmet_product_atoms":[]
},
currentPrefix : null
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
		return '<img border="0" style="background-color:#ffffff" alt="'+val+'" src="'+cmpURI+'&w=150&h=150" usemap="#m'+id+'" id="i'+id+'">\n<map id="m'+id+'" name="m'+id+'"></map>';
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
	return '<img border="0" style="background-color:#ffffff" alt="'+uri+'" src="'+cmpURI+'&w=150&h=150" usemap="#m'+id+'" id="i'+id+'">\n<map id="m'+id+'" name="m'+id+'"></map>';
}

function renderEnzyme(root,enzyme) {
	if (enzyme===undefined || enzyme==null) return "";
	try {
		var sOut = "<a href='"+root+"/catalog/"+enzyme["id"]+"' title='Click to view enzyme: "+ enzyme["name"] +"' target='enzyme'>" + (enzyme["code"]==null?"":enzyme["code"]) + "</a>";
		if ((enzyme["uniprot"]!=undefined) &&  (enzyme["uniprot"]!=null))
			sOut += "<br/><br/><a href='http://www.uniprot.org/uniprot/"+enzyme["uniprot"]+"' title='Click to view UniProt enzyme' target='enzyme'>UniProt: " + enzyme["uniprot"] + "</a>";
		return sOut;
	} catch (err) { return enzyme["code"]}
}

function loadEnzymesList(root,selectTag,allelesTag,enzymeSelected,alleleSelected) {
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
        	  var selected = (enzymeSelected==element.code)?'selected':'';
      		  $("<option value='" + element.code + "'" + selected + ">" + element.code + "&nbsp;" +element.name + "</option>").appendTo(selectTag);
      		  alleles[element.code] = element.alleles;
      	  });
      	  $(selectTag).change(function() {
        		var x = $(this).val();
        		try { if ($.isArray(x)) x = x[0];} catch (err) {};
                $(allelesTag).html("");
                $("<option value=''></option>").appendTo(allelesTag);
                if (alleles[x]!=undefined)
                	alleles[x].forEach(function enz(element, index, array) {
                		  var selected = '';//(alleleSelected==element)?'selected':'';
	              		  $("<option value='" + element + "'" + selected + " >" + element + "</option>").appendTo(allelesTag);
	              	});
      	  });
        },
        error: function(xhr, status, err) { 

        },
        complete: function(xhr, status) { }
     });
}	


/**
 * Fills in two hidden text fields with selected compound URIs
 * @param prefix
 */
function useSelected(prefix) {
	$( '#structureSearchResults li.ui-selected').each(function (index,item) {
		var entry = $(item).find('img');
		var names = $(item).find('div');
		var img_alt = entry.attr('alt');
		var img_src = entry.attr('src');
		
		var results = '#xmet_'+prefix+'_img';
		$('input[name=xmet_'+prefix+'_uppload]').val();
		var results_uri = 'input[name=xmet_'+prefix+'_uri]';
		var results_mol = 'input[name=xmet_'+prefix+'_mol]';
		if (img_alt.indexOf('http')==0) {
			$(results_uri).val(img_alt);
			$(results).empty();
			$(results).append('<li class="ui-state-default" >'+cmp2image(img_alt)+'<div>'+names.html()+'</div></li>');
			$(results_mol).val('');
		} else {
			$(results_uri).val(img_alt);
			$(results).empty();
			$(results).append('<li class="ui-state-default" ><img border="0" src="'+img_src+'"></li>');
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
		var results_name = $('input[name=xmet_name]').val();
		var results_name_div = "";
		try { results_name = results_name.toLowerCase(); } catch (err) {}
		var b64search = encodeURIComponent($.base64.encode(molFile));
		if (!results_name || 0 === results_name.length) { //empty name
			molFile = molFile + "\n$$$$\n";
			$('input[name="xmet_'+prefix+'_name"]').val("");
		} else {
			molFile = molFile + "\n> <Name>\n" + results_name + "\n\n$$$$\n";
			results_name_div = '<div style="margin-top:5px;">'+results_name+'</div>';
			$('input[name="xmet_'+prefix+'_name"]').val(results_name);
		}
		$(results_mol).val(molFile);
		$("#xmet_"+prefix+"_type").val("mol");
		var results_uri = 'input[name=xmet_'+prefix+'_uri]';
		$(results_uri).val();
		$(results).empty();
		runSearchURI(queryService + '/query/compound/search/all?media=application/x-javascript&type=mol&max=1&b64search=' + b64search,
				results,
				function(json) {
					var found = 0;
					$(results_mol).val(molFile);
					$(results).empty();
					json.dataEntry.forEach(function img(element, index, array) {
       					$(results_uri).val(element.compound.URI);
       					var nameid = getID();
       					$(results).append('<li class="ui-state-default" style="background-color:#ffffff;" >'+cmp2image(element.compound.URI)+
       									  results_name_div +
       									  '<div style="margin-top:5px;" id="'+nameid+'"></div></li>');
       					loadStructureIds(prefix,queryService,element.compound.URI,renderStructureIds,'#'+nameid);
       					found++;
       				});
       				if (found==0) {
       					var nameid = getID();
       					$(results).append('<li class="ui-state-default" style="background-color:#ffffff;" ><img border="1" style="background-color:#ffffff" src="'+
       			    			queryService + '/depict/cdk/kekule?type=mol&b64search=' + encodeURIComponent($.base64.encode(molFile)) +
       			    			'&w=150&h=150" alt="'+ molFile +'"><div style="margin-top:5px;" id="'+nameid+'">'+results_name+'</div></li>');
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

function runSearch(queryService,query,form,results) {
	runSearchURI(queryService + query + '?media=application/x-javascript&'+ form.serialize(),
				results,
				function(json) {
					json.dataEntry.forEach(function img(element, index, array) {
						var nameid = getID();
						$(results).append('<li class="ui-state-default" >'+cmp2image(element.compound.URI)+
										  '<div style="margin-top:5px;" id="'+nameid+'"></div></li>');
						loadStructureIds("",queryService,element.compound.URI,renderStructureIds,'#'+nameid);
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
                  var p = $(results).offset();
                  $(window).scrollTop(p.top-100);
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


/**
 * Load structures from remote Ambit dataset uri via JSONP
 */
function loadStructures(prefix,datasetURI, results, atomsid, similarityLink, cmpURISelector, molSelector,selectable,query_service) {
	if ((datasetURI===undefined) || (datasetURI==null)) {
		  $(results).empty();
	} else {
		  var includeMol = (molSelector!=null);	
	      $.ajax({
	          dataType: "jsonp",
	          "crossDomain": true,  //bloody IE
	          url: datasetURI + ((datasetURI.indexOf("?")>0)?"&":"?") + "media=application%2Fx-javascript" + (includeMol?"&mol=true":""),
	          success: function(data, status, xhr) {
	        	  var dataSize = data.dataEntry.length;
	        	  $(results).empty();
	        	  for (i = 0; i < dataSize; i++) {
	        		  $(cmpURISelector).attr('value',data.dataEntry[i].compound.URI);
	        		  try {$(molSelector).attr('value',data.dataEntry[i].compound["mol"]);} catch (err) {$(molSelector).attr('value','');}
	        		  var nameid = "xmet_"+prefix+"_name";
	        		  $(results).append('<li class="ui-widget-content">'+cmpatoms2image(data.dataEntry[i].compound.URI,null)+
	        				  		'<div style="margin-top:5px;" id="'+nameid+'"></div></li>');
	        		  loadStructureIds(prefix,query_service,data.dataEntry[i].compound.URI,renderStructureIds,'#'+nameid);
	        		  var id= data.dataEntry[i].compound.URI.replace(/:/g,"").replace(/\//g,"").replace(/\./g,"");
	        		  var som = null;
	        		  $.each(data.dataEntry[i].values,function(index) {
	        			som = data.dataEntry[i].values[index];
	        		  });
	        		  createImageMap(data.dataEntry[i].compound.URI, '150','150', '#i'+id, '#m'+id,results,atomsid,som,selectable);
	        		  $(similarityLink).html(searchSimilar(data.dataEntry[i].compound.URI));
	        	  };
	          },
	          error: function(xhr, status, err) { 
	          },
	          complete: function(xhr, status) { }
	       });
	}
}



function loadStructureIds(prefix,query_service,compoundURI,callback,nameSelector) {

	var id_uri = query_service + "/query/compound/url/all?search=" + encodeURIComponent(compoundURI) + "&max=1&media=application%2Fx-javascript";
	$.ajax({
	         dataType: "jsonp",
	         "crossDomain": true, 
	         url: id_uri,
	         success: function(data, status, xhr) {
	        	identifiers(data);
	        	$(nameSelector).html("&nbsp;");
	        	$.each(data.dataEntry,function(index, entry) {
	        		callback(nameSelector,
	        				formatValues(entry,"names"),
	        				formatValues(entry,"cas"),
	        				formatValues(entry,"smiles"),
	        				formatValues(entry,"inchi"),
	        				formatValues(entry,"inchikey"));
	        	});
	         },
	         error: function(xhr, status, err) {
	        	 $(nameSelector).html("&nbsp;");
	         },
	         complete: function(xhr, status) { }
	});	
}
function renderStructureIds(nameSelector,names,cas,smiles,inchi,inchikey) {
	try {
		$(nameSelector).html(names.toLowerCase());
	} catch (err) {
		$(nameSelector).html(names);
	}
}

function formatValues(dataEntry,tag) {
	var sOut = "";
	var line = 0;
	var id = "n" + getID();
	var cache = {};
	$.each(dataEntry.lookup[tag], function(index, value) { 
	  if (dataEntry.values[value] != undefined) {
		  $.each(dataEntry.values[value].split("|"), function (index, v) {
			  if (v.indexOf(".mol")==-1) {
				if ("" != v) {  
					var lv = v.toLowerCase();
					if (cache[lv]==null) {
						switch(line) {
						case 0:
							sOut += "<span class='long' title='"+v+"'>"+v+"</span>";
						    break;
						case 1:
							sOut += "<span style='display:none;' id='"+id+"'>";
							sOut += v;
						    break;
						default:
						  	sOut += "<br/>";
							sOut += v;
						}
					  	line++;
					  	cache[lv] = true;
					}
				}
		  	  }
		  });
	  }
	});
	
	switch(line) {
	case 0: 
		sOut += "&nbsp;";
		break;
	case 1: 
		break;
	default:  
	   sOut += "</span>";
	   sOut += " <a href='#' onClick='$(\"#"+id+"\").toggle();' title='Click here for more synonyms'>&raquo;</a> ";
	}
	return sOut;
}


function toggleDrawUI(prefix, idButton, msg) {
	var drawUI = $( '#drawUI');
	_xmet.currentPrefix = prefix;
	if (drawUI.is(':hidden')) {
	 	drawUI.show();
	}
	var molTag = 'input[name=xmet_'+_xmet.currentPrefix+'_mol]';
	//var nameid = "#xmet_"+_xmet.currentPrefix+"_name";
	//$("#xmet_name").val($(nameid).text());
	var molFile = $(molTag).val();
	if ((molFile===undefined) || (molFile==null) || (molFile.trim()=="")) {
		var uriTag = 'input[name=xmet_'+_xmet.currentPrefix+'_uri]';
		$.ajax( {
	        "type": "GET",
	        "dataType": "jsonp", 
	        "crossDomain": true,  //bloody IE
	        "contentType" : "application/x-javascript",
	        "url": $(uriTag).val() + "?mol=true&media=" + encodeURIComponent("application/x-javascript"),
	        "success": function(mol) {
	        	var p = drawUI.offset();
                $(window).scrollTop(p.top);
	        	$(molTag).val(mol);
	        	try {iframeOnLoad();} catch (err) { }
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
	        	console.log(textStatus + error);
	        }
	      } );				
	} else
		try {
			iframeOnLoad();
			var p = drawUI.offset();
            $(window).scrollTop(p.top);
		} catch (err) { }
}      


function iframeOnLoad() {
	if (_xmet.currentPrefix!=null) {
		var molTag = 'input[name=xmet_'+_xmet.currentPrefix+'_mol]';
		var molFile = $(molTag).val();
		if ((molFile===undefined) || (molFile==null) || (molFile.trim()=="")) {
			//do nothing , we'll break ChemDoodles sketcher if assigning empty molecule...
		} else {
			var frame = document.getElementById('iframeSketcher');
			var oDoc = (frame.contentWindow || frame.contentDocument);
			if (oDoc.document) oDoc = oDoc.document;
			frame.contentWindow.loadMoleculeFromFile(molFile);
		}
	}
}

/*
 * Loads single observation via JSON and fills in the relevant HTML tags
*/
function loadObservation(root,observation_uri,query_service,username,isAdmin) {
	var model_uri = null;
	
	var observation;
    $.ajax({
	          dataType: "json",
	          contentType: 'application/json; charset=utf-8',
	          url: observation_uri,
	        	  //"/xmetdb/protocol/XMETDB2?media=application%2Fjson",
	          success: function(data, status, xhr) {
	        	  
	        	  observation = data.observations[0];
	        	  if ((isAdmin==1) || (username == observation.owner.username))  $('#modifyURI').show(); else  $('#modifyURI').hide();
	        	  $('#submittedby').attr('href',observation.owner.uri);
	        	  $('#submittedby').text(observation.owner.username);
	        	  
	        	  $('#xmet_id').replaceWith("<a href='"+ observation["uri"] + "'>" + observation["identifier"] + "</a>");
	        	  $('#xmet_experiment').replaceWith(observation["description"] + " (" + observation["title"] + ")");
	        	  //$('span#xmet_substrate').replaceWith(observation.Substrate.dataset.uri);
	        	  //$('span#xmet_product').replaceWith(observation.Product.dataset.uri);
	        	  $('#xmet_reference').replaceWith(observation["reference"]);
	        	  //Search for the same reference
	        	  try {
	        		  var uri = root+"/protocol?xmet_reference="+encodeURIComponent(observation["reference"]);
	        		  $("#sim_reference").attr("href",uri);
	        		  $("#sim_reference").show();
	        	  } catch (err) {
	        		  $("#sim_reference").hide();
	        	  }
	        	  //DOI link (if available)
	        	  try {
	        		  $('#xmet_doi').hide();
	        		  var doi = observation["reference"].toLowerCase();
	        		  if (doi.indexOf("doi:")==0) {
	        			  doi = "http://dx.doi.org/" + doi.replace("doi:","");
	        			  $('#xmet_doi').attr("href",doi);
	        			  $('#xmet_doi').attr("title",doi);
	        			  $('#xmet_doi').show();
	        		  }	  
	        	  } catch (err) {}
	        	  
	        	  $('#xmet_comments').text(observation["comments"]===undefined?"":observation["comments"]);
	        	  
	        	  $('#xmet_atom_uncertainty').replaceWith(observation["atom_uncertainty"]);
	        	  $('#xmet_product_amount').replaceWith(observation["product_amount"]);
	        	  $('#xmet_editor').attr("href","/xmetdb/editor/"+observation["identifier"]);
	        	  $('#xmet_editorsom').attr("href","/xmetdb/protocol/"+observation["identifier"]+"/som");
	        	  $('#xmet_curator').attr("href","/xmetdb/curator/"+observation["identifier"]);
	        	  $('#xmet_export_json').attr("href",observation["uri"] + "?media=application%2Fjson");
	        	  $('#xmet_curated').text(observation["curated"]?"Curated":"Not curated");
	        	  if (observation["curated"]) $('#imgcurated').show(); else  $('#imgcurated').hide();
	        	  
	        	  if ((observation.Substrate!=undefined)  && (observation.Substrate!=null) && (observation.Substrate.dataset.uri!=null)) {
	        		  $('#xmet_export_substrate').attr("href",observation.Substrate.dataset.uri + "?media=chemical%2Fx-mdl-sdfile");
	        	  	  $('#xmet_export_substrate').show();
        			  var uri = query_service + "/feature?search="+observation["identifier"];
        			  uri = observation.Substrate.dataset.uri + "?feature_uris[]=" + encodeURIComponent(uri) ;  
	        	  	  loadStructures("substrate",uri,"#xmet_substrate","#xmet_substrate_atoms","#sim_substrate","#xmet_substrate_uri",null,false,query_service);
	        	  } else {
	        		  $('#xmet_export_substrate').hide();
	        	  }
	        	  if ((observation.Product!=undefined) && (observation.Product!=null) && (observation.Product.dataset.uri!=null)) {
	        		  $('#xmet_export_product').attr("href",observation.Product.dataset.uri + "?media=chemical%2Fx-mdl-sdfile");
	        		  $('#xmet_export_product').show();
	        		  var uri = query_service + "/feature?search="+observation["identifier"];
        			  uri = observation.Product.dataset.uri + "?feature_uris[]=" + encodeURIComponent(uri) ;  
	        		  loadStructures("product",uri,"#xmet_product","#xmet_product_atoms","#sim_product","#xmet_product_uri",null,false,query_service);
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


function updateSOM(url,form,selector) {
	    $.ajax({
	        type     : "POST",
	        cache    : false,
	        url      : url,
	        data     : form.serialize(),
	        error: function( jqXHR ,  textStatus,  errorThrown ) {
	        	$("#som_status").text(textStatus + " " + errorThrown);
	        },
	        success  : function(data,textStatus, jqXHR ) {
	        	$(selector).val("");
	        	$("#som_status").text("SOM updated. Click on the structure diagram to change atom selection.");
	        }
	    });
}

function updateObservation(root,observation_uri,query_service,mode) {
	var observation;
    $.ajax({
	          dataType: "json",
	          contentType: 'application/json; charset=utf-8',
	          url: observation_uri,
	        	  //"/xmetdb/protocol/XMETDB2?media=application%2Fjson",
	          success: function(data, status, xhr) {
	        	  
	        	  observation = data.observations[0];
	        	  $('#breadCrumb_xmet_id').html("<a href='"+ observation["uri"] + "' title='Click to view the observation'>" + observation["identifier"] + "</a>");
	        	  
	        	  var puri = mode=="curate"?
      			  		(root+"/curator/"+observation["identifier"]):
      		  			(root+"/protocol/"+observation["identifier"]+"/som");
      			  
      			  $('#som_form').attr("action",puri + "?method=put");
	      		  $('#som_form').on('submit',function(e){
	      			    e.preventDefault();
	      			    var hide = false;
	      				if ($('#xmet_substrate_atoms_modified').val()=="YES") {
	      					$('#som_compound_uri').val($('#xmet_substrate_uri').val());
      						$('#som_id').val("xmet_substrate_atoms");
      						$('#som_value').val($('#xmet_substrate_atoms').text());
      						updateSOM(puri + "?method=put",$(this),"#xmet_substrate_atoms_modified");
      						hide = true;
  						}
  						if ($('#xmet_product_atoms_modified').val()=="YES") {
	      					$('#som_compound_uri').val($('#xmet_product_uri').val());
      						$('#som_id').val("xmet_product_atoms");
      						$('#som_value').val($('#xmet_product_atoms').text());
  							updateSOM(puri + "?method=put",$(this),"#xmet_product_atoms_modified");
  							hide = true;
  						}
  						if (hide) {
  							$('#som_submit').hide();
  						}
	      		  });
      			  	
      			  var puriTitle = mode=="curate"?"Curate":"Modify Sites of Metabolism";		
      			  var puriHint = mode=="curate"?"Click to curate the observation":"Modify Sites of Metabolism";
	        	  $('#breadCrumb_xmet_id_modify').html("<a href='"+ puri + "' title='"+puriHint+"'>"+puriTitle+"</a>");
	        	  
	        	  $('#xmet_id').replaceWith(puriTitle + " [observation ID: <a href='"+ observation["uri"] + "' title='Click to view the observation'>" + observation["identifier"] + "</a>]");

	        	  $('#submittedby').attr('href',observation.owner.uri);
	        	  $('#submittedby').text(observation.owner.username);
	        	  
	        	  $('#xmet_atom_uncertainty').text(observation["atom_uncertainty"]);
	        	  $('#xmet_product_amount').replaceWith(observation["product_amount"]);
	        	  
	        	  $('#xmet_experiment').replaceWith(observation["description"] + " (" + observation["title"] + ")");
	        	  
	        	  loadEnzyme(observation);
	        	  
	        	  $('#xmet_reference').text(observation["reference"]);
	        	  $('#curated').html(observation["curated"]?"Curated":"Not curated");
	        	  if (observation["curated"]) $('#imgcurated').show(); else $('#imgcurated').hide();
	        	  $('#xmet_comments').text(observation["comments"]===undefined?"":observation["comments"]);
	        	  
	        	  $('#xmet_substrate_type').val('uri');
	        	  $('#xmet_product_type').val('uri');
	        	  $('#xmet_substrate_upload').val('');
	        	  $('#xmet_product_upload').val('');

	        	  if ((observation.Substrate!=undefined) && (observation.Substrate!=null)  && (observation.Substrate.dataset.uri!=null)) {
	        		  if ( (observation.Substrate.dataset.structure === undefined) || (observation.Substrate.dataset.structure==null)) {
	        			  var uri = query_service + "/feature?search="+observation["identifier"];
	        			  uri = observation.Substrate.dataset.uri + "?feature_uris[]=" + encodeURIComponent(uri) ;
	        			  loadStructures("substrate",uri,"#xmet_substrate_img","#xmet_substrate_atoms","#sim_substrate","#xmet_substrate_uri",null,true,query_service);
	        			  
	        				 $('#xmet_substrate_atoms').editable(
	        				        	puri + '?method=put',{
	        				        	type	: 'text',
	        				        	cancel  : 'Cancel',
	        				        	submit  : 'Update',
	        				        	indicator : '<img src="'+root+'/images/progress.gif">',
	        				        	tooltip  : 'Click to edit...',
	        				        	submitdata :  function(value,settings) { 
	        				        		var data = {};
	        				        		data['compound_uri'] = $('#xmet_substrate_uri').val(); 
	        				        		return data;
	        				        	},
	        				        	callback : function(value, settings) {
	        				        		if (mode=="curate") {
		        				        		$('#imgcurated').show();
		        				        		$('#curated').html("Curated");
	        				        		}
	        				            },
	        				            data: function(value, settings) {
	        				                 //var retval = value.replace(/<br[\s\/]?>/gi, '\n');
	        				                 return value;
	        				            } 	            
	        				        });
	        		  }	  
	        	  }
	        	  if ((observation.Product!=undefined) && (observation.Product!=null) && (observation.Product.dataset.uri!=null)) {
		        	  if ((observation.Product.dataset.structure === undefined) || (observation.Product.dataset.structure==null)) {
	        			  var uri = query_service + "/feature?search="+observation["identifier"];
	        			  uri = observation.Product.dataset.uri + "?feature_uris[]=" + encodeURIComponent(uri) ;
		        		  loadStructures("product",uri,"#xmet_product_img","#xmet_product_atoms","#sim_product","#xmet_product_uri",null,true,query_service);
		        			 $('#xmet_product_atoms').editable(
		        					 	puri +'?method=put',{
		        			        	type	: 'text',
		        			        	cancel  : 'Cancel',
		        			        	submit  : 'Update',
		        			        	indicator : '<img src="'+root+'/images/progress.gif">',
		        			        	tooltip  : 'Click to edit...',
		        			        	submitdata : function(value,settings) { 
		        			        		var data = {};
		        			        		data['compound_uri'] = $('#xmet_product_uri').val(); 
		        			        		return data;
		        			        	},
		        			        	callback : function(value, settings) {
		    				        		if (mode=="curate") {
		        				        		$('#imgcurated').show();
		        				        		$('#curated').html("Curated");
	        				        		}
		        			            }
		        			        });		        		  
		        	  }	  
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

function editObservation(root,observation_uri,query_service) {
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
	        		  //load enzymes and set selected; otherwise the list may not be yet loaded!
	        		  loadEnzymesList(root,"#xmet_enzyme","#xmet_allele",obs.enzyme.code,allele);
	        	  });
	        	  $('#xmet_reference').attr("value",observation["reference"]);
	        	  $('#xmet_curated').text(observation["curated"]?"Curated":"Not curated");
	        	  if (observation["curated"]) $('#imgcurated').show(); else  $('#imgcurated').hide();
	        	  
	        	  $('#xmet_comments').text(observation["comments"]===undefined?"":observation["comments"]);
	        	  $('#xmet_substrate_type').val('uri');
	        	  $('#xmet_product_type').val('uri');
	        	  $('#xmet_substrate_upload').val('');
	        	  $('#xmet_product_upload').val('');
	        	  if ((observation.Substrate!=undefined) && (observation.Substrate!=null)  && (observation.Substrate.dataset.uri!=null)) {
	        		  if ( (observation.Substrate.dataset.structure === undefined) || (observation.Substrate.dataset.structure==null)) {
	        			  var uri = query_service + "/feature?search="+observation["identifier"];
	        			  uri = observation.Substrate.dataset.uri + "?feature_uris[]=" + encodeURIComponent(uri) ;
	        			  
	        			  loadStructures("substrate",uri,"#xmet_substrate_img","#xmet_substrate_atoms","#sim_substrate","#xmet_substrate_uri","#xmet_substrate_mol",false,query_service);
      			  
	        		  }	  
	        	  }
	        	  if ((observation.Product!=undefined) && (observation.Product!=null) && (observation.Product.dataset.uri!=null)) {
		        	  if ((observation.Product.dataset.structure === undefined) || (observation.Product.dataset.structure==null)) {
	        			  var uri = query_service + "/feature?search="+observation["identifier"];
	        			  uri = observation.Product.dataset.uri + "?feature_uris[]=" + encodeURIComponent(uri) ;
		        		  loadStructures("product",uri,"#xmet_product_img","#xmet_product_atoms","#sim_product","#xmet_product_uri","#xmet_product_mol",false,query_service);

		        	  }	  
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
	  $('#xmet_enzyme').replaceWith((observation.enzyme.code==null?"":observation.enzyme.code) + "&nbsp;" +  (observation.enzyme.name==null?"":observation.enzyme.name) );
	  $('#xmet_allele').replaceWith(observation.enzyme['allele']==null?"":observation.enzyme['allele']);
}

/**
* Defines dataTable for a list of observations, retrieved via JSON
*/
function defineObservationsTable(tableSelector,observations_uri,root,curatorLink) {
	$(tableSelector).dataTable( {
		"sAjaxDataProp" : "observations",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": true,
		"sAjaxSource": observations_uri,
		"aoColumns": [
				{ "mDataProp": function (o,val) {
					return  o["identifier"].replace("XMETDB","");
				  }, 
				  "aTargets": [ 0 ],	
				  "asSorting": [ "asc", "desc" ],
				  "bUseRendered" : false,	
 			      "fnRender": function ( o, val ) {
          				return  "<a href='"+o.aData["uri"] + "'>" + o.aData["identifier"] + "</a>";
        			}
				},
				{ "mDataProp": "Substrate" ,
					"aTargets": [ 1 ],
					"bUseRendered" : false,	
					"sDefaultContent" : null,
					"asSorting": [ "asc", "desc" ], 
					"fnRender": function ( o, val ) {
          				try {return val==null?null:val["dataset"]["structure"];} catch (err) {return null;}
        			} 
				},
				{   "mDataProp": "Product" , 
					"aTargets": [ 2 ],
					"bUseRendered" : false,	
					"sDefaultContent" : null,
					"asSorting": [ "asc", "desc" ],
					"fnRender": function ( o, val ) {
          				try {return val==null?null:val["dataset"]["structure"];} catch (err) {return null;}
        			}  
				},
				{ "mDataProp": "product_amount" , 
					"aTargets": [ 3 ],	
					"asSorting": [ "asc", "desc" ], "bVisible": true
				},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ],
					"aTargets": [ 4 ],	
				   "fnRender": function ( o, val ) {
	          				return "<span title='"+ o.aData["description"] +"'>" + val + "</span>";
	        		}
				},
				{ "mDataProp": "enzyme" ,
					"aTargets": [ 5 ],	
				  "asSorting": [ "asc", "desc" ], 
				  "bSearchable" : true,
				   "fnRender": function ( o, val ) {
         				return renderEnzyme(root,val);
				   }				  
				},
				{ "mDataProp": "updated", "asSorting": [ "asc", "desc" ],"aTargets": [ 6 ]	 
				},
				{ 
				  "mDataProp": "curated", 				
				  "bUseRendered" : false,	
				  "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 7 ],	
				  "fnRender": function ( o, val ) {
	          				return (val?"<img src='"+root+"/images/star.png' title='Curated'><br/>":"") +
	          						(curatorLink?" <a href='"+root+"/curator/"+o.aData["identifier"]+"' title='Click to go the curation page'>Curate</a>":"")
	          						;
	        		}
				}				
			],
		"sDom" : '<"help remove-bottom"i><"help"p>Trt<"help"iplf>',	
		"bJQueryUI" : true,
		"bPaginate" : true,
		"sPaginationType": "full_numbers",
		"sPaginate" : ".dataTables_paginate _paging",
		
		"bDeferRender": true,
		"bSearchable": true,
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
		}
		,"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
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
				 /*
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
				 */			 
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
		try {
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
		} catch (err) {
			
		}
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

function getMediaLink(uri, media) {
	return uri + (uri.indexOf("?")>0?"&":"?") + "media=" + encodeURIComponent(media);
}

function getDownloadLinksUsers(root,uri) {
	var val = uri;
	var sOut = "<a href='"+getMediaLink(val,"text/csv")+"' id='csv'><img src='"+root+"/images/excel.png' alt='CSV' title='Download as CSV (Comma delimited file) with SMILES'></a> ";
	sOut += "<a href='"+getMediaLink(val,"application/json")+"' id='json'><img src='"+root+"/images/json.png' alt='JSON' title='JSON'></a> ";
	return sOut;
}

function getDownloadLinksTask(root,uri) {
	var val = uri;
	var sOut = "<a href='"+getMediaLink(val,"application/json")+"' id='json'><img src='"+root+"/images/json.png' alt='JSON' title='JSON'></a> ";
	sOut += "<a href='"+getMediaLink(val,"application/rdf+xml")+"' id='rdfxml'><img src='"+root+"/images/rdf.gif' alt='RDF/XML' title='Download as RDF/XML (Resource Description Framework XML format)'></a> ";
	sOut += "<a href='"+getMediaLink(val,"text/n3")+"' id='rdfn4'><img src='"+root+"/images/rdf.gif' alt='RDF N3' title='Download as RDF N3 (Resource Description Framework N3 format)'></a> ";
	return sOut;
}

function getDownloadLinksStructure(root,uri) {
	var val = uri;
	var sOut = "<a href='"+getMediaLink(val,"text/csv")+"' id='csv'><img src='"+root+"/images/excel.png' alt='CSV' title='Download as CSV (Comma delimited file) with SMILES'></a> ";
	sOut += "<a href='"+getMediaLink(val,"chemical/x-mdl-sdfile")+"' id='sdf'><img src='"+root+"/images/sdf.png' alt='SDF' title='Download as SDF'></a> ";
	sOut += "<a href='"+getMediaLink(val,"chemical/x-cml")+"' id='cml'><img src='"+root+"/images/cml.jpg' alt='CML' title='Download as Chemical Markup Language (CML)'></a> ";
	sOut += "<a href='"+getMediaLink(val,"chemical/x-inchi")+"' id='inchi'><img src='"+root+"/images/inchi.png' alt='InChI' title='Download as InChI'></a> ";
	sOut += "<br/><a href='"+getMediaLink(val,"application/rdf+xml")+"' id='rdfxml'><img src='"+root+"/images/rdf.gif' alt='RDF/XML' title='Download as RDF/XML (Resource Description Framework XML format)'></a> ";
	sOut += "<a href='"+getMediaLink(val,"text/n3")+"' id='rdfn4'><img src='"+root+"/images/rdf.gif' alt='RDF N3' title='Download as RDF N3 (Resource Description Framework N3 format)'></a> ";
	sOut += "<a href='"+getMediaLink(val,"application/json")+"' id='json'><img src='"+root+"/images/json.png' alt='JSON' title='JSON'></a> ";
	return sOut;
}
function getDownloadLinksObservation(root,uri,molecules) {
	   var val = uri;
	   var sOut = "<a href='"+getMediaLink(val,"text/csv")+"' id='csv'><img src='"+root+"/images/excel.png' alt='CSV' title='Download as CSV (Comma delimited file)'></a> ";
	   sOut += "<a href='"+getMediaLink(val,"chemical/x-mdl-sdfile")+"' id='sdf'><img src='"+root+"/images/sdf.png' alt='SDF' title='Download as SDF'></a> ";

	 //  sOut += "<a href='"+getMediaLink(val,"application/rdf+xml")+"' id='rdfxml'><img src='"+root+"/images/rdf.gif' alt='RDF/XML' title='Download as RDF/XML (Resource Description Framework XML format)'></a> ";
	 //  sOut += "<a href='"+getMediaLink(val,"text/n3")+"' id='rdfn3'><img src='"+root+"/images/rdf.gif' alt='RDF/N3' title='Download as RDF N3 (Resource Description Framework N3 format)'></a> ";
	   sOut += "<a href='"+getMediaLink(val,"application/json")+"' id='json' target=_blank><img src='"+root+"/images/json.png' alt='json' title='Download as JSON'></a>";
	   sOut += "<a href='"+getMediaLink(val,"application/xml")+"' id='xml' target=_blank><img src='"+root+"/images/gpml.png' alt='json' title='Download as GPML'></a>";
	 
	   if (molecules) {
		sOut += '<br/>Export substrate  <a id="xmet_export_substrate" href="#" title="Export substrate structure as SDF"><img src="'+root+'/images/sdf.png"></a>';
		sOut += '<br/>Export product(s) <a id="xmet_export_product"   href="#" title="Export product structure(s) as SDF"><img src="'+root+'/images/sdf.png"></a>';
	   }
	   return sOut;
}

function loadDBInfo(root) {
	
    $.ajax({
	          dataType: "text",
	          contentType: 'text/plain; charset=utf-8',
	          url: root + "/admin/database?media=text/plain",
	          success: function(data, status, xhr) {
	        	  $("#dbinfo").text(data);
	        	  $("#dbcreate").hide();
	          },
	          error: function(xhr, status, err) { 
	        	  $("#dbcreate").show();
	        	  $("#dbinfo").text(status + " " + xhr.responseText);
	          },
	          complete: function(xhr, status) { 
	        	  //$("#dbinfo").text(status);
	          }
	       });
}


function getID() {
	   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}	