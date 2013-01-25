function createImageMap(cmpURI, w,h , imgselector, mapselector) {
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
		        						"' onClick='atomNumber("+atomno+")'>\n";
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
		     .mapster('set',true,'1,2,3,4')
		     ;		
	    }
	});
}