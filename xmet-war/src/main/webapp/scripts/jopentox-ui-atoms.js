function createImageMap(cmpURI, w,h , imgselector, mapselector, results_id, atoms_id, som) {
	var uri = cmpURI + "/imagejson?w="+w+"&h="+h;
	$.ajax({
		type : "GET",
	    dataType : "jsonp",
	    "crossDomain": true,  //bloody IE
	    url : uri,
	    success: function(data){
		   	  var map = $(mapselector);
		   	  var r = 0; var c = 0;
		   	  $.each(data["a"], function(key, val) {
		   		 r += val["w"];
		   		 c++;
		   	  });
		   	  if (c>0) { r = Math.round(r/c);} else r = 15;
		  	  $.each(data["a"], function(key, val) {
		  	  		var atomno = val["i"];
		        	var sOut = "<area shape='circle' coords='"+
		        						val['x']+","+val['y']+","+r+
		        						"' href='#' title='"+
		        						atomno+"' atomnumber='"+atomno+
		        						"' onClick='atomNumber("+atomno+",\""+atoms_id+"\")'>\n";
		  	  		map.append(sOut);
		      });
		     $(imgselector).mapster({
		    	fillColor: '0000ff',
		        fillOpacity: 0.3,
		        mapKey: 'atomnumber',
		        stroke: false,
		        render_highlight: {
		            strokeWidth: 2
		        }
		     })
		     .mapster('set',true,som.toString());
		     try {
			     var somarray = som.toString().split(",");
			     _xmet.atoms[atoms_id] = [];
			     $.each(somarray,function (index){
			    	 _xmet.atoms[atoms_id].push(parseInt(somarray[index]));	 
			     });
		     } catch (err ) {
		    	 _xmet.atoms[atoms_id] = [];
		     }
	    }
	});
}
