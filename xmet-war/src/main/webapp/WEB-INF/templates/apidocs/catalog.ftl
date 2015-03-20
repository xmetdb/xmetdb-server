{
	<#include "/apidocs/version.ftl" >
    "produces": [
        "application/json",
        "text/html"
    ],		
    "resourcePath": "/catalog",
	"apis": [
     	{
            "path": "/catalog",
            "operations": [
                {
                    "method": "GET",
                    "summary": "Enzymes search",
                    "notes": "Returns list of enzymes",
                    "type": "Search enzimes",
                    "nickname": "searchEnzyme",
                     <#include "/apidocs/authz.ftl" >
                    "parameters": [
						<#include "/apidocs/parameters_page.ftl" >				            			            
                    ],
                    "responseMessages": [
                        {
                            "code": 404,
                            "message": "Enzymes not found"
                        },
						<#include "/apidocs/error_aa.ftl" >,
						<#include "/apidocs/error_500.ftl" >                            
                    ]
                }
            ]
     	},
     	{
            "path": "/catalog/{id}",
            "operations": [
                {
                    "method": "GET",
                    "summary": "Enzymes search",
                    "notes": "Returns list of enzymes",
                    "type": "Search enzimes",
                    "nickname": "getEnzyme",
                     <#include "/apidocs/authz.ftl" >
                    "parameters": [
			            {
			              "name": "id",
			              "description": "Enzyme by identifier ",
			              "required": true,
			              "type": "string",
			              "paramType": "path",
			              "defaultValue" : "2",
			              "allowMultiple"  : false
			            },
						<#include "/apidocs/parameters_page.ftl" >				            			            
                    ],
                    "responseMessages": [
                        {
                            "code": 404,
                            "message": "Enzyme not found"
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