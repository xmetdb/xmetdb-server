/**
 * jOpenTox JavaScript Library v0.0.1 (alpha)
 * 
 * Copyright 2012, IDEAconsult Ltd.
 * http://www.ideaconsult.net/
 * 
 * TODO: Licence.
 */

function runTask(modelURI, datasetURI, resultDOM, statusDOM, imgRunning, imgReady, imgError) {
	
	var request = new XMLHttpRequest();
	var reqBody = 'dataset_uri=' + datasetURI;
	
	// 'true' is for async
	request.open('POST', modelURI, true);
	
	request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	request.setRequestHeader('Accept', 'text/uri-list');
	
	request.onreadystatechange = function() {
		if (request.readyState != 4) { return false; }
		switch (request.status) {
			case 200:
				document.getElementById(resultDOM).href = request.responseText;
				document.getElementById(statusDOM).src = imgReady;
				document.getElementById(resultDOM).style.display = 'inline';
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			case 201: // fall down
			case 202:
				checkTask(request.responseText, resultDOM, statusDOM, imgReady, imgError);
				document.getElementById(statusDOM).src = imgRunning;
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			default:
				document.getElementById(resultDOM).innerHTML = request.status + ' ' + request.statusText;
				document.getElementById(statusDOM).src = imgError;
				document.getElementById(resultDOM).style.display = 'inline';
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			}
	};
	
	request.send(reqBody);
}

function checkTask(taskURI, resultDOM, statusDOM, imgReady, imgError) {
	
	var request = new XMLHttpRequest();
	
	// 'true' is for async
	request.open('GET', taskURI, true);
	
	request.setRequestHeader('Accept', 'text/uri-list');
	
	request.onreadystatechange = function() {
		if (request.readyState != 4) { return false; }
		switch (request.status) {
			case 200:
				document.getElementById(resultDOM).innerHTML = '<span title=\"' + request.status + ' ' + request.statusText + '\">Ready. Results available.</span>';
				document.getElementById(resultDOM).href = request.responseText;
				document.getElementById(statusDOM).src = imgReady;
				document.getElementById(resultDOM).style.display = 'inline';
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			case 201:
				taskURI = request.responseText; // and then fall down
			case 202:
				document.getElementById(resultDOM).innerHTML = '<span title=\'' + request.status + ' ' + request.statusText + '\'>Waiting ...</span>';
				document.getElementById(resultDOM).href = request.responseText;
				var taskTimer = window.setTimeout(function() {
					checkTask(taskURI, resultDOM, statusDOM, imgReady, imgError);
				}, 1000);
				break;
			default:
				document.getElementById(resultDOM).innerHTML = '<span title=\'' + request.status + ' ' + request.statusText + '\'>Error</span>';
				document.getElementById(statusDOM).src = imgError;
				document.getElementById(resultDOM).style.display = 'inline';
				document.getElementById(statusDOM).style.display = 'inline';
				break;
		}
	};
	
	request.send(null);
}

function readTask(root,url) {
	  $.ajax({
	        dataType: "json",
	        url: url,
	        success: function(data, status, xhr) {
	        	$.each(data["task"],function(index, entry) {
	        		$("#task_started").text(entry["started"]);
	        		console.log(entry);
	        		$("#task_name").text(entry["name"]);
	        		$("#result").prop("href",entry["result"]);
	        		var img = "progress.gif";
	        		switch (entry["status"]) {
	        		case "Completed" : {
	        			img = "tick.png";
	        			break;
	        		}
	        		case "Error" : {
	        			img = "error.png";
	        			break;
	        		}
	        		default : {
	        			img = "progress.gif";	
	        		}
	        		}
	        		$("#status").prop("src",root + "/images/" + img);
	        		$("#task_errorreport").text(entry["error"]);
	        		
	        	});
	        },
	        error: function(xhr, status, err) {
	        },
	        complete: function(xhr, status) {
	        }
	     });
}
function defineTaskTable(root,url) {

	var oTable = $('#task').dataTable( {
		"sAjaxDataProp" : "task",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": false,
		"aoColumnDefs": [
 				{ "mDataProp": "id" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 0 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "fnRender" : function(o,val) {
					  if (o.aData["id"]==null) return "Job";
					  return "<a href='"+root + "/task/" + o.aData["id"]+"' title='"+o.aData["id"]+"'>Job</a>";
				  }
				},
				{ "mDataProp": "name" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 1 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "fnRender" : function(o,val) {
					  	if (o.aData["status"]=='Completed') {
					  		return val + "<a href='"+o.aData["result"]+"'>Ready. Results available.</a>";
					  	} else if (o.aData["status"]=='Error') {
					  		return val + error;

					  	} else
						  	return checkTask(root + "/task/" + o.aData["id"],
						  			'result', 'status',  
						  			root + "/images/tick.png",
						  			root + "/images/cross.png");
				  }
				},
				{ "mDataProp": "started" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 2 ],
				  "bSearchable" : true,
				  "bSortable" : true,
				  "bUseRendered" : false,
				  "fnRender" : function(o,val) {
					  return val;
				  }
				},
				{ "mDataProp": "completed" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 3 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "fnRender" : function(o,val) {
						  return val;
					  }
				}		
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": url,
		"oLanguage": {
	            "sProcessing": "<img src='"+root+"/images/progress.gif' border='0'>",
	            "sLoadingRecords": "No records found."
	    }
	} );
	return oTable;
}