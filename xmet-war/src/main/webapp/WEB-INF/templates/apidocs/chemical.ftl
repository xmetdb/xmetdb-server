{
	<#include "/apidocs/version.ftl" >
    "produces": [
        "application/json",
        "text/html"
    ],		
    "resourcePath": "/chemical",
	"apis": [
     	{
            "path": "/chemical",
            "operations": [
                {
                    "method": "GET",
                    "summary": "Chemical structure search",
                    "notes": "Returns list of chemicals",
                    "type": "Search chemical compounds",
                    "nickname": "searchChemical",
                     <#include "/apidocs/authz.ftl" >
                    "parameters": [
			            {
			              "name": "search",
			              "description": "Chemical identifier or SMILES or InChI or Base64 encoded MOL file, if (type=b64mol)",
			              "required": true,
			              "type": "string",
			              "paramType": "query",
			              "allowMultiple"  : false
			            },
			            {
			              "name": "type",
			              "description": "Defines the expected content of the search parameter",
			              "required": false,
			              "type": "string",
			              "enum" : ["","b64mol"],
			              "defaultValue" : "",
			              "paramType": "query",
			              "allowMultiple"  : false
			            },				       
	            		{                  
			              "name": "search_substrates",
			              "description": "Search within Substrates only, if =on",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			               "enum" : ["on","off", ""],
			              "allowMultiple"  : false
			            },			
       					{                  
			              "name": "search_products",
			              "description": "Search within Products only, if =on",
			              "required": false,
			              "type": "string",
			              "paramType": "query",
			               "enum" : ["on","off", ""],
			              "allowMultiple"  : false
			            },				            
			            {                  
			              "name": "option",
			              "description": "Search mode",
			              "required": false,
			              "type": "double",
			              "paramType": "query",
			               "enum" : ["auto","similarity", "smarts"],
			              "allowMultiple"  : false
			            },	
			            {
			              "name": "threshold",
			              "description": "Tanimoto similarity threshold",
			              "required": false,
			              "type": "double",
			              "paramType": "query",
			              "defaultvalue" : 0.5,
			              "allowMultiple"  : false
			            },		
				            
						<#include "/apidocs/parameters_page.ftl" >				            			            
                    ],
                    "responseMessages": [
                        {
                            "code": 404,
                            "message": "Compounds not found"
                        },
						<#include "/apidocs/error_aa.ftl" >,
						<#include "/apidocs/error_500.ftl" >                            
                    ]
                }
            ]
     	}
    ],
	<#include "/apidocs/info.ftl" >  
}