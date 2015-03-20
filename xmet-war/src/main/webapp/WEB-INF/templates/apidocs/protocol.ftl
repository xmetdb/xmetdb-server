{
	<#include "/apidocs/version.ftl" >
    "produces": [
        "application/json",
        "text/csv",
        "chemical/x-mdl-sdfile",
        "application/xml",
        "text/html"
    ],		    
    "resourcePath": "/protocol",
	"apis": [
		{
            "path": "/protocol/{xmetdbid}",
            "operations": [
                {
                    "method": "GET",
                    "summary": "Retrieve single observation",
                    "notes": "Returns single observation by xmetdbid",
                    "type": "Observation",
                    "nickname": "getObservation",
                     <#include "/apidocs/authz.ftl" >
                    "parameters": [
			            {
			              "name": "xmetdbid",
			              "description": "XMETDB obsrevation by identifier ",
			              "required": true,
			              "type": "string",
			              "paramType": "path",
			              "defaultValue" : "XMETDB2",
			              "allowMultiple"  : false
			            }			            			            
                    ],
                    "responseMessages": [
                        {
                            "code": 404,
                            "message": "Observation not found"
                        },
						<#include "/apidocs/error_aa.ftl" >,
						<#include "/apidocs/error_500.ftl" >                            
                    ]
                }
            ]
     	} ,
     	{
            "path": "/protocol",
            "operations": [
                {
                    "method": "GET",
                    "summary": "Observation search",
                    "notes": "Returns list of observations",
                    "type": "Observation",
                    "nickname": "searchObservations",
                     <#include "/apidocs/authz.ftl" >
                    "parameters": [
			            {
			              "name": "search_curated",
			              "description": "Retrieve observation of type",
			              "required": true,
			              "type": "string",
			              "paramType": "query",
			              "enum" : ["curated","noncurated","any"],
			              "defaultValue" : "any",
			              "allowMultiple"  : false
			            },
			            {
			              "name": "xmet_reference",
			              "description": "XMETDB reference (DOI or plain text)",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			              "allowMultiple"  : false
			            },				            
			            { 
			              "name": "xmet_number",
			              "description": "XMETDB observation identifier",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			              "defaultValue" : "",
			              "allowMultiple"  : false
			            },	
	            		{  
			              "name": "xmet_exp_ms",
			              "description": "Experiment type : Microsomes",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			               "enum" : ["on","off", ""],
			               "defaultValue" : "",
			              "allowMultiple"  : false
			            },			
 		            	 {
			              "name": "xmet_exp_hep",
			              "description": "Experiment type : Hepatocytes",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			               "enum" : ["on","off", ""],
			               "defaultValue" : "",
			              "allowMultiple"  : false
			            },			
  		            	 {
			              "name": "xmet_exp_enz",
			              "description": "Experiment type : Enzymes",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			               "enum" : ["on","off", ""],
			               "defaultValue" : "",
			              "allowMultiple"  : false
			            },			
			            {
			              "name": "xmet_enzyme",
			              "description": "Enzyme",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			              "defaultValue" : "CYP1A1",
			              "allowMultiple"  : false
			            },		
			            {
			              "name": "xmet_allele",
			              "description": "Allele",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			              "allowMultiple"  : false,
			              "defaultValue" : "2"
			            },
						<#include "/apidocs/parameters_page.ftl" >				            			            
                    ],
                    "responseMessages": [
                        {
                            "code": 404,
                            "message": "Observations not found"
                        },
						<#include "/apidocs/error_aa.ftl" >,
						<#include "/apidocs/error_500.ftl" >                            
                    ]
                }
            ]
     	}
    ],
    "models" : {
        "Task" : <#include "/apidocs/json_schema_task.ftl" >
      },    
	<#include "/apidocs/info.ftl" >  
}