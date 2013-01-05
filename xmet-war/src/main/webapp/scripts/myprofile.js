function getMyAccount(root,url,readonly) {
	var facet = {};	

    $.ajax({
        dataType: "json",
        url: url,
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
        	        		
        		var protocolURI = root  + "/user/" + entry.id + "/protocol";
        		var alertURI = root + "/user/" + entry.id + "/alert";
        		
        		$("#protocoluri").prop("href",protocolURI);
        		$("#alerturi").prop("href",alertURI);
        		
        		var sOrg = "";
        		$.each(entry["organisation"],function(index,value) {
        			sOrg += "<p><label for='affiliation'>Affiliation</label>";
        			sOrg += "<input type='text' size='40' name='affiliation' class='affiliation' value='"+value.title+"'"+ (readonly?" readonly ":"") +"/>";
        			sOrg += "<em></em></p>\n";
        		});
        		if (sOrg=="") {
        			sOrg += "<p><label for='affiliation'>Affiliation</label>";
        			sOrg += "<input type='text' size='40' name='affiliation' class='affiliation' value=''"+ (readonly?" readonly ":"") +"/>";
        			sOrg += "<em></em></p>\n";
        		};
    			$("#organisations").html(sOrg);        		
        		
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


function defineUsersTable(root,url) {

	var oTable = $('#users').dataTable( {
		"sAjaxDataProp" : "user",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": false,
		"aoColumnDefs": [
 				{ "mDataProp": "username" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 0 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "fnRender" : function(o,val) {
					  var name = o.aData["firstname"] + " " + o.aData["lastname"];
					  if (o.aData["uri"]==null) return name;
					  return "<a href='"+o.aData["uri"]+"' title='"+o.aData["uri"]+"'>"+name+"</a>";
				  }
				},
				{ "mDataProp": null , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 1 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "fnRender" : function(o,val) {
					    if (o.aData["organisation"]==null) return "";
					    var sOut = "";
			           	$.each(o.aData["organisation"],function(index, entry) {
			        		sOut += "<a href='"+entry.uri+"' title='"+entry.uri+"'>"+entry.title+"</a>";
			        		sOut += " ";
			        	});
			           	return sOut;
				  }
				},
				{ "mDataProp": "email" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 2 ],
				  "bSearchable" : true,
				  "bSortable" : true,
				  "bUseRendered" : false,
				  "fnRender" : function(o,val) {
					  if ((val==null) || (val===undefined)) return "";
					  return name + "<a href='mailto:"+encodeURIComponent(o.aData["email"])+"'>"+o.aData["email"]+"</a>";
				  }
				},
				{ "mDataProp": "keywords" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 3 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "fnRender" : function(o,val) {
						  return val;
					  }
				},
				{ "mDataProp": "reviewer" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 4 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "sWidth" : "10%",
					  "fnRender" : function(o,val) {
						  return "<input type='checkbox' id='"+o.aData["uri"]+"' disabled='disabled' "+ (val?"checked":"") +">";
					  }
				}				
			],
		"sDom" : '<"help remove-bottom"i><"help"p>Trt<"help"lf>',
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": url,
		"oLanguage": {
				"sSearch": "Filter:",
	            "sProcessing": "<img src='"+root+"/images/progress.gif' border='0'>",
	            "sLoadingRecords": "No user entries found.",
	            "sLengthMenu": 'Display <select>' +
                '<option value="10">10</option>' +
                '<option value="20">20</option>' +
                '<option value="50">50</option>' +
                '<option value="100">100</option>' +
                '<option value="-1">all</option>' +
                '</select> users'	            
	    }
	} );
	return oTable;
}


function defineOrganisationTable(root,url) {

	var oTable = $('#organisation').dataTable( {
		"sAjaxDataProp" : "group",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": false,
		"aoColumnDefs": [
 				{ "mDataProp": "groupname" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 0 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "fnRender" : function(o,val) {
					  var name = o.aData["title"];
					  if (o.aData["uri"]==null) return name;
					  return "<a href='"+o.aData["uri"]+"' title='"+o.aData["uri"]+"'>"+name+"</a>";
				  }
				}
			],
		"sDom" : '<"help remove-bottom"i><"help"p>Trt<"help"lf>',		
		"sSearch": "Filter:",
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": url,
		"oLanguage": {
				"sSearch": "Filter:",
	            "sProcessing": "<img src='"+root+"/images/progress.gif' border='0'>",
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
		}		    
	} );
	return oTable;
}

function defineAlertsTable(root,url) {

	var oTable = $('#alerts').dataTable( {
		"sAjaxDataProp" : "alert",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": false,
		"aoColumnDefs": [
 				{ "mDataProp": "created" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 0 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "fnRender" : function(o,val) {
					  return new Date(val).toDateString();
				  }
				},
 				{ "mDataProp": "content" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 1 ],	
					  "bSearchable" : true,
					  "bUseRendered" : false,
					  "bSortable" : true,
					  "sWidth":"35%",
					  "fnRender" : function(o,val) {
						  var q = root + o.aData["title"] + "?" + o.aData["content"];
						  return "<a href='"+q+"' title='Go to search results'>"+(val.replace("pagesize=100","").replace(/&/g," ").replace(/xmet_/g," "))+"</a>\n";
					  }
				},
 				{ "mDataProp": "frequency" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 2 ],	
					  "bSearchable" : true,
					  "bUseRendered" : false,
					  "bSortable" : true,
					  "fnRender" : function(o,val) {
						  return val;
					  }
				},
 				{ "mDataProp": "sent" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 3 ],	
					  "bSearchable" : true,
					  "bUseRendered" : false,
					  "bSortable" : true,
					  "fnRender" : function(o,val) {
						  return new Date(val).toDateString();
					  }
				},
 				{ "mDataProp": "uri" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 4 ],	
					  "bSearchable" : true,
					  "bUseRendered" : false,
					  "bSortable" : true,
					  "fnRender" : function(o,val) {
						  return "<form method='POST' action='"+val+"?method=DELETE'><input class='remove-bottom' type='submit' value='Delete'></form>\n";
					  }
				}					
			],
		"sDom" : '<"help remove-bottom"i><"help"p>Trt<"help"lf>',			
		"sSearch": "Filter:",
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": url,
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
                '</select> alerts'	            
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
	} );
	return oTable;
}