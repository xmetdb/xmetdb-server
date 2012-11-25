function cmp2image(val) {
		if (val.indexOf("/conformer")>=0) {
			cmpURI = val.substring(0,val.indexOf("/conformer"));
		}								
		//if ((opentox["model_uri"]==null) || (opentox["model_uri"] == undefined)) {
			cmpURI = cmpURI + "?media=image/png";
		//} else {
		//		cmpURI = opentox["model_uri"] + "?dataset_uri=" + cmpURI + "&media=image/png";
		//}
		return '<a href="'+val+'" title="'+cmpURI+'"><img border="0" src="'+cmpURI+'&w=150&h=150"></a>';
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
function addSearchUI(searchSelector, tableid, xmet_root, formName) {
	var searchUI = 
	"<form method='GET' action='#' name='" + formName+ "'>"+
	"<table id='"+ tableid + "' style='margin: 0 0 0 0;padding: 0 0 0 0;' class='ui-widget-content ui-corner-all'><tr>"+
	"<td colspan='2'>"+
	"   <input type='hidden' name='type' value='smiles'>"+
	"   <input type='button' class='draw' tabindex='0' value='Draw (sub)structure' title='Launches structure diagram editor' onClick='startEditor(\""+ xmet_root +"\",\""+formName+"\");'><br>"+
	"   <input type='text' name='search' size='40' value='c1ccccc1Cl' tabindex='1' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'>"+
	"</td>"+
	"<td align='left' valign='top'>"+
	"   <input type='radio' value='auto' name='option'  title='Exact structure or search by identifier' size='20'>Auto<br>"+
	"   <input type='radio' name='option' value='similarity' checked title='Enter SMILES or draw structure'>Similarity&nbsp;"+
	"   <select title ='Tanimoto similarity threshold' name='threshold'><option value='0.9'>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5' selected >0.5</option></select>"+
	"   <br>"+
	"   <input type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' size='20'>Substructure"+
	"</td><td  valign='bottom' ><input type='submit' value='Search'></td>" +
	"</tr></table></form>";
	$( searchSelector ).append(searchUI);
}		


function runSearch(queryService,values) {
	console.log(queryService + '?'+ values);
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