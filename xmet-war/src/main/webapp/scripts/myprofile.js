function getMyAccount(url) {
	var facet = {};	

    $.ajax({
        dataType: "json",
        url: url, //"/xmet/myaccount?media=application/json",
        success: function(data, status, xhr) {
        	$.each(data["user"],function(index, entry) {
        		$("#username").text(entry["username"]);
        		$("#useruri").prop("href",entry["uri"]);
        		$("#useruri").text(entry["title"] + " " + entry["firstname"] + " " + entry["lastname"]);
        		$("#email").prop("value",entry["email"]);
        		$("#title").prop("value",entry["title"]);
        		$("#firstname").prop("value",entry["firstname"]);
        		$("#lastname").prop("value",entry["lastname"]);

        		$("#homepage").prop("value",entry["homepage"]);
        		$("#keywords").prop("value",entry["keywords"]);
        		$("#reviewer").attr("checked",entry["reviewer"]);
        	        		
        		var protocolURI = "/qmrf/user/" + entry.id + "/protocol?headless=true&details=false&media=text/html";
        		var alertURI = "/qmrf/user/" + entry.id + "/alert?headless=true&details=false&media=text/html";
        		
        		$("#protocoluri").prop("href",protocolURI);
        		$("#alerturi").prop("href",alertURI);
        		
        		var oTable = $('#organisations').dataTable( {
        			"bPaginate"  : false,
        			"bAutoWidth" : false,
        			"jQuery" : true,
        			"sDom"   : "t",
        			"aaData" : entry.organisation,
        			"bFilter": false,
        			"aoColumnDefs": [
        			 				{ //0
        			 					"aTargets": [ 0 ],	
        			 					"mDataProp" : "uri",
        			 					"bInfo"  : false,
        			 					"bLengthChange" : false,
        			 					"bUseRendered"  : false,
        								"fnRender"      : function(o,val) {
        									 if ((o.aData["title"] === undefined) || (o.aData["title"] == ""))
        									 	return "<a href='"+ o.aData["uri"]+"' target=_blank>N/A</a>";
        									 else
        										 return "<a href='"+ o.aData["uri"]+"' target=_blank>"+o.aData["title"]+"</a>";
        								}
        			 				}        			 				
        			 			]
        		});
        		//reload tabs
        		$(function() {$( ".tabs" ).tabs({cache: true});});
        	});
        },
        error: function(xhr, status, err) {
        },
        complete: function(xhr, status) {
        }
     });
    return facet;
}