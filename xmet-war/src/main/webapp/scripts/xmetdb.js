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