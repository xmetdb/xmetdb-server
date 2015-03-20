{
	<#include "/apidocs/version.ftl" >
    "produces": [
        "application/json",
        "text/html"
    ],		
    "resourcePath": "/stats",
	"apis": [
     	{
            "path": "/stats",
            "operations": [
                {
                    "method": "GET",
                    "summary": "Statistics",
                    "notes": "Returns list of enzymes",
                    "type": "Facet",
                    "nickname": "getStats",
                     <#include "/apidocs/authz.ftl" >
                    "parameters": [
           			            {
          			              "name": "term",
          			              "description": "Type",
          			              "required": true,
          			              "type": "string",
          			              "paramType": "query",
          			              "enum" : ["enzymes","observations","observationsbystatus","structuresbygroup","substratesandproducts","structures","references"],
          			              "defaultValue" : "enzymes",
          			              "allowMultiple"  : false
          			            },                                   
						<#include "/apidocs/parameters_page.ftl" >				            			            
                    ],
                    "responseMessages": [
                        {
                            "code": 404,
                            "message": "Items not found"
                        },
						<#include "/apidocs/error_aa.ftl" >,
						<#include "/apidocs/error_500.ftl" >                            
                    ]
                }
            ]
     	},
  	
    ],
    "models" : {
        "Facet"     : <#include "/apidocs/json_schema_facet.ftl" >,
        "Task" : <#include "/apidocs/json_schema_task.ftl" >,
      },
	<#include "/apidocs/info.ftl" >  
}