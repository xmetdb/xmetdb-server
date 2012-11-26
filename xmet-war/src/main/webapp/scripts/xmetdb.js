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
	var searchSelector = '#' + prefix + 'search';
	var tableid = prefix + 'SearchUI';
	var formName = prefix + 'SearchForm';
	var resultsName = prefix + 'Results';
	var searchUI = 
	"<form method='GET' action='#' name='" + formName+ "'>"+
	"<table id='"+ tableid + "' width='100%' class='ui-widget-content ui-corner-all'>"+
	"<tr><td colspan='2'>\n"+
	"   <input type='hidden' name='type' value='smiles'>"+
	"   <input type='button' class='draw' tabindex='0' value='Draw (sub)structure' title='Launches structure diagram editor' onClick='startEditor(\""+ xmet_root +"\",\""+formName+"\");'><br>"+
	"   <input type='text' name='search' size='40' value='c1ccccc1Cl' tabindex='1' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'>"+
	"</td>\n"+
	"<td align='left' valign='top'>\n"+
	"   <input type='radio' value='auto' name='option'  title='Exact structure or search by identifier' size='20'>Auto<br>"+
	"   <input type='radio' name='option' value='similarity' checked title='Enter SMILES or draw structure'>Similarity&nbsp;"+
	"   <select title ='Tanimoto similarity threshold' name='threshold'><option value='0.9'>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5' selected >0.5</option><option value='0.4' selected >0.4</option><option value='0.3' selected >0.3</option><option value='0.2' selected >0.2</option></select>"+
	"   <br>"+
	"   <input type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' size='20'>Substructure"+
	"</td><td  valign='bottom' ><input type='submit' value='Search'></td>" +
	"</tr>\n<tr>" + 
	"<td colspan='4'><ul id='" + resultsName+ "' class='structresults'></ul></td>"+
	"</tr>" +
	"<tr><td colspan='4' align='right'>" +
	"<button class='useSelected' onClick='useSelected(\"" + prefix+ "\");return false;'>Use selected structures as "+prefix+"s</button>" +
	"</td></tr>"+
	"</table></form>";
	$( searchSelector ).append(searchUI);
	$( '#'+resultsName ).selectable();
	
}		
/**
 * Fills in two hiddent text fields with selected compound URIs
 * @param prefix
 */
function useSelected(prefix) {
	$( '#' + prefix + 'Results li.ui-selected img').each(function (index,entry) {
		$('input:[name=xmet_'+prefix+'_uri]').val(entry.alt);
	});
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

function runSearch(queryService,values,results) {
	var sSource = queryService + '/query/similarity?media=application/x-javascript&'+ values;
	$.ajax( {
	        "type": "GET",
	        "url": sSource ,
	        "dataType": "jsonp", 
	        "contentType" : "application/x-javascript",
	        "success": function(json) {
	        	$(results).empty(); 
   	        	json.dataEntry.forEach(function img(element, index, array) {
	        		$(results).append('<li class="ui-state-default" >'+cmp2image(element.compound.URI)+'</li>');
	        	  });
	        },
	        "cache": false,
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
function toggleSearchUI(id, idButton) {
 	$( id ).toggle( 'blind', {}, function(x) {
 		$( idButton).text($(id).is(":hidden")?'Show search options':'Hide search options');
 	});
}      